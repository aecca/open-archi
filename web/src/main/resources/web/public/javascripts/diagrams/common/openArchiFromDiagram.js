let alreadyProcessedNodes;
let alreadyProcessedLinks;

function commonInnerDiagramElement(node) {
    let object = {};
    object.id = node.id;
    object.key = node.key.toString();
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
    let image = node.image;
    if (image) {
        let raw = image.raw;
        raw = window.atob(raw.replace(/^data:image\/svg\+xml;base64,/, ""));
        object.image = {raw: raw, type: image.type};
    }
    return object;
}

function addLinksToArchitectureModel(element, links) {
    if (element !== undefined && links !== undefined && Array.isArray(links) && links.length > 0) {
        if (element.relationships === undefined) {
            element.relationships = [];
        }
        links.forEach(link => {
            if (link.from.toString() === element.key.toString() || link.to.toString() === element.key.toString()) {
                let relationship = {};
                relationship.source = {};
                relationship.destination = {};
                relationship.source.id = link.from.toString();
                relationship.destination.id = link.to.toString();
                element.relationships.push(relationship);
                alreadyProcessedLinks.push(relationship);
            }
        });
        if (element.relationships.length === 0) {
            delete element.relationships;
        }
    }
}

function processLayerToArchitectureModel(node, parent, links) {
    let layer = commonInnerDiagramElement(node);
    //TODO Añadir campos propios del layer
    //Los layer sólo se pueden agrupar en el modelo directamente, no en otros layers, systems, container, o components
    if (!parent.layers) {
        parent.layers = [];
    }
    addLinksToArchitectureModel(layer, links);
    parent.layers.push(layer);
    alreadyProcessedNodes.push(layer.key);
}

function processSystemToArchitectureModel(node, parent, links) {
    let system = commonInnerDiagramElement(node);
    //TODO Añadir campos propios del system
    //Los systems sólo se pueden agrupar en layers u otros systems
    if (!parent.systems) {
        parent.systems = [];
    }
    addLinksToArchitectureModel(system, links);
    parent.systems.push(system);
    alreadyProcessedNodes.push(system.key);
}

function processContainerToArchitectureModel(node, parent, links) {
    let container = commonInnerDiagramElement(node);
    //TODO Añadir campos propios del container
    //Los containers sólo se pueden agrupar en layers u otros containers
    if (!parent.containers) {
        parent.containers = [];
    }
    addLinksToArchitectureModel(container, links);
    parent.containers.push(container);
    alreadyProcessedNodes.push(container.key);
}

function processComponentToArchitectureModel(node, parent, links) {
    let component = commonInnerDiagramElement(node);
    //TODO Añadir campos propios del component
    //Los components sólo se pueden agrupar en layers u otros components
    if (!parent.components) {
        parent.components = [];
    }
    addLinksToArchitectureModel(component, links);
    parent.components.push(component);
    alreadyProcessedNodes.push(component.key);
}


class OpenArchiFromDiagram {

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
        model.shape = {type: model.kind};
        alreadyProcessedNodes = [];
        alreadyProcessedLinks = [];
        if (!commons.prototype.isEmpty(nodes)) {
            nodes.forEach(function (node) {
                if (!alreadyProcessedNodes.includes(node.key)) {
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
                            model = diagramToArchitectureModel(model, node, links);
                            break;
                        default:
                            console.log("Still not implemented");
                    }
                }
            })
        }
        return model;
    };
}