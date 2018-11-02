class OpenArchiToDiagram {

    static process(model) {
        const type = model.kind;
        let diagram;
        alreadyProcessedNodes = [];
        alreadyProcessedLinks = [];
        switch (type) {
            case "FLOWCHART_MODEL":
                diagram = OpenArchiToDiagram.flowchartModel(model);
                break;
            case "SEQUENCE_MODEL":
                diagram = OpenArchiToDiagram.sequenceModel(model);
                break;
            case "GANTT_MODEL":
                diagram = OpenArchiToDiagram.ganttModel(model);
                break;
            case "ENTITY_RELATIONSHIP_MODEL":
                diagram = OpenArchiToDiagram.entityRelationshipModel(model);
                break;
            case "UML_CLASS_MODEL":
                diagram = OpenArchiToDiagram.umlModel(model);
                break;
            case "BPM_MODEL":
                diagram = OpenArchiToDiagram.bpmModel(model);
                break;
            case "ARCHITECTURE_MODEL":
            case "LAYER":
            case "SYSTEM":
            case "CONTAINER":
            case "COMPONENT":
                diagram = OpenArchiToDiagram.architectureModel(model);
                break;
            default:
                console.log("Still not implemented");
        }
        return diagram;
    };


    static architectureModel(model) {

        let diagram = {
            class: "go.GraphLinksModel"
        };
        if (model.kind !== "ARCHITECTURE_MODEL" && model.kind !== "LAYER" && model.kind !== "SYSTEM" && model.kind !== "CONTAINER" && model.kind !== "COMPONENT") {
            return diagram;
        }
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];

        OpenArchiToDiagram.processElement(model, diagram);

        return diagram;
    }

    static flowchartModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        diagram.nodeDataArray.push({key: -1, category: "Start", loc: "5 0", text: "Start"});
        diagram.linkDataArray.push({from: -1, to: model.id, fromPort: "B", toPort: "T"});
        diagram.nodeDataArray.push({key: model.id, loc: "5 100", text: model.name});
        diagram.linkDataArray.push({from: model.id, to: -2, fromPort: "B", toPort: "T"});
        diagram.nodeDataArray.push({key: -2, category: "End", loc: "5 200", text: "End!"});
        return diagram;
    }

    static sequenceModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        return diagram;
    }

    static ganttModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        return diagram;
    }

    static entityRelationshipModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        return diagram;
    }

    static umlModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        return diagram;
    }

    static bpmModel(model) {
        let diagram = {
            class: "go.GraphLinksModel"
        };
        diagram.nodeDataArray = [];
        diagram.linkDataArray = [];
        return diagram;
    }

    static common(model) {
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

    static processModel(model, nodes, links) {
        let modelElement = OpenArchiToDiagram.common(model);
        links.pushAll(extractLinks(model));
        //TODO Añadir campos propios del model
        let model_ = fulfill(modelElement, modelElement.isGroup);
        nodes.push(model_);
        let links_;
        links.push(links_);
    }

    static processLayer(layer, nodes, links, parentId) {
        let layerElement = OpenArchiToDiagram.common(layer);
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
                OpenArchiToDiagram.processSystem(item, nodes, links, layer.id);
            });
        }

        if (layer.containers) {
            layer.containers.forEach(item => {
                OpenArchiToDiagram.processContainer(item, nodes, links, layer.id);
            });
        }

        if (layer.components) {
            layer.components.forEach(item => {
                OpenArchiToDiagram.processComponent(item, nodes, links, layer.id);
            });
        }
    }

    static processSystem(system, nodes, links, parentId) {
        let systemElement = OpenArchiToDiagram.common(system);
        links.pushAll(extractLinks(system));
        systemElement.isGroup = true;
        //TODO Añadir campos propios del system
        //Los systems sólo se pueden agrupar en layers u otros systems
        let systems = system.systems;
        let hasSystems = systems !== undefined && !commons.prototype.isEmpty(systems);
        if (hasSystems) {
            systems.forEach(function (system_) {
                OpenArchiToDiagram.processSystem(system_, nodes, links, system.id)
            });
        }
        let containers = system.containers;
        let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
        if (hasContainers) {
            containers.forEach(function (container_) {
                OpenArchiToDiagram.processContainer(container_, nodes, links, system.id)
            });
        }
        let components = system.components;
        let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
        if (hasComponents) {
            components.forEach(function (component_) {
                OpenArchiToDiagram.processComponent(component_, nodes, links, system.id)
            });
        }
        if (parentId !== undefined) {
            systemElement.group = parentId;
        }
        nodes.push(fulfill(systemElement, true, parentId));
    }

    static processContainer(container, nodes, links, parentId) {
        let containerElement = OpenArchiToDiagram.common(container);
        links.pushAll(extractLinks(container));
        containerElement.isGroup = true;
        //TODO Añadir campos propios del container
        //Los containers sólo se pueden agrupar en layers u otros containers

        let containers = container.containers;
        let hasContainers = containers !== undefined && !commons.prototype.isEmpty(containers);
        if (hasContainers) {
            containers.forEach(function (container_) {
                OpenArchiToDiagram.processContainer(container_, nodes, links, container.id)
            });
        }

        let components = container.components;
        let hasComponents = components !== undefined && !commons.prototype.isEmpty(components);
        if (hasComponents) {
            components.forEach(function (component_) {
                OpenArchiToDiagram.processComponent(component_, nodes, links, container.id)
            });
        }
        if (parentId !== undefined) {
            containerElement.group = parentId;
        }
        nodes.push(fulfill(containerElement, true, parentId));
    }


    static processComponent(component, nodes, links, parentId) {
        let componentElement = OpenArchiToDiagram.common(component);
        links.pushAll(extractLinks(component));
        componentElement.isGroup = false;
        //TODO Añadir campos propios del component
        //Los components sólo se pueden agrupar en layers u otros components
        if (parentId !== undefined) {
            componentElement.group = parentId;
        }
        nodes.push(fulfill(componentElement, false, parentId));
    }

    static processElement(model, diagram) {
        OpenArchiToDiagram.processModel(model, diagram.nodeDataArray, diagram.linkDataArray);
        if (model.layers) {
            model.layers.forEach(layer => OpenArchiToDiagram.processLayer(layer, diagram.nodeDataArray, diagram.linkDataArray, model.id));
        }
        if (model.systems) {
            model.systems.forEach(system => OpenArchiToDiagram.processSystem(system, diagram.nodeDataArray, diagram.linkDataArray, model.id));
        }
        if (model.containers) {
            model.containers.forEach(container => OpenArchiToDiagram.processContainer(container, diagram.nodeDataArray, diagram.linkDataArray, model.id));
        }
        if (model.components) {
            model.components.forEach(component => OpenArchiToDiagram.processComponent(component, diagram.nodeDataArray, diagram.linkDataArray, model.id));
        }
        diagram.linkDataArray = removeDuplicates(diagram.linkDataArray.filter((link) => {
            return link !== undefined;
        }));
        diagram.nodeDataArray = removeDuplicates(diagram.nodeDataArray.filter((node) => {
            return node !== undefined;
        }));
    }


}