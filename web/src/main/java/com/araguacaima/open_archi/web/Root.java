package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.web.common.Authentication;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.sparkjava.LogoutRoute;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.*;
import static com.araguacaima.open_archi.web.common.Authentication.form;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.forceLogin;
import static com.araguacaima.open_archi.web.OpenArchi.config;
import static spark.Spark.*;

public class Root implements RouteGroup {

    public static Samples samples = new Samples();
    public static Api api = new Api();
    public static Editor editor = new Editor();
    public static Prototyper prototyper = new Prototyper();
    public static Admin admin = new Admin();

    @Override
    public void addRoutes() {
        get(StringUtils.EMPTY, buildRoute(new BeanBuilder().title(OPEN_ARCHI), "/home"), engine);
        get("/desktop", buildRoute(new BeanBuilder().title(OPEN_ARCHI), "/desktop/home"), engine);
/*        before("/login/google", OpenArchi.scopesFilter);
        get("/login/google", Authentication.authGoogle, engine);
        get("/login", Authentication.login, engine);
        get("/callback", OpenArchi.callback);
        post("/callback", (req, res) -> {
            store(req, res);
            return OpenArchi.callback.handle(req, res);
        });*/

        path(Samples.PATH, samples);
        path(Api.PATH, api);
        path(Editor.PATH, editor);
        path(Prototyper.PATH, prototyper);
        path(Admin.PATH, admin);

        /*final LogoutRoute localLogout = new LogoutRoute(config, "/");
        localLogout.setDestroySession(true);
        localLogout.setLocalLogout(false);
        localLogout.setCentralLogout(true);
        get("/logout", localLogout);
        final LogoutRoute centralLogout = new LogoutRoute(config);
        centralLogout.setDefaultUrl("http://" + deployedServer + "");
        centralLogout.setLogoutUrlPattern("http://" + deployedServer + "/.*");
        centralLogout.setLocalLogout(false);
        centralLogout.setCentralLogout(true);
        centralLogout.setDestroySession(true);
        get("/central-logout", centralLogout);
        get("/force-login", (rq, rs) -> forceLogin(config, rq, rs));

        get("/jwt", Authentication::jwt, engine);
        get("/login", (rq, rs) -> form(OpenArchi.config), engine);*/


    }

}
