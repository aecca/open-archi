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

function getNodeByType(paletteModel) {
    let type = paletteModel.shape !== undefined ? paletteModel.shape.type : paletteModel.kind;
    switch (type) {
        case "SOFTWARE_SYSTEM":
        case "SoftwareSystem":
            let node =
                gojs(go.Group, "Auto",
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
                        mouseDrop: finishDrop,
                        handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                        // Groups containing Groups lay out their members horizontally
                        layout:
                            gojs(go.GridLayout,
                                {
                                    wrappingWidth: Infinity, alignment: go.GridLayout.Position,
                                    cellSize: new go.Size(1, 1), spacing: new go.Size(4, 4)
                                })
                    },
                    new go.Binding("background", "isHighlighted", function (h) {
                        return h ? "rgba(255,0,0,0.2)" : "transparent";
                    }).ofObject(),
                    gojs(go.Shape, "RoundedRectangle",
                        {fill: null, stroke: "#000085", strokeWidth: 2}),
                    gojs(go.Panel, "Vertical",  // title above Placeholder
                        gojs(go.Panel, "Auto",  // button next to TextBlock
                            {
                                background: "transparent"
                            },
                            gojs(go.Shape, "RoundedTopRectangle",
                                {
                                    fill: "#000085"
                                }
                            ),
                            gojs(go.Panel, "Horizontal",
                                {
                                    stretch: go.GraphObject.Horizontal,
                                    background: "transparent"
                                },
                                gojs("SubGraphExpanderButton",
                                    {alignment: go.Spot.Right, margin: 5}),
                                gojs(go.TextBlock,
                                    {
                                        alignment: go.Spot.Left,
                                        editable: true,
                                        margin: 5,
                                        font: "bold 18px sans-serif",
                                        stroke: "white"
                                    },
                                    new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                            )),  // end Horizontal Panel
                        gojs(go.Placeholder,
                            {padding: 5, alignment: go.Spot.TopLeft})
                    )  // end Vertical Panel
                );  // end Group
            node.isGroup = true;
            return node;
            break;
        case "SOFTWARE_SYSTEM_":
        case "SoftwareSystem_":
            return gojs(go.Node, "Spot", nodeStyle(),
                // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
                {
                    name: paletteModel.name
                },
                gojs(go.Panel, "Auto",
                    gojs(go.Shape, {
                            fill: "#02172C",
                            stroke: "#02172C",
                            geometry: rightRoundedRectangleGeometry,
                            strokeWidth: 4,
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
                    gojs(go.TextBlock, "text",
                        {
                            text: paletteModel.name,
                            font: "bold 11pt Helvetica, Arial, sans-serif",
                            stroke: 'black',
                            margin: 4,  // make some extra space for the shape around the text
                            isMultiline: true,
                            wrap: go.TextBlock.WrapFit,
                            editable: true  // allow in-place editing by user
                        },
                        new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle)),  // the label shows the node data's text
                    gojs(go.Shape, "RoundedRectangle",
                        {
                            fill: "#02172C",
                            stroke: "#02172C",
                            opacity: 0.50,
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
                makePort("B", go.Spot.Bottom, true, true));
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
    }
}
