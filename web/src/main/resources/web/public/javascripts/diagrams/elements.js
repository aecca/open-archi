const leftRoundedRectangleGeometry = new go.Geometry.parse("m 17.867994,143.04045 h 78.241283 v 48.43189 H 17.867994 c -1.726116,0 -3.115732,-1.338 -3.115732,-3 v -42.43189 c 0,-1.662 1.389616,-3 3.115732,-3 z", false);
const rightRoundedRectangleGeometry = new go.Geometry.parse("M 92.993545,143.04045 H 14.752262 v 48.43189 h 78.241283 c 1.726116,0 3.115732,-1.338 3.115732,-3 v -42.43189 c 0,-1.662 -1.389616,-3 -3.115732,-3 z", false);

go.Shape.defineFigureGenerator("RoundedTopRectangle", function (shape, w, h) {
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

function getNodeByType(paletteModel) {
    let type = paletteModel.shape !== undefined ? paletteModel.shape.type : paletteModel.kind;
    switch (type) {
        case "SYSTEM":
        case "System":
            return gojs(go.Group, "Auto",
                {
                    background: "transparent",
                    // highlight when dragging into the Group
                    mouseDragEnter: function (e, grp, prev) {
                        highlightGroup(e, grp, true);
                    },
                    mouseDragLeave: function (e, grp, next) {
                        highlightGroup(e, grp, false);
                    },
                    ungroupable: true,
                    computesBoundsAfterDrag: true,
                    // when the selection is dropped into a Group, add the selected Parts into that Group;
                    // if it fails, cancel the tool, rolling back any changes
                    mouseDrop: finishDrop,
                    handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                    // Groups containing Groups lay out their members horizontally
                    layout:
                        gojs(go.GridLayout,
                            {
                                wrappingWidth: Infinity, alignment: go.GridLayout.Position,
                                cellSize: new go.Size(1, 1), spacing: new go.Size(4, 4)
                            })
                },
                nodeStyle(),
                new go.Binding("background", "isHighlighted", function (h) {
                    return h ? "rgba(255,0,0,0.2)" : "transparent";
                }).ofObject(),
                gojs(go.Shape, "Rectangle",
                    {
                        fill: null,
                        stroke: "#01203A",
                        stretch: go.GraphObject.Horizontal,
                        strokeWidth: 2
                    },
                ),
                gojs(go.Panel, "Vertical",  // title above Placeholder
                    gojs(go.Panel, "Horizontal",  // button next to TextBlock
                        {
                            name: "HEADER",
                            stretch: go.GraphObject.Horizontal,
                            background: "#01203A"
                        },
                        gojs("SubGraphExpanderButton",
                            {alignment: go.Spot.Right, margin: 5}),
                        gojs(go.TextBlock,
                            {
                                alignment: go.Spot.Left,
                                editable: true,
                                margin: 5,
                                font: "bold 18px sans-serif",
                                stroke: "white",
                                click: toogleCollapseGroup
                            },
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    ),  // end Horizontal Panel
                    gojs(go.Placeholder,
                        {
                            padding: 5,
                            alignment: go.Spot.TopLeft
                        }
                    )
                ),  // end Vertical Panel
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            );
            break;
        case "CONTAINER":
        case "Container":
            return gojs(go.Group, "Auto",
                {
                    background: "transparent",
                    // highlight when dragging into the Group
                    mouseDragEnter: function (e, grp, prev) {
                        highlightGroup(e, grp, true);
                    },
                    mouseDragLeave: function (e, grp, next) {
                        highlightGroup(e, grp, false);
                    },
                    ungroupable: true,
                    computesBoundsAfterDrag: true,
                    // when the selection is dropped into a Group, add the selected Parts into that Group;
                    // if it fails, cancel the tool, rolling back any changes
                    mouseDrop: finishDrop,
                    handlesDragDropForMembers: true,  // don't need to define handlers on member Nodes and Links
                    // Groups containing Groups lay out their members horizontally
                    layout:
                        gojs(go.GridLayout,
                            {
                                wrappingWidth: Infinity, alignment: go.GridLayout.Position,
                                cellSize: new go.Size(1, 1), spacing: new go.Size(4, 4)
                            })
                },
                nodeStyle(),
                new go.Binding("background", "isHighlighted", function (h) {
                    return h ? "rgba(255,0,0,0.2)" : "transparent";
                }).ofObject(),
                gojs(go.Shape, "Rectangle",
                    {
                        fill: null,
                        stroke: "#08427B",
                        stretch: go.GraphObject.Horizontal,
                        strokeWidth: 2
                    },
                ),
                gojs(go.Panel, "Vertical",  // title above Placeholder
                    gojs(go.Panel, "Horizontal",  // button next to TextBlock
                        {
                            name: "HEADER",
                            stretch: go.GraphObject.Horizontal,
                            background: "#08427B"
                        },
                        gojs("SubGraphExpanderButton",
                            {alignment: go.Spot.Right, margin: 5}),
                        gojs(go.TextBlock,
                            {
                                alignment: go.Spot.Left,
                                editable: true,
                                margin: 5,
                                font: "bold 18px sans-serif",
                                stroke: "white",
                                click: toogleCollapseGroup
                            },
                            new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                    ),  // end Horizontal Panel
                    gojs(go.Placeholder,
                        {
                            padding: 5,
                            alignment: go.Spot.TopLeft
                        }
                    )
                ),  // end Vertical Panel
                // four named ports, one on each side:
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            );
            break;
        case "COMPONENT":
        case "Component":
            return gojs(
                go.Node, "Spot", nodeStyle(),
                {
                    name: paletteModel.name
                },
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            fill: "#1368BD",
                            stroke: "#1368BD",
                            minSize: OpenArchiWrapper.toSize(paletteModel)
                        },
                        new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                        new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize)),
                    gojs(go.TextBlock, "Text",
                        {
                            text: paletteModel.name,
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
                makePort("T", go.Spot.Top, true, true),
                makePort("L", go.Spot.Left, true, true),
                makePort("R", go.Spot.Right, true, true),
                makePort("B", go.Spot.Bottom, true, true),
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
            );

        /*  case "GROUP":
              return gojs(go.Group, "Horizontal", groupStyle(),
                  {
                      selectionObjectName: "SHAPE",  // selecting a lane causes the body of the lane to be highlit, not the label
                      resizable: true,
                      resizeObjectName: "SHAPE",  // the custom resizeAdornmentTemplate only permits two kinds of resizing
                      layout: gojs(go.LayeredDigraphLayout,  // automatically lay out the lane's subgraph
                          {
                              isInitial: false,  // don't even do initial layout
                              //isOngoing: false,  // don't invalidate layout when nodes or links are added or removed
                              direction: 0,
                              columnSpacing: 10,
                              layeringOption: go.LayeredDigraphLayout.LayerLongestPathSource
                          }),
                      computesBoundsAfterDrag: true,  // needed to prevent recomputing Group.placeholder bounds too soon
                      computesBoundsIncludingLinks: false,  // to reduce occurrences of links going briefly outside the lane
                      computesBoundsIncludingLocation: true,  // to support empty space at top-left corner of lane
                      //handlesDragDropForMembers: false,  // don't need to define handlers on member Nodes and Links
                      mouseDrop: function (e, grp) {  // dropping a copy of some Nodes and Links onto this Group adds them to this Group
                          //if (!e.shift) return;  // cannot change groups with an unmodified drag-and-drop
                          // don't allow drag-and-dropping a mix of regular Nodes and Groups
                          if (!e.diagram.selection.any(function (n) {
                                  return n instanceof go.Group;
                              })) {
                              const ok = grp.addMembers(grp.diagram.selection, true);
                              if (ok) {
                                  updateCrossLaneLinks(grp);
                              } else {
                                  grp.diagram.currentTool.doCancel();
                              }
                          } else {
                              e.diagram.currentTool.doCancel();
                          }
                      },
                      subGraphExpandedChanged: function (grp) {
                          const shp = grp.resizeObject;
                          if (grp.diagram.undoManager.isUndoingRedoing) return;
                          if (grp.isSubGraphExpanded) {
                              shp.height = grp._savedBreadth;
                          } else {
                              grp._savedBreadth = shp.height;
                              shp.height = NaN;
                          }
                          updateCrossLaneLinks(grp);
                      }
                  },
                  new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),
                  // the lane header consisting of a Shape and a TextBlock
                  new go.Binding("background", "isHighlighted", function (h) {
                      return h ? "rgba(255,0,0,0.2)" : "transparent";
                  }).ofObject(),
                  gojs(go.Panel, "Horizontal",
                      {
                          name: "HEADER",
                          angle: 270,  // maybe rotate the header to read sideways going up
                          alignment: go.Spot.Center,
                          stretch: go.GraphObject.Horizontal
                      },
                      gojs(go.Panel, "Horizontal",  // this is hidden when the swimlane is collapsed
                          new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                          gojs(go.TextBlock,  // the lane label
                              {
                                  font: "bold 13pt sans-serif",
                                  editable: true,
                                  margin: new go.Margin(2, 0, 0, 0)
                              },
                              new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                      ),
                      gojs("SubGraphExpanderButton", {margin: 5})  // but this remains always visible!
                  ),  // end Horizontal Panel
                  gojs(go.Panel, "Auto",  // the lane consisting of a background Shape and a Placeholder representing the subgraph
                      gojs(go.Shape, "Rectangle",  // this is the resized object
                          {
                              name: "SHAPE",
                              fill: "white",
                              stretch: go.GraphObject.Horizontal
                          },
                          new go.Binding("fill", "color"),
                          new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
                      gojs(go.Placeholder,
                          {
                              padding: 12,
                              alignment: go.Spot.TopLeft
                          }),
                      gojs(go.TextBlock,  // this TextBlock is only seen when the swimlane is collapsed
                          {
                              name: "LABEL",
                              font: "bold 13pt sans-serif",
                              editable: true,
                              angle: 0,
                              alignment: go.Spot.TopLeft,
                              margin: new go.Margin(2, 0, 0, 4)
                          },
                          new go.Binding("visible", "isSubGraphExpanded", function (e) {
                              return !e;
                          }).ofObject(),
                          new go.Binding("text", "", OpenArchiWrapper.toTitle).makeTwoWay(OpenArchiWrapper.fromTitle))
                  ), // end Auto Panel
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
              );  // end Group*/
        default:
            return gojs(
                go.Node, "Spot", nodeStyle(),
                {
                    name: paletteModel.name
                },
                gojs(go.Panel, "Auto",
                    gojs(go.Shape,
                        {
                            fill: paletteModel.shape !== undefined ? paletteModel.shape.fill : "",
                            stroke: paletteModel.shape !== undefined ? paletteModel.shape.stroke : "",
                            minSize: OpenArchiWrapper.toSize(paletteModel)
                        },
                        new go.Binding("figure", "", OpenArchiWrapper.toFigure).makeTwoWay(OpenArchiWrapper.fromFigure),
                        new go.Binding("fill", "", OpenArchiWrapper.toFill).makeTwoWay(OpenArchiWrapper.fromFill),
                        new go.Binding("minSize", "", OpenArchiWrapper.toSize).makeTwoWay(OpenArchiWrapper.fromSize),
                        new go.Binding("stroke", "", OpenArchiWrapper.toStroke).makeTwoWay(OpenArchiWrapper.fromStroke)),
                    gojs(go.TextBlock, "Text",
                        {
                            text: paletteModel.name,
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
}
