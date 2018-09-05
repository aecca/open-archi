package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Role;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import com.araguacaima.open_archi.web.BeanBuilder;
import com.araguacaima.open_archi.web.common.Commons;
import spark.RouteGroup;

import java.util.*;

import static com.araguacaima.open_archi.web.Server.engine;
import static com.araguacaima.open_archi.web.common.Commons.*;
import static com.araguacaima.open_archi.web.common.Commons.EMPTY_RESPONSE;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.*;

public class Admin implements RouteGroup {

    public static final String PATH = "/admin";

    @Override
    public void addRoutes() {
        before(Commons.EMPTY_PATH, OpenArchi.adminApiFilter,  OpenArchi.adminSecurityFilter);
        before("/*", OpenArchi.adminApiFilter, OpenArchi.adminSecurityFilter);
        ArrayList<String> header = new ArrayList<>(Arrays.asList("Enabled", "Login", "Email"));
        header.addAll(Commons.ALL_ROLES);
        get(Commons.EMPTY_PATH, buildRoute(new BeanBuilder()
                .title("Open-Archi Admin")
                .accounts(JPAEntityManagerUtils.executeQuery(Account.class, Account.GET_ALL_ACCOUNTS))
                .roles(Commons.ALL_ROLES)
                .header(header), OpenArchi.PATH + Admin.PATH), engine);
        patch(Commons.EMPTY_PATH, (request, response) -> {
            try {
                Map requestInput = jsonUtils.fromJSON(request.body(), Map.class);
                String email = (String) requestInput.get("email");
                boolean approved = (Boolean) requestInput.get("approved");
                Map<String, Object> params = new HashMap<>();
                params.put(Account.PARAM_EMAIL, email);
                Account account = JPAEntityManagerUtils.findByQuery(Account.class, Account.FIND_BY_EMAIL_AND_ENABLED, params);
                if (account != null) {
                    Set<Role> roles = account.getRoles();
                    //TODO AMM: Finish
                    JPAEntityManagerUtils.merge(account);
                    response.status(HTTP_OK);
                    return EMPTY_RESPONSE;
                } else {
                    response.status(HTTP_BAD_REQUEST);
                    return EMPTY_RESPONSE;
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                response.status(HTTP_BAD_REQUEST);
                response.type(JSON_CONTENT_TYPE);
                return EMPTY_RESPONSE;
            }
        });

    }

}
