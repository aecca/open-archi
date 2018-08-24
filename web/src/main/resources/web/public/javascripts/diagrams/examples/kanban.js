MINLENGTH = 200;  // this controls the minimum length of any swimlane
MINBREADTH = 100;  // this controls the minimum breadth of any non-collapsed swimlane

// some shared functions

// this is called after nodes have been moved
function relayoutDiagram() {
    myDiagram.selection.each(function (n) {
        n.invalidateLayout();
    });
    myDiagram.layoutDiagram();
}

// compute the minimum size of the whole diagram needed to hold all of the Lane Groups
function computeMinPoolSize() {
    let len = MINLENGTH;
    myDiagram.findTopLevelGroups().each(function (lane) {
        const holder = lane.placeholder;
        if (holder !== null) {
            const sz = holder.actualBounds;
            len = Math.max(len, sz.height);
        }
        const box = lane.selectionObject;
        // naturalBounds instead of actualBounds to disregard the shape's stroke width
        len = Math.max(len, box.naturalBounds.height);
    });
    return new go.Size(NaN, len);
}

// compute the minimum size for a particular Lane Group
function computeLaneSize(lane) {
    // assert(lane instanceof go.Group);
    const sz = computeMinLaneSize(lane);
    if (lane.isSubGraphExpanded) {
        const holder = lane.placeholder;
        if (holder !== null) {
            const hsz = holder.actualBounds;
            sz.width = Math.max(sz.width, hsz.width);
        }
    }
    // minimum breadth needs to be big enough to hold the header
    const hdr = lane.findObject("HEADER");
    if (hdr !== null) sz.width = Math.max(sz.width, hdr.actualBounds.width);
    return sz;
}

// determine the minimum size of a Lane Group, even if collapsed
function computeMinLaneSize(lane) {
    if (!lane.isSubGraphExpanded) return new go.Size(1, MINLENGTH);
    return new go.Size(MINBREADTH, MINLENGTH);
}


// define a custom grid layout that makes sure the length of each lane is the same
// and that each lane is broad enough to hold its subgraph
function PoolLayout() {
    go.GridLayout.call(this);
    this.cellSize = new go.Size(1, 1);
    this.wrappingColumn = Infinity;
    this.wrappingWidth = Infinity;
    this.spacing = new go.Size(0, 0);
    this.alignment = go.GridLayout.Position;
}

go.Diagram.inherit(PoolLayout, go.GridLayout);

/** @override */
PoolLayout.prototype.doLayout = function (coll) {
    const diagram = this.diagram;
    if (diagram === null) return;
    diagram.startTransaction("PoolLayout");
    // make sure all of the Group Shapes are big enough
    const minsize = computeMinPoolSize();
    diagram.findTopLevelGroups().each(function (lane) {
        if (!(lane instanceof go.Group)) return;
        const shape = lane.selectionObject;
        if (shape !== null) {  // change the desiredSize to be big enough in both directions
            const sz = computeLaneSize(lane);
            shape.width = (!isNaN(shape.width)) ? Math.max(shape.width, sz.width) : sz.width;
            shape.height = (isNaN(shape.height) ? minsize.height : Math.max(shape.height, minsize.height));
            const cell = lane.resizeCellSize;
            if (!isNaN(shape.width) && !isNaN(cell.width) && cell.width > 0) shape.width = Math.ceil(shape.width / cell.width) * cell.width;
            if (!isNaN(shape.height) && !isNaN(cell.height) && cell.height > 0) shape.height = Math.ceil(shape.height / cell.height) * cell.height;
        }
    });
    // now do all of the usual stuff, according to whatever properties have been set on this GridLayout
    go.GridLayout.prototype.doLayout.call(this, coll);
    diagram.commitTransaction("PoolLayout");
};

// end PoolLayout class


function initKanban(nodeDataArray, linkDataArray) {

    if (myDiagram !== undefined) {
        myDiagram.clear();
        myDiagram.div = null;
    }

    // noinspection JSUndeclaredVariable
    myDiagram = gojs(go.Diagram, "diagramDiv",  // create a Diagram for the DIV HTML element
        {
            // start everything in the middle of the viewport
            contentAlignment: go.Spot.Center,
            // use a simple layout to stack the top-level Groups next to each other
            layout: gojs(PoolLayout),
            // disallow nodes to be dragged to the diagram's background
            mouseDrop: function (e) {
                e.diagram.currentTool.doCancel();
            },
            // a clipboard copied node is pasted into the original node's group (i.e. lane).
            "commandHandler.copiesGroupKey": true,
            // automatically re-layout the swim lanes after dragging the selection
            "SelectionMoved": relayoutDiagram,  // this DiagramEvent listener is
            "SelectionCopied": relayoutDiagram, // defined above
            "animationManager.isEnabled": false,
            "undoManager.isEnabled": true
        });

    // Customize the dragging tool:
    // When dragging a Node set its opacity to 0.7 and move it to the foreground layer
    myDiagram.toolManager.draggingTool.doActivate = function () {
        go.DraggingTool.prototype.doActivate.call(this);
        this.currentPart.opacity = 0.7;
        this.currentPart.layerName = "Foreground";
    };
    myDiagram.toolManager.draggingTool.doDeactivate = function () {
        this.currentPart.opacity = 1;
        this.currentPart.layerName = "";
        go.DraggingTool.prototype.doDeactivate.call(this);
    };

    // There are only three note colors by default, blue, red, and yellow but you could add more here:
    const noteColors = ['#009CCC', '#CC293D', '#FFD700'];

    function getNoteColor(num) {
        return noteColors[Math.min(num, noteColors.length - 1)];
    }

    myDiagram.nodeTemplate =
        gojs(go.Node, "Horizontal",
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            gojs(go.Shape, "Rectangle", {
                    fill: '#009CCC', strokeWidth: 1, stroke: '#009CCC',
                    width: 6, stretch: go.GraphObject.Vertical, alignment: go.Spot.Left,
                    // if a user clicks the colored portion of a node, cycle through colors
                    click: function (e, obj) {
                        myDiagram.startTransaction("Update node color");
                        let newColor = parseInt(obj.part.data.color) + 1;
                        if (newColor > noteColors.length - 1) newColor = 0;
                        myDiagram.model.setDataProperty(obj.part.data, "color", newColor);
                        myDiagram.commitTransaction("Update node color");
                    }
                },
                new go.Binding("fill", "color", getNoteColor),
                new go.Binding("stroke", "color", getNoteColor)
            ),
            gojs(go.Panel, "Auto",
                gojs(go.Shape, "Rectangle", {fill: "white", stroke: '#CCCCCC'}),
                gojs(go.Panel, "Table",
                    {width: 130, minSize: new go.Size(NaN, 50)},
                    gojs(go.TextBlock,
                        {
                            name: 'TEXT',
                            margin: 6, font: '11px Lato, sans-serif', editable: true,
                            stroke: "#000", maxSize: new go.Size(130, NaN),
                            alignment: go.Spot.TopLeft
                        },
                        new go.Binding("text", "text").makeTwoWay())
                )
            )
        );

    // unmovable node that acts as a button
    myDiagram.nodeTemplateMap.add('newbutton',
        gojs(go.Node, "Horizontal",
            {
                selectable: false,
                click: function (e, node) {
                    myDiagram.startTransaction('add node');
                    const newdata = {
                        group: "Problems",
                        loc: "0 50",
                        text: "New item " + node.containingGroup.memberParts.count,
                        color: 0
                    };
                    myDiagram.model.addNodeData(newdata);
                    myDiagram.commitTransaction('add node');
                    var node = myDiagram.findNodeForData(newdata);
                    myDiagram.select(node);
                    myDiagram.commandHandler.editTextBlock();
                },
                background: 'white'
            },
            gojs(go.Panel, "Auto",
                gojs(go.Shape, "Rectangle", {strokeWidth: 0, stroke: null, fill: '#6FB583'}),
                gojs(go.Shape, "PlusLine", {
                    margin: 6,
                    strokeWidth: 2,
                    width: 12,
                    height: 12,
                    stroke: 'white',
                    background: '#6FB583'
                })
            ),
            gojs(go.TextBlock, "New item", {font: '10px Lato, sans-serif', margin: 6,})
        )
    );

    // While dragging, highlight the dragged-over group
    function highlightGroup(grp, show) {
        if (show) {
            const part = myDiagram.toolManager.draggingTool.currentPart;
            if (part.containingGroup !== grp) {
                grp.isHighlighted = true;
                return;
            }
        }
        grp.isHighlighted = false;
    }

    myDiagram.groupTemplate =
        gojs(go.Group, "Vertical",
            {
                selectable: false,
                selectionObjectName: "SHAPE", // even though its not selectable, this is used in the layout
                layerName: "Background",  // all lanes are always behind all nodes and links
                layout: gojs(go.GridLayout,  // automatically lay out the lane's subgraph
                    {
                        wrappingColumn: 1,
                        cellSize: new go.Size(1, 1),
                        spacing: new go.Size(5, 5),
                        alignment: go.GridLayout.Position,
                        comparer: function (a, b) {  // can re-order tasks within a lane
                            const ay = a.location.y;
                            const by = b.location.y;
                            if (isNaN(ay) || isNaN(by)) return 0;
                            if (ay < by) return -1;
                            if (ay > by) return 1;
                            return 0;
                        }
                    }),
                click: function (e, grp) {  // allow simple click on group to clear selection
                    if (!e.shift && !e.control && !e.meta) e.diagram.clearSelection();
                },
                computesBoundsAfterDrag: true,  // needed to prevent recomputing Group.placeholder bounds too soon
                handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                mouseDragEnter: function (e, grp, prev) {
                    highlightGroup(grp, true);
                },
                mouseDragLeave: function (e, grp, next) {
                    highlightGroup(grp, false);
                },
                mouseDrop: function (e, grp) {  // dropping a copy of some Nodes and Links onto this Group adds them to this Group
                    // don't allow drag-and-dropping a mix of regular Nodes and Groups
                    if (e.diagram.selection.all(function (n) {
                            return !(n instanceof go.Group);
                        })) {
                        let ok = grp.addMembers(grp.diagram.selection, true);
                        if (!ok) grp.diagram.currentTool.doCancel();
                    }
                },
                subGraphExpandedChanged: function (grp) {
                    const shp = grp.selectionObject;
                    if (grp.diagram.undoManager.isUndoingRedoing) return;
                    if (grp.isSubGraphExpanded) {
                        shp.width = grp._savedBreadth;
                    } else {
                        grp._savedBreadth = shp.width;
                        shp.width = NaN;
                    }
                }
            },
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),
            // the lane header consisting of a TextBlock and an expander button
            gojs(go.Panel, "Horizontal",
                {
                    name: "HEADER",
                    angle: 0,  // maybe rotate the header to read sideways going up
                    alignment: go.Spot.Left
                },
                gojs("SubGraphExpanderButton", {margin: 5}),  // this remains always visible
                gojs(go.Panel, "Horizontal",  // this is hidden when the swimlane is collapsed
                    new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                    gojs(go.TextBlock,  // the lane label
                        {font: "15px Lato, sans-serif", editable: true, margin: new go.Margin(2, 0, 0, 0)},
                        new go.Binding("text", "text").makeTwoWay())
                )
            ),  // end Horizontal Panel
            gojs(go.Panel, "Auto",  // the lane consisting of a background Shape and a Placeholder representing the subgraph
                gojs(go.Shape, "Rectangle",  // this is the resized object
                    {name: "SHAPE", fill: "#F1F1F1", stroke: null, strokeWidth: 4},
                    new go.Binding("fill", "isHighlighted", function (h) {
                        return h ? "#D6D6D6" : "#F1F1F1";
                    }).ofObject(),
                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
                gojs(go.Placeholder,
                    {padding: 12, alignment: go.Spot.TopLeft}),
                gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                    {
                        name: "LABEL",
                        font: "15px Lato, sans-serif", editable: true,
                        angle: 90, alignment: go.Spot.TopLeft, margin: new go.Margin(4, 0, 0, 2)
                    },
                    new go.Binding("visible", "isSubGraphExpanded", function (e) {
                        return !e;
                    }).ofObject(),
                    new go.Binding("text", "text").makeTwoWay())
            )  // end Auto Panel
        );  // end Group
    myDiagram.model = new go.GraphLinksModel(nodeDataArray, linkDataArray);
    myDiagram.delayInitialization(relayoutDiagram);
    const pos = myDiagram.model.modelData.position;
    if (pos) {
        myDiagram.initialPosition = go.Point.parse(pos);
    }

    // Set up a Part as a legend, and place it directly on the diagram
    myDiagram.add(
        gojs(go.Part, "Table",
            {position: new go.Point(300, 10), selectable: false},
            gojs(go.TextBlock, "Key",
                {row: 0, font: "700 14px Droid Serif, sans-serif"}),  // end row 0
            gojs(go.Panel, "Horizontal",
                {row: 1, alignment: go.Spot.Left},
                gojs(go.Shape, "Rectangle",
                    {desiredSize: new go.Size(10, 10), fill: '#CC293D', margin: 5}),
                gojs(go.TextBlock, "Halted",
                    {font: "700 13px Droid Serif, sans-serif"})
            ),  // end row 1
            gojs(go.Panel, "Horizontal",
                {row: 2, alignment: go.Spot.Left},
                gojs(go.Shape, "Rectangle",
                    {desiredSize: new go.Size(10, 10), fill: '#FFD700', margin: 5}),
                gojs(go.TextBlock, "In Progress",
                    {font: "700 13px Droid Serif, sans-serif"})
            ),  // end row 2
            gojs(go.Panel, "Horizontal",
                {row: 3, alignment: go.Spot.Left},
                gojs(go.Shape, "Rectangle",
                    {desiredSize: new go.Size(10, 10), fill: '#009CCC', margin: 5}),
                gojs(go.TextBlock, "Completed",
                    {font: "700 13px Droid Serif, sans-serif"})
            )  // end row 3
        ));

}  // end init