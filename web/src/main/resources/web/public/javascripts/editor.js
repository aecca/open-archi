MINLENGTH = 200;  // this controls the minimum length of any swimlane
MINBREADTH = 20;  // this controls the minimum breadth of any non-collapsed swimlane

const gojs = go.GraphObject.make;
let meta = {};

function capitalize(text) {
    return text.substr(0, 1).toUpperCase() + text.substr(2, text.length).toLowerCase();
}

// Make link labels visible if coming out of a "conditional" node.
// This listener is called by the "LinkDrawn" and "LinkRelinked" DiagramEvents.
function showLinkLabel(e) {
    const label = e.subject.findObject("LABEL");
    if (label !== null) label.visible = (e.subject.fromNode.data.figure === "Diamond");
}

function nodeInfo(d) {  // Tooltip info for a node data object
    let str;
    if (d.description) {
        str = d.description + "\n";
    }
    if (d.group) {
        str += "Forma parte de: " + d.group;
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
            })
    );

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

function fixMetaData() {
    const name = $("#diagram-name").val();
    const type = $("#diagramTypesDropdown").find("a.active").html();
    const prototype = $("#diagram-prototype").prop("checked");
    meta.name = name;
    meta.kind = type;
    meta.prototype = prototype;
}

// Show the diagram's model in JSON format that the user may edit
function save(model) {
    fixMetaData();
    let value_;
    if (model === undefined) {
        value_ = OpenArchiWrapper.fromDiagram(myDiagram.model);
    } else {
        let model_;
        if (typeof model === "string") {
            value_ = JSON.parse(model);
        } else {
            model_ = model;
        }
        value_ = OpenArchiWrapper.fromDiagram(model_);
    }
    let value = JSON.stringify(value_);
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    modelToSaveOrLoad.empty();
    modelToSaveOrLoad.jsonView(value);
    resizeDataModelDiv();
    myDiagram.isModified = false;
    myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
    $.ajax({
        url: "/open-archi/api/models",
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
                alert(response.statusText);
            } else {
                $.ajax({
                    url: "/open-archi/api/models",
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
                            alert(response.statusText);
                        } else {
                            if (response.status === 201) {
                                alert(response.statusText);
                            } else {
                                alert("Not created!");
                            }
                        }
                    }
                ).fail((jqXHR, textStatus, errorThrown) => alert(errorThrown))
            }
        }
    ).fail((jqXHR, textStatus, errorThrown) => alert(errorThrown));
}

function load() {
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    let jsonString = modelToSaveOrLoad.children()[0].innerText;
    expand(jsonString);
}


function checkAndSave() {
    let basicElementData = $('#basic-element-data');
    const key = basicElementData.attr("data-key");
    let data = myDiagram.model.findNodeDataForKey(key);

    if (data !== null) {
        const name = $("#element-name").val();
        const type = getElementType();
        const prototype = $("#element-prototype").prop("checked");
        delete data["text"];
        data.kind = type;
        data.name = name;
        data.image = meta.image;
        myDiagram.startTransaction("Adding new element");
        addNodeToTemplateByType(data, type);
        data.category = type;
        myDiagram.model.removeNodeData(data);
        myDiagram.model.addNodeData(data);
        delete meta.image;
        myDiagram.requestUpdate();
        myDiagram.commitTransaction("Adding new element");
        relayoutLanes();
    }
    basicElementData.modal('hide')
}


function expand(data) {
    let model;
    if (!commons.prototype.isObject(data)) {
        model = JSON.parse(data);
    } else {
        model = data;
    }
    meta.id = model.id;
    const newDiagram = OpenArchiWrapper.toDiagram(model);
    myDiagram.startTransaction("Expand element");
    myDiagram.model.addNodeDataCollection(newDiagram.nodeDataArray);
    myDiagram.model.addLinkDataCollection(newDiagram.linkDataArray);
    const pos = myDiagram.model.modelData.position;
    if (pos) {
        myDiagram.initialPosition = go.Point.parse(pos);
    }
    relayoutLanes();
    myDiagram.commitTransaction("Expand element");
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
    let graphicalModel = OpenArchiWrapper.toDiagram(model);
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

$(function () {

    $.getScript("/javascripts/menu.js").done(function (script, textStatus) {
        showMenu();
    });

    relocateDataModelDiv();
    relocatePaletteDiv();
    showPaletteByType(paletteData);

    switch (source) {
        case "basic":
            initBasic(nodeDataArray, linkDataArray);

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

            $("#paletteDraggable").draggable({handle: "#paletteDraggableHandle"}).resizable({
                // After resizing, perform another layout to fit everything in the palette's viewport
                stop: function () {
                    myPalette.layoutDiagram(true);
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
            $.get("/open-archi/api/models", {$filter: "name=='*" + request.term + "*'"})
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
            load();
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
});

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

function confirm() {
    let diagramInfo = $('#diagram-info');
    const name = $("#diagram-name").val();
    const type = $("#diagramTypesDropdown").find("a.active").html();
    const prototype = $("#diagram-prototype").prop("checked");
    meta.name = name;
    meta.kind = type;
    meta.prototype = prototype;
    diagramInfo.modal('hide');
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
    const elementType = $("#elementTypesDropdown").closest('.dropdown').find('.dropdown-toggle');
    if (elementType) {
        let type = elementType.html();
        if (type) {
            return type.split(" ")[0];
        }
    }
    return undefined;
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
    if (show) {
        // cannot depend on the grp.diagram.selection in the case of external drag-and-drops;
        // instead depend on the DraggingTool.draggedParts or .copiedParts
        let tool = grp.diagram.toolManager.draggingTool;
        let map = tool.draggedParts || tool.copiedParts;  // this is a Map
        // now we can check to see if the Group will accept membership of the dragged Parts
        if (grp.canAddMembers(map.toKeySet())) {
            grp.isHighlighted = true;
            return;
        }
    }
    grp.isHighlighted = false;
}

// this function is used to highlight a Panel that the selection may be dropped into
function highlight(e, element, show) {
    if (!element) return;
    e.handled = true;
    if (show) {
        // cannot depend on the grp.diagram.selection in the case of external drag-and-drops;
        // instead depend on the DraggingTool.draggedParts or .copiedParts
        let tool = element.diagram.toolManager.draggingTool;
        let map = tool.draggedParts || tool.copiedParts;  // this is a Map
        // now we can check to see if the Group will accept membership of the dragged Parts
        if (element.canAddMembers(map.toKeySet())) {
            element.isHighlighted = true;
            return;
        }
    }
    element.isHighlighted = false;
}

function groupStyle() {  // common settings for both Lane and Pool Groups
    return [
        {
            layerName: "Background",  // all pools and lanes are always behind all nodes and links
            background: "transparent",  // can grab anywhere in bounds
            movable: true, // allows users to re-order by dragging
            copyable: false,  // can't copy lanes or pools
            avoidable: false,  // don't impede AvoidsNodes routed Links
            minLocation: new go.Point(NaN, -Infinity),  // only allow vertical movement
            maxLocation: new go.Point(NaN, Infinity)
        },
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify)
    ];
}

// hide links between lanes when either lane is collapsed
function updateCrossLaneLinks(group) {
    group.findExternalLinksConnected().each(function (l) {
        l.visible = (l.fromNode.isVisible() && l.toNode.isVisible());
    });
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
        /* desiredSize: new go.Size(60, 60),
                     width: 60,
                     height: 60*/
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
                let raw = rawImage;
                raw = parseSVG(raw);

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

// this may be called to force the lanes to be laid out again
function relayoutLanes() {
    myDiagram.nodes.each(function (node) {
        if (node.category !== "Lane" && node.category !== "LANE" && node.category !== "Layer" && node.category !== "LAYER") {
            return;
        }
        node.layout.isValidLayout = false;  // force it to be invalid
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
                    computedWidth = computedHeight + bound.width + node.layout.columnSpacing;
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
