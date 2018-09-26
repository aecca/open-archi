let myDiagram;
let myPaletteBasic;
let myPaletteGeneral;
$().ready(function () {
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
    i = 0;
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

    init();
});