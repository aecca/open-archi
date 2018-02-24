// Load necessary scripts:
if (window.require) {
  // declare required libraries and ensure Bootstrap's dependency on jQuery
  require.config({
    paths: {
      "jquery": "./jquery-3.3.1.min"
    },
    shim: {
      "bootstrap": ["jquery"]
    }
  });
  require(["jquery"], function() {});
} else {
  function goLoadSrc(filenames) {
    var scripts = document.getElementsByTagName("script");
    var script = null;
    for (var i = 0; i < scripts.length; i++) {
      if (scripts[i].src.indexOf("menu") > 0) {
        script = scripts[i];
        break;
      }
    }
    for (var i = 0; i < arguments.length; i++) {
      var filename = arguments[i];
      if (!filename) continue;
      var selt = document.createElement("script");
      selt.async = false;
      selt.defer = false;
      selt.src = "/javascripts/" + filename;
      script.parentNode.insertBefore(selt, script.nextSibling);
      script = selt;
    }
  }
  goLoadSrc("highlight.js", (window.jQuery ? "" : "jquery-3.3.1.min.js"), "bootstrap.min.js");
}

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
  // determine if it's an extension
  var isExtension = (location.pathname.split('/').slice(-2)[0].indexOf("extensions") >= 0);
  var isTS = (location.pathname.split('/').slice(-2)[0].indexOf("TS") > 0);

  // save the body for goViewSource() before we modify it
  window.bodyHTML = document.body.innerHTML;
  window.bodyHTML = window.bodyHTML.replace(/</g, "&lt;");
  window.bodyHTML = window.bodyHTML.replace(/>/g, "&gt;");

  // wrap the sample div and sidebar in a fluid container
  var container = document.createElement('div');
  container.className = "container-fluid";
  document.body.appendChild(container);

  // sample content
  var samplediv = document.getElementById('sample') || document.body.firstChild;
  samplediv.className = "col-md-10";
  container.appendChild(samplediv);

  // side navigation
  var navindex = document.createElement('div');
  navindex.id = "navindex";
  navindex.className = "col-md-2";
  navindex.innerHTML = mySampleMenu;
  container.insertBefore(navindex, samplediv);

  // when the page loads, change the class of navigation LI's
  var url = window.location.href;
  var lindex = url.lastIndexOf('/');
  url = url.slice(lindex+1).toLowerCase();  // include "/" to avoid matching prefixes
  var lis = document.getElementById("sections").getElementsByTagName("li");
  var l = lis.length;
  var listed = false;
  for (var i = 0; i < l; i++) {
    var anchor = lis[i].childNodes[0];
    // ....../samples/X.html becomes X.html becomes X
    var split = anchor.href.split('/').pop().split('.');
    var imgname = split[0];
    if (imgname === "index" || imgname === "all") continue;
    var imgtype = split[1];
    if (imgtype === "js") continue;
    var span = document.createElement('span');
    span.className = "samplespan";
    var img = document.createElement('img');
    img.height = "200";
    img.src = "../images/screenshots/" + imgname + ".png";
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
    lis[lis.length -1].childNodes[0].className = "selected";
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
          <li><a href="basic.html">Basic</a></li>\
          <li><a href="shapes.html">Shapes</a></li>\
          <li><a href="icons.html">SVG Icons</a></li>\
          <li><a href="navigation.html">Navigation</a></li>\
          <li><a href="orgChartStatic.html">OrgChart (Static)</a></li>\
          <li><a href="mindMap.html">Mind Map</a></li>\
          <li><a href="entityRelationship.html">Entity Relationship</a></li>\
          <li><a href="gantt.html">Gantt</a></li>\
          <li><a href="shopFloorMonitor.html">Shop Floor Monitor</a></li>\
          <li><a href="grouping.html">Grouping</a></li>\
          <li><a href="swimBands.html">Layer Bands</a></li>\
          <li><a href="swimLanes.html">Swim Lanes</a></li>\
          <li><a href="umlClass.html">UML Class</a></li>\
          <li><a href="flowchart.html">Flowchart</a></li>\
          <li><a href="processFlow.html">Process Flow</a></li>\
          <li><a href="kanban.html">Kanban Board</a></li>\
          <li><a href="sequenceDiagram.html">Sequence Diagram</a></li>\
          <li><a href="logicCircuit.html">Logic Circuit</a></li>\
          <li><a href="records.html">Record Mapper</a></li>\
          <li><a href="draggableLink.html">Draggable Link</a></li>\
          <li><a href="updateDemo.html">Update Demo</a></li>\
          <li><a href="htmlInteraction.html">HTML Interaction</a></li>\
          <li><a href="customContextMenu.html">Context Menu</a></li>\
          <li><a href="comments.html">Comments</a></li>\
          <li><a href="Table.html">Table Layout</a></li>\
          <li><a href="DragCreating.html">Drag Creating</a></li>\
          <li><a href="GuidedDragging.html">Guided Dragging</a></li>\
          <li><a href="ColumnResizing.html">Column Resizing</a></li>\
          <li><a href="DataInspector.html">Data Inspector</a></li>\
          <li><a href="CheckBoxes.html">CheckBoxes</a></li>\
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