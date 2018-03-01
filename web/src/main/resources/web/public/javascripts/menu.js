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
            $("#sectionsModels").append("<li><a onclick=\"openLoadedModel('/open-archi/api/models/" + model.id + "');\">" + model.name + "</a></li>");
        });

    });

    $.ajax({
        url: "/open-archi/api/catalogs/prototype-names",
        beforeSend: function (xhr) {
            xhr.overrideMimeType("application/json; charset=utf-8");
        }
    }).done(function (data) {
        data.forEach(function (model) {
            $("#sectionsPrototype").append("<li><a onclick=\"openLoadedModel('/open-archi/api/models/" + model.id + "');\">" + model.name + "</a></li>");
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
          <li><a onclick="openContent(this);" resource="/diagrams/basic.html">Diagrama Básico</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/checkBoxes.html">Features (checkbox)</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/columnResizing.html">Ajuste de tamaños</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/comments.html">Comentarios</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/contextMenu.html">Menú Contextual</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/dataInspector.html">Meta Datos</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/dragCreating.html">Creación Ágil</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/draggableLink.html">Constraints</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/entityRelationship.html">Entidad Relación</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/flowchart.html">Flujo de Secuencia</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/gantt.html">Diagramas Gantt</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/grouping.html">Expansión</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/guidedDragging.html">Guías visuales</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/htmlInteraction.html">Interoperatividad HTML</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/icons.html">Iconos SVG</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/kanban.html">Tablero Kanban</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/logicCircuit.html">Flujo y Secuencia 1</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/mindMap.html">Mapas Estratégico</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/navigation.html">Seguimiento de Flujos</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/orgChartStatic.html">Zooming</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/records.html">Mapeo de Features</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/sequenceDiagram.html">UML de Secuencia</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/shopFloorMonitor.html">Flujo y Secuencia 2</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimBands.html">Release Planning</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/swimLanes.html">Diagrama de Procesos</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/umlClass.html">UML de Clases</a></li>\
          <li><a onclick="openContent(this);" resource="/diagrams/updateDemo.html">Actualización Realtime</a></li>\
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