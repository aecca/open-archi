var Desktop = {
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
        var icon = wnd.getIcon();
        var wID = wnd.win.attr("id");
        var item = $("<span>").addClass("task-bar-item started").html(icon);

        item.data("wID", wID);

        item.appendTo($(this.options.taskBar));
    },

    removeFromTaskBar: function (wnd) {
        console.log(wnd);
        var wID = wnd.attr("id");
        var items = $(".task-bar-item");
        var that = this;
        $.each(items, function () {
            var item = $(this);
            if (item.data("wID") === wID) {
                delete that.wins[wID];
                item.remove();
            }
        })
    },

    createWindow: function (o) {
        var that = this;
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
        var w = $("<div>").appendTo($(this.options.windowArea));
        var wnd = w.window(o).data("window");

        var win = wnd.win;
        var shift = Metro.utils.objectLength(this.wins) * 16;

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


const w_icons = [
    'rocket', 'apps', 'cog', 'anchor'
];
const w_titles = [
    'rocket', 'apps', 'cog', 'anchor'
];

function createWindow(content) {
    const index = Metro.utils.random(0, 3);
    if (content !== undefined) {
        $.ajax({
            url: basePath + "/desktop/" + content,
            type: 'GET',
            crossDomain: true,
            contentType: "text/html"
        }).done((data, textStatus, response) => {
            Desktop.createWindow({
                width: 300,
                icon: "<span class='mif-" + w_icons[index] + "'></span>",
                title: w_titles[index],
                content: "<div class='p-2'>" + data + "</div>"
            });
        });
    } else {
        Desktop.createWindow({
            width: 300,
            icon: "<span class='mif-" + w_icons[index] + "'></span>",
            title: w_titles[index],
            content: "<div class='p-2'>content_</div>"
        });
    }
}

function createWindowModal() {
    Desktop.createWindow({
        width: 300,
        icon: "<span class='mif-cogs'></span>",
        title: "Modal window",
        content: "<div class='p-2'>This is desktop demo created with Metro 4 Components Library</div>",
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
