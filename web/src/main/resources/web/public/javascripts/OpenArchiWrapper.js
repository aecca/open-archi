function architectureModelToDiagram(model) {

    let diagram = {};
    if (model.kind !== "ARCHITECTURE_MODEL") {
        return diagram;
    }
    diagram.nodes = [];
    diagram.links = [];
    let key = 0;
    let relationships = commons.prototype.findValues(model, "relationships");
    let rank = 0;
    let softwareSystems = model.softwareSystems;
    let hasSoftwareSystems = softwareSystems !== undefined && !commons.prototype.isEmpty(softwareSystems);

    if (relationships) {
        relationships.forEach(function (relationship) {
            diagram.links.push({from: relationship.sourceId, to: relationship.destinationId, stroke: relationship.connector.stroke});
        });
    }

    if (hasSoftwareSystems) {
        softwareSystems.forEach(function (softwareSystem) {
            let containers = softwareSystem.containers;
            let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
            if (hasContainers) {
                diagram.nodes.push({
                    key: softwareSystem.id,
                    name: softwareSystem.name,
                    description: softwareSystem.description,
                    fill: softwareSystem.shape.fill,
                    isGroup: true,
                    group: model.id,
                    shapeType: softwareSystem.shape.type,
                    rank: rank,
                    size: {
                        width: 40.0,
                        height: 40.0
                    },
                    stroke: softwareSystem.shape.stroke,
                    input: softwareSystem.shape.input,
                    output: softwareSystem.shape.output
                });
                rank++;
                containers.forEach(function (container) {
                    let components = container.components;
                    let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
                    if (hasComponents) {
                        diagram.nodes.push({
                            key: container.id,
                            name: container.name,
                            description: container.description,
                            fill: container.shape.fill,
                            isGroup: true,
                            group: softwareSystem.id,
                            shapeType: container.shape.type,
                            rank: rank,
                            size: {
                                width: 40.0,
                                height: 40.0
                            },
                            stroke: container.shape.stroke,
                            input: container.shape.input,
                            output: container.shape.output
                        });
                        rank++;
                        components.forEach(function (component) {
                            let groupComponents = {
                                key: component.id,
                                name: component.name,
                                description: component.description,
                                fill: component.shape.fill,
                                isGroup: true,
                                group: container.id,
                                shapeType: component.shape.type,
                                rank: rank,
                                size: {
                                    width: 40.0,
                                    height: 40.0
                                },
                                stroke: component.shape.stroke,
                                input: component.shape.input,
                                output: component.shape.output
                            };
                            rank++;
                            diagram.nodes.push(groupComponents);
                        });
                    } else {
                        diagram.nodes.push({
                            key: container.id,
                            name: container.name,
                            description: container.description,
                            fill: container.shape.fill,
                            isGroup: false,
                            group: softwareSystem.id,
                            shapeType: container.shape.type,
                            rank: rank,
                            size: {
                                width: 40.0,
                                height: 40.0
                            },
                            stroke: container.shape.stroke,
                            input: container.shape.input,
                            output: container.shape.output
                        });
                    }
                });
            }
        });
    }

    const consumers = model.consumers;
    if (consumers !== undefined && consumers !== null) {
        let groupConsumers = {key: key, name: "Consumers", color: "green", isGroup: true};
        diagram.nodes.push(groupConsumers);
        consumers.forEach(function (consumer) {
            diagram.nodes.push({
                key: consumer.id,
                name: consumer.name,
                fill: "lightgreen",
                group: groupConsumers.key
            });
        })
    }

    diagram.links.push({from: 0, to: model.id, color: "blue"});
    diagram.nodes.push({key: model.id, name: model.name, fill: "orange", isGroup: hasSoftwareSystems});
    return diagram;
}

function flowchartModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    let key = 0;
    diagram.nodes.push({key: -1, category: "Start", loc: "5 0", text: "Start"});
    diagram.links.push({from: -1, to: model.id, fromPort: "B", toPort: "T"});
    diagram.nodes.push({key: model.id, loc: "5 100", text: model.name});
    diagram.links.push({from: model.id, to: -2, fromPort: "B", toPort: "T"});
    diagram.nodes.push({key: -2, category: "End", loc: "5 200", text: "End!"});
    return diagram;
}

function sequenceModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    return diagram;
}

function ganttModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    return diagram;
}

function entityRelationshipModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    return diagram;
}

function umlModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    return diagram;
}

function bpmModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    return diagram;
}

class OpenArchiWrapper {

    static toDiagram(model) {
        const type = model.kind;
        let diagram = {};
        diagram.nodes = [];
        diagram.links = [];

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
        let model = {};
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
}