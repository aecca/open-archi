var mainTreeGraph;
var leafTreeGraph;
var mainTreeDiagram;
var leafTreeDiagram;

function initTreeDiagram() {
    mainTreeGraph = go.GraphObject.make;

    mainTreeDiagram = mainTreeGraph(go.Diagram, "TREE_MODEL",
        {
            layout: mainTreeGraph(go.TreeLayout, {nodeSpacing: 4}),
            allowMove: false
        });

    mainTreeDiagram.addDiagramListener("ChangedSelection",
        function (e) {
            var position = e.diagram.firstInput.documentPoint;
            position.y = position.y - 50;
            mainTreeDiagram.position = position;
        });

    mainTreeDiagram.toolManager.hoverDelay = 75;

    var colorsBar = mainTreeGraph(go.Panel, "Table",

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#F39C11",
                stroke: null,
                row: 0,
                column: 7,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "CORPORATE") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "CORPORATE") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#4C1130",
                stroke: null,
                row: 0,
                column: 0,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "ES") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "ES") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#0000FF",
                stroke: null,
                row: 0,
                column: 1,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "MX") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "MX") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#FF3300",
                stroke: null,
                row: 0,
                column: 2,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "CO") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "CO") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#800080",
                stroke: null,
                row: 0,
                column: 3,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "CL") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "CL") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#7F6000",
                stroke: null,
                row: 0,
                column: 4,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "PE") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "PE") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#009900",
                stroke: null,
                row: 0,
                column: 5,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "AR") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "AR") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#FFC000",
                stroke: null,
                row: 0,
                column: 6,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "VE") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "VE") {
                        return true;
                    }
                }
                return false;
            })),

        mainTreeGraph(go.Shape, "RoundedRectangle", {
                fill: "#666633",
                stroke: null,
                row: 0,
                column: 7,
                height: 5,
                width: 20,
                toolTip: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle", {fill: "#FFF600", stroke: null}),
                    mainTreeGraph(go.TextBlock,
                        {margin: 5, font: "bold 15px Helvetica, bold Arial, sans-serif"},
                        new go.Binding("text", "usedBy", function (s) {
                            var i;
                            for (i = 0; i < s.length; i++) {
                                var origin = s[i].countryName;
                                if (origin == "USA") {
                                    return origin;
                                }
                            }
                        })))
            },
            new go.Binding("visible", "usedBy", function (s) {
                var i;
                for (i = 0; i < s.length; i++) {
                    var origin = s[i].countryName;
                    if (origin == "USA") {
                        return true;
                    }
                }
                return false;
            }))
    );

    mainTreeDiagram.nodeTemplate =
        mainTreeGraph(go.Node, "Auto",
            {
                selectionChanged: onSelectionChanged,
                click: mainTreeDiagramNodeClick
            },

            mainTreeGraph(go.Panel, "Table",
                {
                    margin: 2,
                    defaultAlignment: go.Spot.Left
                },
                mainTreeGraph(go.RowColumnDefinition, {
                    row: 0,
                    column: 0,
                    height: 5,
                    separatorPadding: 3

                }),
                mainTreeGraph(go.RowColumnDefinition, {
                    row: 1,
                    column: 0,
                    sizing: go.RowColumnDefinition.None
                }),
                mainTreeGraph(go.Panel, "Auto",
                    {row: 0, column: 0, alignment: go.Spot.Left},
                    colorsBar
                ),

                mainTreeGraph(go.Panel, "Auto",
                    {row: 1, column: 0},
                    mainTreeGraph(go.Shape, "RoundedRectangle", {
                        name: "node",
                        stroke: null
                    }, new go.Binding("fill", "rgb")),
                    mainTreeGraph(go.TextBlock,
                        {
                            font: "bold 15px Helvetica, bold Arial, sans-serif",
                            stroke: "white", margin: 3
                        },
                        new go.Binding("text", "name"))
                )
            ),
            {
                selectionAdornmentTemplate: mainTreeGraph(go.Adornment, "Auto",
                    mainTreeGraph(go.Shape, "RoundedRectangle",
                        {fill: null, stroke: "#053908", strokeWidth: 5}),
                    mainTreeGraph(go.Placeholder)
                )
            }
        );

    mainTreeDiagram.linkTemplate =
        mainTreeGraph(go.Link, {
                selectable: false
            },
            mainTreeGraph(go.Shape,
                {strokeWidth: 2, stroke: "#2C2C29"}
            ));

    mainTreeDiagram.model = new go.TreeModel(dataArray);

}
