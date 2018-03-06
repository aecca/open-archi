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
                "clickCreatingTool.archetypeNodeData": {text: "Node", color: "white"},

                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {text: "Group", isGroup: true, color: "blue"},
                "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                "LinkRelinked": showLinkLabel,
                scrollsPageOnFocus: false,
                allowDrop: true,  // must be true to accept drops from the Palette
                // enable undo & redo
                "undoManager.isEnabled": true,
                nodeTemplateMap: myPalette.nodeTemplateMap  // share the templates used by myPalette
            });


    // The link shape and arrowhead have their stroke brush data bound to the "color" property
    myDiagram.linkTemplate =
        gojs(go.Link,
            {toShortLength: 3, relinkableFrom: true, relinkableTo: true},  // allow the user to relink existing links
            gojs(go.Shape,
                {strokeWidth: 2},
                new go.Binding("stroke", "color")),
            gojs(go.Shape,
                {toArrow: "Standard", stroke: null},
                new go.Binding("fill", "color")),
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
                    font: "bold 19px sans-serif",
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "text").makeTwoWay(),
                new go.Binding("stroke", "color")),
            gojs(go.Panel, "Auto",
                {name: "PANEL"},
                gojs(go.Shape, "Rectangle",  // the rectangular shape around the members
                    {
                        fill: "rgba(128,128,128,0.2)", stroke: "gray", strokeWidth: 3,
                        portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
                        toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
                    }),
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
    if (nodeDataArray !== "") {
        myDiagram.model = new go.GraphLinksModel(nodeDataArray, linkDataArray);
    }
}