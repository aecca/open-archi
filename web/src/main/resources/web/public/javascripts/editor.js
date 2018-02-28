// Show the diagram's model in JSON format that the user may edit
function save() {
    document.getElementById("modelToSaveOrLoad").value = myDiagram.model.toJson();
    myDiagram.isModified = false;
    myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
    //TODO: Construir y llamar a función wrapper para pasar del modelo a OpenArchi
    //TODO: LLamar al API de OPenArchi para guardar el modelo
}

function load() {
    const model = document.getElementById("modelToSaveOrLoad").value;
    //TODO: Construir y llamar a función wrapper para pasar de OpenArchi al modelo
    myDiagram.model = go.Model.fromJson(model);
    const pos = myDiagram.model.modelData.position;
    if (pos) myDiagram.initialPosition = go.Point.parse(pos);
}

function openContent(element) {
    const url = element.getAttribute("resource");
    getPageContent(url);
}

function getPageContent(url) {
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("text/html; charset=utf-8");
        }
    }).done(function (data) {
        $("#diagramsCanvas").html(data);
    });
}

function openLoadedModel(url) {
    getJsonContent(url, function (model) {
        let graphicalModel = OpenArchiWrapper.toDiagram(model);
        initEditor(graphicalModel.nodes, graphicalModel.links);
    });
}

function getJsonContent(url, callback) {
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("application/json; charset=utf-8");
        }
    }).done(function (data) {
        let jsonData = JSON.stringify(data);
        $("#modelToSaveOrLoad").val(jsonData);
        if (callback !== undefined && (typeof callback === "function")) {
            callback(data)
        }
    });
}

function openModel(type) {
    switch (type) {
        case "FLOWCHART_MODEL":
            getPageContent("/diagrams/flowchart.html");
            return true;
            break;
        case "SEQUENCE_MODEL":
            getPageContent("/diagrams/sequenceDiagram.html");
            return true;
            break;
        case "GANTT_MODEL":
            getPageContent("/diagrams/gantt.html");
            return true;
            break;
        case "ENTITY_RELATIONSHIP_MODEL":
            getPageContent("/diagrams/entityRelationship.html");
            return true;
            break;
        case "UML_CLASS_MODEL":
            getPageContent("/diagrams/umlClass.html");
            return true;
            break;
        case "BPM_MODEL":
            getPageContent("/diagrams/swimLanes.html");
            return true;
            break;
        case "ARCHITECTURE_MODEL":
            getPageContent("/diagrams/basic.html");
            return true;
            break;
        default:
            console.log("Still not implemented");
            return false;
    }
}

function openSVG() {
    let newWindow = window.open("", "newWindow");
    if (!newWindow) return;
    const newDocument = newWindow.document;
    const svg = myDiagram.makeSvg({
        document: newDocument,  // create SVG DOM in new document context
        scale: 1.5
    });
    newDocument.body.appendChild(svg);
}

function initEditor(nodeDataArray, linkDataArray) {
    const editor = go.GraphObject.make;  // for conciseness in defining templates
    let $diagramDiv = $("#diagramDiv");
    $diagramDiv.val(null);
    $diagramDiv.html("");
    $diagramDiv.html(null);
    myDiagram =
        editor(go.Diagram, "diagramDiv",  // create a Diagram for the DIV HTML element
            {
                // position the graph in the middle of the diagram
                initialContentAlignment: go.Spot.Center,

                // allow double-click in background to create a new node
                "clickCreatingTool.archetypeNodeData": {text: "Node", color: "white"},

                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {text: "Group", isGroup: true, color: "blue"},

                // enable undo & redo
                "undoManager.isEnabled": true
            });

    // Define the appearance and behavior for Nodes:

    // First, define the shared context menu for all Nodes, Links, and Groups.

    // To simplify this code we define a function for creating a context menu button:
    function makeButton(text, action, visiblePredicate) {
        return editor("ContextMenuButton",
            editor(go.TextBlock, text),
            {click: action},
            // don't bother with binding GraphObject.visible if there's no predicate
            visiblePredicate ? new go.Binding("visible", "", function (o, e) {
                return o.diagram ? visiblePredicate(o, e) : false;
            }).ofObject() : {});
    }

    // a context menu is an Adornment with a bunch of buttons in them
    const partContextMenu =
        editor(go.Adornment, "Vertical",
            makeButton("Properties",
                function (e, obj) {  // OBJ is this Button
                    const contextmenu = obj.part;  // the Button is in the context menu Adornment
                    const part = contextmenu.adornedPart;  // the adornedPart is the Part that the context menu adorns
                    // now can do something with PART, or with its data, or with the Adornment (the context menu)
                    if (part instanceof go.Link) alert(linkInfo(part.data));
                    else if (part instanceof go.Group) alert(groupInfo(contextmenu));
                    else alert(nodeInfo(part.data));
                }),
            makeButton("Cut",
                function (e, obj) {
                    e.diagram.commandHandler.cutSelection();
                },
                function (o) {
                    return o.diagram.commandHandler.canCutSelection();
                }),
            makeButton("Copy",
                function (e, obj) {
                    e.diagram.commandHandler.copySelection();
                },
                function (o) {
                    return o.diagram.commandHandler.canCopySelection();
                }),
            makeButton("Paste",
                function (e, obj) {
                    e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint);
                },
                function (o) {
                    return o.diagram.commandHandler.canPasteSelection();
                }),
            makeButton("Delete",
                function (e, obj) {
                    e.diagram.commandHandler.deleteSelection();
                },
                function (o) {
                    return o.diagram.commandHandler.canDeleteSelection();
                }),
            makeButton("Undo",
                function (e, obj) {
                    e.diagram.commandHandler.undo();
                },
                function (o) {
                    return o.diagram.commandHandler.canUndo();
                }),
            makeButton("Redo",
                function (e, obj) {
                    e.diagram.commandHandler.redo();
                },
                function (o) {
                    return o.diagram.commandHandler.canRedo();
                }),
            makeButton("Group",
                function (e, obj) {
                    e.diagram.commandHandler.groupSelection();
                },
                function (o) {
                    return o.diagram.commandHandler.canGroupSelection();
                }),
            makeButton("Ungroup",
                function (e, obj) {
                    e.diagram.commandHandler.ungroupSelection();
                },
                function (o) {
                    return o.diagram.commandHandler.canUngroupSelection();
                })
        );

    function nodeInfo(d) {  // Tooltip info for a node data object
        let str = "Node " + d.key + ": " + d.text + "\n";
        if (d.group)
            str += "member of " + d.group;
        else
            str += "top-level node";
        return str;
    }

    // These nodes have text surrounded by a rounded rectangle
    // whose fill color is bound to the node data.
    // The user can drag a node by dragging its TextBlock label.
    // Dragging from the Shape will start drawing a new link.
    myDiagram.nodeTemplate =
        editor(go.Node, "Auto",
            {locationSpot: go.Spot.Center},
            editor(go.Shape, "RoundedRectangle",
                {
                    fill: "white", // the default fill, if there is no data bound value
                    portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
                    // allow all kinds of links from and to this port
                    fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
                    toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
                },
                new go.Binding("fill", "color")),
            editor(go.TextBlock,
                {
                    font: "bold 14px sans-serif",
                    stroke: '#333',
                    margin: 6,  // make some extra space for the shape around the text
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "text").makeTwoWay()),  // the label shows the node data's text
            { // this tooltip Adornment is shared by all nodes
                toolTip:
                    editor(go.Adornment, "Auto",
                        editor(go.Shape, {fill: "#FFFFCC"}),
                        editor(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                            new go.Binding("text", "", nodeInfo))
                    ),
                // this context menu Adornment is shared by all nodes
                contextMenu: partContextMenu
            }
        );

    // Define the appearance and behavior for Links:

    function linkInfo(d) {  // Tooltip info for a link data object
        return "Link:\nfrom " + d.from + " to " + d.to;
    }

    // The link shape and arrowhead have their stroke brush data bound to the "color" property
    myDiagram.linkTemplate =
        editor(go.Link,
            {toShortLength: 3, relinkableFrom: true, relinkableTo: true},  // allow the user to relink existing links
            editor(go.Shape,
                {strokeWidth: 2},
                new go.Binding("stroke", "color")),
            editor(go.Shape,
                {toArrow: "Standard", stroke: null},
                new go.Binding("fill", "color")),
            { // this tooltip Adornment is shared by all links
                toolTip:
                    editor(go.Adornment, "Auto",
                        editor(go.Shape, {fill: "#FFFFCC"}),
                        editor(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling linkInfo(data)
                            new go.Binding("text", "", linkInfo))
                    ),
                // the same context menu Adornment is shared by all links
                contextMenu: partContextMenu
            }
        );

    // Define the appearance and behavior for Groups:

    function groupInfo(adornment) {  // takes the tooltip or context menu, not a group node data object
        const g = adornment.adornedPart;  // get the Group that the tooltip adorns
        const mems = g.memberParts.count;
        let links = 0;
        g.memberParts.each(function (part) {
            if (part instanceof go.Link) links++;
        });
        return "Group " + g.data.key + ": " + g.data.text + "\n" + mems + " members including " + links + " links";
    }

    // Groups consist of a title in the color given by the group node data
    // above a translucent gray rectangle surrounding the member parts
    myDiagram.groupTemplate =
        editor(go.Group, "Vertical",
            {
                selectionObjectName: "PANEL",  // selection handle goes around shape, not label
                ungroupable: true
            },  // enable Ctrl-Shift-G to ungroup a selected Group
            editor(go.TextBlock,
                {
                    font: "bold 19px sans-serif",
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "text").makeTwoWay(),
                new go.Binding("stroke", "color")),
            editor(go.Panel, "Auto",
                {name: "PANEL"},
                editor(go.Shape, "Rectangle",  // the rectangular shape around the members
                    {
                        fill: "rgba(128,128,128,0.2)", stroke: "gray", strokeWidth: 3,
                        portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
                        toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
                    }),
                editor(go.Placeholder, {margin: 10, background: "transparent"})  // represents where the members are
            ),
            { // this tooltip Adornment is shared by all groups
                toolTip:
                    editor(go.Adornment, "Auto",
                        editor(go.Shape, {fill: "#FFFFCC"}),
                        editor(go.TextBlock, {margin: 4},
                            // bind to tooltip, not to Group.data, to allow access to Group properties
                            new go.Binding("text", "", groupInfo).ofObject())
                    ),
                // the same context menu Adornment is shared by all groups
                contextMenu: partContextMenu
            }
        );

    // Define the behavior for the Diagram background:

    function diagramInfo(model) {  // Tooltip info for the diagram's model
        return "Model:\n" + model.nodeDataArray.length + " nodes, " + model.linkDataArray.length + " links";
    }

    // provide a tooltip for the background of the Diagram, when not over any Part
    myDiagram.toolTip =
        editor(go.Adornment, "Auto",
            editor(go.Shape, {fill: "#FFFFCC"}),
            editor(go.TextBlock, {margin: 4},
                new go.Binding("text", "", diagramInfo))
        );

    // provide a context menu for the background of the Diagram, when not over any Part
    myDiagram.contextMenu =
        editor(go.Adornment, "Vertical",
            makeButton("Paste",
                function (e, obj) {
                    e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint);
                },
                function (o) {
                    return o.diagram.commandHandler.canPasteSelection();
                }),
            makeButton("Undo",
                function (e, obj) {
                    e.diagram.commandHandler.undo();
                },
                function (o) {
                    return o.diagram.commandHandler.canUndo();
                }),
            makeButton("Redo",
                function (e, obj) {
                    e.diagram.commandHandler.redo();
                },
                function (o) {
                    return o.diagram.commandHandler.canRedo();
                })
        );

    // Create the Diagram's Model:
    myDiagram.model = new go.GraphLinksModel(nodeDataArray, linkDataArray);
}