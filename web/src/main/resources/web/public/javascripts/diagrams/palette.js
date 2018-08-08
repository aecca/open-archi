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
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
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
            name: "Person",
            locationSpot: go.Spot.Center,
            maxSize: new go.Size(60, 50)
        },
        gojs(go.Panel, "Auto",
            gojs(go.Shape,
                {
                    figure: "Actor",
                    alignment: go.Spot.Center,
                    maxSize: new go.Size(40, 60),
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
                new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke),
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

function toPalette(data, category) {
    let paletteModel = data;
    if (data.id !== undefined && data.id !== null) {
        paletteModel.id = data.id;
    }
    paletteModel.category = category;
    paletteModel.kind = data.kind;
    paletteModel.name = data.name;
    paletteModel.figure = data.shape.type;
    paletteModel.fill = data.shape.fill;
    paletteModel.minSize = OpenArchiWrapper.toSize(data);
    paletteModel.input = data.input;
    paletteModel.output = data.output;
    paletteModel.description = data.description;
    paletteModel.prototype = data.prototype;
    paletteModel.shift = true;
    return paletteModel;
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
                const category = data.shape.type;
                const item = toPalette(data, category);
                paletteModelArray.push(item);
            });
            if (paletteData.complexElements) {
                paletteData.complexElements.forEach(function (data) {
                    const category = data.shape.type;
                    const item = toPalette(data, category);
                    paletteModelArray.push(item);
                });
            }

            myPalette.nodeTemplateMap.add("DEFAULT", getDefaultTemplate());
            myPalette.nodeTemplateMap.add("PERSON", getPersonTemplate());
            myPalette.nodeTemplateMap.add("CONSUMER", getConsumerTemplate());
            myPalette.nodeTemplateMap.add("", gojs(
                go.Node, "Spot", nodeStyle(),
                new go.Binding("clonedFrom", "clonedFrom"),
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            figure: "RoundedRectangle"
                        },
                        new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                        new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
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
                )
            ));

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
    myPalette.requestUpdate();
}
