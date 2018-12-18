let myDiagram;
let myPaletteBasic;
let myPaletteGeneral;

function setup(divIdSuffix) {
    if (fullView) {
        $("#infoDraggable").show();
        $("#controlsDraggable").show();
        $("#dataModelDraggable").show();
    } else {
        $("#infoDraggable").hide();
        $("#controlsDraggable").hide();
        $("#dataModelDraggable").hide();
    }
    const items = [];
    let i = 0;
    let activeText;
    for (const type in diagramTypes) {
        const active = diagramTypes[type];
        if (active) {
            items.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="/editor?type=' + type + '" class="active">' + type + '</a></li>');
            activeText = type;
        } else {
            items.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="/editor?type=' + type + '">' + type + '</a></li>');
        }
        i++;
    }
    const $diagramTypesDropdown = $("#diagramTypesDropdown");
    $diagramTypesDropdown.append(items.join(''));
    $diagramTypesDropdown.on('click', 'a', function () {
        const text = $(this).html();
        $(this).closest('.dropdown').find('.dropdown-toggle').html(text);
    });

    const elementTypeItems = [];

    elementTypes.forEach(function (elementType, i) {
        elementTypeItems.push('<li role="presentation" data-isGroup="' + elementType.group + '"><a role="menuitem" tabindex="' + i + '" href="#">' + elementType.type + '</a></li>');
    });
    const elementTypesDropdown = $("#elementTypesDropdown");
    elementTypesDropdown.append(elementTypeItems.join(''));
    elementTypesDropdown.on('click', 'a', function () {
        const text = $(this).html();
        $(this).closest('.dropdown').find('.dropdown-toggle').html(text);
    });
    if ($("#viewMode").length > 0) {
        viewMode = new Slider("#viewMode", {
            id: "viewMode",
            ticks: [1, 2, 3],
            ticks_labels: ['Compact', 'Moderated', 'Full'],
            tooltip: "hide",
            min: 1,
            max: 3,
            step: 1,
            value: 3
        });
        viewMode.on('slideStop', reexpand);
    }
    const $element1 = $("#element-image");
    $element1.on("change", handleImageSelect);
    const $element2 = $("#element-image-2");
    $element2.on("change", handleImageSelect);

    const $sidebar = $('#sidebar');
    $('#sidebarCollapse').on('click', function () {
        if (!$sidebar.hasClass('active')) {
            $sidebar.addClass('active');
            $('a[aria-expanded=true]').attr('aria-expanded', 'false');
        } else {
            $sidebar.removeClass('active');
        }
        $(this).find('i').toggleClass('glyphicon-menu-left').toggleClass('glyphicon-menu-right');
    });

    init(divIdSuffix);
}

$().ready(function () {
    if (!isDesktop) {
        setup();
    }
});

$.fn.extend({
    treed: function (o) {
        let openedClass = 'glyphicon-minus-sign';
        let closedClass = 'glyphicon-plus-sign';

        if (typeof o !== 'undefined'){
            if (typeof o.openedClass !== 'undefined'){
                openedClass = o.openedClass;
            }
            if (typeof o.closedClass !== 'undefined'){
                closedClass = o.closedClass;
            }
        }

        //initialize each of the top levels
        const tree = $(this);
        tree.addClass("tree");
        tree.find('li').has("ul").each(function () {
            const branch = $(this); //li with children ul
            branch.prepend("<i class='indicator glyphicon " + closedClass + "'></i>");
            branch.addClass('branch');
            branch.on('click', function (e) {
                if (this === e.target) {
                    const icon = $(this).children('i:first');
                    icon.toggleClass(openedClass + " " + closedClass);
                    $(this).children().children().toggle();
                }
            });
            branch.children().children().toggle();
        });
        //fire event from the dynamically added icon
        tree.find('.branch .indicator').each(function(){
            $(this).on('click', function () {
                $(this).closest('li').click();
            });
        });
        //fire event to open branch if the li contains an anchor instead of text
        tree.find('.branch>a').each(function () {
            $(this).on('click', function (e) {
                $(this).closest('li').click();
                e.preventDefault();
            });
        });
        //fire event to open branch if the li contains a button instead of text
        tree.find('.branch>button').each(function () {
            $(this).on('click', function (e) {
                $(this).closest('li').click();
                e.preventDefault();
            });
        });
    }
});

