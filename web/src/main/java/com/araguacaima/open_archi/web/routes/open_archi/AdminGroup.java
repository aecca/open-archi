package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.buildRoute;
import static spark.Spark.*;

public class AdminGroup implements RouteGroup {

    public static final String PATH = "/admin";

    private Admin admin = new Admin();

    @Override
    public void addRoutes() {
        before("/*", OpenArchi.adminSecurityFilter);
        get(Commons.EMPTY_PATH, buildRoute(new BeanBuilder().title("Open-Archi Admin").accounts(JPAEntityManagerUtils.executeQuery(Account.class, Account.GET_ALL_ACCOUNTS)).roles(Commons.ALL_ROLES), OpenArchi.PATH + "/admin"), engine);
        before(Admin.PATH, OpenArchi.adminSecurityFilter);
        path(Admin.PATH, admin);
        path(Admin.PATH + Commons.SEPARATOR_PATH, admin);
    }

}
