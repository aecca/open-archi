function initComments() {

    let $ = go.GraphObject.make;

    myDiagram =
        $(go.Diagram, diagramDiv,  // create a Diagram for the DIV HTML element
            {
                initialContentAlignment: go.Spot.Center,
                layout: $(go.TreeLayout,
                    {
                        angle: 90,
                        setsPortSpot: false,
                        setsChildPortSpot: false
                    }),
                "undoManager.isEnabled": true,
                // When a Node is deleted by the user, also delete all of its Comment Nodes.
                // When a Comment Link is deleted, also delete the corresponding Comment Node.
                "SelectionDeleting": function (e) {
                    const parts = e.subject;  // the collection of Parts to be deleted, the Diagram.selection
                    // iterate over a copy of this collection,
                    // because we may add to the collection by selecting more Parts
                    parts.copy().each(function (p) {
                        if (p instanceof go.Node) {
                            const node = p;
                            node.findNodesConnected().each(function (n) {
                                // remove every Comment Node that is connected with this node
                                if (n.category === "Comment") {
                                    n.isSelected = true;  // include in normal deletion process
                                }
                            });
                        } else if (p instanceof go.Link && p.category === "Comment") {
                            const comlink = p;  // a "Comment" Link
                            const comnode = comlink.fromNode;
                            // remove the Comment Node that is associated with this Comment Link,
                            if (comnode.category === "Comment") {
                                comnode.isSelected = true;  // include in normal deletion process
                            }
                        }
                    });
                }
            });

    myDiagram.nodeTemplate =
        $("Node", "Auto",
            $("Shape", "CreateRequest",
                {fill: "white"},
                new go.Binding("fill", "color")),
            $("TextBlock",
                {margin: 4},
                new go.Binding("text", "key"))
        );

    myDiagram.linkTemplate =
        $("Link",
            $("Shape",
                {strokeWidth: 1.5}),
            $("Shape",
                {toArrow: "Standard", stroke: null})
        );

    myDiagram.nodeTemplateMap.add("Comment",
        $(go.Node,  // this needs to act as a rectangular shape for BalloonLink,
            {background: "transparent"},  // which can be accomplished by setting the background.
            $(go.TextBlock,
                {stroke: "brown", margin: 3},
                new go.Binding("text"))
        ));

    myDiagram.linkTemplateMap.add("Comment",
        // if the BalloonLink class has been loaded from the Extensions directory, use it
        $((typeof BalloonLink === "function" ? BalloonLink : go.Link),
            $(go.Shape,  // the Shape.geometry will be computed to surround the comment node and
                // point all the way to the commented node
                {stroke: "brown", strokeWidth: 1, fill: "lightyellow"})
        ));

    myDiagram.model =
        $(go.GraphLinksModel,
            {
                nodeDataArray: [
                    {key: "Alpha", color: "orange"},
                    {key: "Beta", color: "lightgreen"},
                    {key: "Gamma", color: "lightgreen"},
                    {key: "Delta", color: "pink"},
                    {key: "A comment", text: "comment\nabout Alpha", category: "Comment"},
                    {key: "B comment", text: "comment\nabout Beta", category: "Comment"},
                    {key: "G comment", text: "comment about Gamma", category: "Comment"}
                ],
                linkDataArray: [
                    {from: "Alpha", to: "Beta"},
                    {from: "Alpha", to: "Gamma"},
                    {from: "Alpha", to: "Delta"},
                    {from: "A comment", to: "Alpha", category: "Comment"},
                    {from: "B comment", to: "Beta", category: "Comment"},
                    {from: "G comment", to: "Gamma", category: "Comment"}
                ]
            });

    // show the model in JSON format
    document.getElementById("modelToSaveOrLoad").textContent = JSON.stringify(myDiagram.model, null, 2);
    let textContent = myDiagram.model.toJson();
}