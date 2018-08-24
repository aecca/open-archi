let alreadyProcessedNodes;
let alreadyProcessedLinks;

function commonInnerModelElement(model) {
    let object = {};
    object.id = model.id;
    object.key = model.id;
    object.status = model.status;
    object.name = model.name;
    object.kind = model.kind;
    object.description = model.description;
    object.prototype = model.prototype;
    let loc = model.location;
    if (loc) {
        object.loc = loc.x + " " + loc.y;
    }
    let shape = model.shape;
    if (shape) {
        object.category = shape.type;
        object.size = shape.size.width + " " + shape.size.height;
        object.shape = shape;
        object.figure = shape.figure;
        object.fill = shape.fill;
        object.stroke = shape.stroke;
    } else {
        object.category = model.kind;
        object.figure = (model.figure === undefined || model.figure === "") ? "RoundedRectangle" : model.figure;
    }
    if (model.clonedFrom) {
        object.clonedFrom = model.clonedFrom;
    }
    let image = model.image;
    if (image) {
        let raw = image.raw;
        raw = "data:image/svg+xml;base64," + window.btoa(raw);
        object.image = {raw: raw, type: image.type};
    }
    object.isGroup = model.isGroup;
    return object;
}

function processModelToDiagram(model, nodes, links) {
    let modelElement = commonInnerModelElement(model);
    links.pushAll(extractLinks(model));
    //TODO Añadir campos propios del model
    let model_ = fulfill(modelElement, modelElement.isGroup);
    nodes.push(model_);
    let links_;
    links.push(links_);
}

function processLayerToDiagram(layer, nodes, links, parentId) {
    let layerElement = commonInnerModelElement(layer);
    links.pushAll(extractLinks(layer));
    layerElement.isGroup = true;
    //TODO Añadir campos propios del layer
    //Los layer sólo se pueden agrupar en el modelo directamente, no en otros layers, systems, containers o components
    if (parentId !== undefined) {
        layerElement.group = parentId;
    }
    nodes.push(layerElement);

    if (layer.systems) {
        layer.systems.forEach(item => {
            processSystemToDiagram(item, nodes, links, layer.id);
        });
    }

    if (layer.containers) {
        layer.containers.forEach(item => {
            processContainerToDiagram(item, nodes, links, layer.id);
        });
    }

    if (layer.components) {
        layer.components.forEach(item => {
            processComponentToDiagram(item, nodes, links, layer.id);
        });
    }
}

function processSystemToDiagram(system, nodes, links, parentId) {
    let systemElement = commonInnerModelElement(system);
    links.pushAll(extractLinks(system));
    systemElement.isGroup = true;
    //TODO Añadir campos propios del system
    //Los systems sólo se pueden agrupar en layers u otros systems
    let systems = system.systems;
    let hasSystems = systems !== undefined && !commons.prototype.isEmpty(systems);
    if (hasSystems) {
        systems.forEach(function (system_) {
            processSystemToDiagram(system_, nodes, links, system.id)
        });
    }
    let containers = system.containers;
    let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
    if (hasContainers) {
        containers.forEach(function (container) {
            processContainerToDiagram(container, nodes, links, system.id)
        });
    }
    let components = system.components;
    let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
    if (hasComponents) {
        components.forEach(function (component) {
            processComponentToDiagram(component, nodes, links, system.id)
        });
    }
    if (parentId !== undefined) {
        systemElement.group = parentId;
    }
    nodes.push(fulfill(systemElement, true, parentId));
}

function processContainerToDiagram(container, nodes, links, parentId) {
    let containerElement = commonInnerModelElement(container);
    links.pushAll(extractLinks(container));
    containerElement.isGroup = true;
    //TODO Añadir campos propios del container
    //Los containers sólo se pueden agrupar en layers u otros containers
    let components = container.components;
    let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
    if (hasComponents) {
        components.forEach(function (component) {
            processComponentToDiagram(component, nodes, links, container.id)
        });
    }
    if (parentId !== undefined) {
        containerElement.group = parentId;
    }
    nodes.push(fulfill(containerElement, true, parentId));
}


function processComponentToDiagram(component, nodes, links, parentId) {
    let componentElement = commonInnerModelElement(component);
    links.pushAll(extractLinks(component));
    componentElement.isGroup = false;
    //TODO Añadir campos propios del component
    //Los components sólo se pueden agrupar en layers u otros components
    if (parentId !== undefined) {
        componentElement.group = parentId;
    }
    nodes.push(fulfill(componentElement, false, parentId));
}

function processElementToDiagram(model, diagram) {
    processModelToDiagram(model, diagram.nodeDataArray, diagram.linkDataArray);
    if (model.layers) {
        model.layers.forEach(layer => processLayerToDiagram(layer, diagram.nodeDataArray, diagram.linkDataArray, model.id));
    }
    if (model.systems) {
        model.systems.forEach(system => processSystemToDiagram(system, diagram.nodeDataArray, diagram.linkDataArray, model.id));
    }
    if (model.containers) {
        model.containers.forEach(container => processContainerToDiagram(container, diagram.nodeDataArray, diagram.linkDataArray, model.id));
    }
    if (model.components) {
        model.components.forEach(component => processComponentToDiagram(component, diagram.nodeDataArray, diagram.linkDataArray, model.id));
    }
    diagram.linkDataArray = removeDuplicates(diagram.linkDataArray.filter((link) => {
        return link !== undefined;
    }));
    diagram.nodeDataArray = removeDuplicates(diagram.nodeDataArray.filter((node) => {
        return node !== undefined;
    }));
}

function architectureModelToDiagram(model) {

    let diagram = {
        class: "go.GraphLinksModel"
    };
    if (model.kind !== "ARCHITECTURE_MODEL" && model.kind !== "LAYER" && model.kind !== "SYSTEM" && model.kind !== "CONTAINER" && model.kind !== "COMPONENT") {
        return diagram;
    }
    diagram.nodeDataArray = [];
    diagram.linkDataArray = [];

    processElementToDiagram(model, diagram);

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

class OpenArchiToDiagram {

    static toDiagram(model) {
        const type = model.kind;
        let diagram;
        alreadyProcessedNodes = [];
        alreadyProcessedLinks = [];
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
            case "LAYER":
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

}