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
        model.prototype = meta.isPrototyper;
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
                            model = OpenArchiFromDiagram.architectureModel(model, node, links, nodes);
                            break;
                        case "LAYER":
                        case "SYSTEM":
                        case "CONTAINER":
                        case "COMPONENT":
                            model = OpenArchiFromDiagram.architectureModel(model, node, links, nodes);
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
        object.key = node.key === undefined ? undefined : node.key.toString();
        if (object.key === -1 || object.key === undefined) {
            object.key = object.id;
        }
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
            try {
                raw = window.atob(raw.replace(/^data:image\/svg\+xml;base64,/, ""));
            } catch (err) {
                //Do nothing.
            }
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

    static processBasic(node, links) {
        let element = OpenArchiFromDiagram.common(node);
        OpenArchiFromDiagram.addLinks(element, links);
        alreadyProcessedNodes.push(element.key);
        return element;
    }

    static processInner(node, parent, links, child) {
        if (parent !== undefined) {
            let element = OpenArchiFromDiagram.processBasic(node, links);
            if (!parent[child]) {
                parent[child] = [];
            }
            parent[child].push(element);
        }
    }

    static architectureModel(model, node, links, nodes) {
        let parent;
        let returnModel = false;
        if (node !== undefined) {
            parent = findParent(node.group, model);
            if (parent === undefined) {
                parent = findParent(node.group, nodes);
                if (parent !== undefined) {
                    parent = OpenArchiFromDiagram.processBasic(parent, links);
                } else {
                    return OpenArchiFromDiagram.common(model);
                }
            } else {
                returnModel = true;
            }
            if (node.kind === "LAYER") {
                OpenArchiFromDiagram.processInner(node, parent, links, "layers");
            } else if (node.kind === "SYSTEM") {
                OpenArchiFromDiagram.processInner(node, parent, links, "systems");
            } else if (node.kind === "CONTAINER") {
                OpenArchiFromDiagram.processInner(node, parent, links, "containers");
            } else if (node.kind === "COMPONENT") {
                OpenArchiFromDiagram.processInner(node, parent, links, "components");
            }
        }
        if (returnModel) {
            return model;
        } else {
            return parent;
        }
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