function initBasic(nodeDataArray, linkDataArray) {

    if (myDiagram !== undefined) {
        myDiagram.clear();
        myDiagram.div = null;
    }
    myDiagram =
        gojs(go.Diagram, "diagramDiv",  // create a Diagram for the DIV HTML element
            {
                // position the graph in the middle of the diagram
                initialContentAlignment: go.Spot.Center,

                // allow double-click in background to create a new node
                "clickCreatingTool.archetypeNodeData": {
                    name: "Element",
                    shape: {fill: "#01203A"},
                    kind: "SYSTEM"
                },
                mouseDrop: function(e) { finishDrop(e, null); },
                layout:  // Diagram has simple horizontal layout
                    gojs(go.GridLayout,
                        { wrappingWidth: Infinity, alignment: go.GridLayout.Position, cellSize: new go.Size(1, 1) }),
                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {name: "Group", isGroup: true, color: "blue"},
                "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                "LinkRelinked": showLinkLabel,
                scrollsPageOnFocus: false,
                allowDrop: true,  // must be true to accept drops from the Palette
                // enable undo & redo
                "undoManager.isEnabled": true
            });

    myDiagram.nodeTemplateMap.addAll(myPalette.nodeTemplateMap);
    // These nodes have text surrounded by a rounded rectangle
    // whose fill color is bound to the node data.
    // The user can drag a node by dragging its TextBlock label.
    // Dragging from the Shape will start drawing a new link.
    myDiagram.nodeTemplateMap.add("",
        gojs(go.Node, "Spot", nodeStyle(),
            // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
            gojs(go.Panel, "Auto",
                {
                    name: "New Element"
                },
                gojs(go.Shape, "RoundedRectangle",
                    {
                        fill: "blue", // the default fill, if there is no data bound value
                        portId: "",
                        cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true,
                        fromLinkableSelfNode: true,
                        fromLinkableDuplicates: true,
                        toLinkable: true,
                        toLinkableSelfNode: true,
                        toLinkableDuplicates: true
                    },
                    new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill)),
                gojs(go.TextBlock, "text",
                    {
                        font: "bold 11pt Helvetica, Arial, sans-serif",
                        stroke: 'white',
                        margin: 4,  // make some extra space for the shape around the text
                        isMultiline: true,
                        wrap: go.TextBlock.WrapFit,
                        editable: true  // allow in-place editing by user
                    },
                    new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle)),  // the label shows the node data's text
                { // this tooltip Adornment is shared by all nodes
                    toolTip:
                        gojs(go.Adornment, "Auto",
                            gojs(go.Shape, {fill: "#FFFFCC"}),
                            gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                                new go.Binding("text", "", nodeInfo))
                        ),
                    // this context menu Adornment is shared by all nodes
                    contextMenu: partContextMenu
                }
            ),
            // four named ports, one on each side:
            makePort("T", go.Spot.Top, true, true),
            makePort("L", go.Spot.Left, true, true),
            makePort("R", go.Spot.Right, true, true),
            makePort("B", go.Spot.Bottom, true, true)
        ));
    // The link shape and arrowhead have their stroke brush data bound to the "color" property
    myDiagram.linkTemplate =
        gojs(go.Link,  // the whole link panel
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5, toShortLength: 4,
                relinkableFrom: true,
                relinkableTo: true,
                reshapable: true,
                resegmentable: true,
                // mouse-overs subtly highlight links:
                mouseEnter: function (e, link) {
                    link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
                },
                mouseLeave: function (e, link) {
                    link.findObject("HIGHLIGHT").stroke = "transparent";
                }
            },
            new go.Binding("points").makeTwoWay(),
            gojs(go.Shape,  // the highlight shape, normally transparent
                {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
            gojs(go.Shape,  // the link path shape
                {isPanelMain: true, stroke: "gray", strokeWidth: 2}),
            gojs(go.Shape,  // the arrowhead
                {toArrow: "standard", stroke: null, fill: "gray"}),
            gojs(go.Panel, "Auto",  // the link label, normally not visible
                {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                new go.Binding("visible", "visible").makeTwoWay(),
                gojs(go.Shape, "RoundedRectangle",  // the label shape
                    {fill: "#F8F8F8", stroke: null}),
                gojs(go.TextBlock, "Yes",  // the label
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#333333",
                        editable: true
                    },
                    new go.Binding("text", "", OpenArchiWrapper.toName).makeTwoWay(OpenArchiWrapper.fromName))
            ),
            { // this tooltip Adornment is shared by all links
                toolTip:
                    gojs(go.Adornment, "Auto",
                        gojs(go.Shape, {fill: "#FFFFCC"}),
                        gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling linkInfo(data)
                            new go.Binding("text", "", linkInfo))
                    ),
                // the same context menu Adornment is shared by all links
                contextMenu: partContextMenu
            }
        );


    // Groups consist of a title in the color given by the group node data
    // above a translucent gray rectangle surrounding the member parts
    myDiagram.groupTemplate =
        gojs(go.Group, "Vertical",
            {
                selectionObjectName: "PANEL",  // selection handle goes around shape, not label
                ungroupable: true
            },  // enable Ctrl-Shift-G to ungroup a selected Group
            gojs(go.TextBlock,
                {
                    name: "text",
                    font: "bold 19px sans-serif",
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "", OpenArchiWrapper.toName).makeTwoWay(OpenArchiWrapper.fromName),
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
            gojs(go.Panel, "Auto",
                {name: "PANEL"},
                gojs(go.Shape, "Rectangle",  // the rectangular shape around the members
                    {
                        fill: "rgba(128,128,128,0.2)",
                        stroke: "gray",
                        strokeWidth: 3,
                        portId: "",
                        cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true,
                        fromLinkableSelfNode: true,
                        fromLinkableDuplicates: true,
                        toLinkable: true,
                        toLinkableSelfNode: true,
                        toLinkableDuplicates: true
                    },
                    new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                    new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
                gojs(go.Placeholder, {margin: 10, background: "transparent"})  // represents where the members are
            ),
            { // this tooltip Adornment is shared by all groups
                toolTip:
                    gojs(go.Adornment, "Auto",
                        gojs(go.Shape, {fill: "#FFFFCC"}),
                        gojs(go.TextBlock, {margin: 4},
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
        gojs(go.Adornment, "Auto",
            gojs(go.Shape, {fill: "#FFFFCC"}),
            gojs(go.TextBlock, {margin: 4},
                new go.Binding("text", "", diagramInfo))
        );

    // provide a context menu for the background of the Diagram, when not over any Part
    myDiagram.contextMenu =
        gojs(go.Adornment, "Vertical",
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
    fillDiagram(nodeDataArray, linkDataArray);

    // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
    myDiagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
    myDiagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

    // this DiagramEvent is raised when the user has drag-and-dropped something
    // from another Diagram (a Palette in this case) into this Diagram
    myDiagram.addDiagramListener("ExternalObjectsDropped", function (e) {
        // stop any ongoing text editing
        const node = myDiagram.findNodeForKey(e.subject.first().key);
        const data = node.data;
        if (!data.kind) {
            if (myDiagram.currentTool instanceof go.TextEditingTool) {
                myDiagram.currentTool.acceptText(go.TextEditingTool.LostFocus);
            }
            let modal = $('#basic-element-data');
            modal.attr("data-key", data.key);
            modal.on('show.bs.modal', function (event) {
                const button = $(event.relatedTarget); // Button that triggered the modal
                const modal = $(this);
                modal.find('.modal-title').text('Basic element data for ' + data.name);
                $("#element-name").val(data.name)
            });
            modal.modal({
                backdrop: 'static',
                keyboard: false,
                show: true
            });
        } else {
            let currentViewMode = getCurrentViewMode();
            if (currentViewMode === "Full") {
                if (data.id) {
                    $.get("/open-archi/api/models/" + data.id + "/clone", {suffix: "cloned"})
                        .done(function (completeModel) {
                            expand(completeModel);
                            myDiagram.model.removeNodeData(data);
                        });
                }
            }
        }
    });
}

function fillDiagram(nodeDataArray, linkDataArray) {
    let nodeDataArray_ = [];
    let linkDataArray_ = [];
    if (nodeDataArray !== "") {
        if (typeof nodeDataArray === "string") {
            nodeDataArray_ = JSON.parse(nodeDataArray)
        } else {
            nodeDataArray_ = nodeDataArray;
        }
        if (typeof linkDataArray === "string") {
            linkDataArray_ = JSON.parse(linkDataArray)
        } else {
            linkDataArray_ = linkDataArray;
        }
        if (!commons.prototype.isEmpty(nodeDataArray_)) {
            myDiagram.model = new go.GraphLinksModel(nodeDataArray_, linkDataArray_);
        }
    }
}