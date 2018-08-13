const architectureModelTemplate = gojs(go.Group, "Auto",
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
    groupStyle(),
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
                strokeWidth: 2,
            },
            new go.Binding("stroke", "", OpenArchiWrapper.toFill)
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
);
