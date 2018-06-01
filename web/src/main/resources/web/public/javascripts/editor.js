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


            }, true)
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


// Show the diagram's model in JSON format that the user may edit
function save() {
    let value_ = OpenArchiWrapper.fromDiagram(myDiagram.model);
    let value = JSON.stringify(value_);
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    modelToSaveOrLoad.empty();
    modelToSaveOrLoad.jsonView(value);
    resizeDataModelDiv();
    myDiagram.isModified = false;
    myDiagram.model.modelData.position = go.Point.stringify(myDiagram.position);
    $.post("/open-archi/api/models", value, function (response) {
        if (response === 201) {
            alert("created");
        } else {
            commons.prototype.put("/open-archi/api/models", value_, 'application/json')
                .then(function (data) {
                    if (response === 200) {
                        alert("created");
                    } else {
                        if (response === 201) {
                            alert("accepted");
                        } else {
                            alert(data);
                        }
                    }
                }).catch(function (data) {
                alert(data);
            });
        }
    }, "application/json");
}

function load() {
    let modelToSaveOrLoad = $("#modelToSaveOrLoad");
    let jsonString = modelToSaveOrLoad.children()[0].innerText;
    expand(jsonString);
}

function expand(data) {
    let model;
    if (!commons.prototype.isObject(data)) {
        model = JSON.parse(data);
    } else {
        model = data;
    }
    meta.id = model.id;
    const diagram = OpenArchiWrapper.toDiagram(model);
    const newDiagram = go.Model.fromJson(diagram);

    myDiagram.startTransaction("Adding new element");
    myDiagram.model.addNodeDataCollection(newDiagram.nodeDataArray);
    myDiagram.model.addLinkDataCollection(newDiagram.linkDataArray);
    myDiagram.commitTransaction("Adding new element");

    const pos = myDiagram.model.modelData.position;
    if (pos) {
        myDiagram.initialPosition = go.Point.parse(pos);
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


function openContent(url) {
    getPageContent(url);
}

function getPageContent(url) {
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("text/html; charset=utf-8");
        }
    }).done(function (data) {
        $("#myInfo").html(data);
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
        const newNode = getNodeByType(data);
        let group = newNode.isGroup;
        if (group) {
            myDiagram.groupTemplateMap.add(type, newNode);
        } else {
            myDiagram.nodeTemplateMap.add(type, newNode);
        }
        data.category = type;
        myDiagram.model.removeNodeData(data);
        myDiagram.model.addNodeData(data);
        myDiagram.requestUpdate();
    }
    basicElementData.modal('hide')
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
    let $diagramInfo = $('#diagram-info');
    $diagramInfo.modal('show');
    $diagramInfo.on('hidden.bs.modal', function () {
        save();
    })
}

function getCurrentViewMode() {
    let viewMode_ = $(viewMode.tickLabels[viewMode.getValue() - 1]);
    return viewMode_.html();
}

function getCurrentViewModeValue() {
    return viewMode.getValue();
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
