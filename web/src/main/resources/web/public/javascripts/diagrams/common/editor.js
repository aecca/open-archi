MINLENGTH = 200;  // this controls the minimum length of any swimlane
MINBREADTH = 20;  // this controls the minimum breadth of any non-collapsed swimlane

const gojs = go.GraphObject.make;
let meta = {};

function capitalize(text) {
    return text.substr(0, 1).toUpperCase() + text.substr(2, text.length).toLowerCase();
}

function fixMetaData() {
    const name = $("#diagram-name").val();
    const type = $("#diagramTypesDropdown").find("a.active").html();
    meta.name = name;
    if (meta.kind === undefined) {
        meta.kind = type;
    }
}

// Show the diagram's model in JSON format that the user may edit
function save(model) {
    fixMetaData();
    let value_;
    if (model === undefined) {
        value_ = OpenArchiFromDiagram.process(myDiagram.model);
    } else {
        let model_;
        if (typeof model === "string") {
            value_ = JSON.parse(model);
        } else {
            model_ = model;
        }
        value_ = OpenArchiFromDiagram.process(model_);
    }
    let value = JSON.stringify(value_);
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    modelToSaveOrLoad.empty();
    modelToSaveOrLoad.jsonView(value);
    resizeDataModelDiv();
    myDiagram.isModified = false;
    myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
    $.ajax({
        url: "/api/models",
        data: JSON.stringify(value_),
        type: 'POST',
        crossDomain: true,
        contentType: "application/json",
        converters: {
            "text json": function (response) {
                return (response === "") ? null : JSON.parse(response);
            }
        }
    }).always(data => {
        const diagramInfo = $('#diagram-info');
        diagramInfo.modal('hide');
    }).done((data, textStatus, response) => {
            if (response.status === 201) {
                $.ajax({
                    url: "/api/palette/architectures",
                    type: 'GET',
                    crossDomain: true,
                    contentType: "application/json",
                    converters: {
                        "text json": function (response) {
                            return (response === "") ? null : JSON.parse(response);
                        }
                    }
                }).done((data, textStatus, response) => {
                        if (response.status === 200) {
                            fillPalettes(data);
                        }
                    }
                )
            } else {
                $.ajax({
                    url: "/api/models",
                    data: JSON.stringify(value_),
                    type: 'PUT',
                    crossDomain: true,
                    contentType: "application/json",
                    converters: {
                        "text json": function (response) {
                            return (response === "") ? null : JSON.parse(response);
                        }
                    }
                }).done((data, textStatus, response) => {
                        if (response.status === 200) {
                            fillPalettes(data);
                        } else {
                            if (response.status === 201) {
                                fillPalettes(data);
                            } else {
                                alert("Not created!");
                            }
                        }
                    }
                ).fail((jqXHR, textStatus, errorThrown) => alert(errorThrown))
            }
        }
    ).fail((response, textStatus, errorThrown) => {
        if (response.status === 409) {
            const id = value_.id;
            delete value_.id;
            $.ajax({
                url: "/api/models/" + id,
                data: JSON.stringify(value_),
                type: 'PUT',
                crossDomain: true,
                contentType: "application/json",
                converters: {
                    "text json": function (response) {
                        return (response === "") ? null : JSON.parse(response);
                    }
                }
            }).done((data, textStatus, response) => {
                    if (response.status === 200) {
                        fillPalettes(data);
                    } else {
                        if (response.status === 201) {
                            fillPalettes(data);
                        } else {
                            alert("Not created!");
                        }
                    }
                }
            ).fail((response, textStatus, errorThrown) => alert(errorThrown))
        } else {
            alert(errorThrown);
        }
    });
}

function load(model) {
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    let jsonString = modelToSaveOrLoad.children()[0].innerText;
    if (model === undefined) {
        expand(jsonString);
    } else {
        expand(model);
    }
}

function placeNewNode(elementType, data, name) {
    $.ajax({
        url: "/api/catalogs/element-types/" + elementType.type + "/shape",
        type: 'GET',
        crossDomain: true,
        contentType: "application/json",
        converters: {
            "text json": function (response) {
                return (response === "") ? null : JSON.parse(response);
            }
        }
    }).done((shapeText, textStatus, response) => {
            if (response.status === 200) {
                let shape = JSON.parse(shapeText);
                myDiagram.startTransaction("Deleting new element");
                myDiagram.model.removeNodeData(data);
                myDiagram.requestUpdate();
                myDiagram.commitTransaction("Deleting new element");
                myDiagram.startTransaction("Adding new element");
                delete data["text"];
                delete data["__gohashid"];
                data.kind = elementType.type;
                data.name = name;
                data.image = meta.image;
                data.shape = shape;
                data.fill = shape.fill;
                data.category = elementType.type;
                data.isGroup = OpenArchiWrapper.toIsGroup(shape, null, elementType.group);
                myDiagram.model.addNodeData(data);
                myDiagram.requestUpdate();
                myDiagram.commitTransaction("Adding new element");
                delete meta.image;
                relayoutLanes();
            } else {

            }
        }
    ).fail((jqXHR, textStatus, errorThrown) => alert(errorThrown));
}

function checkAndSave() {
    let basicElementData = $('#basic-element-data');
    const key = basicElementData.attr("data-key");
    let data = myDiagram.model.findNodeDataForKey(key);
    basicElementData.modal('hide');
    if (data !== null) {
        const name = $("#element-name").val();
        const elementType = getElementType();
        //const prototype = $("#element-prototype").prop("checked");
        placeNewNode(elementType, data, name);

    }
}

function expand(data) {
    let model;
    if (!commons.prototype.isObject(data)) {
        model = JSON.parse(data);
    } else {
        model = data;
    }
    const newDiagram = OpenArchiToDiagram.process(model);
    const nodeDataArray = newDiagram.nodeDataArray;
    if (nodeDataArray !== undefined && nodeDataArray !== null) {
        let id;
        const clonedFrom = data.clonedFrom;
        if (clonedFrom === undefined) {
            id = data.id;
        } else {
            id = clonedFrom.id
        }
        const nodedata = findByField(myDiagram.model.nodeDataArray, "id", id);
        if (nodedata !== undefined) {
            myDiagram.startTransaction("Deleting new element");
            myDiagram.model.removeNodeData(myDiagram.model.findNodeDataForKey(nodedata.key));
            myDiagram.requestUpdate();
            myDiagram.commitTransaction("Deleting new element");
        }
        myDiagram.startTransaction("Expand element");
        myDiagram.model.addNodeDataCollection(nodeDataArray);
        const linkDataArray = newDiagram.linkDataArray;
        if (linkDataArray !== undefined && linkDataArray !== null) {
            myDiagram.model.addLinkDataCollection(linkDataArray);
        }
        const pos = myDiagram.model.modelData.position;
        if (pos) {
            myDiagram.initialPosition = go.Point.parse(pos);
        }
        myDiagram.requestUpdate();
        myDiagram.commitTransaction("Expand element");
    }
    relayoutLanes();
}

function fixMeta() {

    const rootNodes = myDiagram.findTreeRoots();

    if (rootNodes !== undefined) {
        let count = 0;
        let rootNodeData = {};
        rootNodes.each(function (node) {
            count++;
            rootNodeData = node;
        });
        if (count === 1) {
            const data = rootNodeData.data;
            if (data !== undefined && meta.name === undefined) {
                meta.name = data.name;
                $("#diagram-name").val(data.name);
            }
            meta.kind = data.kind;
            meta.id = data.id;
        } else {
            meta.id = undefined;
            meta.name = undefined;
            $("#diagram-name").val("");
        }
    }
}

function expandGroups(g, i, level) {
    if (!(g instanceof go.Group)) return;
    g.isSubGraphExpanded = i < level;
    g.memberParts.each(function (m) {
        expandGroups(m, i + 1, level);
    })
}

function reexpand(e) {
    myDiagram.startTransaction("reexpand");
    let level = getCurrentViewModeValue();
    myDiagram.findTopLevelGroups().each(function (g) {
        expandGroups(g, 0, level);
    });
    relayoutDiagram();
    myDiagram.commitTransaction("reexpand");
}

function relocate(el, top, left) {
    el.css({
        position: "absolute",
        marginLeft: 0, marginTop: 0,
        top: top, left: left
    });
}

function resizeDiagramDiv() {
    const diagramDiv = $("#diagramDiv");
    const $menu = $("#menu");
    const windowWidth = $(window).width();
    const menuWidth = $menu.width();
    let width;
    if (windowWidth === menuWidth) {
        width = windowWidth;
    } else {
        width = windowWidth - menuWidth;
    }
    const windowHeight = $(window).height();
    const menuHeight = $menu.height();
    let height;
    if (windowHeight === menuHeight) {
        height = windowHeight;
    } else {
        height = windowHeight - menuHeight;
    }
    diagramDiv.width(width);
    diagramDiv.height(height);
    relocate(diagramDiv, -10, 0);
}

function resizeDataModelDiv() {
    const dataModelDraggable = $("#dataModelDraggable");
    const modelToSaveOrLoad = $("#modelToSaveOrLoad");
    const modelToSaveOrLoadWidth = modelToSaveOrLoad.width();
    const modelToSaveOrLoadHeight = modelToSaveOrLoad.height();
    dataModelDraggable.width(modelToSaveOrLoadWidth);
    dataModelDraggable.height(modelToSaveOrLoadHeight + 30);
}

function relocateDataModelDiv() {
    const dataModelDraggable = $("#dataModelDraggable");
    const windowHeight = $(window).height();
    const menuHeight = dataModelDraggable.height();
    const height = windowHeight - menuHeight;
    relocate(dataModelDraggable, height - 20, 10);
}

function relocatePaletteDiv() {
    const paletteDraggable = $("#paletteDraggable");
    const menu = $("#menu");
    const menuHeight = menu.height();
    relocate(paletteDraggable, menuHeight + 20, 10);
}

function relocateInfoDiv() {
    const infoDraggable = $("#infoDraggable");
    const diagramDiv = $("#diagramDiv");
    const menu = $("#menu");
    const menuHeight = menu.height();
    const width = diagramDiv.width();
    const infoDraggableWidth = infoDraggable.width();
    relocate(infoDraggable, menuHeight + 20, width - infoDraggableWidth - 10);
}

function openExample(url, targetDiv) {
    $("#dataModelDraggable").show();
    getPageContent(url, targetDiv);
}

function openContent(url, targetDiv) {
    getPageContent(url, targetDiv);
}

function getPageContent(url, targetDiv) {
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("text/html; charset=utf-8");
        }
    }).done(function (data) {
        let target;
        if (targetDiv === undefined) {
            target = $("#myInfo");
        } else {
            target = $("#" + targetDiv);
        }
        target.html(data);
        target.show();
    });
}

function openLoadedModel(url) {
    getJsonContent(url, function (model) {
        openModel(model);
    });
}

function getJsonContent(url, callback) {
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("application/json; charset=utf-8");
        }
    }).done(function (data) {
        let jsonData = JSON.stringify(data);
        let modelToSaveOrLoad = $("#modelToSaveOrLoad");
        modelToSaveOrLoad.empty();
        modelToSaveOrLoad.jsonView(jsonData);
        resizeDataModelDiv();
        if (callback !== undefined && (typeof callback === "function")) {
            callback(data)
        }
    });
}

function openModel(model) {
    let graphicalModel = OpenArchiToDiagram.process(model);
    const type = model.kind;
    switch (type) {
        case "FLOWCHART_MODEL":
            $.getScript("/javascripts/diagrams/flowchart.js").done(function (script, textStatus) {
                initFlowchart(graphicalModel.nodes, graphicalModel.links);
            });
            break;
        case "SEQUENCE_MODEL":
            getPageContent("/diagrams/sequenceDiagram.html");
            break;
        case "GANTT_MODEL":
            getPageContent("/diagrams/gantt.html");
            break;
        case "ENTITY_RELATIONSHIP_MODEL":
            getPageContent("/diagrams/entityRelationship.html");
            break;
        case "UML_CLASS_MODEL":
            getPageContent("/diagrams/umlClass.html");
            break;
        case "BPM_MODEL":
            getPageContent("/diagrams/swimLanes.html");
            break;
        case "ARCHITECTURE_MODEL":
            $.getScript("/javascripts/diagrams/basic.js").done(function (script, textStatus) {
                initBasic(graphicalModel.nodes, graphicalModel.links);
            });
            break;
        default:
            console.log("Still not implemented");
    }
}

function openSVG() {
    let newWindow = window.open("", "newWindow");
    if (!newWindow) return;
    const newDocument = newWindow.document;
    const svg = myDiagram.makeSvg({
        document: newDocument,  // create SVG DOM in new document context
        scale: 1.5
    });
    newDocument.body.appendChild(svg);
}

function addNodeToTemplateByType(data, type) {
    const templateNode = getNodeByType(data);
    let isGroup = templateNode.isGroup !== undefined ? templateNode.isGroup : data.isGroup;
    let type_ = type;
    if (type_ === undefined) {
        type_ = data.category !== undefined ? data.category : data.kind
    }
    if (isGroup) {
        myDiagram.groupTemplateMap.add(type_, templateNode);
    } else {
        myDiagram.nodeTemplateMap.add(type_, templateNode);
    }
}

function openMore() {

}

function validateModel() {
    $('#model-validation').modal('show')
}

function confirmAndSave() {
    $('#diagram-info').modal('show');
}

function getCurrentViewMode() {
    if (viewMode) {
        let viewMode_ = $(viewMode.tickLabels[viewMode.getValue() - 1]);
        return viewMode_.html();
    }
    return "";
}

function getCurrentViewModeValue() {
    if (viewMode) {
        return viewMode.getValue();
    }
    return 0;
}

function getElementType() {
    let elementTypesDropdown = $("#elementTypesDropdown");
    const elementType = elementTypesDropdown.closest('.dropdown').find('.dropdown-toggle');
    if (elementType) {
        let type = elementType.html();
        if (type) {
            let elementType_ = {};
            elementType_.type = type.split(" ")[0].trim();
            let isGroup = false;
            let selectedLi = elementTypesDropdown.children("li:contains('" + elementType_.type + "')");
            elementType_.group = selectedLi.attr("data-isgroup");
            return elementType_;
        }
    }
    return null;
}

function handleImageSelect(evt) {
    let files = evt.target.files;
    let i = 0, file;
    for (; file = files[i]; i++) {
        const type = file.type;
        // Only process SVG image files.
        if (type !== 'image/svg+xml') {
            continue;
        }

        const reader = new FileReader();

        // Closure to capture the file information.
        reader.onload = (function (file) {
            return function (e) {
                let rawImage_ = e.target.result;
                let rawImage = rawImage_.replace(/^data:image\/svg\+xml;base64,/, "");
                rawImage = window.atob(rawImage);

                let $element = $('#element-image-data');
                const elementKey = $element.attr("key");

                if (elementKey !== undefined) {
                    myDiagram.model.nodeDataArray.forEach(node => {
                        if (node.key.toString() === elementKey) {
                            myDiagram.model.setDataProperty(node, "image", {raw: rawImage_, type: type});
                        }
                    });
                    myDiagram.requestUpdate();

                    $element.modal('hide');
                } else {
                    meta.image = {raw: rawImage_, type: type};
                }
            };
        })(file);
        // Read in the image file as a data URL.
        reader.readAsDataURL(file);
    }
}

function findValues(obj, key) {
    return findValuesHelper(obj, key, []);
}

function findValuesHelper(obj, key, list) {
    let i;
    if (!obj) return list;
    if (obj instanceof Array) {
        for (i in obj) {
            list = list.concat(findValuesHelper(obj[i], key, []));
        }
        return list;
    }
    if (obj[key]) list.push(obj);

    if ((typeof obj === "object") && (obj !== null)) {
        let children = Object.keys(obj);
        if (children.length > 0) {
            for (i = 0; i < children.length; i++) {
                list = list.concat(findValuesHelper(obj[children[i]], key, []));
            }
        }
    }
    return list;
}

function init() {
    relocateDataModelDiv();
    relocatePaletteDiv();

    switch (source) {
        case "basic":
            initBasic(nodeDataArray, linkDataArray, paletteModel);
            myPaletteBasic.requestUpdate();
            myPaletteGeneral.requestUpdate();
            myDiagram.requestUpdate();
            // when the document is modified, add a "*" to the title and enable the "Save" button
            myDiagram.addDiagramListener("Modified", function (e) {
                let button = $("#SaveButton");
                button.attr('disabled', !myDiagram.isModified);
                let idx = document.title.indexOf("*");
                if (myDiagram.isModified) {
                    if (idx < 0) document.title += "*";
                } else {
                    if (idx >= 0) document.title = document.title.substr(0, idx);
                }
            });

            $("#infoDraggable").draggable({handle: "#infoDraggableHandle"});
            $("#controlsDraggable").draggable({handle: "#controlsDraggableHandle"});

            new Inspector('myInfo', myDiagram,
                {
                    properties: {
                        // key would be automatically added for nodes, but we want to declare it read-only also:
                        "key": {readOnly: true, show: Inspector.showIfPresent},
                        // fill and stroke would be automatically added for nodes, but we want to declare it a color also:
                        "fill": {show: Inspector.showIfPresent, type: 'color'},
                        "stroke": {show: Inspector.showIfPresent, type: 'color'}
                    }
                });
            break;
        default:
            console.log("Still not implemented");
    }

    let dataArray;
    $("#diagramId").autocomplete({
        minLength: 3,
        source: function (request, response) {
            $.get("/api/models", {query: "name=='*" + request.term + "*'"})
                .done(function (data) {
                    const models = [{id: "-1", value: "Select one..."}];
                    dataArray = data;
                    if (Array.isArray(data)) {
                        data.forEach(function (model) {
                            models.push({id: model.id, value: model.name});
                        });
                        response(models);
                    } else {
                        response({});
                    }
                });
        },
        select: function (event, element) {
            const id = element.item.id;
            const model = dataArray.find(function (model) {
                return model.id === id;
            });
            const modelToSaveOrLoad = $("#modelToSaveOrLoad");
            modelToSaveOrLoad.empty();
            modelToSaveOrLoad.jsonView(model);
            resizeDataModelDiv();
            load(model);
        },
        // optional (if other layers overlap autocomplete list)
        open: function (event, ui) {
            $(".ui-autocomplete").css("z-index", 1000);
        }
    }).on("click", function () {
        $(this).select();
    });

    const $dataModelDraggable = $("#dataModelDraggable");
    $dataModelDraggable.draggable({handle: "#dataModelDraggableHandle"}).resizable({
        stop: function (event, ui) {
            // var $modelToSaveOrLoad = $("#modelToSaveOrLoad");
            // $modelToSaveOrLoad.width(ui.size.width - 16);
            // $modelToSaveOrLoad.height(ui.size.height - 22);
        }
    });

    resizeDiagramDiv();
    const $body = $('body');
    const windowHeight = $(window).height();
    $body.attr({style: 'height: ' + windowHeight + 'px; min-height: ' + windowHeight + 'px;'});
    // var $modelToSaveOrLoad = $("#modelToSaveOrLoad");
    // $modelToSaveOrLoad.width($dataModelDraggable.width() - 16);
    // $modelToSaveOrLoad.height($dataModelDraggable.height() - 22);
    relocateInfoDiv();

    $(window).on("resize", function () {
        relocateInfoDiv();
        relocatePaletteDiv();
        resizeDiagramDiv();
    });

    let modalModelValidation = $('#model-validation');
    modalModelValidation.modal({
        backdrop: 'static',
        keyboard: false,
        show: false
    });
}

function copyTextToClipboard(text) {
    const textArea = document.createElement("textArea");

    // Place in top-left corner of screen regardless of scroll position.
    textArea.style.position = 'fixed';
    textArea.style.top = 0;
    textArea.style.left = 0;

    // Ensure it has a small width and height. Setting to 1px / 1em
    // doesn't work as this gives a negative w/h on some browsers.
    textArea.style.width = '2em';
    textArea.style.height = '2em';

    // We don't need padding, reducing the size if it does flash render.
    textArea.style.padding = 0;

    // Clean up any borders.
    textArea.style.border = 'none';
    textArea.style.outline = 'none';
    textArea.style.boxShadow = 'none';

    // Avoid flash of white box if rendered for any reason.
    textArea.style.background = 'transparent';


    textArea.value = text;

    document.body.appendChild(textArea);
    textArea.focus();
    textArea.select();

    try {
        const successful = document.execCommand('copy');
        if (successful) {
            const msg = successful ? 'successful' : 'unsuccessful';
            console.log('Copying text command was ' + msg);
            alert('"' + msg + '" was copied to clipboard')
        }
    } catch (err) {
        console.log('Oops, unable to copy');
    }
    document.body.removeChild(textArea);
}

/*

function toogleCollapseGroup(element, obj) {
    let root = obj.part.findTreeRoot();
    const elementData = root.data;
    const groupId = elementData.key;
    if (!groupId) return;
    if (!(root instanceof go.Group)) return;
    if (root.isSubGraphExpanded) {
        if (myDiagram.commandHandler.canCollapseSubGraph(root)) {
            myDiagram.startTransaction("Collapse group");
            root.collapseSubGraph();
            myDiagram.commitTransaction("Collapse group");
        }
    } else {
        if (myDiagram.commandHandler.canExpandSubGraph(root)) {
            myDiagram.startTransaction("Expand group");
            root.expandTree();
            myDiagram.commitTransaction("Expand group");
        }
    }

}

function convertSVGPolygonPolylineToPath(svg) {
    return svg
    // close path for polygon
        .replace(/(<polygon[\w\W]+?)points=(["'])([\.\d, ]+?)(["'])/g, "$1d=$2M$3z$4")
        // dont close path for polyline
        .replace(/(<polyline[\w\W]+?)points=(["'])([\.\d, ]+?)(["'])/g, "$1d=$2M$3$4")
        .replace(/poly(gon|line)/g, "path");
}

function parseSVG(svg) {
    svg = convertSVGPolygonPolylineToPath(svg);
    return svg;
}

function processCss(style, shape, path) {
    let tokens = style.split(";");
    for (let j = 0; j < tokens.length; j++) {
        let token = tokens[j];
        if (token.match('^fill:')) {
            shape.fill = token.split(":")[1];
        }
        if (token.match('^fill-opacity:')) {
            shape.opacity = parseFloat(token.split(":")[1]);
        }
        if (token.match('^stroke-width:')) {
            let strokewidth = parseFloat(path.getAttribute(token.split(":")[1]));
            if (!isNaN(strokewidth)) shape.strokeWidth = strokewidth;
        }
        if (token.match('^stroke:')) {
            shape.stroke = token.split(":")[1];
        }
    }
    if (!tokens.find(function (token) {
            return token.match('^stroke:');
        })) {
        shape.stroke = null;
    }
}


function buildSVGComponents(raw) {
    const xmldoc = new DOMParser().parseFromString(raw, "text/xml");
    let svgComponents = gojs(go.Panel, {
        /!* desiredSize: new go.Size(60, 60),
                     width: 60,
                     height: 60*!/
    });  // this Panel holds all of the Shapes for the drawing
    const circles = xmldoc.getElementsByTagName("circle");
    for (let i = 0; i < circles.length; i++) {
        // represent each SVG path by a Shape of type Path with its own fill and stroke
        let circle = circles[i];
        let shape = new go.Shape();
        let style = circle.getAttribute("style");
        if (style && typeof style === "string" && style !== "none") {
            processCss(style, shape, circle);
        } else {
            let stroke = circle.getAttribute("stroke");
            if (typeof stroke === "string" && stroke !== "none") {
                shape.stroke = stroke;
            } else {
                shape.stroke = null;
            }
            let strokewidth = parseFloat(circle.getAttribute("stroke-width"));
            if (!isNaN(strokewidth)) shape.strokeWidth = strokewidth;
            let fill = circle.getAttribute("fill");
            if (typeof fill === "string") {
                shape.fill = (fill === "none") ? null : fill;
            }
        }

        let id = circle.getAttribute("id");
        if (typeof id === "string") shape.name = id;

        let cx = circle.getAttribute("cx");
        let cy = circle.getAttribute("cy");
        let r = circle.getAttribute("r");
        let d = parseFloat(r) * 2;
        let data = "M cx, cy m -r, 0 a r,r 0 1,0 d,0 a r,r 0 1,0 -d,0".replace(/cx/g, cx).replace(/cy/g, cy).replace(/r/g, r.toString()).replace(/d/g, d.toString());
        shape.geometry = go.Geometry.parse(data, true);

        // collect these Shapes in the single Panel
        svgComponents.add(shape);
    }

    const paths = xmldoc.getElementsByTagName("path");
    for (let i = 0; i < paths.length; i++) {
        // represent each SVG path by a Shape of type Path with its own fill and stroke
        let path = paths[i];
        let shape = new go.Shape();
        let style = path.getAttribute("style");
        if (style && typeof style === "string" && style !== "none") {
            processCss(style, shape, path);
        } else {
            let stroke = path.getAttribute("stroke");
            if (typeof stroke === "string" && stroke !== "none") {
                shape.stroke = stroke;
            } else {
                shape.stroke = null;
            }
            let strokewidth = parseFloat(path.getAttribute("stroke-width"));
            if (!isNaN(strokewidth)) shape.strokeWidth = strokewidth;
            let fill = path.getAttribute("fill");
            if (typeof fill === "string") {
                shape.fill = (fill === "none") ? null : fill;
            }
        }

        let transform = path.getAttribute("transform");
        if (transform && typeof transform === "string" && transform !== "none") {
            let tokens = transform.split(";");
            for (let j = 0; j < tokens.length; j++) {
                let token = tokens[j];
                if (token.match('^scale\\(')) {
                    let split = token.split("(")[1];
                    let x = split.substr(0, split.length - 1);
                    shape.scale = parseFloat(x);
                }
            }
        }

        let id = path.getAttribute("id");
        if (typeof id === "string") shape.name = id;
        // convert the path data string into a go.Geometry
        let data = path.getAttribute("d");
        if (typeof data === "string") shape.geometry = go.Geometry.parse(data, true);
        // collect these Shapes in the single Panel
        svgComponents.add(shape);
    }
}


function cleanImage(evt) {
    alert(evt.target);
}
*/
