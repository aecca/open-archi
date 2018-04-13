function initSequenceDiagram() {

    const $ = go.GraphObject.make;

    myDiagram =
        gojs(go.Diagram, "diagramDiv", // must be the ID or reference to an HTML DIV
            {
                initialContentAlignment: go.Spot.Center,
                allowCopy: false,
                linkingTool: $(MessagingTool),  // defined below
                "resizingTool.isGridSnapEnabled": true,
                "draggingTool.gridSnapCellSize": new go.Size(1, MessageSpacing / 4),
                "draggingTool.isGridSnapEnabled": true,
                // automatically extend Lifelines as Activities are moved or resized
                "SelectionMoved": ensureLifelineHeights,
                "PartResized": ensureLifelineHeights,
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

    // define the Lifeline Node template.
    myDiagram.groupTemplate =
        gojs(go.Group, "Vertical",
            {
                locationSpot: go.Spot.Bottom,
                locationObjectName: "HEADER",
                minLocation: new go.Point(0, 0),
                maxLocation: new go.Point(9999, 0),
                selectionObjectName: "HEADER"
            },
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            gojs(go.Panel, "Auto",
                {name: "HEADER"},
                gojs(go.Shape, "Rectangle",
                    {
                        fill: gojs(go.Brush, "Linear", {0: "#bbdefb", 1: go.Brush.darkenBy("#bbdefb", 0.1)}),
                        stroke: null
                    }),
                gojs(go.TextBlock,
                    {
                        margin: 5,
                        font: "400 10pt Source Sans Pro, sans-serif"
                    },
                    new go.Binding("text", "text"))
            ),
            gojs(go.Shape,
                {
                    figure: "LineV",
                    fill: null,
                    stroke: "gray",
                    strokeDashArray: [3, 3],
                    width: 1,
                    alignment: go.Spot.Center,
                    portId: "",
                    fromLinkable: true,
                    fromLinkableDuplicates: true,
                    toLinkable: true,
                    toLinkableDuplicates: true,
                    cursor: "pointer"
                },
                new go.Binding("height", "duration", computeLifelineHeight))
        );

    // define the Activity Node template
    myDiagram.nodeTemplate =
        gojs(go.Node,
            {
                locationSpot: go.Spot.Top,
                locationObjectName: "SHAPE",
                minLocation: new go.Point(NaN, LinePrefix - ActivityStart),
                maxLocation: new go.Point(NaN, 19999),
                selectionObjectName: "SHAPE",
                resizable: true,
                resizeObjectName: "SHAPE",
                resizeAdornmentTemplate:
                    gojs(go.Adornment, "Spot",
                        gojs(go.Placeholder),
                        gojs(go.Shape,  // only a bottom resize handle
                            {
                                alignment: go.Spot.Bottom, cursor: "col-resize",
                                desiredSize: new go.Size(6, 6), fill: "yellow"
                            })
                    )
            },
            new go.Binding("location", "", computeActivityLocation).makeTwoWay(backComputeActivityLocation),
            gojs(go.Shape, "Rectangle",
                {
                    name: "SHAPE",
                    fill: "white", stroke: "black",
                    width: ActivityWidth,
                    // allow Activities to be resized down to 1/4 of a time unit
                    minSize: new go.Size(ActivityWidth, computeActivityHeight(0.25))
                },
                new go.Binding("height", "duration", computeActivityHeight).makeTwoWay(backComputeActivityHeight))
        );

    // define the Message Link template.
    myDiagram.linkTemplate =
        $(MessageLink,  // defined below
            {selectionAdorned: true, curviness: 0},
            gojs(go.Shape, "Rectangle",
                {stroke: "black"}),
            gojs(go.Shape,
                {toArrow: "OpenTriangle", stroke: "black"}),
            gojs(go.TextBlock,
                {
                    font: "400 9pt Source Sans Pro, sans-serif",
                    segmentIndex: 0,
                    segmentOffset: new go.Point(NaN, NaN),
                    isMultiline: false,
                    editable: true
                },
                new go.Binding("text", "text").makeTwoWay())
        );

    // create the graph by reading the JSON data saved in "modelToSaveOrLoad" textarea element
    load();
}

function ensureLifelineHeights(e) {
    // iterate over all Activities (ignore Groups)
    const arr = myDiagram.model.nodeDataArray;
    let max = -1;
    for (var i = 0; i < arr.length; i++) {
        const act = arr[i];
        if (act.isGroup) continue;
        max = Math.max(max, act.start + act.duration);
    }
    if (max > 0) {
        // now iterate over only Groups
        for (var i = 0; i < arr.length; i++) {
            const gr = arr[i];
            if (!gr.isGroup) continue;
            if (max > gr.duration) {  // this only extends, never shrinks
                myDiagram.model.setDataProperty(gr, "duration", max);
            }
        }
    }
}

// some parameters
LinePrefix = 20;  // vertical starting point in document for all Messages and Activations
LineSuffix = 30;  // vertical length beyond the last message time
MessageSpacing = 20;  // vertical distance between Messages at different steps
ActivityWidth = 10;  // width of each vertical activity bar
ActivityStart = 5;  // height before start message time
ActivityEnd = 5;  // height beyond end message time

function computeLifelineHeight(duration) {
    return LinePrefix + duration * MessageSpacing + LineSuffix;
}

function computeActivityLocation(act) {
    const groupdata = myDiagram.model.findNodeDataForKey(act.group);
    if (groupdata === null) return new go.Point();
    // get location of Lifeline's starting point
    const grouploc = go.Point.parse(groupdata.loc);
    return new go.Point(grouploc.x, convertTimeToY(act.start) - ActivityStart);
}

function backComputeActivityLocation(loc, act) {
    myDiagram.model.setDataProperty(act, "start", convertYToTime(loc.y + ActivityStart));
}

function computeActivityHeight(duration) {
    return ActivityStart + duration * MessageSpacing + ActivityEnd;
}

function backComputeActivityHeight(height) {
    return (height - ActivityStart - ActivityEnd) / MessageSpacing;
}

// time is just an abstract small non-negative integer
// here we map between an abstract time and a vertical position
function convertTimeToY(t) {
    return t * MessageSpacing + LinePrefix;
}

function convertYToTime(y) {
    return (y - LinePrefix) / MessageSpacing;
}


// a custom routed Link
function MessageLink() {
    go.Link.call(this);
    this.time = 0;  // use this "time" value when this is the temporaryLink
}

go.Diagram.inherit(MessageLink, go.Link);

/** @override */
MessageLink.prototype.getLinkPoint = function (node, port, spot, from, ortho, othernode, otherport) {
    const p = port.getDocumentPoint(go.Spot.Center);
    const r = new go.Rect(port.getDocumentPoint(go.Spot.TopLeft),
        port.getDocumentPoint(go.Spot.BottomRight));
    const op = otherport.getDocumentPoint(go.Spot.Center);

    const data = this.data;
    const time = data !== null ? data.time : this.time;  // if not bound, assume this has its own "time" property

    const aw = this.findActivityWidth(node, time);
    const x = (op.x > p.x ? p.x + aw / 2 : p.x - aw / 2);
    const y = convertTimeToY(time);
    return new go.Point(x, y);
};

MessageLink.prototype.findActivityWidth = function (node, time) {
    let aw = ActivityWidth;
    if (node instanceof go.Group) {
        // see if there is an Activity Node at this point -- if not, connect the link directly with the Group's lifeline
        if (!node.memberParts.any(function (mem) {
                const act = mem.data;
                return (act !== null && act.start <= time && time <= act.start + act.duration);
            })) {
            aw = 0;
        }
    }
    return aw;
};

/** @override */
MessageLink.prototype.getLinkDirection = function (node, port, linkpoint, spot, from, ortho, othernode, otherport) {
    const p = port.getDocumentPoint(go.Spot.Center);
    const op = otherport.getDocumentPoint(go.Spot.Center);
    const right = op.x > p.x;
    return right ? 0 : 180;
};

/** @override */
MessageLink.prototype.computePoints = function () {
    if (this.fromNode === this.toNode) {  // also handle a reflexive link as a simple orthogonal loop
        const data = this.data;
        const time = data !== null ? data.time : this.time;  // if not bound, assume this has its own "time" property
        const p = this.fromNode.port.getDocumentPoint(go.Spot.Center);
        const aw = this.findActivityWidth(this.fromNode, time);

        const x = p.x + aw / 2;
        const y = convertTimeToY(time);
        this.clearPoints();
        this.addPoint(new go.Point(x, y));
        this.addPoint(new go.Point(x + 50, y));
        this.addPoint(new go.Point(x + 50, y + 5));
        this.addPoint(new go.Point(x, y + 5));
        return true;
    } else {
        return go.Link.prototype.computePoints.call(this);
    }
};

// end MessageLink


// a custom LinkingTool that fixes the "time" (i.e. the Y coordinate)
// for both the temporaryLink and the actual newly created Link
function MessagingTool() {
    go.LinkingTool.call(this);
    const $ = go.GraphObject.make;
    this.temporaryLink =
        $(MessageLink,
            gojs(go.Shape, "Rectangle",
                {stroke: "magenta", strokeWidth: 2}),
            gojs(go.Shape,
                {toArrow: "OpenTriangle", stroke: "magenta"}));
};
go.Diagram.inherit(MessagingTool, go.LinkingTool);

/** @override */
MessagingTool.prototype.doActivate = function () {
    go.LinkingTool.prototype.doActivate.call(this);
    const time = convertYToTime(this.diagram.firstInput.documentPoint.y);
    this.temporaryLink.time = Math.ceil(time);  // round up to an integer value
};

/** @override */
MessagingTool.prototype.insertLink = function (fromnode, fromport, tonode, toport) {
    const newlink = go.LinkingTool.prototype.insertLink.call(this, fromnode, fromport, tonode, toport);
    if (newlink !== null) {
        const model = this.diagram.model;
        // specify the time of the message
        const start = this.temporaryLink.time;
        const duration = 1;
        newlink.data.time = start;
        model.setDataProperty(newlink.data, "text", "msg");
        // and create a new Activity node data in the "to" group data
        const newact = {
            group: newlink.data.to,
            start: start,
            duration: duration
        };
        model.addNodeData(newact);
        // now make sure all Lifelines are long enough
        ensureLifelineHeights();
    }
    return newlink;
};
// end MessagingTool


// Show the diagram's model in JSON format
function save() {
    document.getElementById("modelToSaveOrLoad").value = JSON.stringify(myDiagram.model, null, 2);
    let textContent = myDiagram.model.toJson();
    myDiagram.isModified = false;
}

function load() {
    myDiagram.model = go.Model.fromJson(document.getElementById("modelToSaveOrLoad").value);
}