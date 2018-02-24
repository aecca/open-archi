function initUpdateDemo() {

    var $ = go.GraphObject.make;  // for conciseness in defining templates

    blueDiagram =
        $(go.Diagram, "blueDiagram",
            {
                // start everything in the middle of the viewport
                initialContentAlignment: go.Spot.Center,
                // double-click in background creates a new node there
                "clickCreatingTool.archetypeNodeData": {text: "node"}
            });

    blueDiagram.nodeTemplate =
        $(go.Node, "Auto",
            new go.Binding("location", "loc").makeTwoWay(),
            $(go.Shape, "RoundedRectangle",
                {
                    fill: $(go.Brush, "Linear", {0: "#00ACED", 0.5: "#00ACED", 1: "#0079A6"}),
                    stroke: "#0079A6",
                    portId: "", cursor: "pointer",  // the node's only port is the Shape
                    fromLinkable: true, fromLinkableDuplicates: true, fromLinkableSelfNode: true,
                    toLinkable: true, toLinkableDuplicates: true, toLinkableSelfNode: true
                }),
            $(go.TextBlock,
                {margin: 3, font: "bold 10pt Arial, sans-serif", stroke: "whitesmoke", editable: true},
                new go.Binding("text").makeTwoWay())
        );

    blueDiagram.linkTemplate =
        $(go.Link,
            {
                curve: go.Link.Bezier, adjusting: go.Link.Stretch,
                relinkableFrom: true, relinkableTo: true, reshapable: true
            },
            $(go.Shape,  // the link shape
                {strokeWidth: 2, stroke: "blue"}),
            $(go.Shape,  // the arrowhead
                {
                    toArrow: "standard",
                    fill: "blue", stroke: null
                })
        );


    greenDiagram =
        $(go.Diagram, "greenDiagram",
            {
                // start everything in the middle of the viewport
                initialContentAlignment: go.Spot.Center,
                // double-click in background creates a new node there
                "clickCreatingTool.archetypeNodeData": {key: "node"}
            });

    greenDiagram.nodeTemplate =
        $(go.Node, "Vertical",
            new go.Binding("location", "loc").makeTwoWay(),
            $(go.Shape, "Ellipse",
                {fill: "lightgreen", width: 20, height: 20, portId: ""}),
            $(go.TextBlock,
                {margin: 3, font: "bold 12px Georgia, sans-serif"},
                new go.Binding("text"))
        );

    greenDiagram.linkTemplate =
        $(go.Link,
            $(go.Shape,  // the link shape
                {strokeWidth: 2, stroke: "#76C176"}),
            $(go.Shape,  // the arrowhead
                {
                    toArrow: "standard",
                    fill: "#76C176", stroke: null
                })
        );


    // create the model data that will be represented in both diagrams simultaneously
    var model = new go.GraphLinksModel(
        [
            {key: 1, text: "Alpha", loc: new go.Point(20, 20)},
            {key: 2, text: "Beta", loc: new go.Point(160, 120)}
        ],
        [
            {from: 1, to: 2}
        ]);

    // the two Diagrams share the same model
    blueDiagram.model = model;
    greenDiagram.model = model;

    // now turn on undo/redo
    model.undoManager.isEnabled = true;


    // **********************************************************
    // A third diagram is on this page to display the undo state.
    // It functions as a tree view, showing the Transactions
    // in the UndoManager history.
    // **********************************************************

    var undoDisplay =
        $(go.Diagram, "undoDisplay",
            {
                allowMove: false,
                maxSelectionCount: 1,
                layout:
                    $(go.TreeLayout,
                        {
                            alignment: go.TreeLayout.AlignmentStart,
                            angle: 0,
                            compaction: go.TreeLayout.CompactionNone,
                            layerSpacing: 16,
                            layerSpacingParentOverlap: 1,
                            nodeIndent: 2,
                            nodeIndentPastParent: 0.88,
                            nodeSpacing: 0,
                            setsPortSpot: false,
                            setsChildPortSpot: false,
                            arrangementSpacing: new go.Size(2, 2)
                        }),
                "animationManager.isEnabled": false
            });

    undoDisplay.nodeTemplate =
        $(go.Node,
            $("TreeExpanderButton",
                {width: 14, "ButtonBorder.fill": "whitesmoke"}),
            $(go.Panel, "Horizontal",
                {position: new go.Point(16, 0)},
                new go.Binding("background", "color"),
                $(go.TextBlock, {margin: 2},
                    new go.Binding("text", "text"))
            )
        );

    undoDisplay.linkTemplate = $(go.Link);  // not really used

    var undoModel =
        $(go.TreeModel,  // initially empty
            {isReadOnly: true});
    undoDisplay.model = undoModel;

    // ******************************************************
    // Add an undo listener to the main model
    // ******************************************************

    var changedLog = document.getElementById("modelChangedLog");
    var editToRedo = null; // a node in the undoDisplay
    var editList = [];

    model.addChangedListener(function (e) {
        // do not display some uninteresting kinds of transaction notifications
        if (e.change === go.ChangedEvent.Transaction) {
            if (e.propertyName === "CommittingTransaction" || e.modelChange === "SourceChanged") return;
            // do not display any layout transactions
            if (e.oldValue === "Layout") return;
        }  // You will probably want to use e.isTransactionFinished instead

        // Add entries into the log
        var changes = e.toString();
        if (changes[0] !== "*") changes = "&nbsp;&nbsp;" + changes;
        changedLog.innerHTML += changes + "<br/>";
        changedLog.scrollTop = changedLog.scrollHeight;

        // Modify the undoDisplay Diagram, the tree view
        if (e.propertyName === "CommittedTransaction") {
            if (editToRedo != null) {
                // remove from the undo display diagram all nodes after editToRedo
                for (var i = editToRedo.data.index + 1; i < editList.length; i++) {
                    undoDisplay.remove(editList[i]);
                }
                editList = editList.slice(0, editToRedo.data.index);
                editToRedo = null;
            }

            var tx = e.object;
            var txname = (tx !== null ? e.object.name : "");
            var parentData = {text: txname, tag: e.object, index: editList.length - 1};
            undoModel.addNodeData(parentData)
            var parentKey = undoModel.getKeyForNodeData(parentData);
            var parentNode = undoDisplay.findNodeForKey(parentKey);
            editList.push(parentNode);
            if (tx !== null) {
                var allChanges = tx.changes;
                var odd = true;
                allChanges.each(function (change) {
                    var childData = {
                        color: (odd ? "white" : "#E0FFED"),
                        text: change.toString(),
                        parent: parentKey
                    };
                    undoModel.addNodeData(childData)
                    odd = !odd;
                });
                undoDisplay.commandHandler.collapseTree(parentNode);
            }
        } else if (e.propertyName === "FinishedUndo" || e.propertyName === "FinishedRedo") {
            var undoManager = model.undoManager;
            if (editToRedo !== null) {
                editToRedo.isSelected = false;
                editToRedo = null;
            }
            // Find the node that represents the undo or redo state and select it
            var nextEdit = undoManager.transactionToRedo;
            if (nextEdit !== null) {
                var itr = undoDisplay.nodes;
                while (itr.next()) {
                    var node = itr.value;
                    if (node.data.tag === nextEdit) {
                        node.isSelected = true;
                        editToRedo = node;
                        break;
                    }
                }
            }
        }
    }); // end model changed listener

    model.addChangedListener(function (e) {
        if (e.isTransactionFinished) {
            var tx = e.object;
            if (tx instanceof go.Transaction && window.console) {
                window.console.log(tx.toString());
                tx.changes.each(function (c) {
                    if (c.model) window.console.log("  " + c.toString());
                });
            }
        }
    });
} // end init

function clearLog() {
    var div = document.getElementById("modelChangedLog").innerHTML = "";
}