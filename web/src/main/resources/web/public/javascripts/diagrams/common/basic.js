function initBasic(nodeDataArray, linkDataArray, paletteModel) {

    //Basic elements palette
    if (myPaletteBasic !== undefined) {
        myPaletteBasic.clear();
        myPaletteBasic.div = null;
    }
    // initialize the Palette that is on the left side of the page
    // noinspection JSUndeclaredVariable
    myPaletteBasic =
        gojs(go.Palette, "paletteDivBasic",  // must name or refer to the DIV HTML element
            {
                scrollsPageOnFocus: false,
                layout: gojs(PoolLayout, {
                    sorting: go.GridLayout.Forward
                }),
                hoverDelay: 100,
                "draggingTool.dragsTree": false,
                "draggingTool.isGridSnapEnabled": true,
                nodeSelectionAdornmentTemplate: emptyAdornment,
                groupSelectionAdornmentTemplate: emptyAdornment
            });
    myPaletteBasic.nodeTemplateMap.add("DEFAULT", newElementTemplate);
    myPaletteBasic.nodeTemplateMap.add("PERSON", personTemplate);
    myPaletteBasic.nodeTemplateMap.add("CONSUMER", consumerTemplate);
    myPaletteBasic.nodeTemplateMap.add("", defaultTemplate);

    myPaletteBasic.model = new go.GraphLinksModel(paletteModel.basic);

    //Basic elements palette
    if (myPaletteGeneral !== undefined) {
        myPaletteGeneral.clear();
        myPaletteGeneral.div = null;
    }
    // initialize the Palette that is on the left side of the page
    // noinspection JSUndeclaredVariable
    myPaletteGeneral =
        gojs(go.Palette, "paletteDivGeneral",  // must name or refer to the DIV HTML element
            {
                scrollsPageOnFocus: false,
                layout: gojs(PoolLayout, {
                    sorting: go.GridLayout.Forward
                }),
                hoverDelay: 100,
                "draggingTool.dragsTree": false,
                "draggingTool.isGridSnapEnabled": true,
                nodeSelectionAdornmentTemplate: emptyAdornment,
                groupSelectionAdornmentTemplate: emptyAdornment
            });
    myPaletteGeneral.nodeTemplateMap.add("DEFAULT", newElementTemplate);
    myPaletteGeneral.nodeTemplateMap.add("PERSON", personTemplate);
    myPaletteGeneral.nodeTemplateMap.add("CONSUMER", consumerTemplate);
    myPaletteGeneral.nodeTemplateMap.add("", defaultTemplate);

    myPaletteGeneral.model = new go.GraphLinksModel(paletteModel.general);

    if (myDiagram !== undefined) {
        myDiagram.clear();
        myDiagram.div = null;
    }
    myDiagram =
        gojs(go.Diagram, "diagramDiv",  // create a Diagram for the DIV HTML element
            {
                // position the graph in the middle of the diagram
                initialContentAlignment: go.Spot.Center,
                hoverDelay: 100,
                // use a custom ResizingTool (along with a custom ResizeAdornment on each Group)
                resizingTool: new LaneResizingTool(),
                // use a simple layout that ignores links to stack the top-level Pool Groups next to each other
                layout: gojs(PoolLayout),
                // a clipboard copied node is pasted into the original node's group (i.e. lane).
                "commandHandler.copiesGroupKey": true,
                // automatically re-layout the swim lanes after dragging the selection
                "SelectionMoved": relayoutDiagram,  // this DiagramEvent listener is
                "SelectionCopied": relayoutDiagram, // defined above
                "animationManager.isEnabled": false,

                // allow double-click in background to create a new node
                "clickCreatingTool.archetypeNodeData": {
                    name: "Element",
                    shape: {fill: "white"},
                    kind: "LAYER"
                },
                mouseDrop: function (e) {
                    finishDrop(e, null);
                },
                /*                layout:  // Diagram has simple horizontal layout
                                    gojs(go.GridLayout,
                                        { wrappingWidth: Infinity, alignment: go.GridLayout.Position, cellSize: new go.Size(1, 1) }),*/
                // allow Ctrl-G to call groupSelection()
                "commandHandler.archetypeGroupData": {name: "Group", isGroup: true, color: "blue"},
                "LinkDrawn": showLinkLabel,  // this DiagramEvent listener is defined below
                "LinkRelinked": showLinkLabel,
                scrollsPageOnFocus: false,
                allowDrop: true,  // must be true to accept drops from the Palette
                // enable undo & redo
                "undoManager.isEnabled": true
            });
    const newElementTemplate_ = newElementTemplate;
    newElementTemplate_.mouseEnter = function (e, obj) {
        showPorts(obj.part, true);
    };
    newElementTemplate_.mouseLeave = function (e, obj) {
        showPorts(obj.part, false);
    };
    myDiagram.nodeTemplateMap.add("DEFAULT", newElementTemplate_);
    const personTemplate_ = personTemplate;
    personTemplate_.mouseEnter = function (e, obj) {
        showPorts(obj.part, true);
    };
    personTemplate_.mouseLeave = function (e, obj) {
        showPorts(obj.part, false);
    };
    myDiagram.nodeTemplateMap.add("PERSON", personTemplate_);
    const consumerTemplate_ = consumerTemplate;
    consumerTemplate_.mouseEnter = function (e, obj) {
        showPorts(obj.part, true);
    };
    consumerTemplate_.mouseLeave = function (e, obj) {
        showPorts(obj.part, false);
    };
    myDiagram.nodeTemplateMap.add("CONSUMER", consumerTemplate_);
    myDiagram.groupTemplateMap.add("ARCHITECTURE_MODEL", architectureModelTemplate);
    myDiagram.groupTemplateMap.add("LAYER", layerTemplate);
    myDiagram.groupTemplateMap.add("SYSTEM", systemTemplate);
    myDiagram.groupTemplateMap.add("CONTAINER", containerTemplate);
    myDiagram.nodeTemplateMap.add("COMPONENT", componentTemplate);
    myDiagram.nodeTemplateMap.add("", basicElement);

    // The link shape and arrowhead have their stroke brush data bound to the "color" property
    myDiagram.linkTemplate =
        gojs(go.Link,  // the whole link panel
            {
                routing: go.Link.Orthogonal,
                curve: go.Link.None,
                corner: 5,
                toShortLength: 4,
                relinkableFrom: true,
                relinkableTo: true,
                reshapable: true,
                resegmentable: true,
                // mouse-overs subtly highlight links:
                mouseEnter: function (e, link) {
                    link.findObject("HIGHLIGHT").stroke = "rgba(30,144,255,0.2)";
                },
                mouseLeave: function (e, link) {
                    link.findObject("HIGHLIGHT").stroke = "transparent";
                }
            },
            new go.Binding("points").makeTwoWay(),
            gojs(go.Shape,  // the highlight shape, normally transparent
                {isPanelMain: true, strokeWidth: 8, stroke: "transparent", name: "HIGHLIGHT"}),
            gojs(go.Shape,  // the link path shape
                {isPanelMain: true, stroke: "gray", strokeWidth: 2}),
            gojs(go.Shape,  // the arrowhead
                {toArrow: "standard", stroke: null, fill: "gray"}),
            gojs(go.Panel, "Auto",  // the link label, normally not visible
                {visible: false, name: "LABEL", segmentIndex: 2, segmentFraction: 0.5},
                new go.Binding("visible", "visible").makeTwoWay(),
                gojs(go.Shape, "RoundedRectangle",  // the label shape
                    {fill: "#F8F8F8", stroke: null}),
                gojs(go.TextBlock, "Yes",  // the label
                    {
                        textAlign: "center",
                        font: "10pt helvetica, arial, sans-serif",
                        stroke: "#333333",
                        editable: true
                    },
                    new go.Binding("text", "", OpenArchiWrapper.toName).makeTwoWay(OpenArchiWrapper.fromName))
            ),
            { // this tooltip Adornment is shared by all links
                toolTip:
                    gojs(go.Adornment, "Auto",
                        gojs(go.Shape, {fill: "#FFFFCC"}),
                        gojs(go.TextBlock, {margin: 4},  // the tooltip shows the result of calling linkInfo(data)
                            new go.Binding("text", "", linkInfo))
                    ),
                // the same context menu Adornment is shared by all links
                contextMenu: partContextMenu
            }
        );


    // Groups consist of a title in the color given by the group node data
    // above a translucent gray rectangle surrounding the member parts
    myDiagram.groupTemplate =
        gojs(go.Group, "Vertical",
            {
                selectionObjectName: "PANEL",  // selection handle goes around shape, not label
                ungroupable: true
            },  // enable Ctrl-Shift-G to ungroup a selected Group
            gojs(go.TextBlock,
                {
                    name: "text",
                    font: "bold 19px sans-serif",
                    isMultiline: false,  // don't allow newlines in text
                    editable: true  // allow in-place editing by user
                },
                new go.Binding("text", "", OpenArchiWrapper.toName).makeTwoWay(OpenArchiWrapper.fromName),
                new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor)),
            gojs(go.Panel, "Auto",
                {name: "PANEL"},
                gojs(go.Shape, "Rectangle",  // the rectangular shape around the members
                    {
                        fill: "rgba(128,128,128,0.2)",
                        stroke: "gray",
                        strokeWidth: 3,
                        portId: "",
                        cursor: "pointer",  // the Shape is the port, not the whole Node
                        // allow all kinds of links from and to this port
                        fromLinkable: true,
                        fromLinkableSelfNode: true,
                        fromLinkableDuplicates: true,
                        toLinkable: true,
                        toLinkableSelfNode: true,
                        toLinkableDuplicates: true
                    },
                    new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                    new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
                gojs(go.Placeholder, {margin: 10, background: "transparent"})  // represents where the members are
            ),
            { // this tooltip Adornment is shared by all groups
                toolTip:
                    gojs(go.Adornment, "Auto",
                        gojs(go.Shape, {fill: "#FFFFCC"}),
                        gojs(go.TextBlock, {margin: 4},
                            // bind to tooltip, not to Group.data, to allow access to Group properties
                            new go.Binding("text", "", groupInfo).ofObject())
                    ),
                // the same context menu Adornment is shared by all groups
                contextMenu: partContextMenu
            }
        );

    // define a custom resize adornment that has two resize handles if the group is expanded
    myDiagram.groupTemplate.resizeAdornmentTemplate =
        gojs(go.Adornment, "Spot",
            gojs(go.Placeholder),
            gojs(go.Shape,  // for changing the length of a lane
                {
                    alignment: go.Spot.Right,
                    desiredSize: new go.Size(7, 50),
                    fill: "lightblue", stroke: "dodgerblue",
                    cursor: "col-resize"
                },
                new go.Binding("visible", "", function (ad) {
                    if (ad.adornedPart === null) return false;
                    return ad.adornedPart.isSubGraphExpanded;
                }).ofObject()),
            gojs(go.Shape,  // for changing the breadth of a lane
                {
                    alignment: go.Spot.Bottom,
                    desiredSize: new go.Size(50, 7),
                    fill: "lightblue", stroke: "dodgerblue",
                    cursor: "row-resize"
                },
                new go.Binding("visible", "", function (ad) {
                    if (ad.adornedPart === null) return false;
                    return ad.adornedPart.isSubGraphExpanded;
                }).ofObject())
        );

    // Define the behavior for the Diagram background:

    function diagramInfo(model) {  // Tooltip info for the diagram's model
        return "Model:\n" + model.nodeDataArray.length + " nodes, " + model.linkDataArray.length + " links";
    }

    // provide a tooltip for the background of the Diagram, when not over any Part
    myDiagram.toolTip =
        gojs(go.Adornment, "Auto",
            gojs(go.Shape, {fill: "#FFFFCC"}),
            gojs(go.TextBlock, {margin: 4},
                new go.Binding("text", "", diagramInfo))
        );

    // provide a context menu for the background of the Diagram, when not over any Part
    myDiagram.contextMenu =
        gojs(go.Adornment, "Vertical",
            makeButton("Paste",
                function (e, obj) {
                    e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint);
                },
                function (o) {
                    return o.diagram.commandHandler.canPasteSelection();
                }),
            makeButton("Undo",
                function (e, obj) {
                    e.diagram.commandHandler.undo();
                },
                function (o) {
                    return o.diagram.commandHandler.canUndo();
                }),
            makeButton("Redo",
                function (e, obj) {
                    e.diagram.commandHandler.redo();
                },
                function (o) {
                    return o.diagram.commandHandler.canRedo();
                })
        );
    fillDiagram(nodeDataArray, linkDataArray);

    // temporary links used by LinkingTool and RelinkingTool are also orthogonal:
    myDiagram.toolManager.linkingTool.temporaryLink.routing = go.Link.Orthogonal;
    myDiagram.toolManager.relinkingTool.temporaryLink.routing = go.Link.Orthogonal;

    // this DiagramEvent is raised when the user has drag-and-dropped something
    // from another Diagram (a Palette in this case) into this Diagram
    myDiagram.addDiagramListener("ExternalObjectsDropped", function (e) {
        // stop any ongoing text editing
        const node = myDiagram.findNodeForKey(e.subject.first().key);
        const data = node.data;
        if (!data.kind) {
            if (myDiagram.currentTool instanceof go.TextEditingTool) {
                myDiagram.currentTool.acceptText(go.TextEditingTool.LostFocus);
            }
            let modal = $('#basic-element-data');
            modal.attr("data-key", data.key);
            $('#element-image-2').val('');
            $('#elementTypesDropdown').val('');
            modal.on('show.bs.modal', function (event) {
                const button = $(event.relatedTarget); // Button that triggered the modal
                const modal = $(this);
                modal.find('.modal-title').text('Basic element data for ' + data.name);
                $("#element-name").val(data.name)
            });
            modal.modal({
                backdrop: 'static',
                keyboard: false,
                show: true
            });
        } else {
            let currentViewMode = getCurrentViewMode();
            if (currentViewMode === "Full") {
                if (data.id) {
                    let param = {};
                    let uri = "/api/models/" + data.id;

                    if (!meta.isPrototyper) {
                        param = {suffix: "cloned"};
                        uri = uri + "/clone";
                    }
                    $.get(uri, param)
                        .done(function (completeModel) {
                            expand(completeModel);
                            myDiagram.model.removeNodeData(data);
                        });
                }
            }
        }
        fixMeta();
    });
}

function fillDiagram(nodeDataArray, linkDataArray) {
    let nodeDataArray_ = [];
    let linkDataArray_ = [];
    if (nodeDataArray !== "") {
        if (typeof nodeDataArray === "string") {
            nodeDataArray_ = JSON.parse(nodeDataArray)
        } else {
            nodeDataArray_ = nodeDataArray;
        }
        if (typeof linkDataArray === "string") {
            linkDataArray_ = JSON.parse(linkDataArray)
        } else {
            linkDataArray_ = linkDataArray;
        }
        if (!commons.prototype.isEmpty(nodeDataArray_)) {
            myDiagram.model = new go.GraphLinksModel(nodeDataArray_, linkDataArray_);
        }
    }
}

// this is shown by the mouseHover event handler
const nodeHoverAdornment =
    gojs(go.Adornment, "Spot",
        {
            background: "transparent",
            // hide the Adornment when the mouse leaves it
            zOrder: 2800
        },
        gojs(go.Placeholder,
            {
                background: "transparent",  // to allow this Placeholder to be "seen" by mouse events
                isActionable: true,  // needed because this is in a temporary Layer
                click: function (e, obj) {
                    const node = obj.part.adornedPart;
                    node.diagram.select(node);
                }
            })
    );


function fillPalettes(data) {
    paletteModel = {
        basic: [],
        general: []
    };
    const basicElements = OpenArchiWrapper.fixCategory(data.basicElements);
    const generalElements = OpenArchiWrapper.fixCategory(data.generalElements);
    paletteModel.basic.pushAll(basicElements);
    paletteModel.general.pushAll(generalElements);
    myPaletteBasic.model = new go.GraphLinksModel(basicElements);
    myPaletteGeneral.model = new go.GraphLinksModel(generalElements);
}

function resizePalete(containerElement, palette) {
    const pdrag = $("#" + containerElement);
    const pdragDiv = palette.div;
    const width = pdrag.width();
    const height = pdrag.height();
    pdragDiv.style.width = width;
    palette.documentBounds.width = width;
    pdragDiv.style.height = height;
    palette.documentBounds.height = height;
    palette.layoutDiagram(true);
    palette.requestUpdate();
}