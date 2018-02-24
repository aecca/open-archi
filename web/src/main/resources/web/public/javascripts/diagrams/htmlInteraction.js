function initHtmlInteraction() {


    // Note that we do not use $ here as an alias for go.GraphObject.make because we are using $ for jQuery
    var GO = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        GO(go.Diagram, diagramDiv,  // must name or refer to the DIV HTML element
            {allowDrop: true});  // must be true to accept drops from the Palette

    // define several shared Brushes
    var fill1 = "rgb(105,210,231)"
    var brush1 = "rgb(65,180,181)";

    var fill2 = "rgb(167,219,216)"
    var brush2 = "rgb(127,179,176)";

    var fill3 = "rgb(224,228,204)"
    var brush3 = "rgb(184,188,164)";

    var fill4 = "rgb(243,134,48)"
    var brush4 = "rgb(203,84,08)";

    myDiagram.nodeTemplateMap.add("", // default category
        GO(go.Node, "Auto",
            {locationSpot: go.Spot.Center},
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            GO(go.Shape, "Ellipse",
                {strokeWidth: 2, fill: fill1, name: "SHAPE"},
                new go.Binding("figure", "figure"),
                new go.Binding("fill", "fill"),
                new go.Binding("stroke", "stroke")
            ),
            GO(go.TextBlock,
                {
                    margin: 5,
                    maxSize: new go.Size(200, NaN),
                    wrap: go.TextBlock.WrapFit,
                    textAlign: "center",
                    editable: true,
                    font: "bold 9pt Helvetica, Arial, sans-serif",
                    name: "TEXT"
                },
                new go.Binding("text", "text").makeTwoWay())));

    // On selection changed, make sure infoDraggable will resize as necessary
    myDiagram.addDiagramListener("ChangedSelection", function (diagramEvent) {
        var idrag = document.getElementById("infoDraggable");
        idrag.style.width = "";
        idrag.style.height = "";
    });


    // initialize the Palette that is in a floating, draggable HTML container
    myPalette = new go.Palette("paletteDiv");  // must name or refer to the DIV HTML element
    myPalette.nodeTemplateMap = myDiagram.nodeTemplateMap;
    myPalette.model = new go.GraphLinksModel([
        {text: "Lake", fill: fill1, stroke: brush1, figure: "Hexagon"},
        {text: "Ocean", fill: fill2, stroke: brush2, figure: "Rectangle"},
        {text: "Sand", fill: fill3, stroke: brush3, figure: "Diamond"},
        {text: "Goldfish", fill: fill4, stroke: brush4, figure: "Octagon"}
    ]);

    myPalette.addDiagramListener("InitialLayoutCompleted", function (diagramEvent) {
        var pdrag = document.getElementById("paletteDraggable");
        var palette = diagramEvent.diagram;
        pdrag.style.width = palette.documentBounds.width + 28 + "px"; // account for padding/borders
        pdrag.style.height = palette.documentBounds.height + 38 + "px";
    });


    $(function () {
        $("#paletteDraggable").draggable({handle: "#paletteDraggableHandle"}).resizable({
            // After resizing, perform another layout to fit everything in the palette's viewport
            stop: function () {
                myPalette.layoutDiagram(true);
            }
        });

        $("#infoDraggable").draggable({handle: "#infoDraggableHandle"});


        var inspector = new Inspector('myInfo', myDiagram,
            {
                properties: {
                    // key would be automatically added for nodes, but we want to declare it read-only also:
                    "key": {readOnly: true, show: Inspector.showIfPresent},
                    // fill and stroke would be automatically added for nodes, but we want to declare it a color also:
                    "fill": {show: Inspector.showIfPresent, type: 'color'},
                    "stroke": {show: Inspector.showIfPresent, type: 'color'}
                }
            });
    });

}