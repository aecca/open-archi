function getDefaultTemplate() {
    return gojs(
        go.Node, "Spot", nodeStyle(),
        {
            name: "Default"
        },
        new go.Binding("clonedFrom", "clonedFrom"),
        gojs(go.Panel, "Auto",
            gojs(go.Shape,
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
            ),
            gojs(go.TextBlock, "Text",
                {
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
        makePort("T", go.Spot.Top),
        makePort("L", go.Spot.Left),
        makePort("R", go.Spot.Right),
        makePort("B", go.Spot.Bottom)
    );
}

function getPersonTemplate() {
    return gojs(
        go.Node, "Spot",
        {
            locationSpot: go.Spot.Center,
            maxSize: new go.Size(60, 50)
        },
        new go.Binding("name", "", OpenArchiWrapper.toCategory).makeTwoWay(OpenArchiWrapper.fromCategory),
        gojs(go.Panel, "Auto",
            gojs(go.Shape,
                {
                    figure: "Actor",
                    alignment: go.Spot.Center,
                    maxSize: new go.Size(40, 60)
                },
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
            ),
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
        makePort("T", go.Spot.Top),
        makePort("L", go.Spot.Left),
        makePort("R", go.Spot.Right),
        makePort("B", go.Spot.Bottom)
    );
}

function getConsumerTemplate() {
    return gojs(
        go.Node, "Spot",
        {
            name: "Consumer",
            locationSpot: go.Spot.Center
        },
        new go.Binding("category", "", OpenArchiWrapper.toCategory).makeTwoWay(OpenArchiWrapper.fromCategory),
        gojs(go.Panel, "Spot",
            gojs(go.Shape,
                {
                    figure: "PrimitiveToCall",
                    maxSize: new go.Size(NaN, 40),
                },
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill)
            ),
            gojs(go.TextBlock, "Text",
                {
                    font: "bold 11pt Helvetica, Arial, sans-serif",
                    stroke: lightText,
                    margin: 8,
                    maxSize: new go.Size(160, NaN),
                    wrap: go.TextBlock.WrapFit,
                    editable: true,
                    alignment: go.Spot.Center
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
        makePort("T", go.Spot.Top),
        makePort("L", go.Spot.Left),
        makePort("R", go.Spot.Right),
        makePort("B", go.Spot.Bottom)
    );
}
