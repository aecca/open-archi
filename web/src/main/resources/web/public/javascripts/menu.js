const head = document.getElementsByTagName("head")[0];

let link = document.createElement("link");
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

    const container = document.createElement('div');
    container.className = "container-fluid";
    document.body.appendChild(container);

    // sample content
    const diagramsCanvasDiv = document.getElementById('diagramsCanvas') || document.body.firstChild;
    diagramsCanvasDiv.className = "col-md-10";
    container.appendChild(diagramsCanvasDiv);

    // side navigation
    const navindex = document.createElement('div');
    navindex.id = "navindex";
    navindex.className = "col-md-2";
    navindex.innerHTML = mySampleMenu;
    container.insertBefore(navindex, diagramsCanvasDiv);

    // when the page loads, change the class of navigation LI's
    let url = window.location.href;
    const lindex = url.lastIndexOf('/');
    url = url.slice(lindex + 1).toLowerCase();  // include "/" to avoid matching prefixes
    const lis = document.getElementById("sectionsExamples").getElementsByTagName("li");
    const l = lis.length;
    for (let i = 0; i < l; i++) {
        const anchor = lis[i].childNodes[0];
        const split = anchor.getAttribute("resource").split('/').pop().split('.');
        const imgname = split[0];
        if (imgname === "index" || imgname === "all") continue;
        const imgtype = split[1];
        if (imgtype === "js") continue;
        const span = document.createElement('span');
        span.className = "samplespan";
        const img = document.createElement('img');
        img.height = "200";
        img.src = "/images/screenshots/" + imgname + ".png";
        span.appendChild(img);
        anchor.appendChild(span);
    }

    $.ajax({
        url: "/open-archi/api/catalogs/diagram-names",
        beforeSend: function (xhr) {
            xhr.overrideMimeType("application/json; charset=utf-8");
        }
    }).done(function (data) {
        data.forEach(function (model) {
            $("#sectionsModels").append("<li><a onclick=\"openUrlContent('/open-archi/api/models/' + model.id);\">" + model.name + "</a></li>");
        });

    });

    $.ajax({
        url: "/open-archi/api/catalogs/prototype-names",
        beforeSend: function (xhr) {
            xhr.overrideMimeType("application/json; charset=utf-8");
        }
    }).done(function (data) {
        data.forEach(function (model) {
            $("#sectionsPrototype").append("<li><a onclick=\"openUrlContent('/open-archi/api/models/' + model.id);\">" + model.name + "</a></li>");
        });
    });
}

let mySampleMenu = '\
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
        <ul id="sectionsExamples" class="classList nav navbar-nav">\
          <li><a onclick="openContent(this);" resource="/diagrams/basic.html">Basic</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/checkBoxes.html">CheckBoxes</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/columnResizing.html">Column Resizing</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/comments.html">Comments</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/contextMenu.html">Context Menu</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/dataInspector.html">Data Inspector</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/dragCreating.html">Drag Creating</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/draggableLink.html">Draggable Link</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/entityRelationship.html">Entity Relationship</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/flowchart.html">Flowchart</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/gantt.html">Gantt</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/grouping.html">Grouping</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/guidedDragging.html">Guided Dragging</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/htmlInteraction.html">HTML Interaction</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/icons.html">SVG Icons</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/kanban.html">Kanban Board</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/logicCircuit.html">Logic Circuit</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/mindMap.html">Mind Map</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/navigation.html">Navigation</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/orgChartStatic.html">OrgChart (Static)</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/records.html">Record Mapper</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/sequenceDiagram.html">Sequence Diagram</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/shopFloorMonitor.html">Shop Floor Monitor</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimBands.html">Layer Bands</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimLanes.html">Swim Lanes</a></li>\
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
        <ul id="sectionsModels" class="classList nav navbar-nav">\
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
        <ul id="sectionsPrototype" class="classList nav navbar-nav">\
        </ul>\
      </div>\
    </div>\
  </div>';