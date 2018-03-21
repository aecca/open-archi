const GeneratorEllipseSpot1 = new go.Spot(0.156, 0.156);
const GeneratorEllipseSpot2 = new go.Spot(0.844, 0.844);

go.Shape.defineFigureGenerator("Circle", function (shape, w, h) {  // predefined in 2.0
    const geo = new go.Geometry(go.Geometry.Ellipse);
    geo.startX = 0;
    geo.startY = 0;
    geo.endX = w;
    geo.endY = h;
    geo.spot1 = GeneratorEllipseSpot1;
    geo.spot2 = GeneratorEllipseSpot2;
    geo.defaultStretch = go.GraphObject.Uniform;
    return geo;
});

go.Shape.defineFigureGenerator("Consumer", function (shape, w, h) {
    const geo = new go.Geometry();
    const fig = new go.PathFigure(.5 * w, h, true);
    geo.add(fig);

    fig.add(new go.PathSegment(go.PathSegment.Line, w, .85 * h));
    fig.add(new go.PathSegment(go.PathSegment.Line, w, .15 * h));
    fig.add(new go.PathSegment(go.PathSegment.Line, .5 * w, 0));
    fig.add(new go.PathSegment(go.PathSegment.Line, 0, .15 * h));
    fig.add(new go.PathSegment(go.PathSegment.Line, 0, .85 * h).close());
    const fig2 = new go.PathFigure(.5 * w, h, false);
    geo.add(fig2);
    fig2.add(new go.PathSegment(go.PathSegment.Line, .5 * w, .3 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .15 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Move, .5 * w, .3 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, w, .15 * h));
    geo.spot1 = new go.Spot(0, .3);
    geo.spot2 = new go.Spot(.5, .85);
    return geo;
});

go.Shape.defineFigureGenerator("Person", function (shape, w, h) {
    const geo = new go.Geometry();
    const fig = new go.PathFigure(0, 0, false);
    geo.add(fig);

    const fig2 = new go.PathFigure(.335 * w, (1 - .555) * h, true);
    geo.add(fig2);
    // Shirt
    fig2.add(new go.PathSegment(go.PathSegment.Line, .335 * w, (1 - .405) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, (1 - .335) * w, (1 - .405) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, (1 - .335) * w, (1 - .555) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, w, .68 * h, (1 - .12) * w, .46 * h,
        (1 - .02) * w, .54 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, w, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, 0, .68 * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, .335 * w, (1 - .555) * h, .02 * w, .54 * h,
        .12 * w, .46 * h));
    // Start of neck
    fig2.add(new go.PathSegment(go.PathSegment.Line, .365 * w, (1 - .595) * h));
    const radiushead = .5 - .285;
    const centerx = .5;
    const centery = radiushead;
    const alpha2 = Math.PI / 4;
    const KAPPA = ((4 * (1 - Math.cos(alpha2))) / (3 * Math.sin(alpha2)));
    const cpOffset = KAPPA * .5;
    const radiusw = radiushead;
    const radiush = radiushead;
    const offsetw = KAPPA * radiusw;
    const offseth = KAPPA * radiush;
    // Circle (head)
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx - radiusw) * w, centery * h, (centerx - ((offsetw + radiusw) / 2)) * w, (centery + ((radiush + offseth) / 2)) * h,
        (centerx - radiusw) * w, (centery + offseth) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, centerx * w, (centery - radiush) * h, (centerx - radiusw) * w, (centery - offseth) * h,
        (centerx - offsetw) * w, (centery - radiush) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (centerx + radiusw) * w, centery * h, (centerx + offsetw) * w, (centery - radiush) * h,
        (centerx + radiusw) * w, (centery - offseth) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Bezier, (1 - .365) * w, (1 - .595) * h, (centerx + radiusw) * w, (centery + offseth) * h,
        (centerx + ((offsetw + radiusw) / 2)) * w, (centery + ((radiush + offseth) / 2)) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, (1 - .365) * w, (1 - .595) * h));
    // Neckline
    fig2.add(new go.PathSegment(go.PathSegment.Line, (1 - .335) * w, (1 - .555) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, (1 - .335) * w, (1 - .405) * h));
    fig2.add(new go.PathSegment(go.PathSegment.Line, .335 * w, (1 - .405) * h));
    const fig3 = new go.PathFigure(.2 * w, h, false);
    geo.add(fig3);
    // Arm lines
    fig3.add(new go.PathSegment(go.PathSegment.Line, .2 * w, .8 * h));
    const fig4 = new go.PathFigure(.8 * w, h, false);
    geo.add(fig4);
    fig4.add(new go.PathSegment(go.PathSegment.Line, .8 * w, .8 * h));
    return geo;
});

function toPalette(data, category) {
    let paletteModel = {};
    paletteModel.category = category;
    paletteModel.text = data.name;
    paletteModel.name = data.name;
    paletteModel.figure = data.shape.type;
    paletteModel.fill = data.shape.fill;
    paletteModel.stroke = data.shape.stroke;
    paletteModel.minSize = OpenArchiWrapper.toSize(data);
    return paletteModel;
}

function fillElement(paletteModel) {
    return gojs(
        go.Node, "Spot", nodeStyle(),
        gojs(go.Panel, "Auto",
            gojs(go.Shape, paletteModel.shape.type,
                {
                    "fill": paletteModel.shape.fill,
                    "stroke": paletteModel.shape.stroke,
                    "minSize": OpenArchiWrapper.toSize(paletteModel)
                },
                new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
            gojs(go.TextBlock, "Text",
                {
                    name: paletteModel.name,
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

function showPaletteByType(paletteData) {
    switch (paletteData.type) {
        case "ARCHITECTURE":
            if (myPalette !== undefined) {
                myPalette.clear();
                myPalette.div = null;
            }
            // initialize the Palette that is on the left side of the page
            // noinspection JSUndeclaredVariable
            myPalette =
                gojs(go.Palette, "paletteDiv",  // must name or refer to the DIV HTML element
                    {
                        scrollsPageOnFocus: false
                    });
            let paletteModelArray = [];
            paletteData.basicElements.forEach(function (data) {
                paletteModelArray.push(toPalette(data, data.shape.type));
                myPalette.nodeTemplateMap.add(data.shape.type, fillElement(data));
            });
            if (paletteData.softwareSystems) {
                paletteData.softwareSystems.forEach(function (data) {
                    paletteModelArray.push(toPalette(data, "SOFTWARE_SYSTEM"));
                    myPalette.nodeTemplateMap.add("SOFTWARE_SYSTEM", fillElement(data));
                });
            }
            if (paletteData.containers) {
                paletteData.containers.forEach(function (data) {
                    paletteModelArray.push(toPalette(data, "CONTAINER"));
                    myPalette.nodeTemplateMap.add("CONTAINER", fillElement(data));
                });
            }
            if (paletteData.components) {
                paletteData.components.forEach(function (data) {
                    paletteModelArray.push(toPalette(data, "COMPONENT"));
                    myPalette.nodeTemplateMap.add("COMPONENT", fillElement(data));
                });
            }
            myPalette.model = new go.GraphLinksModel(paletteModelArray);
            myPalette.addDiagramListener("InitialLayoutCompleted", function (diagramEvent) {
                const pdrag = document.getElementById("paletteDraggable");
                const palette = diagramEvent.diagram;
                pdrag.style.width = palette.documentBounds.width + 28 + "px"; // account for padding/borders
                pdrag.style.height = palette.documentBounds.height + 38 + "px";
            });
            break;
        case "BPM":
            break;
        case "FLOWCHART":
            break;
        case "SEQUENCE":
            break;
        case "GANTT":
            break;
        case "ENTITY_RELATIONSHIP":
            break;
        case "UML_CLASS":
            break;
        default:
            console.log("Still not implemented");
    }
}

// Define a function for creating a "port" that is normally transparent.
// The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
// and where the port is positioned on the node, and the boolean "output" and "input" arguments
// control whether the user can draw links from or to the port.
function makePort(name, spot, output, input) {
    // the port is basically just a small circle that has a white stroke when it is made visible
    return gojs(go.Shape, "Circle",
        {
            fill: "transparent",
            stroke: null,  // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(8, 8),
            alignment: spot, alignmentFocus: spot,  // align the port on the main Shape
            portId: name,  // declare this object to be a "port"
            fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
            fromLinkable: output, toLinkable: input,  // declare whether the user may draw links to/from here
            cursor: "pointer"  // show a different cursor to indicate potential link point
        });
}

// helper definitions for node templates

function nodeStyle() {
    return [
        // The Node.location comes from the "loc" property of the node data,
        // converted by the Point.parse static method.
        // If the Node.location is changed, it updates the "loc" property of the node data,
        // converting back using the Point.stringify static method.
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
        {
            // the Node.location is at the center of each node
            locationSpot: go.Spot.Center,
            //isShadowed: true,
            //shadowColor: "#888",
            // handle mouse enter/leave events to show/hide the ports
            mouseEnter: function (e, obj) {
                showPorts(obj.part, true);
            },
            mouseLeave: function (e, obj) {
                showPorts(obj.part, false);
            }
        }
    ];
}

// Make all ports on a node visible when the mouse is over the node
function showPorts(node, show) {
    let diagram = node.diagram;
    if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
    node.ports.each(function (port) {
        port.stroke = (show ? "white" : null);
    });
}