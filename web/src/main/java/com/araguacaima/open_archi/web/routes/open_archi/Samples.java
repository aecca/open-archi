package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.ExampleData;
import com.araguacaima.open_archi.web.common.Commons;
import spark.Redirect;
import spark.RouteGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.buildRoute;
import static spark.Spark.get;
import static spark.Spark.redirect;

public class Samples implements RouteGroup {

    public static final String PATH = "/samples";

    public static Collection<ExampleData> getExamples() {
        Collection<ExampleData> result = new ArrayList<>();
        result.add(new ExampleData("/diagrams/checkBoxes.html", "Features (checkbox)"));
        result.add(new ExampleData("/diagrams/columnResizing.html", "Ajuste de tamaños"));
        result.add(new ExampleData("/diagrams/comments.html", "Comentarios"));
        result.add(new ExampleData("/diagrams/dragCreating.html", "Creación Ágil"));
        result.add(new ExampleData("/diagrams/draggableLink.html", "Constraints"));
        result.add(new ExampleData("/diagrams/entityRelationship.html", "Entidad Relación"));
        result.add(new ExampleData("/diagrams/flowchart.html", "Flujo de Secuencia"));
        result.add(new ExampleData("/diagrams/gantt.html", "Diagramas Gantt"));
        result.add(new ExampleData("/diagrams/grouping.html", "Expansión"));
        result.add(new ExampleData("/diagrams/regrouping.html", "Re-agrupación"));
        result.add(new ExampleData("/diagrams/guidedDragging.html", "Guías visuales"));
        result.add(new ExampleData("/diagrams/icons.html", "Iconos SVG"));
        result.add(new ExampleData("/diagrams/kanban.html", "Tablero Kanban"));
        result.add(new ExampleData("/diagrams/logicCircuit.html", "Flujo y Secuencia 1"));
        result.add(new ExampleData("/diagrams/mindMap.html", "Mapas Estratégicos"));
        result.add(new ExampleData("/diagrams/navigation.html", "Seguimiento de Flujos"));
        result.add(new ExampleData("/diagrams/orgChartStatic.html", "Zooming"));
        result.add(new ExampleData("/diagrams/records.html", "Mapeo de Features"));
        result.add(new ExampleData("/diagrams/sequenceDiagram.html", "UML de Secuencia"));
        result.add(new ExampleData("/diagrams/shopFloorMonitor.html", "Flujo y Secuencia 2"));
        result.add(new ExampleData("/diagrams/swimBands.html", "Release Planning"));
        result.add(new ExampleData("/diagrams/swimLanes.html", "Diagrama de Procesos"));
        result.add(new ExampleData("/diagrams/umlClass.html", "UML de Clases"));
        result.add(new ExampleData("/diagrams/updateDemo.html", "Actualización Realtime"));
        return result;
    }

    @Override
    public void addRoutes() {
        final List nodeDataArray = new ArrayList<BeanBuilder>() {{
            add(new BeanBuilder().key("1").text("Alpha").color("lightblue"));
            add(new BeanBuilder().key("2").text("Beta").color("orange"));
            add(new BeanBuilder().key("3").text("Gamma").color("lightgreen").group("5"));
            add(new BeanBuilder().key("4").text("Delta").color("pink").group("5"));
            add(new BeanBuilder().key("5").text("Epsilon").color("green").isGroup(true));
        }};
        final List linkDataArray = new ArrayList<BeanBuilder>() {{
            add(new BeanBuilder().from("1").to("2").color("blue"));
            add(new BeanBuilder().from("2").to("2"));
            add(new BeanBuilder().from("3").to("4").color("green"));
            add(new BeanBuilder().from("3").to("1").color("purple"));
        }};

        List<String> steps = new ArrayList<>();
        steps.add("Con doble-click en cualquier área vacía del canvas se crea un nuevo componente (siempre será una cajita)");
        steps.add("Con doble-click en cualquier componente (cajita) se editará su nombre");
        steps.add("Al hacer click en el borde de un componente se puede crear conectores (flechas) hacia cualquier componente");
        BeanBuilder bean = new BeanBuilder()
                .nodeDataArray(nodeDataArray)
                .linkDataArray(linkDataArray)
                .source("basic")
                .mainTitle("Propuesta para diagrama básico de componentes - Primer nivel")
                .caption("¡Leyendo ya desde Open Archi!")
                .fullDescription("Sencillo, pero fácil de adaptar para construir modelos de solución a alto nivel. Intuitivo y fácil de usar.")
                .steps(steps);
        get("/basic", buildRoute(bean, "editor"), engine);
    }

}
