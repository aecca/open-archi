"use strict";

function initColumnResizingTool() {
    var $ = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        $(go.Diagram, diagramDiv,
            {
                initialContentAlignment: go.Spot.Center,
                validCycle: go.Diagram.CycleNotDirected,  // don't allow loops
                "undoManager.isEnabled": true
            });

    myDiagram.toolManager.mouseDownTools.add(new RowResizingTool());
    myDiagram.toolManager.mouseDownTools.add(new ColumnResizingTool());

    // This template is a Panel that is used to represent each item in a Panel.itemArray.
    // The Panel is data bound to the item object.
    var fieldTemplate =
        $(go.Panel, "TableRow",  // this Panel is a row in the containing Table
            new go.Binding("portId", "name"),  // this Panel is a "port"
            {
                background: "transparent",  // so this port's background can be picked by the mouse
                fromSpot: go.Spot.Right,  // links only go from the right side to the left side
                toSpot: go.Spot.Left,
                // allow drawing links from or to this port:
                fromLinkable: true, toLinkable: true
            },
            $(go.Shape,
                {
                    column: 0,
                    width: 12, height: 12, margin: 4,
                    // but disallow drawing links from or to this shape:
                    fromLinkable: false, toLinkable: false
                },
                new go.Binding("figure", "figure"),
                new go.Binding("fill", "color")),
            $(go.TextBlock,
                {
                    column: 1,
                    margin: new go.Margin(0, 2),
                    stretch: go.GraphObject.Horizontal,
                    font: "bold 13px sans-serif",
                    wrap: go.TextBlock.None,
                    overflow: go.TextBlock.OverflowEllipsis,
                    // and disallow drawing links from or to this text:
                    fromLinkable: false, toLinkable: false
                },
                new go.Binding("text", "name")),
            $(go.TextBlock,
                {
                    column: 2,
                    margin: new go.Margin(0, 2),
                    stretch: go.GraphObject.Horizontal,
                    font: "13px sans-serif",
                    maxLines: 3,
                    overflow: go.TextBlock.OverflowEllipsis,
                    editable: true
                },
                new go.Binding("text", "info").makeTwoWay())
        );

    // Return initialization for a RowColumnDefinition, specifying a particular column
    // and adding a Binding of RowColumnDefinition.width to the IDX'th number in the data.widths Array
    function makeWidthBinding(idx) {
        // These two conversion functions are closed over the IDX variable.
        // This source-to-target conversion extracts a number from the Array at the given index.
        function getColumnWidth(arr) {
            if (Array.isArray(arr) && idx < arr.length) return arr[idx];
            return NaN;
        }

        // This target-to-source conversion sets a number in the Array at the given index.
        function setColumnWidth(w, data) {
            var arr = data.widths;
            if (!arr) arr = [];
            if (idx >= arr.length) {
                for (var i = arr.length; i <= idx; i++) arr[i] = NaN;  // default to NaN
            }
            arr[idx] = w;
            return arr;  // need to return the Array (as the value of data.widths)
        }

        return [
            {column: idx},
            new go.Binding("width", "widths", getColumnWidth).makeTwoWay(setColumnWidth)
        ]
    }

    // This template represents a whole "record".
    myDiagram.nodeTemplate =
        $(go.Node, "Auto",
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            // this rectangular shape surrounds the content of the node
            $(go.Shape,
                {fill: "#EEEEEE"}),
            // the content consists of a header and a list of items
            $(go.Panel, "Vertical",
                {stretch: go.GraphObject.Horizontal, alignment: go.Spot.TopLeft},
                // this is the header for the whole node
                $(go.Panel, "Auto",
                    {stretch: go.GraphObject.Horizontal},  // as wide as the whole node
                    $(go.Shape,
                        {fill: "#1570A6", stroke: null}),
                    $(go.TextBlock,
                        {
                            alignment: go.Spot.Center,
                            margin: 3,
                            stroke: "white",
                            textAlign: "center",
                            font: "bold 12pt sans-serif"
                        },
                        new go.Binding("text", "key"))),
                // this Panel holds a Panel for each item object in the itemArray;
                // each item Panel is defined by the itemTemplate to be a TableRow in this Table
                $(go.Panel, "Table",
                    {
                        name: "TABLE", stretch: go.GraphObject.Horizontal,
                        minSize: new go.Size(100, 10),
                        defaultAlignment: go.Spot.Left,
                        defaultStretch: go.GraphObject.Horizontal,
                        defaultColumnSeparatorStroke: "gray",
                        defaultRowSeparatorStroke: "gray",
                        itemTemplate: fieldTemplate
                    },
                    $(go.RowColumnDefinition, makeWidthBinding(0)),
                    $(go.RowColumnDefinition, makeWidthBinding(1)),
                    $(go.RowColumnDefinition, makeWidthBinding(2)),
                    new go.Binding("itemArray", "fields")
                )  // end Table Panel of items
            )  // end Vertical Panel
        );  // end Node

    myDiagram.linkTemplate =
        $(go.Link,
            {relinkableFrom: true, relinkableTo: true, toShortLength: 4},  // let user reconnect links
            $(go.Shape, {strokeWidth: 1.5}),
            $(go.Shape, {toArrow: "Standard", stroke: null})
        );

    myDiagram.model =
        $(go.GraphLinksModel,
            {
                linkFromPortIdProperty: "fromPort",
                linkToPortIdProperty: "toPort",
                // automatically update the model that is shown on this page
                "Changed": function (e) {
                    if (e.isTransactionFinished) showModel();
                },
                nodeDataArray: [
                    {
                        key: "Record1",
                        widths: [NaN, NaN, 60],
                        fields: [
                            {name: "field1", info: "first field", color: "#F7B84B", figure: "Ellipse"},
                            {name: "field2", info: "the second one", color: "#F25022", figure: "Ellipse"},
                            {name: "fieldThree", info: "3rd", color: "#00BCF2"}
                        ],
                        loc: "0 0"
                    },
                    {
                        key: "Record2",
                        widths: [NaN, NaN, NaN],
                        fields: [
                            {name: "fieldA", info: "", color: "#FFB900", figure: "Diamond"},
                            {name: "fieldB", info: "", color: "#F25022", figure: "Rectangle"},
                            {name: "fieldC", info: "", color: "#7FBA00", figure: "Diamond"},
                            {name: "fieldD", info: "fourth", color: "#00BCF2", figure: "Rectangle"}
                        ],
                        loc: "250 0"
                    }
                ],
                linkDataArray: [
                    {from: "Record1", fromPort: "field1", to: "Record2", toPort: "fieldA"},
                    {from: "Record1", fromPort: "field2", to: "Record2", toPort: "fieldD"},
                    {from: "Record1", fromPort: "fieldThree", to: "Record2", toPort: "fieldB"}
                ]
            });

    showModel();  // show the diagram's initial model

    function showModel() {
        document.getElementById("modelToSaveOrLoad").textContent = myDiagram.model.toJson();
    }
}

function ColumnResizingTool() {
    go.Tool.call(this);
    this.name = "ColumnResizing";

    var h = new go.Shape();
    h.geometryString = "M0 0 V14 M2 0 V14";
    h.desiredSize = new go.Size(2, 14);
    h.cursor = "col-resize";
    h.geometryStretch = go.GraphObject.None;
    h.background = "rgba(255,255,255,0.5)";
    h.stroke = "rgba(30,144,255,0.5)";
    /** @type {GraphObject} */
    this._handleArchetype = h;

    /** @type {string} */
    this._tableName = "TABLE";

    // internal state
    /** @type {GraphObject} */
    this._handle = null;
    /** @type {Panel} */
    this._adornedTable = null;
}

go.Diagram.inherit(ColumnResizingTool, go.Tool);


/*
* A small GraphObject used as a resize handle for each column.
* This tool expects that this object's {@link GraphObject#desiredSize} (a.k.a width and height) has been set to real numbers.
* @name ColumnResizingTool#handleArchetype 
* @function.
* @return {GraphObject}
*/
Object.defineProperty(ColumnResizingTool.prototype, "handleArchetype", {
    get: function () {
        return this._handleArchetype;
    },
    set: function (value) {
        this._handleArchetype = value;
    }
});

/*
* The name of the Table Panel to be resized, by default the name "TABLE".
* @name ColumnResizingTool#tableName
* @function.
* @return {string}
*/
Object.defineProperty(ColumnResizingTool.prototype, "tableName", {
    get: function () {
        return this._tableName;
    },
    set: function (value) {
        this._tableName = value;
    }
});

/*
* This read-only property returns the {@link GraphObject} that is the tool handle being dragged by the user.
* This will be contained by an {@link Adornment} whose category is "ColumnResizing".
* Its {@link Adornment#adornedObject} is the same as the {@link #adornedTable}.
* @name ColumnResizingTool#handle
* @function.
* @return {GraphObject}
*/
Object.defineProperty(ColumnResizingTool.prototype, "handle", {
    get: function () {
        return this._handle;
    }
});

/*
* Gets the {@link Panel} of type {@link Panel#Table} whose columns may be resized.
* This must be contained within the selected Part.
* @name ColumnResizingTool#adornedTable
* @function.
* @return {Panel}
*/
Object.defineProperty(ColumnResizingTool.prototype, "adornedTable", {
    get: function () {
        return this._adornedTable;
    }
});


/**
 * Show an {@link Adornment} with a resize handle at each column.
 * Don't show anything if {@link #tableName} doesn't identify a {@link Panel}
 * that has a {@link Panel#type} of type {@link Panel#Table}.
 * @this {ColumnResizingTool}
 * @param {Part} part the part.
 */
ColumnResizingTool.prototype.updateAdornments = function (part) {
    if (part === null || part instanceof go.Link) return;  // this tool never applies to Links
    if (part.isSelected && !this.diagram.isReadOnly) {
        var selelt = part.findObject(this.tableName);
        if (selelt instanceof go.Panel && selelt.actualBounds.isReal() && selelt.isVisibleObject() &&
            part.actualBounds.isReal() && part.isVisible() &&
            selelt.type === go.Panel.Table) {
            var table = selelt;
            var adornment = part.findAdornment(this.name);
            if (adornment === null) {
                adornment = this.makeAdornment(table);
                part.addAdornment(this.name, adornment);
            }
            if (adornment !== null) {
                var pad = table.padding;
                var numcols = table.columnCount;
                // update the position/alignment of each handle
                adornment.elements.each(function (h) {
                    if (!h.pickable) return;
                    var coldef = table.getColumnDefinition(h.column);
                    var wid = coldef.actual;
                    if (wid > 0) wid = coldef.total;
                    var sep = 0;
                    // find next non-zero-width column's separatorStrokeWidth
                    var idx = h.column + 1;
                    while (idx < numcols && table.getColumnDefinition(idx).actual === 0) idx++;
                    if (idx < numcols) {
                        sep = table.getColumnDefinition(idx).separatorStrokeWidth;
                        if (isNaN(sep)) sep = table.defaultColumnSeparatorStrokeWidth;
                    }
                    h.alignment = new go.Spot(0, 0, pad.left + coldef.position + wid + sep / 2, pad.top + h.height / 2);
                });
                adornment.locationObject.desiredSize = table.actualBounds.size;
                adornment.location = table.getDocumentPoint(adornment.locationSpot);
                adornment.angle = table.getDocumentAngle();
                return;
            }
        }
    }
    part.removeAdornment(this.name);
};

/*
* @this {ColumnResizingTool}
* @param {Panel} table the Table Panel whose columns may be resized
* @return {Adornment}
*/
ColumnResizingTool.prototype.makeAdornment = function (table) {
    // the Adornment is a Spot Panel holding resize handles
    var adornment = new go.Adornment();
    adornment.category = this.name;
    adornment.adornedObject = table;
    adornment.type = go.Panel.Spot;
    adornment.locationObjectName = "BLOCK";
    // create the "main" element of the Spot Panel
    var block = new go.TextBlock();  // doesn't matter much what this is
    block.name = "BLOCK";
    block.pickable = false;  // it's transparent and not pickable
    adornment.add(block);
    // now add resize handles for each column
    for (var i = 0; i < table.columnCount; i++) {
        var coldef = table.getColumnDefinition(i);
        adornment.add(this.makeHandle(table, coldef));
    }
    return adornment;
};

/*
* @this {ColumnResizingTool}
* @param {Panel} table the Table Panel whose columns may be resized
* @param {RowColumnDefinition} coldef the column definition to be resized
* @return a copy of the {@link #handleArchetype}
*/
ColumnResizingTool.prototype.makeHandle = function (table, coldef) {
    var h = this.handleArchetype;
    if (h === null) return null;
    var c = h.copy();
    c.column = coldef.index;
    return c;
};


/*
* This predicate is true when there is a resize handle at the mouse down point.
* @this {ColumnResizingTool}
* @return {boolean}
*/
ColumnResizingTool.prototype.canStart = function () {
    if (!this.isEnabled) return false;

    var diagram = this.diagram;
    if (diagram === null || diagram.isReadOnly) return false;
    if (!diagram.lastInput.left) return false;
    var h = this.findToolHandleAt(diagram.firstInput.documentPoint, this.name);
    return (h !== null);
};

/**
 * @this {ColumnResizingTool}
 */
ColumnResizingTool.prototype.doActivate = function () {
    var diagram = this.diagram;
    if (diagram === null) return;
    this._handle = this.findToolHandleAt(diagram.firstInput.documentPoint, this.name);
    if (this.handle === null) return;
    var panel = this.handle.part.adornedObject;
    if (!panel || panel.type !== go.Panel.Table) return;
    this._adornedTable = panel;
    diagram.isMouseCaptured = true;
    this.startTransaction(this.name);
    this.isActive = true;
};

/**
 * @this {ColumnResizingTool}
 */
ColumnResizingTool.prototype.doDeactivate = function () {
    this.stopTransaction();
    this._handle = null;
    this._adornedTable = null;
    var diagram = this.diagram;
    if (diagram !== null) diagram.isMouseCaptured = false;
    this.isActive = false;
};

/**
 * @this {ColumnResizingTool}
 */
ColumnResizingTool.prototype.doMouseMove = function () {
    var diagram = this.diagram;
    if (this.isActive && diagram !== null) {
        var newpt = this.computeResize(diagram.lastInput.documentPoint);
        this.resize(newpt);
    }
};

/**
 * @this {ColumnResizingTool}
 */
ColumnResizingTool.prototype.doMouseUp = function () {
    var diagram = this.diagram;
    if (this.isActive && diagram !== null) {
        var newpt = this.computeResize(diagram.lastInput.documentPoint);
        this.resize(newpt);
        this.transactionResult = this.name;  // success
    }
    this.stopTool();
};

/**
 * This should change the {@link RowColumnDefinition#width} of the column being resized
 * to a value corresponding to the given mouse point.
 * @expose
 * @this {ColumnResizingTool}
 * @param {Point} newPoint the value of the call to {@link #computeResize}.
 */
ColumnResizingTool.prototype.resize = function (newPoint) {
    var table = this.adornedTable;
    var pad = table.padding;
    var numcols = table.columnCount;
    var locpt = table.getLocalPoint(newPoint);
    var h = this.handle;
    var coldef = table.getColumnDefinition(h.column);
    var sep = 0;
    var idx = h.column + 1;
    while (idx < numcols && table.getColumnDefinition(idx).actual === 0) idx++;
    if (idx < numcols) {
        sep = table.getColumnDefinition(idx).separatorStrokeWidth;
        if (isNaN(sep)) sep = table.defaultColumnSeparatorStrokeWidth;
    }
    coldef.width = Math.max(0, locpt.x - pad.left - coldef.position - (coldef.total - coldef.actual) - sep / 2);
};


/**
 * This can be overridden in order to customize the resizing process.
 * @expose
 * @this {ColumnResizingTool}
 * @param {Point} p the point where the handle is being dragged.
 * @return {Point}
 */
ColumnResizingTool.prototype.computeResize = function (p) {
    return p;
};

/**
 * Pressing the Delete key removes any column width setting and stops this tool.
 * @this {ColumnResizingTool}
 */
ColumnResizingTool.prototype.doKeyDown = function () {
    if (!this.isActive) return;
    var e = this.diagram.lastInput;
    if (e.key === 'Del' || e.key === '\t') {  // remove width setting
        var coldef = this.adornedTable.getColumnDefinition(this.handle.column);
        coldef.width = NaN;
        this.transactionResult = this.name;  // success
        this.stopTool();
    } else {
        go.Tool.prototype.doKeyDown.call(this);
    }
};

function initColumnResizing() {

    var $ = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        $(go.Diagram, diagramDiv,
            {
                initialContentAlignment: go.Spot.Center,
                validCycle: go.Diagram.CycleNotDirected,  // don't allow loops
                "undoManager.isEnabled": true
            });

    myDiagram.toolManager.mouseDownTools.add(new RowResizingTool());
    myDiagram.toolManager.mouseDownTools.add(new ColumnResizingTool());

    // This template is a Panel that is used to represent each item in a Panel.itemArray.
    // The Panel is data bound to the item object.
    var fieldTemplate =
        $(go.Panel, "TableRow",  // this Panel is a row in the containing Table
            new go.Binding("portId", "name"),  // this Panel is a "port"
            {
                background: "transparent",  // so this port's background can be picked by the mouse
                fromSpot: go.Spot.Right,  // links only go from the right side to the left side
                toSpot: go.Spot.Left,
                // allow drawing links from or to this port:
                fromLinkable: true, toLinkable: true
            },
            $(go.Shape,
                {
                    column: 0,
                    width: 12, height: 12, margin: 4,
                    // but disallow drawing links from or to this shape:
                    fromLinkable: false, toLinkable: false
                },
                new go.Binding("figure", "figure"),
                new go.Binding("fill", "color")),
            $(go.TextBlock,
                {
                    column: 1,
                    margin: new go.Margin(0, 2),
                    stretch: go.GraphObject.Horizontal,
                    font: "bold 13px sans-serif",
                    wrap: go.TextBlock.None,
                    overflow: go.TextBlock.OverflowEllipsis,
                    // and disallow drawing links from or to this text:
                    fromLinkable: false, toLinkable: false
                },
                new go.Binding("text", "name")),
            $(go.TextBlock,
                {
                    column: 2,
                    margin: new go.Margin(0, 2),
                    stretch: go.GraphObject.Horizontal,
                    font: "13px sans-serif",
                    maxLines: 3,
                    overflow: go.TextBlock.OverflowEllipsis,
                    editable: true
                },
                new go.Binding("text", "info").makeTwoWay())
        );

    // Return initialization for a RowColumnDefinition, specifying a particular column
    // and adding a Binding of RowColumnDefinition.width to the IDX'th number in the data.widths Array
    function makeWidthBinding(idx) {
        // These two conversion functions are closed over the IDX variable.
        // This source-to-target conversion extracts a number from the Array at the given index.
        function getColumnWidth(arr) {
            if (Array.isArray(arr) && idx < arr.length) return arr[idx];
            return NaN;
        }

        // This target-to-source conversion sets a number in the Array at the given index.
        function setColumnWidth(w, data) {
            var arr = data.widths;
            if (!arr) arr = [];
            if (idx >= arr.length) {
                for (var i = arr.length; i <= idx; i++) arr[i] = NaN;  // default to NaN
            }
            arr[idx] = w;
            return arr;  // need to return the Array (as the value of data.widths)
        }

        return [
            {column: idx},
            new go.Binding("width", "widths", getColumnWidth).makeTwoWay(setColumnWidth)
        ]
    }

    // This template represents a whole "record".
    myDiagram.nodeTemplate =
        $(go.Node, "Auto",
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            // this rectangular shape surrounds the content of the node
            $(go.Shape,
                {fill: "#EEEEEE"}),
            // the content consists of a header and a list of items
            $(go.Panel, "Vertical",
                {stretch: go.GraphObject.Horizontal, alignment: go.Spot.TopLeft},
                // this is the header for the whole node
                $(go.Panel, "Auto",
                    {stretch: go.GraphObject.Horizontal},  // as wide as the whole node
                    $(go.Shape,
                        {fill: "#1570A6", stroke: null}),
                    $(go.TextBlock,
                        {
                            alignment: go.Spot.Center,
                            margin: 3,
                            stroke: "white",
                            textAlign: "center",
                            font: "bold 12pt sans-serif"
                        },
                        new go.Binding("text", "key"))),
                // this Panel holds a Panel for each item object in the itemArray;
                // each item Panel is defined by the itemTemplate to be a TableRow in this Table
                $(go.Panel, "Table",
                    {
                        name: "TABLE", stretch: go.GraphObject.Horizontal,
                        minSize: new go.Size(100, 10),
                        defaultAlignment: go.Spot.Left,
                        defaultStretch: go.GraphObject.Horizontal,
                        defaultColumnSeparatorStroke: "gray",
                        defaultRowSeparatorStroke: "gray",
                        itemTemplate: fieldTemplate
                    },
                    $(go.RowColumnDefinition, makeWidthBinding(0)),
                    $(go.RowColumnDefinition, makeWidthBinding(1)),
                    $(go.RowColumnDefinition, makeWidthBinding(2)),
                    new go.Binding("itemArray", "fields")
                )  // end Table Panel of items
            )  // end Vertical Panel
        );  // end Node

    myDiagram.linkTemplate =
        $(go.Link,
            {relinkableFrom: true, relinkableTo: true, toShortLength: 4},  // let user reconnect links
            $(go.Shape, {strokeWidth: 1.5}),
            $(go.Shape, {toArrow: "Standard", stroke: null})
        );

    myDiagram.model =
        $(go.GraphLinksModel,
            {
                linkFromPortIdProperty: "fromPort",
                linkToPortIdProperty: "toPort",
                // automatically update the model that is shown on this page
                "Changed": function (e) {
                    if (e.isTransactionFinished) showModel();
                },
                nodeDataArray: [
                    {
                        key: "Record1",
                        widths: [NaN, NaN, 60],
                        fields: [
                            {name: "field1", info: "first field", color: "#F7B84B", figure: "Ellipse"},
                            {name: "field2", info: "the second one", color: "#F25022", figure: "Ellipse"},
                            {name: "fieldThree", info: "3rd", color: "#00BCF2"}
                        ],
                        loc: "0 0"
                    },
                    {
                        key: "Record2",
                        widths: [NaN, NaN, NaN],
                        fields: [
                            {name: "fieldA", info: "", color: "#FFB900", figure: "Diamond"},
                            {name: "fieldB", info: "", color: "#F25022", figure: "Rectangle"},
                            {name: "fieldC", info: "", color: "#7FBA00", figure: "Diamond"},
                            {name: "fieldD", info: "fourth", color: "#00BCF2", figure: "Rectangle"}
                        ],
                        loc: "250 0"
                    }
                ],
                linkDataArray: [
                    {from: "Record1", fromPort: "field1", to: "Record2", toPort: "fieldA"},
                    {from: "Record1", fromPort: "field2", to: "Record2", toPort: "fieldD"},
                    {from: "Record1", fromPort: "fieldThree", to: "Record2", toPort: "fieldB"}
                ]
            });

    showModel();  // show the diagram's initial model

    function showModel() {
        document.getElementById("modelToSaveOrLoad").textContent = myDiagram.model.toJson();
    }
}