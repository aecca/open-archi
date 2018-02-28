function architectureModelToDiagram(model) {
    let diagram = {};
    diagram.nodes = [];
    diagram.links = [];
    let key = 0;

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
    diagram.nodes.push({key: -1, category: "Start", loc: "175 0", text: "Start"});
    diagram.links.push({from: -1, to: model.id, fromPort: "B", toPort: "T"});
    diagram.nodes.push({key: model.id, loc: "175 100", text: model.name});
    diagram.links.push({from: model.id, to: -2, fromPort: "B", toPort: "T"});
    diagram.nodes.push({key: -2, category: "End", loc: "175 200", text: "End!"});
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
                getPageContent("/diagrams/sequenceDiagram.html");
                break;
            case "GANTT_MODEL":
                getPageContent("/diagrams/gantt.html");
                break;
            case "ENTITY_RELATIONSHIP_MODEL":
                getPageContent("/diagrams/entityRelationship.html");
                break;
            case "UML_CLASS_MODEL":
                getPageContent("/diagrams/umlClass.html");
                break;
            case "BPM_MODEL":
                getPageContent("/diagrams/swimLanes.html");
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