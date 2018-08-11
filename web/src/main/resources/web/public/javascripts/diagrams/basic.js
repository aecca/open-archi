function initBasic(nodeDataArray, linkDataArray, paletteModelArray) {

    if (myPalette !== undefined) {
        myPalette.clear();
        myPalette.div = null;
    }
    // initialize the Palette that is on the left side of the page
    // noinspection JSUndeclaredVariable
    myPalette =
        gojs(go.Palette, "paletteDiv",  // must name or refer to the DIV HTML element
            {
                scrollsPageOnFocus: false,
                layout: gojs(PoolLayout, {
                    sorting: go.GridLayout.Forward
                }),
                hoverDelay: 100
            });
    myPalette.nodeTemplateMap.add("DEFAULT", getNewElementTemplate());
    myPalette.nodeTemplateMap.add("PERSON", getPersonTemplate());
    myPalette.nodeTemplateMap.add("CONSUMER", getConsumerTemplate());
    myPalette.nodeTemplateMap.add("", defaultTemplate);
    myPalette.addDiagramListener("InitialLayoutCompleted", function (diagramEvent) {
        const pdrag = document.getElementById("paletteDraggable");
        const palette = diagramEvent.diagram;
        pdrag.style.width = palette.documentBounds.width + 28 + "px"; // account for padding/borders
        pdrag.style.height = palette.documentBounds.height + 38 + "px";
    });

    myPalette.model = new go.GraphLinksModel(paletteModelArray);

    if (myDiagram !== undefined) {
        myDiagram.clear();
        myDiagram.div = null;
    }
    myDiagram =
        gojs(go.Diagram, "diagramDiv",  // create a Diagram for the DIV HTML element
            {
                // position the graph in the middle of the diagram
                initialContentAlignment: go.Spot.Center,
                hoverDelay: 100,
                // use a custom ResizingTool (along with a custom ResizeAdornment on each Group)
                resizingTool: new LaneResizingTool(),
                // use a simple layout that ignores links to stack the top-level Pool Groups next to each other
                layout: gojs(PoolLayout),
                // a clipboard copied node is pasted into the original node's group (i.e. lane).
                "commandHandler.copiesGroupKey": true,
                // automatically re-layout the swim lanes after dragging the selection
                "SelectionMoved": relayoutDiagram,  // this DiagramEvent listener is
                "SelectionCopied": relayoutDiagram, // defined above
                "animationManager.isEnabled": false,

                // allow double-click in background to create a new node
                "clickCreatingTool.archetypeNodeData": {
                    name: "Element",
                    shape: {fill: "white"},
                    kind: "LAYER"
                },
                mouseDrop: function (e) {
                    finishDrop(e, null);
                },
                /*                layout:  // Diagram has simple horizontal layout
                                    gojs(go.GridLayout,
                                        { wrappingWidth: Infinity, alignment: go.GridLayout.Position, cellSize: new go.Size(1, 1) }),*/
                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {name: "Group", isGroup: true, color: "blue"},
                "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                "LinkRelinked": showLinkLabel,
                scrollsPageOnFocus: false,
                allowDrop: true,  // must be true to accept drops from the Palette
                // enable undo & redo
                "undoManager.isEnabled": true
            });

    myDiagram.nodeTemplateMap.add("DEFAULT", getNewElementTemplate());
    myDiagram.nodeTemplateMap.add("PERSON", getPersonTemplate());
    myDiagram.nodeTemplateMap.add("CONSUMER", getConsumerTemplate());
    myDiagram.groupTemplateMap.add("ARCHITECTURE_MODEL", gojs(go.Group, "Auto",
        { // use a simple layout that ignores links to stack the "lane" Groups on top of each other

            resizable: false,
            movable: true, // allows users to re-order by dragging
            copyable: false,  // can't copy lanes or pools
            avoidable: false,  // don't impede AvoidsNodes routed Links
            minLocation: new go.Point(NaN, -Infinity),  // only allow vertical movement
            maxLocation: new go.Point(NaN, Infinity),
            layerName: "Background",  // all pools and lanes are always behind all nodes and links
            layout: gojs(PoolLayout, {spacing: new go.Size(3, 3)}),  // no space between lanes
            zOrder: 10,
            ungroupable: true,
            computesBoundsAfterDrag: true,  // needed to prevent recomputing Group.placeholder bounds too soon
            computesBoundsIncludingLinks: false,  // to reduce occurrences of links going briefly outside the lane
            computesBoundsIncludingLocation: true,  // to support empty space at top-left corner of lane
            handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
            subGraphExpandedChanged: function (grp) {
                const shp = grp.resizeObject;
                if (grp.diagram.undoManager.isUndoingRedoing) return;
                if (grp.isSubGraphExpanded) {
                    shp.height = grp._savedBreadth;
                } else {
                    grp._savedBreadth = shp.height;
                    shp.height = NaN;
                }
                updateCrossLaneLinks(grp);
            },
            contextMenu: partContextMenu,
            // the Node.location is at the center of each node
            locationSpot: go.Spot.Center,
            //isShadowed: true,
            //shadowColor: "#888",
            // handle mouse enter/leave events to show/hide the ports
            mouseEnter: function (e, obj) {
                const object = obj.findObject("SHAPE");
                object.fill = go.Brush.lighten(obj.part.background.color);
                showPorts(obj.part, true);
            },
            mouseLeave: function (e, obj) {
                const object = obj.findObject("SHAPE");
                object.fill = "white";
                showPorts(obj.part, false);
            },
        },
        new go.Binding("clonedFrom", "clonedFrom"),
        gojs(go.Shape,
            new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
            new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
            new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
            new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay()
        ),
        gojs(go.Panel, "Table",
            {
                defaultColumnSeparatorStroke: "black",
                padding: 10
            },
            gojs(go.Panel, "Horizontal",
                {
                    column: 0,
                    angle: 270
                },
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                gojs("SubGraphExpanderButton", {margin: 5}),
                gojs(go.Panel, {
                        name: "IMAGE"
                    },
                    new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),
                    new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                    gojs(go.Picture,
                        {
                            row: 0,
                            column: 1,
                            margin: 2,
                            maxSize: new go.Size(60, 60),
                            imageStretch: go.GraphObject.Uniform,
                            alignment: go.Spot.TopRight
                        },
                        new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage),
                        new go.Binding("minSize", "source", function (e) {
                            return e === undefined ? new go.Size(0, 0) : new go.Size(30, 30);
                        }).ofObject())),
                gojs(go.TextBlock, "Text",
                    {
                        font: "bold 17pt Helvetica, Arial, sans-serif",
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        editable: true
                    },
                    new go.Binding("text", "", OpenArchiWrapper.toTitle),
                    new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                    new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)
                )
            ),
            gojs(go.Placeholder,
                {column: 1, padding: 10})
        ),
        gojs(go.Panel, "Auto",  // the lane consisting of a background Shape and a Placeholder representing the subgraph
            gojs(go.Shape, "Rectangle",  // this is the resized object
                {
                    name: "SHAPE",
                    fill: "white",
                    stroke: "grey"
                },
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)
            ),
            gojs(go.Panel, "Table",
                {
                    margin: 2,
                    maxSize: new go.Size(200, NaN),
                    name: "HEADER"
                },
                new go.Binding("visible", "isSubGraphExpanded", function (e) {
                    return !e;
                }).ofObject(),
                // the two TextBlocks in column 0 both stretch in width
                // but align on the left side
                gojs(go.RowColumnDefinition,
                    {
                        column: 0,
                        stretch: go.GraphObject.Horizontal,
                        alignment: go.Spot.Left
                    }),
                gojs("SubGraphExpanderButton", {row: 0, column: 0}),  // but this remains always visible!
                gojs(go.Picture,
                    {
                        name: "IMAGE",
                        row: 0,
                        column: 1,
                        margin: 2,
                        maxSize: new go.Size(30, 30),
                        imageStretch: go.GraphObject.Uniform,
                        alignment: go.Spot.TopLeft
                    },
                    new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage),
                    new go.Binding("minSize", "source", function (e) {
                        return e === undefined ? new go.Size(0, 0) : new go.Size(30, 30);
                    }).ofObject()
                ),

                gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                    {
                        name: "LABEL",
                        editable: true,
                        row: 0,
                        column: 2,
                        angle: 0,
                        margin: 2,
                        font: "bold 13pt sans-serif",
                        alignment: go.Spot.BottomRight
                    },
                    new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle),
                    new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor)
                )
            )
        ),  // end Auto Panel
        makePort("T", go.Spot.Top),
        makePort("L", go.Spot.Left),
        makePort("R", go.Spot.Right),
        makePort("B", go.Spot.Bottom),
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
    ));

    myDiagram.groupTemplateMap.add("LAYER",        layerTemplate);

    myDiagram.groupTemplateMap.add("SYSTEM",
        gojs(go.Group, "Auto",
            {
                background: "transparent",
                // highlight when dragging into the Group
                mouseDragEnter: function (e, grp, prev) {
                    highlightGroup(e, grp, true);
                },
                mouseDragLeave: function (e, grp, next) {
                    highlightGroup(e, grp, false);
                    let selection = e.diagram.selection;
                    if (selection.size === 0) return;
                    let ok = true;
                    selection.each(selection => {
                        ok = ok && (selection.data.group === grp.key);
                    });
                    if (ok) {
                        if (!e.diagram.lastInput.shift) {
                            e.diagram.currentTool.doCancel();
                        }
                    }
                },
                ungroupable: true,
                computesBoundsAfterDrag: true,
                // when the selection is dropped into a Group, add the selected Parts into that Group;
                // if it fails, cancel the tool, rolling back any changes
                mouseDrop: function (e, grp) {
                    let selection = e.diagram.selection;
                    let ok = true;
                    selection.each(selection => {
                        ok = ok && (selection.data.group === grp.key);
                    });
                    if (ok) {
                        grp.diagram.currentTool.doCancel();
                    } else {
                        ok = (grp !== null
                            ? grp.addMembers(grp.diagram.selection, true)
                            : e.diagram.commandHandler.addTopLevelParts(selection, true));
                        if (!ok) {
                            e.diagram.currentTool.doCancel();
                        }
                    }
                },
                handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                // Groups containing Groups lay out their members horizontally
                layout:
                    gojs(go.GridLayout,
                        {
                            wrappingWidth: Infinity, alignment: go.GridLayout.Position,
                            cellSize: new go.Size(1, 1), spacing: new go.Size(12, 12)
                        })
            },
            nodeStyle(),
            new go.Binding("background", "isHighlighted", function (h) {
                return h ? "rgba(255,0,0,0.2)" : "transparent";
            }).ofObject(),
            gojs(go.Shape, "Rectangle",
                {
                    name: "SHAPE",
                    stretch: go.GraphObject.Horizontal,
                    strokeWidth: 2
                }
            ),
            gojs(go.Panel, "Vertical",  // title above Placeholder
                gojs(go.Panel, "Horizontal",  // button next to TextBlock
                    {
                        name: "HEADER",
                        stretch: go.GraphObject.Horizontal
                    },
                    new go.Binding("background", "", OpenArchiWrapper.toFill),
                    gojs("SubGraphExpanderButton",
                        {alignment: go.Spot.Right, margin: 5}),
                    gojs(go.Panel, "Table",
                        {
                            margin: 6,
                            maxSize: new go.Size(200, NaN),
                            name: "IMAGE"
                        },
                        // the two TextBlocks in column 0 both stretch in width
                        // but align on the left side
                        gojs(go.RowColumnDefinition,
                            {
                                column: 0,
                                stretch: go.GraphObject.Horizontal,
                                alignment: go.Spot.Left
                            }),
                        gojs(go.Picture,
                            {
                                name: "IMAGE",
                                row: 0,
                                column: 0,
                                margin: 2,
                                maxSize: new go.Size(30, 30),
                                imageStretch: go.GraphObject.Uniform,
                                alignment: go.Spot.TopLeft
                            },
                            new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage)),
                        gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                            {
                                name: "LABEL",
                                editable: true,
                                row: 0,
                                column: 1,
                                angle: 0,
                                margin: 2,
                                font: "bold 13pt sans-serif",
                                alignment: go.Spot.BottomRight,
                                stroke: "white"
                            },
                            new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    )
                ),  // end Horizontal Panel
                gojs(go.Placeholder,
                    {
                        padding: 10,
                        alignment: go.Spot.TopLeft
                    }
                )
            ),  // end Vertical Panel
            // four named ports, one on each side:
            makePort("T", go.Spot.Top),
            makePort("L", go.Spot.Left),
            makePort("R", go.Spot.Right),
            makePort("B", go.Spot.Bottom),
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
        ));
    myDiagram.groupTemplateMap.add("CONTAINER",
        gojs(go.Group, "Auto",
            {
                background: "transparent",
                // highlight when dragging into the Group
                mouseDragEnter: function (e, grp, prev) {
                    highlightGroup(e, grp, true);
                },
                mouseDragLeave: function (e, grp, next) {
                    highlightGroup(e, grp, false);
                    let selection = e.diagram.selection;
                    if (selection.size === 0) return;
                    let ok = true;
                    selection.each(selection => {
                        ok = ok && (selection.data.group === grp.key);
                    });
                    if (ok) {
                        if (!e.diagram.lastInput.shift) {
                            e.diagram.currentTool.doCancel();
                        }
                    }
                },
                ungroupable: true,
                computesBoundsAfterDrag: true,
                // when the selection is dropped into a Group, add the selected Parts into that Group;
                // if it fails, cancel the tool, rolling back any changes
                handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                // Groups containing Groups lay out their members horizontally
                layout:
                    gojs(go.GridLayout,
                        {
                            wrappingWidth: Infinity, alignment: go.GridLayout.Position,
                            cellSize: new go.Size(1, 1), spacing: new go.Size(12, 12)
                        }),
                mouseDrop: function (e, grp) {
                    let selection = e.diagram.selection;
                    let ok = true;
                    selection.each(selection => {
                        ok = ok && (selection.data.group === grp.key);
                    });
                    if (ok) {
                        grp.diagram.currentTool.doCancel();
                    } else {
                        ok = (grp !== null
                            ? grp.addMembers(grp.diagram.selection, true)
                            : e.diagram.commandHandler.addTopLevelParts(selection, true));
                        if (!ok) {
                            e.diagram.currentTool.doCancel();
                        }
                    }
                }
            },
            nodeStyle(),
            new go.Binding("background", "isHighlighted", function (h) {
                return h ? "rgba(255,0,0,0.2)" : "transparent";
            }).ofObject(),
            gojs(go.Shape, "Rectangle",
                {
                    name: "SHAPE",
                    stretch: go.GraphObject.Horizontal,
                    strokeWidth: 2
                },
            ),
            gojs(go.Panel, "Vertical",  // title above Placeholder
                gojs(go.Panel, "Horizontal",  // button next to TextBlock
                    {
                        name: "HEADER",
                        stretch: go.GraphObject.Horizontal
                    },
                    gojs("SubGraphExpanderButton",
                        {alignment: go.Spot.Right, margin: 5}),
                    gojs(go.Panel, "Table",
                        {
                            margin: 6,
                            maxSize: new go.Size(200, NaN),
                            name: "IMAGE"
                        },
                        // the two TextBlocks in column 0 both stretch in width
                        // but align on the left side
                        gojs(go.RowColumnDefinition,
                            {
                                column: 0,
                                stretch: go.GraphObject.Horizontal,
                                alignment: go.Spot.Left
                            }),
                        gojs(go.Picture,
                            {
                                name: "IMAGE",
                                row: 0,
                                column: 0,
                                margin: 2,
                                maxSize: new go.Size(30, 30),
                                imageStretch: go.GraphObject.Uniform,
                                alignment: go.Spot.TopLeft
                            },
                            new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage)),
                        gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                            {
                                name: "LABEL",
                                editable: true,
                                row: 0,
                                column: 1,
                                angle: 0,
                                margin: 2,
                                font: "bold 13pt sans-serif",
                                alignment: go.Spot.BottomRight,
                                stroke: "white"
                            },
                            new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    )
                ),  // end Horizontal Panel
                gojs(go.Placeholder,
                    {
                        padding: 10,
                        alignment: go.Spot.TopLeft
                    }
                )
            ),  // end Vertical Panel
            // four named ports, one on each side:
            makePort("T", go.Spot.Top),
            makePort("L", go.Spot.Left),
            makePort("R", go.Spot.Right),
            makePort("B", go.Spot.Bottom),
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
        ));

    myDiagram.nodeTemplateMap.add("COMPONENT",
        gojs(
            go.Node, "Spot",
            nodeStyle(),
            gojs(go.Panel, "Auto",
                gojs(go.Shape,
                    {
                        name: "SHAPE"
                    },
                    new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                    new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)),
                gojs(go.Panel, "Table",
                    {
                        margin: 6,
                        maxSize: new go.Size(200, NaN),
                        name: "IMAGE"
                    },
                    // the two TextBlocks in column 0 both stretch in width
                    // but align on the left side
                    gojs(go.RowColumnDefinition,
                        {
                            column: 0,
                            stretch: go.GraphObject.Horizontal,
                            alignment: go.Spot.Left
                        }),
                    gojs(go.Picture,
                        {
                            name: "IMAGE",
                            row: 0,
                            column: 0,
                            margin: 2,
                            maxSize: new go.Size(30, 30),
                            imageStretch: go.GraphObject.Uniform,
                            alignment: go.Spot.TopLeft
                        },
                        new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage)),
                    gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                        {
                            name: "LABEL",
                            editable: true,
                            row: 0,
                            column: 1,
                            angle: 0,
                            margin: 2,
                            font: "bold 13pt sans-serif",
                            alignment: go.Spot.BottomRight,
                            stroke: "white"
                        },
                        new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                        new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                )
            ),
            // three named ports, one on each side except the top, all output only:
            // four named ports, one on each side:
            makePort("T", go.Spot.Top),
            makePort("L", go.Spot.Left),
            makePort("R", go.Spot.Right),
            makePort("B", go.Spot.Bottom),
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
        ));

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
                        fill: "black", // the default fill, if there is no data bound value
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
                        margin: 4,  // make some extra space for the shape around the text
                        isMultiline: true,
                        wrap: go.TextBlock.WrapFit,
                        editable: true  // allow in-place editing by user
                    },
                    new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                    new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle)
                ),  // the label shows the node data's text
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
                new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor)),
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

    // define a custom resize adornment that has two resize handles if the group is expanded
    myDiagram.groupTemplate.resizeAdornmentTemplate =
        gojs(go.Adornment, "Spot",
            gojs(go.Placeholder),
            gojs(go.Shape,  // for changing the length of a lane
                {
                    alignment: go.Spot.Right,
                    desiredSize: new go.Size(7, 50),
                    fill: "lightblue", stroke: "dodgerblue",
                    cursor: "col-resize"
                },
                new go.Binding("visible", "", function (ad) {
                    if (ad.adornedPart === null) return false;
                    return ad.adornedPart.isSubGraphExpanded;
                }).ofObject()),
            gojs(go.Shape,  // for changing the breadth of a lane
                {
                    alignment: go.Spot.Bottom,
                    desiredSize: new go.Size(50, 7),
                    fill: "lightblue", stroke: "dodgerblue",
                    cursor: "row-resize"
                },
                new go.Binding("visible", "", function (ad) {
                    if (ad.adornedPart === null) return false;
                    return ad.adornedPart.isSubGraphExpanded;
                }).ofObject())
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
            $('#element-image-2').val('');
            $('#elementTypesDropdown').val('');
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

// this is shown by the mouseHover event handler
const nodeHoverAdornment =
    gojs(go.Adornment, "Spot",
        {
            background: "transparent",
            // hide the Adornment when the mouse leaves it
            zOrder: 2800
        },
        gojs(go.Placeholder,
            {
                background: "transparent",  // to allow this Placeholder to be "seen" by mouse events
                isActionable: true,  // needed because this is in a temporary Layer
                click: function (e, obj) {
                    const node = obj.part.adornedPart;
                    node.diagram.select(node);
                }
            })
    );

const defaultTemplate = gojs(
    go.Node, "Spot", nodeStyle(),
    new go.Binding("isGroup", "", function () {
        return false;
    }),
    new go.Binding("clonedFrom", "clonedFrom"),
    gojs(go.Panel, "Auto",
        gojs(go.Shape,
            {
                figure: "RoundedRectangle",
                stroke: "transparent"
            },
            new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
            new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)
        ),
        gojs(go.TextBlock, "Text",
            {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                margin: 8,
                maxSize: new go.Size(160, NaN),
                wrap: go.TextBlock.WrapFit,
                editable: true
            },
            new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
            new go.Binding("text", "", OpenArchiWrapper.toTitle),
            new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)),
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
    )
);

const layerTemplate = gojs(go.Group, "Horizontal",
    groupStyle(),
    {
        selectionObjectName: "SHAPE",  // selecting a lane causes the body of the lane to be highlit, not the label
        resizable: true,
        resizeObjectName: "SHAPE",  // the custom resizeAdornmentTemplate only permits two kinds of resizing
        resizeAdornmentTemplate:
            gojs(go.Adornment, "Spot",
                gojs(go.Placeholder),
                gojs(go.Shape,  // for changing the length of a lane
                    {
                        alignment: go.Spot.Right,
                        desiredSize: new go.Size(7, 50),
                        fill: "lightblue",
                        stroke: "dodgerblue",
                        cursor: "col-resize"
                    },
                    new go.Binding("visible", "", function (ad) {
                        if (ad.adornedPart === null) return false;
                        return ad.adornedPart.isSubGraphExpanded;
                    }).ofObject()),
                gojs(go.Shape,  // for changing the breadth of a lane
                    {
                        alignment: go.Spot.Bottom,
                        desiredSize: new go.Size(50, 7),
                        fill: "lightblue",
                        stroke: "dodgerblue",
                        cursor: "row-resize"
                    },
                    new go.Binding("visible", "", function (ad) {
                        if (ad.adornedPart === null) return false;
                        return ad.adornedPart.isSubGraphExpanded;
                    }).ofObject())
            ),
        layout: gojs(go.LayeredDigraphLayout,  // automatically lay out the lane's subgraph
            {
                isInitial: true,  // don't even do initial layout
                isOngoing: true,  // don't invalidate layout when nodes or links are added or removed
                direction: 90,
                columnSpacing: 10,
                layerSpacing: 10,
                layeringOption: go.LayeredDigraphLayout.LayerLongestPathSource
            }),
        zOrder: 1000,
        computesBoundsAfterDrag: true,  // needed to prevent recomputing Group.placeholder bounds too soon
        computesBoundsIncludingLinks: false,  // to reduce occurrences of links going briefly outside the lane
        computesBoundsIncludingLocation: true,  // to support empty space at top-left corner of lane
        handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
        mouseDrop: function (e, grp) {  // dropping a copy of some Nodes and Links onto this Group adds them to this Group
            let ok = true;
            e.targetDiagram.selection.each(selection => {
                ok = ok && (selection.data.group === grp.key);
            });
            if (ok) {
                grp.diagram.currentTool.doCancel();
            } else {
                ok = grp.addMembers(grp.diagram.selection, true);
            }
            if (ok) {
                //relayoutLanes();
                updateCrossLaneLinks(grp);
            } else {
                grp.diagram.currentTool.doCancel();
            }
        },
        mouseDragEnter: function (e, group, prev) {
            highlightGroup(e, group, true);
        },
        mouseDragLeave: function (e, group, next) {
            highlightGroup(e, group, false);
            let selection = e.diagram.selection;
            if (selection.size === 0) return;
            let ok = true;
            selection.each(selection => {
                ok = ok && (selection.data.group === group.key);
            });
            if (ok) {
                if (!e.diagram.lastInput.shift) {
                    e.diagram.currentTool.doCancel();
                }
            }
        },
        subGraphExpandedChanged: function (grp) {
            const shp = grp.resizeObject;
            if (grp.diagram.undoManager.isUndoingRedoing) return;
            if (grp.isSubGraphExpanded) {
                shp.height = grp._savedBreadth;
            } else {
                grp._savedBreadth = shp.height;
                shp.height = NaN;
            }
            updateCrossLaneLinks(grp);
        },
        mouseEnter: function (e, obj) {
            const object = obj.findObject("SHAPE");
            object.fill = go.Brush.lighten(obj.part.background.color);
        },
        mouseLeave: function (e, obj) {
            const object = obj.findObject("SHAPE");
            object.fill = "white";
        },
        contextMenu: partContextMenu
    },
    new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),
    // the lane header consisting of a Shape and a TextBlock
    new go.Binding("background", "isHighlighted", function (h) {
        return h ? "rgba(255,0,0,0.2)" : "transparent";
    }).ofObject(),
    gojs(go.Panel, "Horizontal",
        {
            name: "HEADER",
            angle: 270,  // maybe rotate the header to read sideways going up
            alignment: go.Spot.Center,
        },
        gojs(go.Panel, "Horizontal",  // this is hidden when the swimlane is collapsed
            new go.Binding("visible", "isSubGraphExpanded").ofObject(),
            gojs(go.Panel, {
                    name: "IMAGE"
                },
                new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),
                new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                gojs(go.Picture,
                    {
                        row: 0,
                        column: 1,
                        margin: 2,
                        maxSize: new go.Size(60, 60),
                        imageStretch: go.GraphObject.Uniform,
                        alignment: go.Spot.TopRight
                    },
                    new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage),
                    new go.Binding("minSize", "source", function (e) {
                        return e === undefined ? new go.Size(0, 0) : new go.Size(30, 30);
                    }).ofObject())),
            gojs(go.TextBlock,  // the lane label
                {
                    font: "bold 13pt sans-serif",
                    editable: true,
                    margin: new go.Margin(2, 0, 0, 0),
                    alignment: go.Spot.BottomCenter
                },
                new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
        ),
        gojs("SubGraphExpanderButton", {margin: 5})  // but this remains always visible!
    ),  // end Horizontal Panel
    gojs(go.Panel, "Auto",  // the lane consisting of a background Shape and a Placeholder representing the subgraph
        gojs(go.Shape, "Rectangle",  // this is the resized object
            {
                name: "SHAPE",
                fill: "white",
                stroke: "grey",
                strokeWidth: 4,
            },
            //new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)
        ),
        gojs(go.Placeholder,
            {
                padding: 10,
                alignment: go.Spot.TopLeft,

            }),
        gojs(go.Panel, "Table",
            {
                margin: 2,
                maxSize: new go.Size(200, NaN),
                name: "HEADER"
            },
            new go.Binding("visible", "isSubGraphExpanded", function (e) {
                return !e;
            }).ofObject(),
            // the two TextBlocks in column 0 both stretch in width
            // but align on the left side
            gojs(go.RowColumnDefinition,
                {
                    column: 0,
                    stretch: go.GraphObject.Horizontal,
                    alignment: go.Spot.Left
                }),
            gojs(go.Picture,
                {
                    name: "IMAGE",
                    row: 0,
                    column: 0,
                    margin: 2,
                    maxSize: new go.Size(30, 30),
                    imageStretch: go.GraphObject.Uniform,
                    alignment: go.Spot.TopLeft
                },
                new go.Binding("source", "", OpenArchiWrapper.toImage).makeTwoWay(OpenArchiWrapper.fromImage),
                new go.Binding("minSize", "source", function (e) {
                    return e === undefined ? new go.Size(0, 0) : new go.Size(30, 30);
                }).ofObject()
            ),

            gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                {
                    name: "LABEL",
                    editable: true,
                    row: 0,
                    column: 1,
                    angle: 0,
                    margin: 2,
                    font: "bold 13pt sans-serif",
                    alignment: go.Spot.BottomRight
                },
                new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
                new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
        )
    ),  // end Auto Panel
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
);
