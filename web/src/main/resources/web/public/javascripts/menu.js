var head = document.getElementsByTagName("head")[0];

var link = document.createElement("link");
link.type = "text/css";
link.rel = "stylesheet";
link.href = "/stylesheets/bootstrap.min.css";
head.appendChild(link);

link = document.createElement("link");
link.type = "text/css";
link.rel = "stylesheet";
link.href = "/stylesheets/highlight.css";
head.appendChild(link);

link = document.createElement("link");
link.type = "text/css";
link.rel = "stylesheet";
link.href = "/stylesheets/main.css";
head.appendChild(link);

function showMenu() {

    var container = document.createElement('div');
    container.className = "container-fluid";
    document.body.appendChild(container);

    // sample content
    var diagramsCanvasDiv = document.getElementById('diagramsCanvas') || document.body.firstChild;
    diagramsCanvasDiv.className = "col-md-10";
    container.appendChild(diagramsCanvasDiv);

    // side navigation
    var navindex = document.createElement('div');
    navindex.id = "navindex";
    navindex.className = "col-md-2";
    navindex.innerHTML = mySampleMenu;
    container.insertBefore(navindex, diagramsCanvasDiv);

    // when the page loads, change the class of navigation LI's
    var url = window.location.href;
    var lindex = url.lastIndexOf('/');
    url = url.slice(lindex + 1).toLowerCase();  // include "/" to avoid matching prefixes
    var lis = document.getElementById("sections").getElementsByTagName("li");
    var l = lis.length;
    var listed = false;
    for (var i = 0; i < l; i++) {
        var anchor = lis[i].childNodes[0];
        var split = anchor.getAttribute("resource").split('/').pop().split('.');
        var imgname = split[0];
        if (imgname === "index" || imgname === "all") continue;
        var imgtype = split[1];
        if (imgtype === "js") continue;
        var span = document.createElement('span');
        span.className = "samplespan";
        var img = document.createElement('img');
        img.height = "200";
        img.src = "/images/screenshots/" + imgname + ".png";
        span.appendChild(img);
        anchor.appendChild(span);
        if (!anchor.href) continue;
        var lowerhref = anchor.href.toLowerCase();
        if (!listed && lowerhref.indexOf('/' + url) !== -1) {
            anchor.className = "selected";
            listed = true;
        }
    }
    if (!listed) {
        lis[lis.length - 1].childNodes[0].className = "selected";
    }

}

var mySampleMenu = '\
  <div class="sidebar-nav">\
    <div class="navbar navbar-default" role="navigation">\
      <div class="navbar-header" style="width: 100%;">\
        <div class="navheader-container">\
          <div class="navheader-collapse" data-toggle="collapse" data-target="#DiagramNavbarExamples">\
  		    <span class="navbar-brand" class="navbar-toggle" data-toggle="collapse" data-target="#DiagramNavbarExamples">Ejemplos</span>\
          </div>\
        </div>\
      </div>\
      <div id="DiagramNavbarExamples" class="collapse">\
        <ul id="sections" class="classList nav navbar-nav">\
          <li><a onclick="openContent(this);" resource="/diagrams/basic.html">Basic</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/checkBoxes.html">CheckBoxes</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/ColumnResizing.html">Column Resizing</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/comments.html">Comments</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/contextMenu.html">Context Menu</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/DataInspector.html">Data Inspector</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/DragCreating.html">Drag Creating</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/draggableLink.html">Draggable Link</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/entityRelationship.html">Entity Relationship</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/flowchart.html">Flowchart</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/gantt.html">Gantt</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/grouping.html">Grouping</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/GuidedDragging.html">Guided Dragging</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/htmlInteraction.html">HTML Interaction</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/icons.html">SVG Icons</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/kanban.html">Kanban Board</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/logicCircuit.html">Logic Circuit</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/mindMap.html">Mind Map</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/navigation.html">Navigation</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/orgChartStatic.html">OrgChart (Static)</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/processFlow.html">Process Flow</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/records.html">Record Mapper</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/sequenceDiagram.html">Sequence Diagram</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/shapes.html">Shapes</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/shopFloorMonitor.html">Shop Floor Monitor</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimBands.html">Layer Bands</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimLanes.html">Swim Lanes</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/Table.html">Table Layout</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/umlClass.html">UML Class</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/updateDemo.html">Update Demo</a></li>\
        </ul>\
      </div>\
    </div>\
	<div class="navbar navbar-default" role="navigation">\
      <div class="navbar-header" style="width: 100%;">\
        <div class="navheader-container">\
          <div class="navheader-collapse">\
  		    <span class="navbar-brand" class="navbar-toggle" data-toggle="collapse" data-target="#DiagramNavbarModels">Modelos</span>\
          </div>\
        </div>\
      </div>\
      <div id="DiagramNavbarModels" class="collapse">\
        <ul id="sections" class="classList nav navbar-nav">\
          <li><a href="#">Ejemplo Modelo 1</a></li>\
          <li><a href="#">Ejemplo Modelo 2</a></li>\
          <li><a href="#">Ejemplo Modelo 3</a></li>\
        </ul>\
      </div>\
    </div>\
	<div class="navbar navbar-default" role="navigation">\
      <div class="navbar-header" style="width: 100%;">\
        <div class="navheader-container">\
          <div class="navheader-collapse">\
  		    <span class="navbar-brand" class="navbar-toggle" data-toggle="collapse" data-target="#DiagramNavbarPrototypes">Prototipos</span>\
          </div>\
        </div>\
      </div>\
      <div id="DiagramNavbarPrototypes" class="collapse">\
        <ul id="sections" class="classList nav navbar-nav">\
          <li><a href="#">Ejemplo Prototipo 1</a></li>\
          <li><a href="#">Ejemplo Prototipo 2</a></li>\
        </ul>\
      </div>\
    </div>\
  </div>';

function openContent(element) {
    var url = element.getAttribute("resource");
    $.ajax({
        url: url,
        beforeSend: function (xhr) {
            xhr.overrideMimeType("text/html; charset=utf-8");
        }
    }).done(function (data) {
        $("#diagramsCanvas").html(data);
    });
}