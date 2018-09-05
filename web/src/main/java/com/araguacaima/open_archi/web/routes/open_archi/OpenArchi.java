package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.commons.IdName;
import com.araguacaima.open_archi.persistence.diagrams.architectural.Palette;
import com.araguacaima.open_archi.persistence.diagrams.core.*;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.APIFilter;
import com.araguacaima.open_archi.web.AdminAPIFilter;
import com.araguacaima.open_archi.web.ConfigFactory;
import com.araguacaima.open_archi.web.common.Authentication;
import com.araguacaima.open_archi.web.common.Commons;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.DefaultAuthorizers;
import org.pac4j.sparkjava.CallbackRoute;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static com.araguacaima.open_archi.web.Server.*;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.JWT_SALT;

public class OpenArchi {

    public static final String PATH = "/open-archi";
    final static Config config = new ConfigFactory(JWT_SALT, engine).build(serverName, assignedPort, PATH, clients);
    final static CallbackRoute callback = new CallbackRoute(config, null, true);
    public static Root root = new Root();
    static Filter basicSecurityFilter = Authentication.buildBasicSecurityFilter(config);
    static Filter strongSecurityFilter = Authentication.buildStrongSecurityFilter(config);
    static Filter adminSecurityFilter = Authentication.buildAdminSecurityFilter(config);
    static Filter adminApiFilter = new AdminAPIFilter(config, clients, "adminAuthorizer,custom," + DefaultAuthorizers.ALLOW_AJAX_REQUESTS + "," + DefaultAuthorizers.IS_AUTHENTICATED);
    static Filter apiFilter = new APIFilter(config, clients, "checkHttpMethodAuthorizer,requireAnyRoleAuthorizer,custom," + DefaultAuthorizers.ALLOW_AJAX_REQUESTS + "," + DefaultAuthorizers.IS_AUTHENTICATED);

    public static void fixCompositeFromItem(Item object) {
        Set<Item> items = reflectionUtils.extractByType(object, Item.class);
        Set<CompositeElement> composites = reflectionUtils.extractByType(object, CompositeElement.class);
        items.forEach(item -> {
            String id = item.getId();
            String key = item.getKey();
            composites.forEach(composite -> {
                if (composite.getId().equals(key)) {
                    composite.setId(id);
                    String link = composite.getLink();
                    if (StringUtils.isNotBlank(link)) {
                        link = link.replaceAll(key, id);
                    } else {
                        link = Models.PATH + Commons.SEPARATOR_PATH + id;
                    }
                    composite.setLink(link);
                }
            });
        });
    }

    static Palette getArchitecturePalette() {
        Palette palette = new Palette();
        List<Item> models;
        models = JPAEntityManagerUtils.executeQuery(Item.class, Item.GET_ALL_PROTOTYPES);
        int rank = 3;
        if (models != null) {
            for (Item model : models) {
                palette.addElement(buildPalette(rank, model));
                rank++;
            }
        }
        return palette;
    }

    private static PaletteItem buildPalette(int rank, Item model) {
        PaletteItem item = new PaletteItem();
        item.setId(model.getId());
        item.setRank(rank);
        item.setKind(ElementKind.ARCHITECTURE_MODEL);
        item.setName(model.getName());
        Shape shape = model.getShape();
        item.setShape(shape);
        item.setPrototype(model.isPrototype());
        return item;
    }


    static List<ElementShape> getElementTypes() {
        return JPAEntityManagerUtils.executeQuery(ElementShape.class, ElementShape.GET_ALL_ELEMENT_SHAPES);
    }

    public static Object getItemNames(Request request, Response response, String query) throws IOException, URISyntaxException {
        return getItemNames(request, response, query, null);
    }

    public static Object getItemNames(Request request, Response response, String query, String contentType) throws IOException, URISyntaxException {
        String diagramNames = (String) getList(request, response, query, null, IdName.class);
        List diagramNamesList = getListIdName(diagramNames);
        return getList(request, response, diagramNamesList, contentType);
    }


}
