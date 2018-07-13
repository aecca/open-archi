let alreadyProcessedNodes;

function fulfill(item, isGroup, group, rank) {
    item.key = item.id;
    item.isGroup = isGroup;
    if (group) {
        item.group = group;
    }
    item.rank = rank;
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
            shape.size = {
                size: {
                    width: node.size.width,
                    height: node.size.height
                }
            };
        }
        model.shape = shape;
    }
    return model;
}

function commonInnerDiagramElement(model, node) {
    let object = {};
    object.id = node.id;
    object.key = node.key;
    object.meta = node.meta;
    object.status = node.status | "INITIAL";
    object.name = node.name;
    object.kind = node.kind;
    object.description = node.description;
    object.prototype = node.prototype;
    object.location = {};
    let loc = node.loc;
    if (loc) {
        object.location.x = loc.split(" ")[0];
        object.location.y = loc.split(" ")[1];
    }
    object = fillShape(object, node);
    return object;
}

function processLayer(model, node, parent, links) {
    let layer = commonInnerDiagramElement(model, node);
    //TODO Añadir campos propios del layer
    //Los layer sólo se pueden agrupar en el modelo directamente, no en otros layers, systems, container, o components
    if (!parent.layers) {
        parent.layers = [];
    }
    parent.layers.push(layer);
    alreadyProcessedNodes.push(layer.key);
}

function processSystem(model, node, parent, links) {
    let system = commonInnerDiagramElement(model, node);
    //TODO Añadir campos propios del system
    //Los systems sólo se pueden agrupar en layers u otros systems
    if (!parent.systems) {
        parent.systems = [];
    }
    parent.systems.push(system);
    alreadyProcessedNodes.push(system.key);
}

function processContainer(model, node, parent, links) {
    let container = commonInnerDiagramElement(model, node);
    //TODO Añadir campos propios del container
    //Los containers sólo se pueden agrupar en layers u otros containers
    if (!parent.containers) {
        parent.containers = [];
    }
    parent.containers.push(container);
    alreadyProcessedNodes.push(container.key);
}

function processComponent(model, node, parent, links) {
    let component = commonInnerDiagramElement(model, node);
    //TODO Añadir campos propios del component
    //Los components sólo se pueden agrupar en layers u otros components
    if (!parent.components) {
        parent.components = [];
    }
    parent.components.push(component);
    alreadyProcessedNodes.push(component.key);
}

function findValues(obj, key){
    return findValuesHelper(obj, key, []);
}

function findValuesHelper(obj, key, list) {
    let i;
    if (!obj) return list;
    if (obj instanceof Array) {
        for (i in obj) {
            list = list.concat(findValuesHelper(obj[i], key, []));
        }
        return list;
    }
    if (obj[key]) list.push(obj);

    if ((typeof obj === "object") && (obj !== null) ){
        let children = Object.keys(obj);
        if (children.length > 0){
            for (i = 0; i < children.length; i++ ){
                list = list.concat(findValuesHelper(obj[children[i]], key, []));
            }
        }
    }
    return list;
}

function findParent(parentId, model) {
    if (parentId !== undefined && model !== undefined) {
        let nodes = findValues(model, "key");
        return nodes.find(node => {
            return node.key === parentId;
        })
    }
    return undefined;
}

function diagramToArchitectureModel(model, node, links, nodes) {
    if (node) {
        let parent = findParent(node.group, model);
        if (parent === undefined) {
            parent = model;
        }
        if (node.kind === "LAYER") {
            processLayer(model, node, parent, links);
        } else if (node.kind === "SYSTEM") {
            processSystem(model, node, parent, links);
        } else if (node.kind === "CONTAINER") {
            processContainer(model, node, parent, links);
        } else if (node.kind === "COMPONENT") {
            processComponent(model, node, parent, links);
        }
    }
    return model;
}

function diagramToFlowchartModel(model, node, links) {
}

function diagramToSequenceModel(model, node, links) {
}

function diagramToGanttModel(model, node, links) {
}

function diagramToEntityRelationshipModel(model, node, links) {
}

function diagramToUmlModel(model, node, links) {
}

function diagramToBpmModel(model, node, links) {
}

function architectureModelToDiagram(model) {

    let diagram = {
        class: "go.GraphLinksModel"
    };
    if (model.kind !== "ARCHITECTURE_MODEL" && model.kind !== "SYSTEM" && model.kind !== "CONTAINER" && model.kind !== "COMPONENT") {
        return diagram;
    }
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];
    let key = "consumers";
    let relationships = commons.prototype.findValues(model, "relationships");
    let rank = 0;
    let systems = model.kind === "SYSTEM"
        ? [model]
        : (model.kind === "ARCHITECTURE_MODEL"
            ? model.systems
            : undefined);
    let parentGroup = model.kind === "ARCHITECTURE_MODEL" ? model.id : undefined;
    let hasSystems = systems !== undefined && !commons.prototype.isEmpty(systems);

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
    if (hasSystems) {
        systems.forEach(function (system) {
            if (!alreadyProcessedNodes.includes(system.id)) {
                let containers = system.containers;
                let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
                if (hasContainers) {
                    diagram.nodeDataArray.push(fulfill(system, true, parentGroup, rank));
                    alreadyProcessedNodes.push(system.id);
                    rank++;
                    containers.forEach(function (container) {
                        if (!alreadyProcessedNodes.includes(container.id)) {
                            let components = container.components;
                            let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
                            if (hasComponents) {
                                diagram.nodeDataArray.push(fulfill(container, true, system.id, rank));
                                alreadyProcessedNodes.push(container.id);
                                rank++;
                                components.forEach(function (component) {
                                    if (!alreadyProcessedNodes.includes(component.id)) {
                                        diagram.nodeDataArray.push(fulfill(component, false, container.id, rank));
                                        alreadyProcessedNodes.push(component.id);
                                        rank++;
                                    }
                                });
                            } else {
                                diagram.nodeDataArray.push(fulfill(container, false, system.id, rank));
                                alreadyProcessedNodes.push(container.id);
                                rank++;
                            }
                        }
                    });
                }
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
        });
        diagram.linkDataArray.push({from: key, to: model.id, stroke: "black"});
    }
    if (parentGroup) {
        diagram.nodeDataArray.push({key: model.id, name: model.name, fill: "orange", isGroup: hasSystems});
    }
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
        alreadyProcessedNodes = [];

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
            case "SYSTEM":
            case "CONTAINER":
            case "COMPONENT":
                diagram = architectureModelToDiagram(model);
                break;
            default:
                console.log("Still not implemented");
        }
        return diagram;
    };

    static fromDiagram(diagram) {
        let model = {};
        delete diagram.class;
        let nodes = diagram.nodeDataArray;
        let links = diagram.linkDataArray;
        model.status = meta.status || "INITIAL";
        if (meta.id) {
            model.id = meta.id;
        }
        model.name = meta.name;
        model.kind = meta.kind;
        model.description = meta.description;
        model.prototype = meta.prototype;
        alreadyProcessedNodes = [];
        if (!commons.prototype.isEmpty(nodes)) {
            nodes.forEach(function (node) {
                if (!alreadyProcessedNodes.includes(node.key)) {
                    model = fillShape(model, node);
                    switch (model.kind) {
                        case "FLOWCHART_MODEL":
                            model = diagramToFlowchartModel(model, node, links);
                            break;
                        case "SEQUENCE_MODEL":
                            model = diagramToSequenceModel(model, node, links);
                            break;
                        case "GANTT_MODEL":
                            model = diagramToGanttModel(model, node, links);
                            break;
                        case "ENTITY_RELATIONSHIP_MODEL":
                            model = diagramToEntityRelationshipModel(model, node, links);
                            break;
                        case "UML_CLASS_MODEL":
                            model = diagramToUmlModel(model, node, links);
                            break;
                        case "BPM_MODEL":
                            model = diagramToBpmModel(model, node, links);
                            break;
                        case "ARCHITECTURE_MODEL":
                            model = diagramToArchitectureModel(model, node, links, nodes);
                            break;
                        default:
                            console.log("Still not implemented");
                    }
                }
            })
        }
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

    static fromTitle(text, data, model) {
        model.setDataProperty(data, "text", text);
        model.setDataProperty(data, "name", text);
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
        if (data.shape && data.shape.size) {
            size = new go.Size(data.shape.size.width, data.shape.size.height);
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
        return new go.Brush(data.shape.stroke);
    }

    static fromStroke(stroke, data, model) {
        model.setDataProperty(data, "stroke", data.shape.stroke);
    }

}