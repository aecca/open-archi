var myDiagram;
var myPalette;

function initComponentsDiagram() {
    var openArchiEditor = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        openArchiEditor(go.Diagram, "component-diagram",  // must name or refer to the DIV HTML element
            {
                initialContentAlignment: go.Spot.Center,
                allowDrop: true,  // must be true to accept drops from the Palette
                "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                "LinkRelinked": showLinkLabel,
                scrollsPageOnFocus: false,
                "undoManager.isEnabled": true,  // enable undo & redo
                // allow double-click in background to create a new node
                "clickCreatingTool.archetypeNodeData": {text: "Node", color: "white"},
                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {text: "Group", isGroup: true, color: "blue"}
            });

    // when the document is modified, add a "*" to the title and enable the "Save" button
    myDiagram.addDiagramListener("Modified", function(e) {
        var button = document.getElementById("SaveButton");
        if (button) button.disabled = !myDiagram.isModified;
        var idx = document.title.indexOf("*");
        if (myDiagram.isModified) {
            if (idx < 0) document.title += "*";
        } else {
            if (idx >= 0) document.title = document.title.substr(0, idx);
        }
    });






    var linkSelectionAdornmentTemplate =
        openArchiEditor(go.Adornment, "Link",
            openArchiEditor(go.Shape,
                // isPanelMain declares that this Shape shares the Link.geometry
                {isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0})  // use selection object's strokeWidth
        );

    // Define the appearance and behavior for Nodes:
    // First, define the shared context menu for all Nodes, Links, and Groups.
    // To simplify this code we define a function for creating a context menu button:
    function makeButton(text, action, visiblePredicate) {
        return openArchiEditor("ContextMenuButton",
            openArchiEditor(go.TextBlock, text),
            {click: action},
            // don't bother with binding GraphObject.visible if there's no predicate
            visiblePredicate ? new go.Binding("visible", "", function (o, e) {
                return o.diagram ? visiblePredicate(o, e) : false;
            }).ofObject() : {});
    }



    function nodeInfo(d) {  // Tooltip info for a node data object
        var str = "Node " + d.key + ": " + d.text + "\n";
        if (d.group)
            str += "member of " + d.group;
        else
            str += "top-level node";
        return str;
    }

    // a context menu is an Adornment with a bunch of buttons in them
    var partContextMenu =
        openArchiEditor(go.Adornment, "Vertical",
            makeButton("Properties",
                function (e, obj) {  // OBJ is this Button
                    var contextmenu = obj.part;  // the Button is in the context menu Adornment
                    var part = contextmenu.adornedPart;  // the adornedPart is the Part that the context menu adorns
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





    var nodeSelectionAdornmentTemplate =
        openArchiEditor(go.Adornment, "Auto",
            openArchiEditor(go.Shape, {fill: null, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2]}),
            openArchiEditor(go.Placeholder)
        );

    var nodeResizeAdornmentTemplate =
        openArchiEditor(go.Adornment, "Spot",
            {locationSpot: go.Spot.Right},
            openArchiEditor(go.Placeholder),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.TopLeft,
                cursor: "nw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.Top,
                cursor: "n-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.TopRight,
                cursor: "ne-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),

            openArchiEditor(go.Shape, {
                alignment: go.Spot.Left,
                cursor: "w-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.Right,
                cursor: "e-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),

            openArchiEditor(go.Shape, {
                alignment: go.Spot.BottomLeft,
                cursor: "se-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.Bottom,
                cursor: "s-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                alignment: go.Spot.BottomRight,
                cursor: "sw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            })
        );

    var nodeRotateAdornmentTemplate =
        openArchiEditor(go.Adornment,
            {locationSpot: go.Spot.Center, locationObjectName: "CIRCLE"},
            openArchiEditor(go.Shape, "Circle", {
                name: "CIRCLE",
                cursor: "pointer",
                desiredSize: new go.Size(7, 7),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            openArchiEditor(go.Shape, {
                geometryString: "M3.5 7 L3.5 30",
                isGeometryPositioned: true,
                stroke: "deepskyblue",
                strokeWidth: 1.5,
                strokeDashArray: [4, 2]
            })
        );

    function showSmallPorts(node, show) {
        node.ports.each(function (port) {
            if (port.portId !== "") {  // don't change the default port, which is the big shape
                port.fill = show ? "rgba(0,0,0,.3)" : null;
            }
        });
    }

    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makePort(name, spot, output, input) {
        // the port is basically just a small transparent square
        return openArchiEditor(go.Shape, "Circle",
            {
                fill: null,  // not seen, by default; set to a translucent gray by showSmallPorts, defined below
                stroke: null,
                desiredSize: new go.Size(7, 7),
                alignment: spot,  // align the port on the main Shape
                alignmentFocus: spot,  // just inside the Shape
                portId: name,  // declare this object to be a "port"
                fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
                fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
                cursor: "pointer"  // show a different cursor to indicate potential link point
            });
    }

    // helper definitions for node templates

    function nodeStyle() {
        return [
            // The Node.location comes from the "loc" property of the node data,
            // converted by the Point.parse static method.
            // If the Node.location is changed, it updates the "loc" property of the node data,
            // converting back using the Point.stringify static method.
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            {
                // the Node.location is at the center of each node
                locationSpot: go.Spot.Center,
                //isShadowed: true,
                //shadowColor: "#888",
                // handle mouse enter/leave events to show/hide the ports
                mouseEnter: function (e, obj) { showPorts(obj.part, true); },
                mouseLeave: function (e, obj) { showPorts(obj.part, false); }
            }
        ];
    }

    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makePort(name, spot, output, input) {
        // the port is basically just a small circle that has a white stroke when it is made visible
        return openArchiEditor(go.Shape, "Circle",
            {
                fill: "transparent",
                stroke: null,  // this is changed to "white" in the showPorts function
                desiredSize: new go.Size(8, 8),
                alignment: spot, alignmentFocus: spot,  // align the port on the main Shape
                portId: name,  // declare this object to be a "port"
                fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
                fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
                cursor: "pointer"  // show a different cursor to indicate potential link point
            });
    }

    // define the Node templates for regular nodes

    var lightText = 'whitesmoke';

    myDiagram.nodeTemplate =
        openArchiEditor(go.Node, "Auto",
            {locationSpot: go.Spot.Center},
            { // this tooltip Adornment is shared by all nodes
                toolTip:
                    openArchiEditor(go.Adornment, "Auto",
                        openArchiEditor(go.Shape, {fill: "#FFFFCC"}),
                        openArchiEditor(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                            new go.Binding("text", "", nodeInfo))
                    ),
                // this context menu Adornment is shared by all nodes
                contextMenu: partContextMenu
            }
        );


    myDiagram.nodeTemplateMap.add("",  // the default category
        openArchiEditor(go.Node, "Spot", nodeStyle(),
            // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
            openArchiEditor(go.Panel, "Auto",
                openArchiEditor(go.Shape, "Rectangle",
                    { fill: "#00A9C9", stroke: null },
                    new go.Binding("figure", "figure")),
                openArchiEditor(go.TextBlock,
                    {
                        font: "bold 11pt Helvetica, Arial, sans-serif",
                        stroke: lightText,
                        margin: 8,
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        editable: true
                    },
                    new go.Binding("text").makeTwoWay())
            ),
            // four named ports, one on each side:
            makePort("T", go.Spot.Top, false, true),
            makePort("L", go.Spot.Left, true, true),
            makePort("R", go.Spot.Right, true, true),
            makePort("B", go.Spot.Bottom, true, false)
        ));

    myDiagram.nodeTemplateMap.add("Start",
        openArchiEditor(go.Node, "Spot", nodeStyle(),
            openArchiEditor(go.Panel, "Auto",
                openArchiEditor(go.Shape, "Circle",
                    { minSize: new go.Size(40, 40), fill: "#79C900", stroke: null }),
                openArchiEditor(go.TextBlock, "Start",
                    { font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText },
                    new go.Binding("text"))
            ),
            // three named ports, one on each side except the top, all output only:
            makePort("L", go.Spot.Left, true, false),
            makePort("R", go.Spot.Right, true, false),
            makePort("B", go.Spot.Bottom, true, false)
        ));

    myDiagram.nodeTemplateMap.add("End",
        openArchiEditor(go.Node, "Spot", nodeStyle(),
            openArchiEditor(go.Panel, "Auto",
                openArchiEditor(go.Shape, "Circle",
                    { minSize: new go.Size(40, 40), fill: "#DC3C00", stroke: null }),
                openArchiEditor(go.TextBlock, "End",
                    { font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText },
                    new go.Binding("text"))
            ),
            // three named ports, one on each side except the bottom, all input only:
            makePort("T", go.Spot.Top, false, true),
            makePort("L", go.Spot.Left, false, true),
            makePort("R", go.Spot.Right, false, true)
        ));

    myDiagram.nodeTemplateMap.add("Comment",
        openArchiEditor(go.Node, "Auto", nodeStyle(),
            openArchiEditor(go.Shape, "File",
                { fill: "#EFFAB4", stroke: null }),
            openArchiEditor(go.TextBlock,
                {
                    margin: 5,
                    maxSize: new go.Size(200, NaN),
                    wrap: go.TextBlock.WrapFit,
                    textAlign: "center",
                    editable: true,
                    font: "bold 12pt Helvetica, Arial, sans-serif",
                    stroke: '#454545'
                },
                new go.Binding("text").makeTwoWay())
            // no ports, because no links are allowed to connect with a comment
        ));


    // replace the default Link template in the linkTemplateMap
    myDiagram.linkTemplate =
        openArchiEditor(go.Link,  // the whole link panel
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5, toShortLength: 4,
                relinkableFrom: true,
                relinkableTo: true,
                reshapable: true,
                resegmentable: true,
                // mouse-overs subtly highlight links:
                mouseEnter: function(e, link) { link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)"; },
                mouseLeave: function(e, link) { link.findObject("HIGHLIGHT").stroke = "transparent"; }
            },
            new go.Binding("points").makeTwoWay(),
            openArchiEditor(go.Shape,  // the highlight shape, normally transparent
                { isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT" }),
            openArchiEditor(go.Shape,  // the link path shape
                { isPanelMain: true, stroke: "gray", strokeWidth: 2 }),
            openArchiEditor(go.Shape,  // the arrowhead
                { toArrow: "standard", stroke: null, fill: "gray"}),
            openArchiEditor(go.Panel, "Auto",  // the link label, normally not visible
                { visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                new go.Binding("visible", "visible").makeTwoWay(),
                openArchiEditor(go.Shape, "RoundedRectangle",  // the label shape
                    { fill: "#F8F8F8", stroke: null }),
                openArchiEditor(go.TextBlock, "Yes",  // the label
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#333333",
                        editable: true
                    },
                    new go.Binding("text").makeTwoWay()),
                { // this tooltip Adornment is shared by all links
                    toolTip:
                        openArchiEditor(go.Adornment, "Auto",
                            openArchiEditor(go.Shape, {fill: "#FFFFCC"}),
                            openArchiEditor(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling linkInfo(data)
                                new go.Binding("text", "", linkInfo))
                        ),
                    // the same context menu Adornment is shared by all links
                    contextMenu: partContextMenu
                }
            )
        );



    // Define the appearance and behavior for Groups:
    function groupInfo(adornment) {  // takes the tooltip or context menu, not a group node data object
        var g = adornment.adornedPart;  // get the Group that the tooltip adorns
        var mems = g.memberParts.count;
        var links = 0;
        g.memberParts.each(function (part) {
            if (part instanceof go.Link) links++;
        });
        return "Group " + g.data.key + ": " + g.data.text + "\n" + mems + " members including " + links + " links";
    }

    // Groups consist of a title in the color given by the group node data
    // above a translucent gray rectangle surrounding the member parts
    myDiagram.groupTemplate =
        openArchiEditor(go.Group, "Vertical",
            {
                selectionObjectName: "PANEL",  // selection handle goes around shape, not label
                ungroupable: true
            },  // enable Ctrl-Shift-G to ungroup a selected Group
            openArchiEditor(go.TextBlock,
                {
                    font: "bold 19px sans-serif",
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "text").makeTwoWay(),
                new go.Binding("stroke", "color")),
            openArchiEditor(go.Panel, "Auto",
                {name: "PANEL"},
                openArchiEditor(go.Shape, "Rectangle",  // the rectangular shape around the members
                    {
                        fill: "rgba(128,128,128,0.2)", stroke: "gray", strokeWidth: 3,
                        portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
                        toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
                    }),
                openArchiEditor(go.Placeholder, {margin: 10, background: "transparent"})  // represents where the members are
            ),
            { // this tooltip Adornment is shared by all groups
                toolTip:
                    openArchiEditor(go.Adornment, "Auto",
                        openArchiEditor(go.Shape, {fill: "#FFFFCC"}),
                        openArchiEditor(go.TextBlock, {margin: 4},
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
        openArchiEditor(go.Adornment, "Auto",
            openArchiEditor(go.Shape, {fill: "#FFFFCC"}),
            openArchiEditor(go.TextBlock, {margin: 4},
                new go.Binding("text", "", diagramInfo))
        );
    // provide a context menu for the background of the Diagram, when not over any Part
    myDiagram.contextMenu =
        openArchiEditor(go.Adornment, "Vertical",
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

    // Define the appearance and behavior for Links:
    function linkInfo(d) {  // Tooltip info for a link data object
        return "Link:\nfrom " + d.from + " to " + d.to;
    }

    // Make link labels visible if coming out of a "conditional" node.
    // This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
    function showLinkLabel(e) {
        var label = e.subject.findObject("LABEL");
        if (label !== null) label.visible = (e.subject.fromNode.data.figure === "Diamond");
    }

    // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
    myDiagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
    myDiagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

    load();  // load an initial diagram from some JSON text

    // initialize the Palette that is on the left side of the page
    myPalette =
        openArchiEditor(go.Palette, "palette",  // must name or refer to the DIV HTML element
            {
                scrollsPageOnFocus: false,
                nodeTemplateMap: myDiagram.nodeTemplateMap,  // share the templates used by myDiagram
                model: new go.GraphLinksModel([  // specify the contents of the Palette
                    { category: "Start", text: "Start" },
                    { text: "Step" },
                    { text: "???", figure: "Diamond" },
                    { category: "End", text: "End" },
                    { category: "Comment", text: "Comment" }
                ])
            });
} // end init

// Make all ports on a node visible when the mouse is over the node
function showPorts(node, show) {
    var diagram = node.diagram;
    if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
    node.ports.each(function(port) {
        port.stroke = (show ? "white" : null);
    });
}


// Show the diagram's model in JSON format that the user may edit
function save() {
    document.getElementById("savedModel").value = myDiagram.model.toJson();
    myDiagram.isModified = false;
}
function load() {
    myDiagram.model = go.Model.fromJson(document.getElementById("savedModel").value);
}

// add an SVG rendering of the diagram at the end of this page
function makeSVG() {
    var svg = myDiagram.makeSvg({
        scale: 0.5
    });
    svg.style.border = "1px solid black";
    obj = document.getElementById("SVGArea");
    obj.appendChild(svg);
    if (obj.children.length > 0) {
        obj.replaceChild(svg, obj.children[0]);
    }
}
