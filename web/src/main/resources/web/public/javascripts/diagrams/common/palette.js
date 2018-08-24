const newElementTemplate = gojs(
    go.Node, "Spot",
    {
        name: "Default",
        locationSpot: go.Spot.Center,
        selectionAdornmentTemplate: emptyAdornment
    },
    new go.Binding("clonedFrom", "clonedFrom"),
    new go.Binding("name", "", openArchiWrapper.toTitle).makeTwoWay(openArchiWrapper.fromTitle),
    new go.Binding("isGroup", "", function () {
        return false;
    }),
    gojs(go.Panel, "Auto",
        gojs(go.Shape,
            {
                stroke: "transparent"
            },
            new go.Binding("fill", "", openArchiWrapper.toFill).makeTwoWay(openArchiWrapper.fromFill)
        ),
        gojs(go.TextBlock, "Text",
            {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                margin: 8,
                maxSize: new go.Size(160, NaN),
                wrap: go.TextBlock.WrapFit,
                editable: false
            },
            new go.Binding("stroke", "", openArchiWrapper.toComplementColor),
            new go.Binding("text", "", openArchiWrapper.toTitle),
            new go.Binding("minSize", "", openArchiWrapper.toSize).makeTwoWay(openArchiWrapper.fromSize)
        ),
        { // this tooltip Adornment is shared by all nodes
            toolTip:
                gojs(go.Adornment, "Auto",
                    gojs(go.Shape, {fill: "#FFFFCC"}),
                    gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                        new go.Binding("text", "", nodeInfo))
                )
        }
    ),
    // three named ports, one on each side except the top, all output only:
    // four named ports, one on each side:
    makePort("T", go.Spot.Top),
    makePort("L", go.Spot.Left),
    makePort("R", go.Spot.Right),
    makePort("B", go.Spot.Bottom)
);

const personTemplate = gojs(
    go.Node, "Spot",
    {
        locationSpot: go.Spot.Center,
        maxSize: new go.Size(60, 50),
        name: "Person",
        selectionAdornmentTemplate: emptyAdornment
    },
    new go.Binding("isGroup", "", function () {
        return false;
    }),
    gojs(go.Panel, "Auto",
        gojs(go.Shape,
            {
                figure: "Actor",
                alignment: go.Spot.Center,
                maxSize: new go.Size(40, 60)
            },
            new go.Binding("stroke", "", openArchiWrapper.toStroke).makeTwoWay(openArchiWrapper.fromStroke),
            new go.Binding("fill", "", openArchiWrapper.toFill).makeTwoWay(openArchiWrapper.fromFill)
        ),
        { // this tooltip Adornment is shared by all nodes
            toolTip:
                gojs(go.Adornment, "Auto",
                    gojs(go.Shape, {fill: "#FFFFCC"}),
                    gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                        new go.Binding("text", "", nodeInfo))
                )
        }
    ),
    // three named ports, one on each side except the top, all output only:
    // four named ports, one on each side:
    makePort("T", go.Spot.Top),
    makePort("L", go.Spot.Left),
    makePort("R", go.Spot.Right),
    makePort("B", go.Spot.Bottom)
);

const consumerTemplate = gojs(
    go.Node, "Spot",
    {
        name: "Consumer",
        locationSpot: go.Spot.Center,
        selectionAdornmentTemplate: emptyAdornment
    },
    new go.Binding("isGroup", "", function () {
        return false;
    }),
    gojs(go.Panel, "Spot",
        gojs(go.Shape,
            {
                figure: "PrimitiveToCall",
                maxSize: new go.Size(NaN, 40)
            },
            new go.Binding("stroke", "", openArchiWrapper.toStroke).makeTwoWay(openArchiWrapper.fromStroke),
            new go.Binding("fill", "", openArchiWrapper.toFill).makeTwoWay(openArchiWrapper.fromFill)
        ),
        gojs(go.TextBlock, "Text",
            {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                margin: 8,
                maxSize: new go.Size(160, NaN),
                wrap: go.TextBlock.WrapFit,
                editable: false,
                alignment: go.Spot.Center
            },
            new go.Binding("stroke", "", openArchiWrapper.toComplementColor),
            new go.Binding("text", "", openArchiWrapper.toTitle),
            new go.Binding("minSize", "", openArchiWrapper.toSize).makeTwoWay(openArchiWrapper.fromSize)
        ),
        { // this tooltip Adornment is shared by all nodes
            toolTip:
                gojs(go.Adornment, "Auto",
                    gojs(go.Shape, {fill: "#FFFFCC"}),
                    gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                        new go.Binding("text", "", nodeInfo))
                )
        }
    ),
    // three named ports, one on each side except the top, all output only:
    // four named ports, one on each side:
    makePort("T", go.Spot.Top),
    makePort("L", go.Spot.Left),
    makePort("R", go.Spot.Right),
    makePort("B", go.Spot.Bottom)
);


const defaultTemplate = gojs(
    go.Node, "Spot", nodeStyle(),
    {
        selectionAdornmentTemplate: emptyAdornment
    },
    new go.Binding("isGroup", "", function () {
        return false;
    }),
    new go.Binding("clonedFrom", "clonedFrom"),
    gojs(go.Panel, "Auto",
        gojs(go.Shape,
            {
                figure: "RoundedRectangle",
                stroke: "transparent",
                strokeWidth: 3
            },
            new go.Binding("fill", "", openArchiWrapper.toFill).makeTwoWay(openArchiWrapper.fromFill),
            new go.Binding("stroke", "", openArchiWrapper.toComplementColor),
            new go.Binding("minSize", "", openArchiWrapper.toSize).makeTwoWay(openArchiWrapper.fromSize)
        ),
        gojs(go.TextBlock, "Text",
            {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                margin: 8,
                maxSize: new go.Size(160, NaN),
                wrap: go.TextBlock.WrapFit,
                editable: true
            },
            new go.Binding("stroke", "", openArchiWrapper.toComplementColor),
            new go.Binding("text", "", openArchiWrapper.toTitle),
            new go.Binding("minSize", "", openArchiWrapper.toSize).makeTwoWay(openArchiWrapper.fromSize)),
        { // this tooltip Adornment is shared by all nodes
            toolTip:
                gojs(go.Adornment, "Auto",
                    gojs(go.Shape, {fill: "#FFFFCC"}),
                    gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling nodeInfo(data)
                        new go.Binding("text", "", nodeInfo))
                ),
            // this context menu Adornment is shared by all nodes
            contextMenu: partContextMenuPalette
        }
    )
);
