  function initProcessFlow() {
    
    var $ = go.GraphObject.make;  // for more concise visual tree definitions

    myDiagram =
      $(go.Diagram, diagramDiv,
        {
          "grid.visible": true,
          "grid.gridCellSize": new go.Size(30, 20),
          "draggingTool.isGridSnapEnabled": true,
          "resizingTool.isGridSnapEnabled": true,
          "rotatingTool.snapAngleMultiple": 90,
          "rotatingTool.snapAngleEpsilon": 45,
          "undoManager.isEnabled": true
        });

    // when the document is modified, add a "*" to the title and enable the "Save" button
    myDiagram.addDiagramListener("Modified", function(e) {
      var button = document.getElementById("SaveButton");
      if (button) button.disabled = !myDiagram.isModified;
      var idx = document.title.indexOf("*");
      if (myDiagram.isModified) {
        if (idx < 0) document.title += "*";
      } else {
        if (idx >= 0) document.title = document.title.substr(0, idx);
      }
    });

    myDiagram.nodeTemplateMap.add("Process",
      $(go.Node, "Auto",
        { locationSpot: new go.Spot(0.5, 0.5), locationObjectName: "SHAPE",
          resizable: true, resizeObjectName: "SHAPE" },
        new go.Binding("location", "pos", go.Point.parse).makeTwoWay(go.Point.stringify),
        $(go.Shape, "Cylinder1",
          { name: "SHAPE",
            strokeWidth: 2,
            fill: $(go.Brush, "Linear",
                    { start: go.Spot.Left, end: go.Spot.Right,
                      0: "gray", 0.5: "white", 1: "gray" }),
            minSize: new go.Size(50, 50),
            portId: "", fromSpot: go.Spot.AllSides, toSpot: go.Spot.AllSides
          },
          new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
        $(go.TextBlock,
          { alignment: go.Spot.Center, textAlign: "center", margin: 5,
            editable: true },
          new go.Binding("text").makeTwoWay())
      ));

    myDiagram.nodeTemplateMap.add("Valve",
      $(go.Node, "Vertical",
        { locationSpot: new go.Spot(0.5, 1, 0, -21), locationObjectName: "SHAPE",
          selectionObjectName: "SHAPE", rotatable: true },
        new go.Binding("angle").makeTwoWay(),
        new go.Binding("location", "pos", go.Point.parse).makeTwoWay(go.Point.stringify),
        $(go.TextBlock,
          { alignment: go.Spot.Center, textAlign: "center", margin: 5, editable: true },
          new go.Binding("text").makeTwoWay(),
          // keep the text upright, even when the whole node has been rotated upside down
          new go.Binding("angle", "angle", function(a) { return a === 180 ? 180 : 0; }).ofObject()),
        $(go.Shape,
          { name: "SHAPE",
            geometryString: "F1 M0 0 L40 20 40 0 0 20z M20 10 L20 30 M12 30 L28 30",
            strokeWidth: 2,
            fill: $(go.Brush, "Linear", { 0: "gray", 0.35: "white", 0.7: "gray" }),
            portId: "", fromSpot: new go.Spot(1, 0.35), toSpot: new go.Spot(0, 0.35) })
      ));

    myDiagram.linkTemplate =
      $(go.Link,
        { routing: go.Link.AvoidsNodes, curve: go.Link.JumpGap, corner: 10, reshapable: true, toShortLength: 7 },
        new go.Binding("points").makeTwoWay(),
        // mark each Shape to get the link geometry with isPanelMain: true
        $(go.Shape, { isPanelMain: true, stroke: "black", strokeWidth: 5 }),
        $(go.Shape, { isPanelMain: true, stroke: "gray", strokeWidth: 3 }),
        $(go.Shape, { isPanelMain: true, stroke: "white", strokeWidth: 1, name: "PIPE", strokeDashArray: [10, 10] }),
        $(go.Shape, { toArrow: "Triangle", fill: "black", stroke: null })
      );

    load();

    loop();  // animate some flow through the pipes
  }

  function loop() {
    var diagram = myDiagram;
    setTimeout(function() {
      var oldskips = diagram.skipsUndoManager;
      diagram.skipsUndoManager = true;
      diagram.links.each(function(link) {
          var shape = link.findObject("PIPE");
          var off = shape.strokeDashOffset - 2;
          shape.strokeDashOffset = (off <= 0) ? 20 : off;
        });
      diagram.skipsUndoManager = oldskips;
      loop();
    }, 100);
  }

  function save() {
    document.getElementById("modelToSaveOrLoad").value = myDiagram.model.toJson();
    myDiagram.isModified = false;
  }
  function load() {
    myDiagram.model = go.Model.fromJson(document.getElementById("modelToSaveOrLoad").value);
  }