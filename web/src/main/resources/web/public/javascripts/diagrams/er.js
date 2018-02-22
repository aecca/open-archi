
var dataArray;
var mainErDiagram;
var mainErGraph;
var mainErDataArray = [];
var erLinkDataArray = [];

function initErDiagram() {

    mainErGraph = go.GraphObject.make;

    mainErDiagram = mainErGraph(go.Diagram, "ENTITY_RELATIONSHIP_MODEL",
        {
            initialContentAlignment: go.Spot.Center,
            allowDelete: false,
            allowCopy: false,
            layout: mainErGraph(go.ForceDirectedLayout),
            "undoManager.isEnabled": true
        });

    var bluegrad = mainErGraph(go.Brush, go.Brush.Linear, {
        0: "rgb(150, 150, 250)",
        0.5: "rgb(86, 86, 186)",
        1: "rgb(86, 86, 186)"
    });
    var greengrad = mainErGraph(go.Brush, go.Brush.Linear, {0: "rgb(158, 209, 159)", 1: "rgb(67, 101, 56)"});
    var redgrad = mainErGraph(go.Brush, go.Brush.Linear, {0: "rgb(206, 106, 100)", 1: "rgb(180, 56, 50)"});
    var yellowgrad = mainErGraph(go.Brush, go.Brush.Linear, {0: "rgb(254, 221, 50)", 1: "rgb(254, 182, 50)"});
    var lightgrad = mainErGraph(go.Brush, go.Brush.Linear, {1: "#E6E6FA", 0: "#FFFAF0"});

    var itemTempl =
        mainErGraph(go.Panel, "Horizontal",
            mainErGraph(go.TextBlock,
                {
                    stroke: "#333333",
                    font: "bold 14px sans-serif"
                },
                new go.Binding("text", "name"))
        );

    mainErDiagram.nodeTemplate =
        mainErGraph(go.Node, "Auto",
            {
                selectionAdorned: true,
                resizable: true,
                layoutConditions: go.Part.LayoutStandard & ~go.Part.LayoutNodeSized,
                fromSpot: go.Spot.AllSides,
                toSpot: go.Spot.AllSides,
                isShadowed: true,
                shadowColor: "#C5C1AA"
            },
            new go.Binding("location", "location").makeTwoWay(),

            mainErGraph(go.Shape, "Rectangle",
                {fill: lightgrad, stroke: "#756875", strokeWidth: 3}),

            mainErGraph(go.Panel, "Table",
                {margin: 8, stretch: go.GraphObject.Fill},
                mainErGraph(go.RowColumnDefinition, {
                    row: 0,
                    sizing: go.RowColumnDefinition.None
                }),

                mainErGraph(go.TextBlock,
                    {
                        row: 0, alignment: go.Spot.Center,
                        margin: new go.Margin(0, 14, 0, 2),
                        font: "bolder 16px sans-serif"
                    },
                    new go.Binding("text", "name")),

                mainErGraph("Button",
                    {
                        row: 0, alignment: go.Spot.TopRight,
                        "ButtonBorder.stroke": null,
                        click: function (e, but) {
                            var list = but.part.findObject("LIST");
                            if (list !== null) {
                                list.diagram.startTransaction("collapse/expand");
                                list.visible = !list.visible;
                                var shape = but.findObject("SHAPE");
                                if (shape !== null) shape.figure = (list.visible ? "TriangleUp" : "TriangleDown");
                                list.diagram.commitTransaction("collapse/expand");
                            }
                        }
                    },
                    mainErGraph(go.Shape, "TriangleUp",
                        {name: "SHAPE", width: 6, height: 4})),

                mainErGraph(go.Panel, "Vertical",
                    {
                        name: "LIST",
                        row: 1,
                        padding: 3,
                        alignment: go.Spot.TopLeft,
                        defaultAlignment: go.Spot.Left,
                        stretch: go.GraphObject.Horizontal,
                        itemTemplate: itemTempl
                    },
                    new go.Binding("itemArray", "attributes"))
            )
        );

    mainErDiagram.linkTemplate =
        mainErGraph(go.Link,
            {
                selectionAdorned: true,
                layerName: "Foreground",
                reshapable: true,
                routing: go.Link.AvoidsNodes,
                corner: 5,
                curve: go.Link.JumpOver
            },
            mainErGraph(go.Shape,
                {stroke: "#303B45", strokeWidth: 2.5}),
            mainErGraph(go.TextBlock,
                {
                    textAlign: "center",
                    font: "bold 14px sans-serif",
                    stroke: "#1967B3",
                    segmentIndex: 0,
                    segmentOffset: new go.Point(NaN, NaN),
                    segmentOrientation: go.Link.OrientUpright
                },
                new go.Binding("text", "fromText")),
            mainErGraph(go.TextBlock,
                {
                    textAlign: "center",
                    font: "bold 14px sans-serif",
                    stroke: "#1967B3",
                    segmentIndex: -1,
                    segmentOffset: new go.Point(NaN, NaN),
                    segmentOrientation: go.Link.OrientUpright
                },
                new go.Binding("text", "toText"))
        );

    mainErDiagram.model = new go.GraphLinksModel(dataArray, erLinkDataArray);
}
