function initDraggableLink() {

    if (myDiagram !== undefined) {
        myDiagram.clear();
        myDiagram.div = null;
    }

    myDiagram =
        gojs(go.Diagram, "diagramDiv",  // must name or refer to the DIV HTML element<
            {
                grid: gojs(go.Panel, "Grid",
                    gojs(go.Shape, "LineH", {stroke: "lightgray", strokeWidth: 0.5}),
                    gojs(go.Shape, "LineH", {stroke: "gray", strokeWidth: 0.5, interval: 10}),
                    gojs(go.Shape, "LineV", {stroke: "lightgray", strokeWidth: 0.5}),
                    gojs(go.Shape, "LineV", {stroke: "gray", strokeWidth: 0.5, interval: 10})
                ),
                allowDrop: true,  // must be true to accept drops from the Palette
                "draggingTool.dragsLink": true,
                "draggingTool.isGridSnapEnabled": true,
                "linkingTool.isUnconnectedLinkValid": true,
                "linkingTool.portGravity": 20,
                "relinkingTool.isUnconnectedLinkValid": true,
                "relinkingTool.portGravity": 20,
                "relinkingTool.fromHandleArchetype":
                    gojs(go.Shape, "Diamond", {
                        segmentIndex: 0,
                        cursor: "pointer",
                        desiredSize: new go.Size(8, 8),
                        fill: "tomato",
                        stroke: "darkred"
                    }),
                "relinkingTool.toHandleArchetype":
                    gojs(go.Shape, "Diamond", {
                        segmentIndex: -1,
                        cursor: "pointer",
                        desiredSize: new go.Size(8, 8),
                        fill: "darkred",
                        stroke: "tomato"
                    }),
                "linkReshapingTool.handleArchetype":
                    gojs(go.Shape, "Diamond", {
                        desiredSize: new go.Size(7, 7),
                        fill: "lightblue",
                        stroke: "deepskyblue"
                    }),
                rotatingTool: gojs(TopRotatingTool),  // defined below
                "rotatingTool.snapAngleMultiple": 15,
                "rotatingTool.snapAngleEpsilon": 15,
                "undoManager.isEnabled": true
            });

    // when the document is modified, add a "*" to the title and enable the "Save" button
    myDiagram.addDiagramListener("Modified", function (e) {
        const button = document.getElementById("SaveButton");
        if (button) button.disabled = !myDiagram.isModified;
        const idx = document.title.indexOf("*");
        if (myDiagram.isModified) {
            if (idx < 0) document.title += "*";
        } else {
            if (idx >= 0) document.title = document.title.substr(0, idx);
        }
    });

    // Define a function for creating a "port" that is normally transparent.
    // The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
    // and where the port is positioned on the node, and the boolean "output" and "input" arguments
    // control whether the user can draw links from or to the port.
    function makePort(name, spot, output, input) {
        // the port is basically just a small transparent square
        return gojs(go.Shape, "Circle",
            {
                fill: null,  // not seen, by default; set to a translucent gray by showSmallPorts, defined below
                stroke: null,
                desiredSize: new go.Size(7, 7),
                alignment: spot,  // align the port on the main Shape
                alignmentFocus: spot,  // just inside the Shape
                portId: name,  // declare this object to be a "port"
                fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
                fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
                cursor: "pointer"  // show a different cursor to indicate potential link point
            });
    }

    const nodeSelectionAdornmentTemplate =
        gojs(go.Adornment, "Auto",
            gojs(go.Shape, {fill: null, stroke: "deepskyblue", strokeWidth: 1.5, strokeDashArray: [4, 2]}),
            gojs(go.Placeholder)
        );

    const nodeResizeAdornmentTemplate =
        gojs(go.Adornment, "Spot",
            {locationSpot: go.Spot.Right},
            gojs(go.Placeholder),
            gojs(go.Shape, {
                alignment: go.Spot.TopLeft,
                cursor: "nw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                alignment: go.Spot.Top,
                cursor: "n-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                alignment: go.Spot.TopRight,
                cursor: "ne-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),

            gojs(go.Shape, {
                alignment: go.Spot.Left,
                cursor: "w-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                alignment: go.Spot.Right,
                cursor: "e-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),

            gojs(go.Shape, {
                alignment: go.Spot.BottomLeft,
                cursor: "se-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                alignment: go.Spot.Bottom,
                cursor: "s-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                alignment: go.Spot.BottomRight,
                cursor: "sw-resize",
                desiredSize: new go.Size(6, 6),
                fill: "lightblue",
                stroke: "deepskyblue"
            })
        );

    const nodeRotateAdornmentTemplate =
        gojs(go.Adornment,
            {locationSpot: go.Spot.Center, locationObjectName: "CIRCLE"},
            gojs(go.Shape, "Circle", {
                name: "CIRCLE",
                cursor: "pointer",
                desiredSize: new go.Size(7, 7),
                fill: "lightblue",
                stroke: "deepskyblue"
            }),
            gojs(go.Shape, {
                geometryString: "M3.5 7 L3.5 30",
                isGeometryPositioned: true,
                stroke: "deepskyblue",
                strokeWidth: 1.5,
                strokeDashArray: [4, 2]
            })
        );

    myDiagram.nodeTemplate =
        gojs(go.Node, "Spot",
            {locationSpot: go.Spot.Center},
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            {selectable: true, selectionAdornmentTemplate: nodeSelectionAdornmentTemplate},
            {resizable: true, resizeObjectName: "PANEL", resizeAdornmentTemplate: nodeResizeAdornmentTemplate},
            {rotatable: true, rotateAdornmentTemplate: nodeRotateAdornmentTemplate},
            new go.Binding("angle").makeTwoWay(),
            // the main object is a Panel that surrounds a TextBlock with a Shape
            gojs(go.Panel, "Auto",
                {name: "PANEL"},
                new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                gojs(go.Shape, "Rectangle",  // default figure
                    {
                        portId: "", // the default port: if no spot on link data, use closest side
                        fromLinkable: true, toLinkable: true, cursor: "pointer",
                        fill: "white",  // default color
                        strokeWidth: 2
                    },
                    new go.Binding("figure"),
                    new go.Binding("fill")),
                gojs(go.TextBlock,
                    {
                        font: "bold 11pt Helvetica, Arial, sans-serif",
                        margin: 8,
                        maxSize: new go.Size(160, NaN),
                        wrap: go.TextBlock.WrapFit,
                        editable: true
                    },
                    new go.Binding("text").makeTwoWay())
            ),
            // four small named ports, one on each side:
            makePort("T", go.Spot.Top, false, true),
            makePort("L", go.Spot.Left, true, true),
            makePort("R", go.Spot.Right, true, true),
            makePort("B", go.Spot.Bottom, true, false),
            { // handle mouse enter/leave events to show/hide the ports
                mouseEnter: function (e, node) {
                    showSmallPorts(node, true);
                },
                mouseLeave: function (e, node) {
                    showSmallPorts(node, false);
                }
            }
        );

    function showSmallPorts(node, show) {
        node.ports.each(function (port) {
            if (port.portId !== "") {  // don't change the default port, which is the big shape
                port.fill = show ? "rgba(0,0,0,.3)" : null;
            }
        });
    }

    const linkSelectionAdornmentTemplate =
        gojs(go.Adornment, "Link",
            gojs(go.Shape,
                // isPanelMain declares that this Shape shares the Link.geometry
                {isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0})  // use selection object's strokeWidth
        );

    myDiagram.linkTemplate =
        gojs(go.Link,  // the whole link panel
            {selectable: true, selectionAdornmentTemplate: linkSelectionAdornmentTemplate},
            {relinkableFrom: true, relinkableTo: true, reshapable: true},
            {
                routing: go.Link.AvoidsNodes,
                curve: go.Link.JumpOver,
                corner: 5,
                toShortLength: 4
            },
            new go.Binding("points").makeTwoWay(),
            gojs(go.Shape,  // the link path shape
                {isPanelMain: true, strokeWidth: 2}),
            gojs(go.Shape,  // the arrowhead
                {toArrow: "Standard", stroke: null}),
            gojs(go.Panel, "Auto",
                new go.Binding("visible", "isSelected").ofObject(),
                gojs(go.Shape, "RoundedRectangle",  // the link shape
                    {fill: "#F8F8F8", stroke: null}),
                gojs(go.TextBlock,
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#919191",
                        margin: 2,
                        minSize: new go.Size(10, NaN),
                        editable: true
                    },
                    new go.Binding("text").makeTwoWay())
            )
        );

    if (myPaletteBasic !== undefined) {
        myPaletteBasic.clear();
        myPaletteBasic.div = null;
    }

    // initialize the Palette that is on the left side of the page
    // noinspection JSUndeclaredVariable
    myPaletteBasic =
        gojs(go.Palette, "paletteDivBasic",  // must name or refer to the DIV HTML element
            {
                maxSelectionCount: 1,
                nodeTemplateMap: myDiagram.nodeTemplateMap,  // share the templates used by myDiagram
                linkTemplate: // simplify the link template, just in this Palette
                    gojs(go.Link,
                        { // because the GridLayout.alignment is Location and the nodes have locationSpot == Spot.Center,
                            // to line up the Link in the same manner we have to pretend the Link has the same location spot
                            locationSpot: go.Spot.Center,
                            selectionAdornmentTemplate:
                                gojs(go.Adornment, "Link",
                                    {locationSpot: go.Spot.Center},
                                    gojs(go.Shape,
                                        {isPanelMain: true, fill: null, stroke: "deepskyblue", strokeWidth: 0}),
                                    gojs(go.Shape,  // the arrowhead
                                        {toArrow: "Standard", stroke: null})
                                )
                        },
                        {
                            routing: go.Link.AvoidsNodes,
                            curve: go.Link.JumpOver,
                            corner: 5,
                            toShortLength: 4
                        },
                        new go.Binding("points"),
                        gojs(go.Shape,  // the link path shape
                            {isPanelMain: true, strokeWidth: 2}),
                        gojs(go.Shape,  // the arrowhead
                            {toArrow: "Standard", stroke: null})
                    ),
                model: new go.GraphLinksModel([  // specify the contents of the Palette
                    {text: "Start", figure: "Circle", fill: "#00AD5F"},
                    {text: "Step"},
                    {text: "DB", figure: "Database", fill: "lightgray"},
                    {text: "???", figure: "Diamond", fill: "lightskyblue"},
                    {text: "End", figure: "Circle", fill: "#CE0620"},
                    {text: "Comment", figure: "RoundedRectangle", fill: "lightyellow"}
                ], [
                    // the Palette also has a disconnected Link, which the user can drag-and-drop
                    {points: new go.List(go.Point).addAll([new go.Point(0, 0), new go.Point(30, 0), new go.Point(30, 40), new go.Point(60, 40)])}
                ])
            });
}


function TopRotatingTool() {
    go.RotatingTool.call(this);
}

go.Diagram.inherit(TopRotatingTool, go.RotatingTool);

/** @override */
TopRotatingTool.prototype.updateAdornments = function (part) {
    go.RotatingTool.prototype.updateAdornments.call(this, part);
    const adornment = part.findAdornment("Rotating");
    if (adornment !== null) {
        adornment.location = part.rotateObject.getDocumentPoint(new go.Spot(0.5, 0, 0, -30));  // above middle top
    }
};

/** @override */
TopRotatingTool.prototype.rotate = function (newangle) {
    go.RotatingTool.prototype.rotate.call(this, newangle + 90);
};
// end of TopRotatingTool class


