function fulfill(item, isGroup, group, rank) {
    item.key = item.id;
    item.isGroup = isGroup;
    if (group) {
        item.group = group;
    }
    item.rank = rank;
    return item;
}

function architectureModelToDiagram(model) {

    let diagram = {
        class: "go.GraphLinksModel"
    };
    if (model.kind !== "ARCHITECTURE_MODEL") {
        return diagram;
    }
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    let key = "consumers";
    let relationships = commons.prototype.findValues(model, "relationships");
    let rank = 0;
    let softwareSystems = model.softwareSystems;
    let hasSoftwareSystems = softwareSystems !== undefined && !commons.prototype.isEmpty(softwareSystems);

    if (relationships) {
        relationships.forEach(function (relationship) {
            diagram.linkDataArray.push({
                from: relationship.sourceId,
                to: relationship.destinationId,
                stroke: relationship.connector.stroke
            });
            rank++;
        });
    }
    rank = 0;
    if (hasSoftwareSystems) {
        softwareSystems.forEach(function (softwareSystem) {
            let containers = softwareSystem.containers;
            let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
            if (hasContainers) {
                diagram.nodeDataArray.push(fulfill(softwareSystem, true, model.id, rank));
                rank++;
                containers.forEach(function (container) {
                    let components = container.components;
                    let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
                    if (hasComponents) {
                        diagram.nodeDataArray.push(fulfill(container, true, softwareSystem.id, rank));
                        rank++;
                        components.forEach(function (component) {
                            diagram.nodeDataArray.push(fulfill(component, false, container.id, rank));
                            rank++;
                        });
                    } else {
                        diagram.nodeDataArray.push(fulfill(container, false, softwareSystem.id, rank));
                        rank++;
                    }
                });
            }
        });
    }

    rank = 0;

    const consumers = model.consumers;
    if (consumers !== undefined && consumers !== null) {
        let groupConsumers = {key: key, name: "Consumers", fill: "green", isGroup: true};
        diagram.nodeDataArray.push(groupConsumers);
        consumers.forEach(function (consumer) {
            diagram.nodeDataArray.push(fulfill(consumer, false, key, rank));
        })
    }

    diagram.linkDataArray.push({from: key, to: model.id, stroke: "black"});
    diagram.nodeDataArray.push({key: model.id, name: model.name, fill: "orange", isGroup: hasSoftwareSystems});
    return diagram;
}

function flowchartModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    let key = 0;
    diagram.nodeDataArray.push({key: -1, category: "Start", loc: "5 0", text: "Start"});
    diagram.linkDataArray.push({from: -1, to: model.id, fromPort: "B", toPort: "T"});
    diagram.nodeDataArray.push({key: model.id, loc: "5 100", text: model.name});
    diagram.linkDataArray.push({from: model.id, to: -2, fromPort: "B", toPort: "T"});
    diagram.nodeDataArray.push({key: -2, category: "End", loc: "5 200", text: "End!"});
    return diagram;
}

function sequenceModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    return diagram;
}

function ganttModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    return diagram;
}

function entityRelationshipModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    return diagram;
}

function umlModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    return diagram;
}

function bpmModelToDiagram(model) {
    let diagram = {
        class: "go.GraphLinksModel"
    };
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    return diagram;
}

class OpenArchiWrapper {

    static toDiagram(model) {
        const type = model.kind;
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];

        switch (type) {
            case "FLOWCHART_MODEL":
                diagram = flowchartModelToDiagram(model);
                break;
            case "SEQUENCE_MODEL":
                diagram = sequenceModelToDiagram(model);
                break;
            case "GANTT_MODEL":
                diagram = ganttModelToDiagram(model);
                break;
            case "ENTITY_RELATIONSHIP_MODEL":
                diagram = entityRelationshipModelToDiagram(model);
                break;
            case "UML_CLASS_MODEL":
                diagram = umlModelToDiagram(model);
                break;
            case "BPM_MODEL":
                diagram = bpmModelToDiagram(model);
                break;
            case "ARCHITECTURE_MODEL":
                diagram = architectureModelToDiagram(model);
                break;
            default:
                console.log("Still not implemented");
        }
        return diagram;
    };

    static fromDiagram(diagram) {
        let model = diagram.nodeDataArray;
        return model;
    };

    static toLocation(data, node) {
        return new go.Point(data.x, data.y);
    }

    static fromLocation(loc, data, model) {
        model.setDataProperty(data, "x", loc.x);
        model.setDataProperty(data, "y", loc.y);
    }

    static toFill(data, node) {
        return new go.Brush(data.shape.fill);
    }

    static fromFill(loc, data, model) {
        model.setDataProperty(data, "fill", data.shape.fill);
    }

    static toTitle(data, node) {
        return data.name;
    }

    static fromTitle(loc, data, model) {
        model.setDataProperty(data, "text", data.name);
    }

    static toName(data, node) {
        return data.name;
    }

    static fromName(loc, data, model) {
        model.setDataProperty(data, "text", data.name);
    }

    static toSize(data, node) {
        return new go.Size(data.shape.size.width, data.shape.size.height);
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
        return new go.Brush(data.shape.stroke);
    }

    static fromStroke(stroke, data, model) {
        model.setDataProperty(data, "stroke", data.shape.stroke);
    }

}