package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import org.apache.commons.collections4.IterableUtils;
import org.pac4j.core.profile.CommonProfile;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.RouteGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.buildRoute;
import static com.araguacaima.open_archi.web.common.Commons.clients;
import static com.araguacaima.open_archi.web.common.Commons.getProfiles;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static spark.Spark.*;

public class AdminGroup implements RouteGroup {

    public static final String PATH = "/admin";

    private Admin admin = new Admin();

    @Override
    public void addRoutes() {
        before("/*", OpenArchi.adminSecurityFilter);
        get(Commons.EMPTY_PATH, buildRoute(new BeanBuilder().title("Open-Archi Admin"), OpenArchi.PATH + "/admin"), engine);
        before(Admin.PATH, OpenArchi.adminSecurityFilter);
        path(Admin.PATH, admin);
        path(Admin.PATH + Commons.SEPARATOR_PATH, admin);
    }

}
