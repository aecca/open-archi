function initShapes() {

    const $ = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        $(go.Diagram, diagramDiv,  // create the Diagram for the HTML DIV element
            {
                layout: $(go.GridLayout), // use a GridLayout
                padding: new go.Margin(5, 5, 25, 5) // to see the names of shapes on the bottom row
            });

    function mouseEnter(e, obj) {
        obj.isHighlighted = true;
    };

    function mouseLeave(e, obj) {
        obj.isHighlighted = false;
    };


    myDiagram.nodeTemplate =
        $(go.Node, "Vertical",
            {
                mouseEnter: mouseEnter,
                mouseLeave: mouseLeave,
                locationSpot: go.Spot.Center,  // the location is the center of the Shape
                locationObjectName: "SHAPE",
                selectionAdorned: false,  // no selection handle when selected
                resizable: true, resizeObjectName: "SHAPE",  // user can resize the Shape
                rotatable: true, rotateObjectName: "SHAPE",  // rotate the Shape without rotating the label
                // don't re-layout when node changes size
                layoutConditions: go.Part.LayoutStandard & ~go.Part.LayoutNodeSized
            },
            new go.Binding("layerName", "isHighlighted", function (h) {
                return h ? "Foreground" : "";
            }).ofObject(),
            $(go.Shape,
                {
                    name: "SHAPE",  // named so that the above properties can refer to this GraphObject
                    width: 70, height: 70,
                    stroke: "#C2185B",
                    fill: "#F48FB1",
                    strokeWidth: 3
                },
                // bind the Shape.figure to the figure name, which automatically gives the Shape a Geometry
                new go.Binding("figure", "key")),
            $(go.TextBlock,  // the label
                {
                    margin: 4,
                    font: "bold 18px sans-serif",
                    background: 'white'
                },
                new go.Binding("visible", "isHighlighted").ofObject(),
                new go.Binding("text", "key"))
        );

    // initialize the model
    myDiagram.model.nodeDataArray = go.Shape.getFigureGenerators().toArray();
}