function toLocation(data, node) {
    return new go.Point(data.x, data.y);
}

function fromLocation(loc, data, model) {
    model.setDataProperty(data, "x", loc.x);
    model.setDataProperty(data, "y", loc.y);
}

function toFill(data, node) {
    return new go.Brush(data.shape.fill);
}

function fromFill(loc, data, model) {
    model.setDataProperty(data, "fill", data.shape.fill);
}

function toTitle(data, node) {
    return new go.TextBlock(data.name);
}

function fromTitle(loc, data, model) {
    model.setDataProperty(data, "text", data.name);
}

function architectureModelToDiagram(model) {

    let diagram = {};
    let modelIsGroup = false;
    if (model.kind !== "ARCHITECTURE_MODEL") {

        return diagram;
    }

    diagram.nodes = [];
    diagram.links = [];
    let key = 0;

    let relationships = commons.prototype.findValues(model, "relationships");

    let softwareSystems = model.softwareSystems;
    if (softwareSystems !== undefined && !commons.prototype.isEmpty(softwareSystems)) {
        modelIsGroup = true;
        softwareSystems.forEach(function (softwareSystem) {
            let groupSoftwareSystems = {
                key: softwareSystem.id,
                text: softwareSystem.name,
                color: "green",
                isGroup: true,
                group: model.id
            };
            diagram.nodes.push(groupSoftwareSystems);
            let containers = softwareSystem.containers;
            if (containers !== undefined && !commons.prototype.isEmpty(containers)) {
                diagram.nodes.push({
                    key: softwareSystem.id,
                    text: softwareSystem.name,
                    color: "lightgreen",
                    group: "softwareSystems"
                });
                containers.forEach(function (container) {
                    let groupContainers = {
                        key: container.id,
                        text: container.name,
                        color: "red",
                        isGroup: true,
                        group: softwareSystem.id
                    };
                    diagram.nodes.push(groupContainers);
                    let components = container.components;
                    if (components !== undefined && !commons.prototype.isEmpty(components)) {
                        diagram.nodes.push({key: component.id, text: component.name, color: "yellow", group: container.id});
                        diagram.nodes.push({
                            key: softwareSystem.id,
                            text: softwareSystem.name,
                            color: "lightgreen",
                            group: "softwareSystems"
                        });
                        components.forEach(function (component) {
                            let groupComponents = {
                                key: component.id,
                                text: component.name,
                                color: "red",
                                isGroup: true,
                                group: softwareSystem.id
                            };
                            diagram.nodes.push(groupComponents);
                        });
                    }
                });
            }

        });
    }

    const consumers = model.consumers;
    if (consumers !== undefined && consumers !== null) {
        let groupConsumers = {key: key, text: "Consumers", color: "green", isGroup: true};
        diagram.nodes.push(groupConsumers);
        consumers.forEach(function (consumer) {
            diagram.nodes.push({key: consumer.id, text: consumer.name, color: "lightgreen", group: groupConsumers.key});
        })
    }

    diagram.links.push({from: 0, to: model.id, color: "blue"});
    diagram.nodes.push({key: model.id, text: model.name, color: "orange", isGroup: modelIsGroup});
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
}