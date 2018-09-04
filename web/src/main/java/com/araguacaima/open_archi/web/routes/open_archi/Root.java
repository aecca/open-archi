package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Authentication;
import org.pac4j.sparkjava.LogoutRoute;
import spark.Redirect;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.*;
import static com.araguacaima.open_archi.web.common.Authentication.form;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Security.forceLogin;
import static com.araguacaima.open_archi.web.routes.open_archi.OpenArchi.config;
import static spark.Spark.*;

public class Root implements RouteGroup {

    public static Samples samples = new Samples();
    public static Api api = new Api();
    public static Editor editor = new Editor();
    public static Prototyper prototyper = new Prototyper();

    @Override
    public void addRoutes() {
        redirect.get("/details", OpenArchi.PATH + "/details/", Redirect.Status.TEMPORARY_REDIRECT);
        get("/details/", buildRoute(new BeanBuilder().title("Detailed Specification"), OpenArchi.PATH + "/details"), engine);
        redirect.get("/api/", OpenArchi.PATH + "/api", Redirect.Status.PERMANENT_REDIRECT);
        redirect.get("/editor/", OpenArchi.PATH + "/editor", Redirect.Status.PERMANENT_REDIRECT);
        redirect.get("/prototyper/", OpenArchi.PATH + "/prototyper", Redirect.Status.TEMPORARY_REDIRECT);

        get("/", buildRoute(new BeanBuilder().title(OPEN_ARCHI), OpenArchi.PATH + "/home"), engine);
        before("/login/google", OpenArchi.basicSecurityFilter);
        get("/login/google", Authentication.authGoogle, engine);
        get("/login", Authentication.login, engine);
        get("/callback", OpenArchi.callback);
        post("/callback", (req, res) -> {
            store(req, res);
            return OpenArchi.callback.handle(req, res);
        });

        path("/samples", samples);
        path("/api", api);
        path("/editor", editor);
        path("/prototyper", prototyper);

        final LogoutRoute localLogout = new LogoutRoute(config, "/open-archi");
        localLogout.setDestroySession(true);
        localLogout.setLocalLogout(false);
        localLogout.setCentralLogout(true);
        get("/logout", localLogout);
        final LogoutRoute centralLogout = new LogoutRoute(config);
        centralLogout.setDefaultUrl("http://" + serverName + ":" + assignedPort + "/open-archi");
        centralLogout.setLogoutUrlPattern("http://" + serverName + ":" + assignedPort + "/.*");
        centralLogout.setLocalLogout(false);
        centralLogout.setCentralLogout(true);
        centralLogout.setDestroySession(true);
        get("/central-logout", centralLogout);
        get("/force-login", (rq, rs) -> forceLogin(config, rq, rs));

        get("/jwt", Authentication::jwt, engine);
        get("/oidc", Authentication::protectedIndex, engine);
        get("/rest-jwt", Authentication::protectedIndex, engine);
        get("/login", (rq, rs) -> form(OpenArchi.config), engine);


    }

}
