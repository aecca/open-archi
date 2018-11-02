let alreadyProcessedNodes;
let alreadyProcessedLinks;

function fulfill(item, isGroup, group, rank) {
    item.key = item.id;
    item.isGroup = isGroup;
    if (group !== undefined) {
        item.group = group;
    }
    if (rank !== undefined) {
        item.rank = rank;
    }
    return item;
}

function fillShape(model, node) {
    if (node) {
        let shape = {
            type: node.category,
            fill: node.fill,
            stroke: node.stroke,
            input: node.input,
            output: node.output
        };
        if (node.size) {
            shape.minSize = {
                width: node.size.width,
                height: node.size.height
            };
        } else {
            if (node.minSize) {
                shape.minSize = node.minSize;
            }
        }
        model.shape = shape;
    }
    return model;
}

function extractLinks(model) {
    let links = [];
    if (model.relationships !== undefined && model.relationships !== null) {
        model.relationships.forEach(relationship => {
            links.push({
                to: relationship.destination.id,
                from: relationship.source.id
            });
        });
    }
    return links;
}

function removeDuplicates(arr) {
    let unique_array = [];
    for (let i = 0; i < arr.length; i++) {
        const element = arr[i];
        if (unique_array.length === 0) {
            unique_array.push(element)
        } else {
            let flag = false;
            for (let j = 0; j < unique_array.length; j++) {
                const uniqueArrayElement = unique_array[j];
                if (_.isEqual(uniqueArrayElement, element)) {
                    flag = true;
                }
            }
            if (!flag) {
                unique_array.push(element);
            }
        }
    }
    return unique_array;
}

function findParent(parentId, model) {
    if (parentId !== undefined && model !== undefined) {
        let nodes = findValues(model, "key");
        return nodes.find(node => {
            return node.key.toString() === parentId.toString();
        })
    }
    return undefined;
}

function findByField(model, field, value) {
    if (value !== undefined && model !== undefined) {
        let nodes = findValues(model, field);
        return nodes.find(node => {
            return node[field] === value;
        })
    }
    return undefined;
}

class OpenArchiWrapper {

    static toLocation(data, node) {
        return new go.Point(data.x, data.y);
    }

    static fromLocation(loc, data, model) {
        model.setDataProperty(data, "x", loc.x);
        model.setDataProperty(data, "y", loc.y);
    }

    static toFill(data, node) {
        const fill = data.shape.fill;
        const brush = new go.Brush(go.Brush.Solid);
        brush.color = fill ? fill : data.fill;
        return brush;
    }

    static fromFill(loc, data, model) {
        const fill = data.shape.fill;
        model.setDataProperty(data, "fill", fill ? fill : data.fill);
    }

    static toTitle(data, node) {
        return data.name;
    }

    static fromTitle(text, data, model) {
        model.setDataProperty(data, "text", text);
        model.setDataProperty(data, "name", text);
    }

    static toImage(data, node) {
        return data.image ? data.image.raw : "";
    }

    static fromImage(text, data, model) {
        model.setDataProperty(data, "source", data.image.raw);
    }

    static toName(data, node) {
        return data.name;
    }

    static fromName(loc, data, model) {
        model.setDataProperty(data, "text", data.name);
        model.setDataProperty(data, "name", data.name);
    }

    static toSize(data, node) {
        let size;
        const shape = data.shape;
        const size2 = shape.size;
        if (shape && size2) {
            const width = size2.width;
            const height = size2.height;
            size = new go.Size(width === 0 ? 25 : width, height === 0 ? 15 : height);
        } else {
            size = new go.Size(25, 15);
        }
        return size;
    }

    static fromSize(size, data, model) {
        model.setDataProperty(data, "width", size.width);
        model.setDataProperty(data, "height", size.height);
    }

    static toFigure(data, node) {
        return data.shape.type;
    }

    static fromFigure(figure, data, model) {
        model.setDataProperty(data, "figure", data.shape.type);
    }

    static toStroke(data, node) {
        const stroke = data.shape.stroke;
        const brush = new go.Brush(go.Brush.Solid);
        brush.color = stroke ? stroke : data.stroke;
        return brush;
    }

    static fromStroke(stroke_, data, model) {
        const stroke = data.shape.stroke;
        model.setDataProperty(data, "stroke", stroke ? stroke : data.stroke);
    }

    static toFromLinkable(data, node) {
        const shape = data.shape;
        if (shape) {
            return shape.output;
        }
        return true;
    }

    static fromFromLinkable(loc, data, model) {
        let fromLinkable = true;
        const shape = data.shape;
        if (shape) {
            fromLinkable = shape.output;
        }
        model.setDataProperty(data, "fromLinkable", fromLinkable);
    }


    static toToLinkable(data, node) {
        const shape = data.shape;
        if (shape) {
            return shape.input;
        }
        return true;
    }

    static fromToLinkable(loc, data, model) {
        let toLinkable = true;
        const shape = data.shape;
        if (shape) {
            toLinkable = shape.input;
        }
        model.setDataProperty(data, "toLinkable", toLinkable);
    }

    static toCategory(data, node) {
        const shape = data.shape;
        if (shape) {
            return shape.type;
        }
        return "DEFAULT";
    }

    static fromCategory(loc, data, model) {
        let category = "DEFAULT";
        const shape = data.shape;
        if (shape) {
            category = shape.type;
        }
        model.setDataProperty(data, "category", category);
    }

    static toIsGroup(data, node, isGroup) {
        if (data === undefined || data === null) {
            return false;
        }
        const shape = data.shape;
        if (shape !== undefined) {
            if (isGroup !== undefined) {
                return isGroup;
            } else {
                return data.isGroup;
            }
        } else {
            return data.isGroup;
        }
    }

    static fixCategory(elements) {
        if (elements !== undefined) {
            elements.forEach(function (element) {
                element.category = OpenArchiWrapper.toCategory(element);
            });
            return elements.sort(function (a, b) {
                return a.rank - b.rank;
            });
        }
    }

    static toComplementColor(data, node) {
        let color = "#ffffff";
        if (data !== undefined) {
            const shape = data.shape;
            if (shape && shape.fill) {
                color = complementRBG(shape.fill);
            }
        }
        return color;
    }


}