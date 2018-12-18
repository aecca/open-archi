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
        const item = $("<span>").addClass("task-bar-item started").html(icon);

        item.data("wID", wID);

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
        const that = this;
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
        const w = $("<div>").appendTo($(this.options.windowArea));
        const wnd = w.window(o).data("window");

        var win = wnd.win;
        const shift = Metro.utils.objectLength(this.wins) * 16;

        if (wnd.options.place === "auto" && wnd.options.top === "auto" && wnd.options.left === "auto") {
            win.css({
                top: shift,
                left: shift
            });
        }
        this.wins[win.attr("id")] = wnd;
        this.addToTaskBar(wnd);
        w.remove();
    }
};

Desktop.setup();

function createApiWindow() {
    const content = "<div class='container'><div id='swagger-ui'></div>";
    Desktop.createWindow({
        width: "100%",
        height: "auto",
        icon: "<img src=\"" + basePath + "/images/open-archi-api-logo.png\" style=\"display: inherit;\" class=\"img-responsive img-rounded\">",
        title: "OpenArchi Api",
        content: "<div class='p-2'>" + content + "</div>"
    });
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

function createPrototyperWindow() {
    const index = Metro.utils.random(0, 3);
    const content = "<div class='container'><div id='swagger-ui'></div>";
    Desktop.createWindow({
        width: "100%",
        height: "auto",
        icon: "<img src=\"" + basePath + "/images/open-archi-prototyper-logo.png\" style=\"display: inherit;\" class=\"img-responsive img-rounded\">",
        title: "OpenArchi Prototyper",
        content: "<div class='p-2'>" + content + "</div>"
    });
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

function createWindowYoutube() {
    Desktop.createWindow({
        width: 500,
        icon: "<span class='mif-youtube'></span>",
        title: "Youtube video",
        content: "https://youtu.be/S9MeTn1i72g",
        clsContent: "bg-dark"
    });
}
