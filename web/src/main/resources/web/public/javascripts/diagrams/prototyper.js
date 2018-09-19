let myDiagram;
let myPaletteBasic;
let myPaletteGeneral;
$(function () {
    if (fullView) {
        $("#infoDraggable").show();
        $("#controlsDraggable").show();
        $("#dataModelDraggable").show();
    } else {
        $("#infoDraggable").hide();
        $("#controlsDraggable").hide();
        $("#dataModelDraggable").hide();
    }
    init();
    var items = [];
    var i = 0;
    var activeText;
    for (var type in diagramTypes) {
        var active = diagramTypes[type];
        if (active) {
            items.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="/editor?type=' + type + '" class="active">' + type + '</a></li>');
            activeText = type;
        } else {
            items.push('<li role="presentation"><a role="menuitem" tabindex="' + i + '" href="/editor?type=' + type + '">' + type + '</a></li>');
        }
        i++;
    }
    var $diagramTypesDropdown = $("#diagramTypesDropdown");
    $diagramTypesDropdown.append(items.join(''));
    $diagramTypesDropdown.on('click', 'a', function () {
        var text = $(this).html();
        var htmlText = text;
        $(this).closest('.dropdown').find('.dropdown-toggle').html(htmlText);
    });

    var elementTypeItems = [];
    i = 0;
    elementTypes.forEach(function (elementType, i) {
        elementTypeItems.push('<li role="presentation" data-isGroup="' + elementType.group + '"><a role="menuitem" tabindex="' + i + '" href="#">' + elementType.type + '</a></li>');
    });
    var elementTypesDropdown = $("#elementTypesDropdown");
    elementTypesDropdown.append(elementTypeItems.join(''));
    elementTypesDropdown.on('click', 'a', function () {
        var text = $(this).html();
        var htmlText = text;
        $(this).closest('.dropdown').find('.dropdown-toggle').html(htmlText);
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
    var $element1 = $("#element-image");
    $element1.on("change", handleImageSelect);
    var $element2 = $("#element-image-2");
    $element2.on("change", handleImageSelect);

    $('#sidebarCollapse').on('click', function () {
        const $sidebar = $('#sidebar');
        if (!$sidebar.hasClass('active')) {
            $sidebar.addClass('active');
            $('.collapse.in').toggleClass('in');
            $('a[aria-expanded=true]').attr('aria-expanded', 'false');
        } else {
            $sidebar.removeClass('active');
        }
    });

    $("#paletteContainerBasic").ready(function () {
        resizePalete("paletteContainerBasic", myPaletteBasic);
    });

    $("#paletteContainerGeneral").ready(function () {
        resizePalete("paletteContainerGeneral", myPaletteGeneral);
    });
});