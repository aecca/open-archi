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

function toText(data, node) {
    return new go.TextBlock(data.name);
}

function fromText(loc, data, model) {
    model.setDataProperty(data, "text", data.name);
}

function architectureModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    let key = 0;

    let tal = commons.findValues(model, "relationships");
    if (model.kind !== "ARCHITECTURE_MODEL") {
        return diagram;
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
    diagram.nodes.push({key: model.id, text: model.name, color: "orange"});
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