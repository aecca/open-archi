const leftRoundedRectangleGeometry = new go.Geometry.parse("m 17.867994,143.04045 h 78.241283 v 48.43189 H 17.867994 c -1.726116,0 -3.115732,-1.338 -3.115732,-3 v -42.43189 c 0,-1.662 1.389616,-3 3.115732,-3 z", false);
const rightRoundedRectangleGeometry = new go.Geometry.parse("M 92.993545,143.04045 H 14.752262 v 48.43189 h 78.241283 c 1.726116,0 3.115732,-1.338 3.115732,-3 v -42.43189 c 0,-1.662 -1.389616,-3 -3.115732,-3 z", false);

go.Shape.defineFigureGenerator("RoundedTopRectangle", function (shape, w, h) {
    // this figure takes one parameter, the size of the corner
    let p1 = 5;  // default corner size
    if (shape !== null) {
        const param1 = shape.parameter1;
        if (!isNaN(param1) && param1 >= 0) p1 = param1;  // can't be negative or NaN
    }
    p1 = Math.min(p1, w / 2);
    p1 = Math.min(p1, h / 2);  // limit by whole height or by half height?
    const geo = new go.Geometry();
    // a single figure consisting of straight lines and quarter-circle arcs
    geo.add(new go.PathFigure(0, p1)
        .add(new go.PathSegment(go.PathSegment.Arc, 180, 90, p1, p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, w - p1, 0))
        .add(new go.PathSegment(go.PathSegment.Arc, 270, 90, w - p1, p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, w, h))
        .add(new go.PathSegment(go.PathSegment.Line, 0, h).close()));
    // don't intersect with two top corners when used in an "Auto" Panel
    geo.spot1 = new go.Spot(0, 0, 0.3 * p1, 0.3 * p1);
    geo.spot2 = new go.Spot(1, 1, -0.3 * p1, 0);
    return geo;
});

go.Shape.defineFigureGenerator("RoundedBottomRectangle", function (shape, w, h) {
    // this figure takes one parameter, the size of the corner
    var p1 = 5;  // default corner size
    if (shape !== null) {
        var param1 = shape.parameter1;
        if (!isNaN(param1) && param1 >= 0) p1 = param1;  // can't be negative or NaN
    }
    p1 = Math.min(p1, w / 2);
    p1 = Math.min(p1, h / 2);  // limit by whole height or by half height?
    var geo = new go.Geometry();
    // a single figure consisting of straight lines and quarter-circle arcs
    geo.add(new go.PathFigure(0, 0)
        .add(new go.PathSegment(go.PathSegment.Line, w, 0))
        .add(new go.PathSegment(go.PathSegment.Line, w, h - p1))
        .add(new go.PathSegment(go.PathSegment.Arc, 0, 90, w - p1, h - p1, p1, p1))
        .add(new go.PathSegment(go.PathSegment.Line, p1, h))
        .add(new go.PathSegment(go.PathSegment.Arc, 90, 90, p1, h - p1, p1, p1).close()));
    // don't intersect with two bottom corners when used in an "Auto" Panel
    geo.spot1 = new go.Spot(0, 0, 0.3 * p1, 0);
    geo.spot2 = new go.Spot(1, 1, -0.3 * p1, -0.3 * p1);
    return geo;
});

// a context menu is an Adornment with a bunch of buttons in them
const partContextMenu =
    gojs(go.Adornment, "Vertical",

        makeButton("Diagrams",
            function (e, obj) {
                const model = e.diagram.model;
                const node = obj.part.data;
                $.get("/open-archi/api/catalogs/element-roles")
                    .done(function (data) {
                        let modal = $('#element-role-data');
                        let elementRoleItems = [];
                        i = 0;
                        data.forEach(function (elementRole, i) {
                            elementRoleItems.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="#">' + elementRole.name + '</a></li>');
                        });
                        let elementRolesDropdown = $("#elementRolesDropdown");
                        elementRolesDropdown.append(elementRoleItems.join(''));
                        elementRolesDropdown.on('click', 'a', function () {
                            const text = $(this).html();
                            const htmlText = text + ' <span class="caret"></span>';
                            $(this).closest('.dropdown').find('.dropdown-toggle').html(htmlText);
                        });
                        modal.modal({
                            backdrop: 'static',
                            keyboard: false,
                            show: true
                        });
                        modal.on('hidden.bs.modal', function () {
                            const role = $("#elementRolesDropdown").find("a.active").html();
                            model.setDataProperty(node, "role", role);
                        })
                    });
            }, function (o) {
                return true;
            }),
        makeButton("Role",
            function (e, obj) {
                const model = e.diagram.model;
                const node = obj.part.data;
                $.get("/open-archi/api/catalogs/element-roles")
                    .done(function (data) {
                        let modal = $('#element-role-data');
                        let elementRoleItems = [];
                        i = 0;
                        data.forEach(function (elementRole, i) {
                            elementRoleItems.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="#">' + elementRole.name + '</a></li>');
                        });
                        let elementRolesDropdown = $("#elementRolesDropdown");
                        elementRolesDropdown.append(elementRoleItems.join(''));
                        elementRolesDropdown.on('click', 'a', function () {
                            const text = $(this).html();
                            const htmlText = text + ' <span class="caret"></span>';
                            $(this).closest('.dropdown').find('.dropdown-toggle').html(htmlText);
                        });
                        modal.modal({
                            backdrop: 'static',
                            keyboard: false,
                            show: true
                        });
                        modal.on('hidden.bs.modal', function () {
                            const role = $("#elementRolesDropdown").find("a.active").html();
                            model.setDataProperty(node, "role", role);
                        })
                    });


            }, function (o) {
                return true;
            }),
        makeButton("Add image",
            function (e, obj) {
                const model = e.diagram.model;
                const node = obj.part.data;

                let modal = $('#element-image-data');
                modal.attr("key", node.key);
                modal.modal({
                    backdrop: 'static',
                    keyboard: false,
                    show: true
                });
            }, function (o) {
                return true;
            }),
        makeButton("Properties",
            function (e, obj) {  // OBJ is this Button
                const contextmenu = obj.part;  // the Button is in the context menu Adornment
                const part = contextmenu.adornedPart;  // the adornedPart is the Part that the context menu adorns
                // now can do something with PART, or with its data, or with the Adornment (the context menu)
                if (part instanceof go.Link) alert(linkInfo(part.data));
                else if (part instanceof go.Group) alert(groupInfo(contextmenu));
                else alert(nodeInfo(part.data));
            }),
        makeButton("Cut",
            function (e, obj) {
                e.diagram.commandHandler.cutSelection();
            },
            function (o) {
                return o.diagram.commandHandler.canCutSelection();
            }),
        makeButton("Copy",
            function (e, obj) {
                e.diagram.commandHandler.copySelection();
            },
            function (o) {
                return o.diagram.commandHandler.canCopySelection();
            }),
        makeButton("Paste",
            function (e, obj) {
                e.diagram.commandHandler.pasteSelection(e.diagram.lastInput.documentPoint);
            },
            function (o) {
                return o.diagram.commandHandler.canPasteSelection();
            }),
        makeButton("Delete",
            function (e, obj) {
                e.diagram.commandHandler.deleteSelection();
            },
            function (o) {
                return o.diagram.commandHandler.canDeleteSelection();
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
            }),
        makeButton("Group",
            function (e, obj) {
                e.diagram.commandHandler.groupSelection();
            },
            function (o) {
                return o.diagram.commandHandler.canGroupSelection();
            }),
        makeButton("Ungroup",
            function (e, obj) {
                e.diagram.commandHandler.ungroupSelection();
            },
            function (o) {
                return o.diagram.commandHandler.canUngroupSelection();
            }),
        makeButton("Re-Group",
            function (e, obj) {
                e.diagram.commandHandler.groupSelection();
            },
            function (o) {
                return o.diagram.commandHandler.canGroupSelection();
            }),
        makeButton("Copy Basic Info",
            function (e, obj) {

                let data = obj.part.data;
                let text = "id: " + data.key + "\n";
                if (data.clonedFrom) {
                    text = text + "Cloned from id: " + data.clonedFrom.id + "\n";
                }
                copyTextToClipboard(text);
            },
            function (o) {
                return true;
            }),
        makeButton("Copy Data",
            function (e, obj) {
                copyTextToClipboard(JSON.stringify(obj.part.data));
            },
            function (o) {
                return true;
            })
    );


// Make link labels visible if coming out of a "conditional" node.
// This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
function showLinkLabel(e) {
    const label = e.subject.findObject("LABEL");
    if (label !== null) label.visible = (e.subject.fromNode.data.figure === "Diamond");
}

function nodeInfo(d) {  // Tooltip info for a node data object
    let str = "Element id: " + d.key + ".\n";
    if (d.description) {
        str = d.description + "\n";
    }
    if (d.group) {
        str += " Grouped in: " + d.group + "\n";
    }
    if (d.clonedFrom) {
        str += " Cloned from: " + d.group + "\n";
    }
    if (str === undefined) {
        str = d.name;
    }
    return str;
}

// Define the appearance and behavior for Links:

function linkInfo(d) {  // Tooltip info for a link data object
    return "Link:\nfrom " + d.from + " to " + d.to;
}

// Define the appearance and behavior for Groups:

function groupInfo(adornment) {  // takes the tooltip or context menu, not a group node data object
    const g = adornment.adornedPart;  // get the Group that the tooltip adorns
    const mems = g.memberParts.count;
    let links = 0;
    g.memberParts.each(function (part) {
        if (part instanceof go.Link) links++;
    });
    return "Group " + g.data.key + ": " + g.data.text + "\n" + mems + " members including " + links + " links";
}

// Define the appearance and behavior for Nodes:

// First, define the shared context menu for all Nodes, Links, and Groups.

// To simplify this code we define a function for creating a context menu button:
function makeButton(text, action, visiblePredicate) {
    return gojs("ContextMenuButton",
        gojs(go.TextBlock, text),
        {click: action},
        // don't bother with binding GraphObject.visible if there's no predicate
        visiblePredicate ? new go.Binding("visible", "", function (o, e) {
            return o.diagram ? visiblePredicate(o, e) : false;
        }).ofObject() : {});
}


// Upon a drop onto a Group, we try to add the selection as members of the Group.
// Upon a drop onto the background, or onto a top-level Node, make selection top-level.
// If this is OK, we're done; otherwise we cancel the operation to rollback everything.
function finishDrop(e, grp) {
    let ok = (grp !== null
        ? grp.addMembers(grp.diagram.selection, true)
        : e.diagram.commandHandler.addTopLevelParts(e.diagram.selection, true));
    if (!ok) e.diagram.currentTool.doCancel();
}

// this function is used to highlight a Group that the selection may be dropped into
function highlightGroup(e, grp, show) {
    if (!grp) return;
    e.handled = true;
/*    if (show) {
        // cannot depend on the grp.diagram.selection in the case of external drag-and-drops;
        // instead depend on the DraggingTool.draggedParts or .copiedParts
        let tool = grp.diagram.toolManager.draggingTool;
        let map = tool.draggedParts || tool.copiedParts;  // this is a Map
        // now we can check to see if the Group will accept membership of the dragged Parts
        if (grp.canAddMembers(map.toKeySet())) {
            grp.isHighlighted = true;
            return;
        }
    }*/
    grp.isHighlighted = false;
}

// Define a function for creating a "port" that is normally transparent.
// The "name" is used as the GraphObject.portId, the "spot" is used to control how links connect
// and where the port is positioned on the node, and the boolean "output" and "input" arguments
// control whether the user can draw links from or to the port.
function makePort(name, spot, input, output) {
    // the port is basically just a small circle that has a white stroke when it is made visible
    return gojs(go.Shape, "Circle",
        {
            fill: "transparent",
            stroke: null,  // this is changed to "white" in the showPorts function
            desiredSize: new go.Size(8, 8),
            alignment: spot, alignmentFocus: spot,  // align the port on the main Shape
            portId: name,  // declare this object to be a "port"
            fromSpot: spot, toSpot: spot,  // declare where links may connect at this port
            fromLinkable: output,
            toLinkable: input,  // declare whether the user may draw links to/from here
            cursor: "pointer"  // show a different cursor to indicate potential link point
        },
        new go.Binding("fromLinkable", "", OpenArchiWrapper.toFromLinkable).makeTwoWay(OpenArchiWrapper.fromFromLinkable),
        new go.Binding("toLinkable", "", OpenArchiWrapper.toToLinkable).makeTwoWay(OpenArchiWrapper.fromToLinkable));
}

// Make all ports on a node visible when the mouse is over the node
function showPorts(node, show) {
    let diagram = node.diagram;
    if (!diagram || diagram.isReadOnly || !diagram.allowLink) return;
    node.ports.each(function (port) {
        port.stroke = (show ? "white" : null);
        port.fill = (show ? "orange" : "transparent");
        port.zOrder = 1000;
    });
}

// helper definitions for node templates
function nodeStyle() {
    return [
        // The Node.location comes from the "loc" property of the node data,
        // converted by the Point.parse static method.
        // If the Node.location is changed, it updates the "loc" property of the node data,
        // converting back using the Point.stringify static method.
        {
            // the Node.location is at the center of each node
            locationSpot: go.Spot.Center,
            isShadowed: true,
            shadowColor: "#888",
            // handle mouse enter/leave events to show/hide the ports
            mouseEnter: function (e, obj) {
                const object = obj.findObject("HEADER");
                if (object !== undefined && object !== null) {
                    let background = object.background;
                    let color;
                    if (background !== undefined && background !== null) {
                        color = background.color;
                    } else {
                        color = object.fill;
                    }
                    const object_ = obj.findObject("SHAPE");
                    if (object_ !== undefined) {
                        object_.stroke = "red";
                        object_.fill = go.Brush.lighten(color);
                    }
                } else {
                    const object_ = obj.findObject("SHAPE");
                    if (object_ !== undefined && object_ !== null) {
                        object_.stroke = "red";
                        object_.fill = go.Brush.lighten(object_.fill);
                    }
                }
                showPorts(obj.part, true);
                e.handled = true;
            },
            mouseLeave: function (e, obj) {
                obj.background = "white";
                const object_ = obj.findObject("SHAPE");
                if (object_ !== undefined && object_ !== null) {
                    const object = obj.findObject("HEADER");
                    if (object !== undefined && object !== null) {
                        object_.stroke = object.part.data.shape.fill;
                        object_.fill = "white"
                    } else {
                        object_.fill = object_.part.data.shape.fill;
                    }
                } else {
                    obj.fill = obj.part.data.shape.fill;
                }
                showPorts(obj.part, false);
                obj.stroke = "transparent";
                e.handled = true;
            },
            dragComputation: stayInGroup
        },
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
        new go.Binding("name", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle),
        new go.Binding("clonedFrom", "clonedFrom"),
        new go.Binding("isGroup", "", OpenArchiWrapper.toIsGroup)
    ];
}

function groupStyle() {  // common settings for both Lane and Pool Groups
    return [
        {
            layerName: "Background",  // all pools and lanes are always behind all nodes and links
            movable: true, // allows users to re-order by dragging
            copyable: false,  // can't copy lanes or pools
            avoidable: false,  // don't impede AvoidsNodes routed Links
            minLocation: new go.Point(NaN, -Infinity),  // only allow vertical movement
            maxLocation: new go.Point(NaN, Infinity),
            isShadowed: true,
            shadowColor: "#888"
        },
        new go.Binding("background", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
        new go.Binding("name", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle),
        new go.Binding("clonedFrom", "clonedFrom"),
        new go.Binding("isGroup", "", OpenArchiWrapper.toIsGroup)
    ];
}

// hide links between lanes when either lane is collapsed
function updateCrossLaneLinks(group) {
    group.findExternalLinksConnected().each(function (l) {
        l.visible = (l.fromNode.isVisible() && l.toNode.isVisible());
    });
}


// this may be called to force the lanes to be laid out again
function relayoutLanes() {
    myDiagram.nodes.each(function (node) {
        if (!(node instanceof go.Group)) {
            return;
        }
        if (node.category !== "Lane" && node.category !== "LANE" && node.category !== "Layer" && node.category !== "LAYER") {
            return;
        }
        if (node.layout) {
            node.layout.isValidLayout = false;  // force it to be invalid
        }
        const sz = computeMinLaneSize(node);
        let topBottomPadding = 0;
        let leftRightPadding = 0;
        if (node.isSubGraphExpanded) {
            const holder = node.placeholder;
            if (holder !== null) {
                const hsz = holder.measuredBounds;
                topBottomPadding = holder.padding.top + holder.padding.bottom;
                leftRightPadding = holder.padding.left + holder.padding.right;

                let computedHeight = 0;
                let computedWidth = 0;
                node.memberParts.each(function (node) {
                    const bound = node.measuredBounds;
                    computedHeight = computedHeight + bound.height;
                    computedWidth = computedHeight + bound.width + ((node.layout && node.layout.columnSpacing) ? node.layout.columnSpacing : 0);
                });
                sz.height = computedHeight;
                sz.width = computedWidth;
            }
        }
        // minimum breadth needs to be big enough to hold the header
        const hdr = node.findObject("HEADER");
        if (hdr !== null) {
            sz.height = Math.max(sz.height, hdr.actualBounds.height);
            sz.width = Math.max(sz.width, hdr.actualBounds.width);
        }
        const shape = node.resizeObject;
        if (shape !== null) {
            shape.height = sz.height + topBottomPadding;
            shape.width = sz.width + leftRightPadding;
        }
        updateCrossLaneLinks(node);
    });
    myDiagram.layoutDiagram();
}

// this is called after nodes have been moved or lanes resized, to layout all of the Pool Groups again
function relayoutDiagram() {
    myDiagram.layout.invalidateLayout();
    myDiagram.findTopLevelGroups().each(function (g) {
        if (g.category === "LANE" || g.category === "Lane" || g.category === "LAYER" || g.category === "Layer") {
            g.layout.invalidateLayout();
        }
    });
    myDiagram.layoutDiagram();
}

// compute the minimum size of a Pool Group needed to hold all of the Lane Groups
/*function computeMinPoolSize(pool) {
    // assert(pool instanceof go.Group && pool.category === "Pool");
    let len = MINLENGTH;
    pool.memberParts.each(function (lane) {
        // pools ought to only contain lanes, not plain Nodes
        if (!(lane instanceof go.Group)) return;
        const holder = lane.placeholder;
        if (holder !== null) {
            const sz = holder.actualBounds;
            len = Math.max(len, sz.width);
        }
    });
    return new go.Size(len, NaN);
}*/

// compute the minimum size for a particular Lane Group
function computeLaneSize(lane) {
    // assert(lane instanceof go.Group && lane.category !== "Pool");
    const sz = computeMinLaneSize(lane);
    if (lane.isSubGraphExpanded) {
        const holder = lane.placeholder;
        if (holder !== null) {
            const hsz = holder.actualBounds;
            sz.height = Math.max(sz.height, hsz.height);
            sz.width = Math.max(sz.width, hsz.width);
        }
    }
    // minimum breadth needs to be big enough to hold the header
    const hdr = lane.findObject("HEADER");
    if (hdr !== null) {
        sz.height = Math.max(sz.height, hdr.actualBounds.height);
        sz.width = Math.max(sz.width, hdr.actualBounds.width);
    }
    return sz;
}

// determine the minimum size of a Lane Group, even if collapsed
function computeMinLaneSize(lane) {
    if (!lane.isSubGraphExpanded) return new go.Size(MINLENGTH, 1);
    return new go.Size(MINLENGTH, MINBREADTH);
}


// define a custom ResizingTool to limit how far one can shrink a lane Group
function LaneResizingTool() {
    go.ResizingTool.call(this);
    this.isRealtime = false;
}

go.Diagram.inherit(LaneResizingTool, go.ResizingTool);

LaneResizingTool.prototype.isLengthening = function () {
    return (this.handle.alignment === go.Spot.Right);
};

LaneResizingTool.prototype.isHeightening = function () {
    return (this.handle.alignment === go.Spot.Top);
};


/** @override */
LaneResizingTool.prototype.computeMinSize = function () {
    const lane = this.adornedObject.part;
    // assert(lane instanceof go.Group && lane.category !== "Pool");
    const msz = computeMinLaneSize(lane);  // get the absolute minimum size
    const sz = computeLaneSize(lane);
    if (!this.isLengthening()) {  // compute the minimum length of all lanes
        msz.width = Math.max(msz.width, sz.width);
    }
    if (!this.isHeightening()) {  // compute the minimum length of all lanes
        msz.height = Math.max(msz.height, sz.height);
    }
    return msz;
};

/** @override */
LaneResizingTool.prototype.resize = function (newr) {
    const lane = this.adornedObject.part;
    if (this.isLengthening()) {  // changing the length of all of the lanes
        lane.diagram.nodes.each(function (lane) {
            if (!(lane instanceof go.Group) || lane.category !== "LAYER") return;
            const shape = lane.resizeObject;
            if (shape !== null) {  // set its desiredSize length, but leave each breadth alone
                shape.width = newr.width;
                shape.height = newr.height;
            }
        });
    } else {  // changing the breadth of a single lane
        go.ResizingTool.prototype.resize.call(this, newr);
    }
    relayoutDiagram();  // now that the lane has changed size, layout the pool again
};
// end LaneResizingTool class


// define a custom grid layout that makes sure the length of each lane is the same
// and that each lane is broad enough to hold its subgraph
function PoolLayout() {
    go.GridLayout.call(this);
    this.cellSize = new go.Size(1, 1);
    this.wrappingColumn = 1;
    this.wrappingWidth = Infinity;
    this.isRealtime = false;  // don't continuously layout while dragging
    this.alignment = go.GridLayout.Position;
    // This sorts based on the location of each Group.
    // This is useful when Groups can be moved up and down in order to change their order.
    this.comparer = function (a, b) {
        const ay = a.location.y;
        const by = b.location.y;
        if (isNaN(ay) || isNaN(by)) return 0;
        if (ay < by) return -1;
        if (ay > by) return 1;
        return 0;
    };
}

go.Diagram.inherit(PoolLayout, go.GridLayout);

/** @override */
PoolLayout.prototype.doLayout = function (coll) {
    const diagram = this.diagram;
    if (diagram === null) return;
    diagram.startTransaction("PoolLayout");

    // now do all of the usual stuff, according to whatever properties have been set on this GridLayout
    go.GridLayout.prototype.doLayout.call(this, coll);
    diagram.commitTransaction("PoolLayout");
};

// end PoolLayout class

// this is a Part.dragComputation function for limiting where a Node may be dragged
function stayInGroup(part, pt, gridpt) {
    // don't constrain top-level nodes
    const grp = part.containingGroup;
    if (grp === null) return pt;
    // try to stay within the background Shape of the Group
    const back = grp.resizeObject;
    if (back === null) return pt;
    // allow dragging a Node out of a Group if the Shift key is down
    if (part.diagram.lastInput.shift) return pt;
    const p1 = back.getDocumentPoint(go.Spot.TopLeft);
    const p2 = back.getDocumentPoint(go.Spot.BottomRight);
    const b = part.actualBounds;
    const loc = part.location;
    // find the padding inside the group's placeholder that is around the member parts
    const m = grp.placeholder.padding;
    // now limit the location appropriately
    const x = Math.max(p1.x + m.left, Math.min(pt.x, p2.x - m.right - b.width - 1)) + (loc.x - b.x);
    const y = Math.max(p1.y + m.top, Math.min(pt.y, p2.y - m.bottom - b.height - 1)) + (loc.y - b.y);
    return new go.Point(x, y);
}

const basicElement = gojs(go.Node, "Spot",
    nodeStyle(),
    // the main object is a Panel that surrounds a TextBlock with a rectangular Shape
    gojs(go.Panel, "Auto",
        {
            name: "New Element"
        },
        gojs(go.Shape, "RoundedRectangle",
            {
                fill: "black", // the default fill, if there is no data bound value
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
            new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill)),
        gojs(go.TextBlock, "text",
            {
                font: "bold 11pt Helvetica, Arial, sans-serif",
                margin: 4,  // make some extra space for the shape around the text
                isMultiline: true,
                wrap: go.TextBlock.WrapFit,
                editable: true  // allow in-place editing by user
            },
            new go.Binding("stroke", "", OpenArchiWrapper.toComplementColor),
            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle)
        ),  // the label shows the node data's text
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
    // four named ports, one on each side:
    makePort("T", go.Spot.Top, true, true),
    makePort("L", go.Spot.Left, true, true),
    makePort("R", go.Spot.Right, true, true),
    makePort("B", go.Spot.Bottom, true, true)
);