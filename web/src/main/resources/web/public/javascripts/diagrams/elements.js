const leftRoundedRectangleGeometry = new go.Geometry.parse("m 17.867994,143.04045 h 78.241283 v 48.43189 H 17.867994 c -1.726116,0 -3.115732,-1.338 -3.115732,-3 v -42.43189 c 0,-1.662 1.389616,-3 3.115732,-3 z", false);
const rightRoundedRectangleGeometry = new go.Geometry.parse("M 92.993545,143.04045 H 14.752262 v 48.43189 h 78.241283 c 1.726116,0 3.115732,-1.338 3.115732,-3 v -42.43189 c 0,-1.662 -1.389616,-3 -3.115732,-3 z", false);

go.Shape.defineFigureGenerator("RoundedTopRectangle", function (shape, w, h) {
    // this figure takes one parameter, the size of the corner
    var p1 = 5;  // default corner size
    if (shape !== null) {
        var param1 = shape.parameter1;
        if (!isNaN(param1) && param1 >= 0) p1 = param1;  // can't be negative or NaN
    }
    p1 = Math.min(p1, w / 2);
    p1 = Math.min(p1, h / 2);  // limit by whole height or by half height?
    var geo = new go.Geometry();
    // a single figure consisting of straight lines and quarter-circle arcs
    geo.add(new go.PathFigure(0, p1)
        .add(new go.PathSegment(go.PathSegment.Arc, 180, 90, p1, p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, w - p1, 0))
        .add(new go.PathSegment(go.PathSegment.Arc, 270, 90, w - p1, p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, w, h))
        .add(new go.PathSegment(go.PathSegment.Line, 0, h).close()));
    // don't intersect with two top corners when used in an "Auto" Panel
    geo.spot1 = new go.Spot(0, 0, 0.3 * p1, 0.3 * p1);
    geo.spot2 = new go.Spot(1, 1, -0.3 * p1, 0);
    return geo;
});

go.Shape.defineFigureGenerator("RoundedBottomRectangle", function (shape, w, h) {
    // this figure takes one parameter, the size of the corner
    var p1 = 5;  // default corner size
    if (shape !== null) {
        var param1 = shape.parameter1;
        if (!isNaN(param1) && param1 >= 0) p1 = param1;  // can't be negative or NaN
    }
    p1 = Math.min(p1, w / 2);
    p1 = Math.min(p1, h / 2);  // limit by whole height or by half height?
    var geo = new go.Geometry();
    // a single figure consisting of straight lines and quarter-circle arcs
    geo.add(new go.PathFigure(0, 0)
        .add(new go.PathSegment(go.PathSegment.Line, w, 0))
        .add(new go.PathSegment(go.PathSegment.Line, w, h - p1))
        .add(new go.PathSegment(go.PathSegment.Arc, 0, 90, w - p1, h - p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, p1, h))
        .add(new go.PathSegment(go.PathSegment.Arc, 90, 90, p1, h - p1, p1, p1).close()));
    // don't intersect with two bottom corners when used in an "Auto" Panel
    geo.spot1 = new go.Spot(0, 0, 0.3 * p1, 0);
    geo.spot2 = new go.Spot(1, 1, -0.3 * p1, -0.3 * p1);
    return geo;
});

function getNodeByType(paletteModel, suffix) {
    let type = paletteModel.shape !== undefined ? paletteModel.shape.type : paletteModel.kind;
    if (suffix) {
        type = type + suffix;
    }
    switch (type) {
        case "ARCHITECTURE_MODEL":
            return gojs(go.Group, "Auto",
                { // use a simple layout that ignores links to stack the "lane" Groups on top of each other
                    selectionObjectName: "SHAPE",  // selecting a lane causes the body of the lane to be highlit, not the label
                    resizable: false,
                    resizeObjectName: "SHAPE",  // the custom resizeAdornmentTemplate only permits two kinds of resizing
                    movable: true, // allows users to re-order by dragging
                    copyable: false,  // can't copy lanes or pools
                    avoidable: false,  // don't impede AvoidsNodes routed Links
                    minLocation: new go.Point(NaN, -Infinity),  // only allow vertical movement
                    maxLocation: new go.Point(NaN, Infinity),
                    layerName: "Background",  // all pools and lanes are always behind all nodes and links
                    layout: gojs(PoolLayout, {spacing: new go.Size(3, 3)}),  // no space between lanes
                    mouseDragEnter: function (e, grp, prev) {
                        highlightGroup(e, grp, true);
                    },
                    mouseDragLeave: function (e, grp, next) {
                        highlightGroup(e, grp, false);
                    },
                    zOrder: 100,
                    ungroupable: true,
                    computesBoundsAfterDrag: true,  // needed to prevent recomputing Group.placeholder bounds too soon
                    computesBoundsIncludingLinks: false,  // to reduce occurrences of links going briefly outside the lane
                    computesBoundsIncludingLocation: true,  // to support empty space at top-left corner of lane
                    handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                    mouseDrop: finishDrop,
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
                        showPorts(obj.part, true);
                        obj.part.background = "rgba(240, 173, 75,0.2)";
                    },
                    mouseLeave: function (e, obj) {
                        showPorts(obj.part, false);
                        obj.part.background = "transparent";
                    }
                },
                new go.Binding("location", "", OpenArchiWrapper.toLocation).makeTwoWay(OpenArchiWrapper.fromLocation),
                new go.Binding("background", "isHighlighted", function (h) {
                    return h ? "rgba(255,0,0,0.2)" : "transparent";
                }).ofObject(),
                gojs(go.Shape,
                    {
                        fill: paletteModel.shape !== undefined ? paletteModel.shape.fill : "white",
                        stroke: paletteModel.shape !== undefined ? paletteModel.shape.stroke : "black",
                        minSize: OpenArchiWrapper.toSize(paletteModel)
                    },
                    new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                    new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                    new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                    new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
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
                        gojs(go.TextBlock, "Text",
                            {
                                text: paletteModel.name,
                                font: "bold 11pt Helvetica, Arial, sans-serif",
                                stroke: "black",
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,
                                editable: true
                            },
                            new go.Binding("text", "", OpenArchiWrapper.toTitle),
                            new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
                            new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)),
                    ),
                    gojs(go.Placeholder,
                        {column: 1, padding: 5})
                ),
                makePort("T", go.Spot.Top, paletteModel.shape.input, paletteModel.shape.output),
                makePort("L", go.Spot.Left, paletteModel.shape.input, paletteModel.shape.output),
                makePort("R", go.Spot.Right, paletteModel.shape.input, paletteModel.shape.output),
                makePort("B", go.Spot.Bottom, paletteModel.shape.input, paletteModel.shape.output)
            );
            break;
        case "ARCHITECTURE_MODEL_PALETTE":
            return gojs(
                go.Node, "Spot", nodeStyle(),
                {
                    name: paletteModel.name
                },
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            fill: paletteModel.shape !== undefined ? paletteModel.shape.fill : "white",
                            stroke: paletteModel.shape !== undefined ? paletteModel.shape.stroke : "black",
                            minSize: OpenArchiWrapper.toSize(paletteModel)
                        },
                        new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                        new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                        new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
                    gojs(go.TextBlock, "Text",
                        {
                            text: paletteModel.name,
                            font: "bold 11pt Helvetica, Arial, sans-serif",
                            stroke: "black",
                            margin: 8,
                            maxSize: new go.Size(160, NaN),
                            wrap: go.TextBlock.WrapFit,
                            editable: true
                        },
                        new go.Binding("text", "", OpenArchiWrapper.toTitle),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
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
            break;
        case "SYSTEM":
        case "System":
            return gojs(go.Group, "Auto",
                {
                    background: "transparent",
                    // highlight when dragging into the Group
                    mouseDragEnter: function (e, grp, prev) {
                        highlightGroup(e, grp, true);
                    },
                    mouseDragLeave: function (e, grp, next) {
                        highlightGroup(e, grp, false);
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
                        fill: null,
                        stroke: "#01203A",
                        stretch: go.GraphObject.Horizontal,
                        strokeWidth: 2
                    },
                ),
                gojs(go.Panel, "Vertical",  // title above Placeholder
                    gojs(go.Panel, "Horizontal",  // button next to TextBlock
                        {
                            name: "HEADER",
                            stretch: go.GraphObject.Horizontal,
                            background: "#01203A"
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
                                new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                        )
                    ),  // end Horizontal Panel
                    gojs(go.Placeholder,
                        {
                            padding: 5,
                            alignment: go.Spot.TopLeft
                        }
                    )
                ),  // end Vertical Panel
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            break;
        case "CONTAINER":
        case "Container":
            return gojs(go.Group, "Auto",
                {
                    background: "transparent",
                    // highlight when dragging into the Group
                    mouseDragEnter: function (e, grp, prev) {
                        highlightGroup(e, grp, true);
                    },
                    mouseDragLeave: function (e, grp, next) {
                        highlightGroup(e, grp, false);
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
                        fill: null,
                        stroke: "#08427B",
                        stretch: go.GraphObject.Horizontal,
                        strokeWidth: 2
                    },
                ),
                gojs(go.Panel, "Vertical",  // title above Placeholder
                    gojs(go.Panel, "Horizontal",  // button next to TextBlock
                        {
                            name: "HEADER",
                            stretch: go.GraphObject.Horizontal,
                            background: "#08427B"
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
                                new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                        )
                    ),  // end Horizontal Panel
                    gojs(go.Placeholder,
                        {
                            padding: 5,
                            alignment: go.Spot.TopLeft
                        }
                    )
                ),  // end Vertical Panel
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            break;
        case "COMPONENT":
        case "Component":
            return gojs(
                go.Node, "Spot", nodeStyle(),
                {
                    name: paletteModel.name
                },
                nodeStyle(),
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            fill: "#1368BD",
                            stroke: "#1368BD",
                            minSize: OpenArchiWrapper.toSize(paletteModel)
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
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    )
                ),
                // three named ports, one on each side except the top, all output only:
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            break;
        case "Layer":
        case "LAYER":
            return gojs(go.Group, "Horizontal", groupStyle(),
                {
                    selectionObjectName: "SHAPE",  // selecting a lane causes the body of the lane to be highlit, not the label
                    resizable: true, resizeObjectName: "SHAPE",  // the custom resizeAdornmentTemplate only permits two kinds of resizing
                    resizeAdornmentTemplate:
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
                            relayoutLanes();
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
                        obj.part.background = "rgba(240, 173, 75,0.2)";
                    },
                    mouseLeave: function (e, obj) {
                        obj.part.background = "transparent";
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
                        alignment: go.Spot.Center
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
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    ),
                    gojs("SubGraphExpanderButton", {margin: 5})  // but this remains always visible!
                ),  // end Horizontal Panel
                gojs(go.Panel, "Auto",  // the lane consisting of a background Shape and a Placeholder representing the subgraph
                    gojs(go.Shape, "Rectangle",  // this is the resized object
                        {
                            name: "SHAPE",
                            fill: "white"
                        },
                        new go.Binding("fill", "color"),
                        new go.Binding("minSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
                    gojs(go.Placeholder,
                        {
                            padding: 12,
                            alignment: go.Spot.TopLeft
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
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    )
                )  // end Auto Panel
            );  // end Layer
            break;
        default:
            return gojs(
                go.Node, "Spot", nodeStyle(),
                {
                    name: paletteModel.name
                },
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            fill: paletteModel.shape !== undefined ? paletteModel.shape.fill : "",
                            stroke: paletteModel.shape !== undefined ? paletteModel.shape.stroke : "",
                            minSize: OpenArchiWrapper.toSize(paletteModel)
                        },
                        new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                        new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                        new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
                    gojs(go.TextBlock, "Text",
                        {
                            text: paletteModel.name,
                            font: "bold 11pt Helvetica, Arial, sans-serif",
                            stroke: lightText,
                            margin: 8,
                            maxSize: new go.Size(160, NaN),
                            wrap: go.TextBlock.WrapFit,
                            editable: true
                        },
                        new go.Binding("text", "", OpenArchiWrapper.toTitle),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
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
                ),
                // three named ports, one on each side except the top, all output only:
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, paletteModel.shape.input, paletteModel.shape.output),
                makePort("L", go.Spot.Left, paletteModel.shape.input, paletteModel.shape.output),
                makePort("R", go.Spot.Right, paletteModel.shape.input, paletteModel.shape.output),
                makePort("B", go.Spot.Bottom, paletteModel.shape.input, paletteModel.shape.output)
            );
            break;
    }
}
