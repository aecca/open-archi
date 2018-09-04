package com.araguacaima.open_archi.web.common;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.DefaultAuthorizers;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.araguacaima.open_archi.web.common.Commons.clients;
import static com.araguacaima.open_archi.web.common.Commons.store;
import static com.araguacaima.open_archi.web.common.Security.JWT_SALT;

public class Authentication {

    public static AuthGoogle authGoogle = new AuthGoogle();
    public static Login login = new Login();
    private static Map<String, Object> map = new HashMap<>();

    public static ModelAndView jwt(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        final Optional<CommonProfile> profile = manager.get(true);
        String token = "";
        if (profile.isPresent()) {
            JwtGenerator<CommonProfile> generator = new JwtGenerator<>(new SecretSignatureConfiguration(JWT_SALT));
            token = generator.generate(profile.get());
        }
        final Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        return new ModelAndView(map, "jwt.mustache");
    }

    public static ModelAndView form(final Config config) {
        final Map<String, String> map = new HashMap<String, String>();
        final FormClient formClient = config.getClients().findClient(FormClient.class);
        map.put("callbackUrl", formClient.getCallbackUrl());
        return new ModelAndView(map, "loginForm.mustache");
    }

    public static ModelAndView protectedIndex(final Request request, final Response response) {
        final Map<String, List<CommonProfile>> map = new HashMap<>();
        map.put("profiles", getProfiles(request, response));
        return new ModelAndView(map, "protectedIndex.mustache");
    }

    public static List<CommonProfile> getProfiles(final Request request, final Response response) {
        final SparkWebContext context = new SparkWebContext(request, response);
        final ProfileManager<CommonProfile> manager = new ProfileManager<>(context);
        return manager.getAll(true);
    }

    public static Filter buildBasicSecurityFilter(Config config) {
        return new SecurityFilter(config, clients);
    }

    public static Filter buildStrongSecurityFilter(Config config) {
        return new SecurityFilter(config, clients, "admin,custom," + DefaultAuthorizers.ALLOW_AJAX_REQUESTS + "," + DefaultAuthorizers.IS_REMEMBERED + "," + DefaultAuthorizers.IS_AUTHENTICATED);
    }

    private static class AuthGoogle implements TemplateViewRoute {

        @Override
        public ModelAndView handle(Request req, Response res) throws Exception {
            store(req, res);
            Session session = req.session(true);
            String originalRequest = session.attribute("originalRequest");
            String originalQueryParams = session.attribute("originalQueryParams");
            String[] splittedOriginalQueryParams = originalQueryParams.split("&");
            for (String splittedOriginalQueryParam : splittedOriginalQueryParams) {
                String[] splittedQueryParam = splittedOriginalQueryParam.split("=");
                String key = splittedQueryParam[0];
                String value;
                try {
                    value = splittedQueryParam[1];
                } catch (IndexOutOfBoundsException ignored) {
                    value = StringUtils.EMPTY;
                }
                map.put(key, value);
            }
            return new ModelAndView(map, originalRequest);
        }
    }

    private static class Login implements TemplateViewRoute {
        @Override
        public ModelAndView handle(Request req, Response res) throws Exception {

            final StringBuilder url = new StringBuilder();
            final StringBuilder queryParams = new StringBuilder();
            QueryParamsMap queryParamsMap = req.queryMap();
            if (queryParamsMap != null) {
                QueryParamsMap redirect_uri = queryParamsMap.get("redirect_uri");
                if (redirect_uri != null) {
                    url.append(redirect_uri.value());
                } else {
                    url.append(req.uri());
                }
                queryParamsMap.toMap().forEach((key, value) -> {
                    if (!key.equals("redirect_uri")) {
                        queryParams.append(key).append("=").append(StringUtils.join(value)).append("&");
                    }
                });
            } else {
                url.append(req.uri());
            }
            req.session(true).attribute("originalRequest", url.toString());
            req.session(true).attribute("originalQueryParams", queryParams.toString());
            return new ModelAndView(map, "/open-archi/login");

        }
    }
}
