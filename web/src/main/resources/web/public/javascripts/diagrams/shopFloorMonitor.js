function initShopFloorMonitor() {

    const $ = go.GraphObject.make;  // for conciseness in defining templates

    myDiagram =
        $(go.Diagram, diagramDiv,
            {
                "animationManager.isEnabled": false,
                initialContentAlignment: go.Spot.Center
            });

    // conversion functions for Bindings in the Node template:

    function nodeTypeImage(type) {
        switch (type) {
            case "S2":
                return "/images/diagrams/voice atm switch.jpg";
            case "S3":
                return "/images/diagrams/server switch.jpg";
            case "P1":
                return "/images/diagrams/general processor.jpg";
            case "P2":
                return "/images/diagrams/storage array.jpg";
            case "M4":
                return "/images/diagrams/iptv broadcast server.jpg";
            case "M5":
                return "/images/diagrams/content engine.jpg";
            case "I1":
                return "/images/diagrams/pc.jpg";
            default:
                return "/images/diagrams/pc.jpg";
        }
        if (type.charAt(0) === "S") return;
        if (type.charAt(0) === "P") return "images/general processor.jpg";
        if (type.charAt(0) === "M")
            return "images/pc.jpg";
    }

    function nodeProblemConverter(msg) {
        if (msg) return "red";
        return null;
    }

    function nodeOperationConverter(s) {
        if (s >= 2) return "TriangleDown";
        if (s >= 1) return "Rectangle";
        return "Circle";
    }

    function nodeStatusConverter(s) {
        if (s >= 2) return "red";
        if (s >= 1) return "yellow";
        return "green";
    }

    myDiagram.nodeTemplate =
        $(go.Node, "Vertical",
            {locationObjectName: "ICON"},
            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
            $(go.Panel, "Spot",
                $(go.Panel, "Auto",
                    {name: "ICON"},
                    $(go.Shape,
                        {fill: null, stroke: null},
                        new go.Binding("background", "problem", nodeProblemConverter)),
                    $(go.Picture,
                        {margin: 5},
                        new go.Binding("source", "type", nodeTypeImage))
                ),  // end Auto Panel
                $(go.Shape, "Circle",
                    {
                        alignment: go.Spot.TopLeft, alignmentFocus: go.Spot.TopLeft,
                        width: 12, height: 12, fill: "orange"
                    },
                    new go.Binding("figure", "operation", nodeOperationConverter)),
                $(go.Shape, "Triangle",
                    {
                        alignment: go.Spot.TopRight, alignmentFocus: go.Spot.TopRight,
                        width: 12, height: 12, fill: "blue"
                    },
                    new go.Binding("fill", "status", nodeStatusConverter))
            ),  // end Spot Panel
            $(go.TextBlock,
                new go.Binding("text"))
        );  // end Node


    // conversion function for Bindings in the Link template:

    function linkProblemConverter(msg) {
        if (msg) return "red";
        return "gray";
    }

    myDiagram.linkTemplate =
        $(go.Link, go.Link.AvoidsNodes,
            {corner: 3},
            $(go.Shape,
                {strokeWidth: 2, stroke: "gray"},
                new go.Binding("stroke", "problem", linkProblemConverter))
        );

    load();


    // simulate some real-time problem monitoring, once every two seconds:
    function randomProblems() {
        const model = myDiagram.model;
        // update all nodes
        let arr = model.nodeDataArray;
        for (var i = 0; i < arr.length; i++) {
            data = arr[i];
            data.problem = (Math.random() < 0.8) ? "" : "Power loss due to ...";
            data.status = Math.random() * 3;
            data.operation = Math.random() * 3;
            model.updateTargetBindings(data);
        }
        // and update all links
        arr = model.linkDataArray;
        for (i = 0; i < arr.length; i++) {
            data = arr[i];
            data.problem = (Math.random() < 0.7) ? "" : "No Power";
            model.updateTargetBindings(data);
        }
    }

    function loop() {
        setTimeout(function () {
            randomProblems();
            loop();
        }, 2000);
    }

    loop();  // start the simulation
}

function load() {
    myDiagram.model = go.Model.fromJson(document.getElementById("modelToSaveOrLoad").value);
}