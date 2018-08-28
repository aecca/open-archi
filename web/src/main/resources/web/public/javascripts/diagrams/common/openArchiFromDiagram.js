class OpenArchiFromDiagram {

    static process(diagram) {
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
                            model = OpenArchiFromDiagram.flowchartModel(model, node, links);
                            break;
                        case "SEQUENCE_MODEL":
                            model = OpenArchiFromDiagram.sequenceModel(model, node, links);
                            break;
                        case "GANTT_MODEL":
                            model = OpenArchiFromDiagram.ganttModel(model, node, links);
                            break;
                        case "ENTITY_RELATIONSHIP_MODEL":
                            model = OpenArchiFromDiagram.entityRelationshipModel(model, node, links);
                            break;
                        case "UML_CLASS_MODEL":
                            model = OpenArchiFromDiagram.umlModel(model, node, links);
                            break;
                        case "BPM_MODEL":
                            model = OpenArchiFromDiagram.bpmModel(model, node, links);
                            break;
                        case "ARCHITECTURE_MODEL":
                            model = OpenArchiFromDiagram.architectureModel(model, node, links);
                            break;
                        default:
                            console.log("Still not implemented");
                    }
                }
            })
        }
        return model;
    };

    static common(node) {
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

    static addLinks(element, links) {
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

    static processLayer(node, parent, links) {
        let layer = OpenArchiFromDiagram.common(node);
        //TODO Añadir campos propios del layer
        //Los layer sólo se pueden agrupar en el modelo directamente, no en otros layers, systems, container, o components
        if (!parent.layers) {
            parent.layers = [];
        }
        OpenArchiFromDiagram.addLinks(layer, links);
        parent.layers.push(layer);
        alreadyProcessedNodes.push(layer.key);
    }

    static processSystem(node, parent, links) {
        let system = OpenArchiFromDiagram.common(node);
        //TODO Añadir campos propios del system
        //Los systems sólo se pueden agrupar en layers u otros systems
        if (!parent.systems) {
            parent.systems = [];
        }
        OpenArchiFromDiagram.addLinks(system, links);
        parent.systems.push(system);
        alreadyProcessedNodes.push(system.key);
    }

    static processContainer(node, parent, links) {
        let container = OpenArchiFromDiagram.common(node);
        //TODO Añadir campos propios del container
        //Los containers sólo se pueden agrupar en layers u otros containers
        if (!parent.containers) {
            parent.containers = [];
        }
        OpenArchiFromDiagram.addLinks(container, links);
        parent.containers.push(container);
        alreadyProcessedNodes.push(container.key);
    }

    static processComponent(node, parent, links) {
        let component = OpenArchiFromDiagram.common(node);
        //TODO Añadir campos propios del component
        //Los components sólo se pueden agrupar en layers u otros components
        if (!parent.components) {
            parent.components = [];
        }
        OpenArchiFromDiagram.addLinks(component, links);
        parent.components.push(component);
        alreadyProcessedNodes.push(component.key);
    }

    static architectureModel(model, node, links) {
        if (node) {
            let parent = findParent(node.group, model);
            if (parent === undefined) {
                parent = model;
            }
            if (node.kind === "LAYER") {
                OpenArchiFromDiagram.processLayer(node, parent, links);
            } else if (node.kind === "SYSTEM") {
                OpenArchiFromDiagram.processSystem(node, parent, links);
            } else if (node.kind === "CONTAINER") {
                OpenArchiFromDiagram.processContainer(node, parent, links);
            } else if (node.kind === "COMPONENT") {
                OpenArchiFromDiagram.processComponent(node, parent, links);
            }
        }
        return model;
    }

    static flowchartModel(model, node, links) {
    }

    static sequenceModel(model, node, links) {
    }

    static ganttModel(model, node, links) {
    }

    static entityRelationshipModel(model, node, links) {
    }

    static umlModel(model, node, links) {
    }

    static bpmModel(model, node, links) {
    }


}