package com.araguacaima.open_archi.web;

import com.araguacaima.open_archi.persistence.meta.Account;
import com.araguacaima.open_archi.persistence.utils.JPAEntityManagerUtils;
import org.apache.commons.collections4.IterableUtils;
import org.pac4j.core.config.Config;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.araguacaima.open_archi.web.common.Commons.getProfiles;
import static org.pac4j.core.util.CommonHelper.assertNotNull;
import static spark.Spark.halt;

public class AdminAPIFilter implements Filter {

    private static final String SECURITY_GRANTED_ACCESS = "SECURITY_GRANTED_ACCESS";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityLogic<Object, SparkWebContext> securityLogic = new DefaultSecurityLogic<>();

    private Config config;

    private String clients;

    private String authorizers;

    private String matchers;

    private Boolean multiProfile;

    public AdminAPIFilter(final Config config, final String clients) {
        this(config, clients, null, null);
    }

    public AdminAPIFilter(final Config config, final String clients, final String authorizers) {
        this(config, clients, authorizers, null);
    }

    public AdminAPIFilter(final Config config, final String clients, final String authorizers, final String matchers) {
        this(config, clients, authorizers, matchers, null);
    }

    public AdminAPIFilter(final Config config, final String clients, final String authorizers, final String matchers, final Boolean multiProfile) {
        this.config = config;
        this.clients = clients;
        this.authorizers = authorizers;
        this.matchers = matchers;
        this.multiProfile = multiProfile;
    }

    @Override
    public void handle(final Request request, final Response response) {

        assertNotNull("securityLogic", securityLogic);
        assertNotNull("config", config);
        final SparkWebContext context = new SparkWebContext(request, response, config.getSessionStore());
        Object result;
        List<CommonProfile> profiles = getProfiles(request, response);
        CommonProfile profile = IterableUtils.find(profiles, object -> clients.contains(object.getClientName()));
        if (profile != null) {
            Account account;
            String email = profile.getEmail();
            Map<String, Object> params = new HashMap<>();
            params.put(Account.PARAM_EMAIL, email);
            account = JPAEntityManagerUtils.findByQuery(Account.class, Account.FIND_BY_EMAIL, params);
            if (account != null && account.isSuperuser()) {
                result = securityLogic.perform(context, this.config,
                        (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                        this.clients, this.authorizers, this.matchers, this.multiProfile);
                if (result == SECURITY_GRANTED_ACCESS) {
                    // It means that the access is granted: continue
                    logger.debug("Received SECURITY_GRANTED_ACCESS -> continue");
                } else {
                    logger.debug("Halt the request processing");
                    // stop the processing if no SECURITY_GRANTED_ACCESS has been received
                    throw halt();
                }
            } else {
                logger.debug("Halt the request processing. User is no superuser");
                // stop the processing if no SECURITY_GRANTED_ACCESS has been received
                throw halt();
            }
        }
    }

    public SecurityLogic<Object, SparkWebContext> getSecurityLogic() {
        return securityLogic;
    }

    public void setSecurityLogic(final SecurityLogic<Object, SparkWebContext> securityLogic) {
        this.securityLogic = securityLogic;
    }

    public String getAuthorizers() {
        return authorizers;
    }

    public void setAuthorizers(final String authorizers) {
        this.authorizers = authorizers;
    }

    public String getMatchers() {
        return matchers;
    }

    public void setMatchers(final String matchers) {
        this.matchers = matchers;
    }

    public Boolean getMultiProfile() {
        return multiProfile;
    }

    public void setMultiProfile(final Boolean multiProfile) {
        this.multiProfile = multiProfile;
    }
}
