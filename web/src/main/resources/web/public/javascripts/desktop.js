const Desktop = {
    options: {
        windowArea: ".window-area",
        windowAreaClass: "",
        taskBar: ".task-bar > .tasks",
        taskBarClass: ""
    },

    wins: {},

    setup: function (options) {
        this.options = $.extend({}, this.options, options);
        return this;
    },

    addToTaskBar: function (wnd) {
        const icon = wnd.getIcon();
        const wID = wnd.win.attr("id");
        const type = wnd.win.attr("type");
        const item = $("<span>").addClass("task-bar-item started").html(icon);

        item.data("wID", wID);
        if (type !== undefined) {
            item.data("type", type);
        }
        item.click(function () {
            const el = $("#" + wID);
            let topZ = 0;
            $(".window.resizable").each(function () {
                const thisZ = parseInt($(this).css("z-index"), 10);
                if (thisZ > topZ) {
                    topZ = thisZ;
                }
            });
            el.css('z-index', topZ + 1);
        });
        item.appendTo($(this.options.taskBar));
    },

    removeFromTaskBar: function (wnd) {
        console.log(wnd);
        const wID = wnd.attr("id");
        const items = $(".task-bar-item");
        const that = this;
        $.each(items, function () {
            const item = $(this);
            if (item.data("wID") === wID) {
                delete that.wins[wID];
                item.remove();
            }
        })
    },

    createWindow: function (o) {
        let win;
        const that = this;
        const type = o.type;
        const items = $(".task-bar-item");
        let alreadyCreated = false;
        if (type !== undefined) {
            $.each(items, function () {
                const item = $(this);
                if (item.data("type") === type) {
                    item.focus();
                    alreadyCreated = true;
                }
            });
            if (alreadyCreated) {
                return;
            }
        }
        o.onDragStart = function (pos, el) {
            win = $(el);
            $(".window").css("z-index", 1);
            if (!win.hasClass("modal"))
                win.css("z-index", 3);
        };
        o.onDragStop = function (pos, el) {
            win = $(el);
            if (!win.hasClass("modal"))
                win.css("z-index", 2);
        };
        o.onWindowDestroy = function (win) {
            that.removeFromTaskBar(win);
        };
        const div = o.suffix ? $("<div>", {id: "window-" + o.suffix}) : $("<div>");
        const w = div.appendTo($(this.options.windowArea));
        const wnd = w.window(o).data("window");

        win = wnd.win;
        const shift = Metro.utils.objectLength(this.wins) * 16;

        if (wnd.options.place === "auto" && wnd.options.top === "auto" && wnd.options.left === "auto") {
            win.css({
                top: shift,
                left: shift
            });
        }
        const id = win.attr("id");
        if (type !== undefined) {
            win.attr("type", type);
        }
        this.wins[id] = wnd;
        this.addToTaskBar(wnd);
        w.remove();
        return id;
    }
};

Desktop.setup();

// noinspection JSUnusedGlobalSymbols
function createApiWindow() {
    const content = "<div class='container'><div id='swagger-ui'></div>";
    if (Desktop.createWindow({
            width: "100%",
            height: "auto",
            type: "api",
            icon: "<img src=\"" + basePath + "/images/open-archi-api-logo.png\" style=\"display: inherit;\" class=\"img-responsive\">",
            title: "OpenArchi Api",
            content: "<div class='p-2'>" + content + "</div>"
        })) {
        window.ui = SwaggerUIBundle({
            url: "/open-archi-apis.yaml",
            logo: "/images/open-archi.png",
            dom_id: '#swagger-ui',
            deepLinking: true,
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIStandalonePreset
            ],
            plugins: [
                SwaggerUIBundle.plugins.DownloadUrl
            ],
            layout: "StandaloneLayout",
            requestInterceptor: function (req) {
                req.headers["Access-Control-Allow-Origin"] = "*";
                req.headers["Access-Control-Request-Method"] = "*";
                req.headers["Access-Control-Allow-Headers"] = "*";
                return req;
            },
            responseInterceptor: function (res) {
                return res;
            }
        });
    }
}

// noinspection JSUnusedGlobalSymbols
function createPrototyperWindow() {
    const generatedId = (Math.random() + ' ').substring(2, 10) + (Math.random() + ' ').substring(2, 10);
    let content =
        "<div class=\"wrapper\">\n" +
        "    <nav class=\"active\" id=\"sidebar" + "-" + generatedId + "\" style=\"position: initial;\">\n" +
        "      <ul class=\"list-unstyled components\" style=\"margin-left: 0\">\n" +
        //        "        <li class=\"active\"><a href=\"?#basicPaletteSubMenu" + "-" + generatedId + "\" data-toggle=\"collapse\" aria-expanded=\"true\" style=\"font-weight: bold\">Basic</a>\n" +
        //        "          <ul class=\"collapse in list-unstyled\" id=\"basicPaletteSubMenu" + "-" + generatedId + "\">\n" +
        "            <div id=\"paletteDivBasic" + "-" + generatedId + "\" style=\"height: 160px; width: 164px; overflow-y: 'hidden'; overflow-x: 'hidden';\"></div>\n" +
        //        "          </ul>\n" +
        //        "        </li>\n" +
        //        "        <li class=\"active\"><a href=\"?#generalPaletteSubMenu" + "-" + generatedId + "\" data-toggle=\"collapse\" aria-expanded=\"true\" style=\"font-weight: bold\">Extended</a>\n" +
        //        "          <ul class=\"collapse in list-unstyled\" id=\"generalPaletteSubMenu" + "-" + generatedId + "\">\n" +
        "            <div id=\"paletteDivGeneral" + "-" + generatedId + "\" style=\"height: 280px; width: 164px; overflow-y: 'hidden';\"></div>\n" +
        //        "          </ul>\n" +
        //        "        </li>\n" +
        "      </ul>\n" +
        "    </nav>" +
        "    <div id=\"content" + "-" + generatedId + "\">\n" +
        "        <!-- Page Content Holder-->\n" +
        "        <div id=\"diagramDiv" + "-" + generatedId + "\" style=\"width: 100%; height: 100%;\">\n" +
        "            <div class=\"container\" id=\"diagramsCanvas" + "-" + generatedId + "\" style=\"justify-content: space-between; z-order: -1001\"></div>\n" +
        "        </div>\n" +
        "        <div class=\"draggable\" id=\"infoDraggable" + "-" + generatedId + "\"\n" +
        "             style=\"display: none; vertical-align: top; top: 20px; left: 380px; position: relative\">\n" +
        "            <div class=\"handle\" id=\"infoDraggableHandle" + "-" + generatedId + "\">Info</div>\n" +
        "            <div>\n" +
        "                <div id=\"myInfo" + "-" + generatedId + "\"></div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"draggable\" id=\"controlsDraggable" + "-" + generatedId + "\"\n" +
        "             style=\"display: none; vertical-align: top; top: 20px; left: 380px; position: relative; width: 380px; padding-bottom: 10px\">\n" +
        "            <div class=\"handle\" id=\"controlsDraggableHandle" + "-" + generatedId + "\">Controls</div>\n" +
        "            <div>\n" +
        "                <div id=\"myControls" + "-" + generatedId + "\" style=\"margin-top: 10px\">\n" +
        "                    <div class=\"col-sm-4\"><b>View Mode:</b></div>\n" +
        "                    <div class=\"col-sm-4 text-right\">\n" +
        "                        <input id=\"viewMode" + "-" + generatedId + "\" type=\"text\"/>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <br/>\n" +
        "        <div class=\"container-fluid draggable\" id=\"dataModelDraggable" + "-" + generatedId + "\"\n" +
        "             style=\"height: 100px; width: 400px; display: none\">\n" +
        "            <div class=\"handle\" id=\"dataModelDraggableHandle" + "-" + generatedId + "\">Model</div>\n" +
        "            <div id=\"dataModelContainer" + "-" + generatedId + "\"><span id=\"modelToSaveOrLoad" + "-" + generatedId + "\" style=\"display: block\"></span></div>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>";
    const id = Desktop.createWindow({
        id: "window-" + generatedId,
        width: "100%",
        height: "100%",
        icon: "<img src=\"" + basePath + "/images/open-archi-prototyper-logo.png\" style=\"display: inherit;\" class=\"img-responsive\">",
        title: "OpenArchi Prototyper",
        content: "<div class='p-2'>" + content + "</div>"
    });
    setup(generatedId, true);
}


// noinspection JSUnusedGlobalSymbols
function createWindowModal() {
    Desktop.createWindow({
        width: 300,
        icon: "<span class='mif-cogs'></span>",
        title: "Modal window",
        content: "<div class='p-2'>Example</div>",
        overlay: true,
        //overlayColor: "transparent",
        modal: true,
        place: "center",
        onShow: function (win) {
            win.addClass("ani-swoopInTop");
            setTimeout(function () {
                win.removeClass("ani-swoopInTop");
            }, 1000);
        },
        onClose: function (win) {
            win.addClass("ani-swoopOutTop");
        }
    });
}

// noinspection JSUnusedGlobalSymbols
function createWindowYoutube() {
    Desktop.createWindow({
        width: 500,
        icon: "<span class='mif-youtube'></span>",
        title: "Youtube video",
        content: "https://youtu.be/S9MeTn1i72g",
        clsContent: "bg-dark"
    });
}
