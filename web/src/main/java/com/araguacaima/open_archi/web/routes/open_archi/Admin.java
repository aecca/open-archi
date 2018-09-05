package com.araguacaima.open_archi.web.routes.open_archi;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.meta.Role;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import spark.RouteGroup;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.araguacaima.open_archi.web.common.Commons.*;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;
import static spark.Spark.before;
import static spark.Spark.patch;

public class Admin implements RouteGroup {

    public static final String PATH = "/admin";

    @Override
    public void addRoutes() {
        before("/*", OpenArchi.adminApiFilter);
        patch("/admin", (request, response) -> {
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
