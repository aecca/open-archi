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
                        scrollsPageOnFocus: false,
                        nodeTemplate:
                            gojs(go.Node, "Auto",
                                {locationSpot: go.Spot.Center},
                                gojs(go.Shape, "RoundedRectangle",
                                    {
                                        fill: "white", // the default fill, if there is no data bound value
                                        portId: "", cursor: "pointer",  // the Shape is the port, not the whole Node
                                        // allow all kinds of links from and to this port
                                        fromLinkable: true, fromLinkableSelfNode: true, fromLinkableDuplicates: true,
                                        toLinkable: true, toLinkableSelfNode: true, toLinkableDuplicates: true
                                    },
                                    new go.Binding("fill", "color")),
                                gojs(go.TextBlock,
                                    {
                                        font: "bold 14px sans-serif",
                                        stroke: '#333',
                                        margin: 6,  // make some extra space for the shape around the text
                                        isMultiline: false,  // don't allow newlines in text
                                        editable: true  // allow in-place editing by user
                                    },
                                    new go.Binding("text", "text").makeTwoWay()),  // the label shows the node data's text
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
                    });
            let paletteModelArray = [];
            myPalette.nodeTemplateMap.add("",
                gojs(go.Node, "Spot", nodeStyle(),
                    // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
                    gojs(go.Panel, "Auto",
                        gojs(go.Shape, "Rectangle",
                            {fill: "#00A9C9", stroke: null},
                            new go.Binding("figure", "figure")),
                        gojs(go.TextBlock,
                            {
                                font: "bold 11pt Helvetica, Arial, sans-serif",
                                stroke: lightText,
                                margin: 8,
                                maxSize: new go.Size(160, NaN),
                                wrap: go.TextBlock.WrapFit,
                                editable: true
                            },
                            new go.Binding("text").makeTwoWay())
                    ),
                    // four named ports, one on each side:
                    makePort("T", go.Spot.Top, true, true),
                    makePort("L", go.Spot.Left, true, true),
                    makePort("R", go.Spot.Right, true, true),
                    makePort("B", go.Spot.Bottom, true, true)
                ));

            paletteData.basicElements.forEach(function (data) {
                const shapeType = data.shapeType;
                const input = data.input;
                const output = data.output;
                let paletteModel = {};
                paletteModel.category = data.name;
                paletteModel.text = data.name;
                paletteModel.figure = shapeType;
                paletteModelArray.push(paletteModel);
                myPalette.nodeTemplateMap.add(data.name,
                    gojs(go.Node, "Spot", nodeStyle(),
                        gojs(go.Panel, "Auto",
                            gojs(go.Shape, shapeType,
                                {
                                    minSize: new go.Size(data.size.width, data.size.height),
                                    fill: data.fill,
                                    stroke: data.stroke
                                }),
                            gojs(go.TextBlock, data.name,
                                {font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText},
                                new go.Binding("text"))
                        ),
                        // three named ports, one on each side except the top, all output only:
                        // four named ports, one on each side:
                        makePort("T", go.Spot.Top, input, output),
                        makePort("L", go.Spot.Left, input, output),
                        makePort("R", go.Spot.Right, input, output),
                        makePort("B", go.Spot.Bottom, input, output)
                    ));
            });
            if (paletteData.softwareSystems) {
                paletteData.softwareSystems.forEach(function (data) {
                    const shapeType = data.shapeType;
                    const input = data.input;
                    const output = data.output;
                    let paletteModel = {};
                    paletteModel.category = data.name;
                    paletteModel.text = data.name;
                    paletteModel.figure = shapeType;
                    paletteModelArray.push(paletteModel);
                    myPalette.nodeTemplateMap.add(data.name,
                        gojs(go.Node, "Spot", nodeStyle(),
                            gojs(go.Panel, "Auto",
                                gojs(go.Shape, shapeType,
                                    {
                                        minSize: new go.Size(data.size.width, data.size.height),
                                        fill: data.fill,
                                        stroke: data.stroke
                                    }),
                                gojs(go.TextBlock, data.name,
                                    {font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText},
                                    new go.Binding("text"))
                            ),
                            // three named ports, one on each side except the top, all output only:
                            // four named ports, one on each side:
                            makePort("T", go.Spot.Top, input, output),
                            makePort("L", go.Spot.Left, input, output),
                            makePort("R", go.Spot.Right, input, output),
                            makePort("B", go.Spot.Bottom, input, output)
                        ));
                });
            }
            if (paletteData.containers) {
                paletteData.containers.forEach(function (data) {
                    const shapeType = data.shapeType;
                    const input = data.input;
                    const output = data.output;
                    let paletteModel = {};
                    paletteModel.category = data.name;
                    paletteModel.text = data.name;
                    paletteModel.figure = shapeType;
                    paletteModelArray.push(paletteModel);
                    myPalette.nodeTemplateMap.add(data.name,
                        gojs(go.Node, "Spot", nodeStyle(),
                            gojs(go.Panel, "Auto",
                                gojs(go.Shape, shapeType,
                                    {
                                        minSize: new go.Size(data.size.width, data.size.height),
                                        fill: data.fill,
                                        stroke: data.stroke
                                    }),
                                gojs(go.TextBlock, data.name,
                                    {font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText},
                                    new go.Binding("text"))
                            ),
                            // three named ports, one on each side except the top, all output only:
                            // four named ports, one on each side:
                            makePort("T", go.Spot.Top, input, output),
                            makePort("L", go.Spot.Left, input, output),
                            makePort("R", go.Spot.Right, input, output),
                            makePort("B", go.Spot.Bottom, input, output)
                        ));
                });
            }
            if (paletteData.components) {

                paletteData.components.forEach(function (data) {
                    const shapeType = data.shapeType;
                    const input = data.input;
                    const output = data.output;
                    let paletteModel = {};
                    paletteModel.category = data.name;
                    paletteModel.text = data.name;
                    paletteModel.figure = shapeType;
                    paletteModelArray.push(paletteModel);
                    myPalette.nodeTemplateMap.add(data.name,
                        gojs(go.Node, "Spot", nodeStyle(),
                            gojs(go.Panel, "Auto",
                                gojs(go.Shape, shapeType,
                                    {
                                        minSize: new go.Size(data.size.width, data.size.height),
                                        fill: data.fill,
                                        stroke: data.stroke
                                    }),
                                gojs(go.TextBlock, data.name,
                                    {font: "bold 11pt Helvetica, Arial, sans-serif", stroke: lightText},
                                    new go.Binding("text"))
                            ),
                            // three named ports, one on each side except the top, all output only:
                            // four named ports, one on each side:
                            makePort("T", go.Spot.Top, input, output),
                            makePort("L", go.Spot.Left, input, output),
                            makePort("R", go.Spot.Right, input, output),
                            makePort("B", go.Spot.Bottom, input, output)
                        ));
                });
            }
            myPalette.model = new go.GraphLinksModel(paletteModelArray);
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