package com.araguacaima.open_archi.web;

import org.pac4j.core.authorization.authorizer.CheckHttpMethodAuthorizer;
import org.pac4j.core.config.Config;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.engine.DefaultSecurityLogic;
import org.pac4j.core.engine.SecurityLogic;
import org.pac4j.sparkjava.SparkWebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.route.HttpMethod;

import static org.pac4j.core.util.CommonHelper.assertNotNull;
import static spark.Spark.halt;

/**
 * <p>This filter protects an url, based on the {@link #securityLogic}.</p>
 * <p>
 * <p>The configuration can be provided via the following parameters: <code>config</code> (security configuration),
 * <code>clients</code> (list of clients for authentication), <code>authorizers</code> (list of authorizers),
 * <code>matchers</code> (list of matchers) and <code>multiProfile</code> (whether multiple profiles should be kept).</p>
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
public class APIFilter implements Filter {

    private static final String SECURITY_GRANTED_ACCESS = "SECURITY_GRANTED_ACCESS";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private SecurityLogic<Object, SparkWebContext> securityLogic = new DefaultSecurityLogic<>();

    private Config config;

    private String clients;

    private String authorizers;

    private String matchers;

    private Boolean multiProfile;

    public APIFilter(final Config config, final String clients) {
        this(config, clients, null, null);
    }

    public APIFilter(final Config config, final String clients, final String authorizers) {
        this(config, clients, authorizers, null);
    }

    public APIFilter(final Config config, final String clients, final String authorizers, final String matchers) {
        this(config, clients, authorizers, matchers, null);
    }

    public APIFilter(final Config config, final String clients, final String authorizers, final String matchers, final Boolean multiProfile) {
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
        HttpMethod requestedHttpMethod = HttpMethod.get(request.requestMethod().toLowerCase());
        if (requestedHttpMethod.equals(HttpMethod.get)) {
            CheckHttpMethodAuthorizer checkHttpMethodAuthorizer = (CheckHttpMethodAuthorizer) config.getAuthorizers().get("checkHttpMethodAuthorizer");
            checkHttpMethodAuthorizer.setElements(HttpConstants.HTTP_METHOD.GET);
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
            checkHttpMethodAuthorizer.getElements().remove(HttpConstants.HTTP_METHOD.GET);
        } else if (requestedHttpMethod.equals(HttpMethod.post)) {
            CheckHttpMethodAuthorizer checkHttpMethodAuthorizer = (CheckHttpMethodAuthorizer) config.getAuthorizers().get("checkHttpMethodAuthorizer");
            checkHttpMethodAuthorizer.setElements(HttpConstants.HTTP_METHOD.POST);
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
            checkHttpMethodAuthorizer.getElements().remove(HttpConstants.HTTP_METHOD.POST);
        } else if (requestedHttpMethod.equals(HttpMethod.patch)) {
            CheckHttpMethodAuthorizer checkHttpMethodAuthorizer = (CheckHttpMethodAuthorizer) config.getAuthorizers().get("checkHttpMethodAuthorizer");
            checkHttpMethodAuthorizer.setElements(HttpConstants.HTTP_METHOD.PATCH);
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
            checkHttpMethodAuthorizer.getElements().remove(HttpConstants.HTTP_METHOD.PATCH);
        } else if (requestedHttpMethod.equals(HttpMethod.put)) {
            CheckHttpMethodAuthorizer checkHttpMethodAuthorizer = (CheckHttpMethodAuthorizer) config.getAuthorizers().get("checkHttpMethodAuthorizer");
            checkHttpMethodAuthorizer.setElements(HttpConstants.HTTP_METHOD.PUT);
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
            checkHttpMethodAuthorizer.getElements().remove(HttpConstants.HTTP_METHOD.PUT);
        } else if (requestedHttpMethod.equals(HttpMethod.delete)) {
            CheckHttpMethodAuthorizer checkHttpMethodAuthorizer = (CheckHttpMethodAuthorizer) config.getAuthorizers().get("checkHttpMethodAuthorizer");
            checkHttpMethodAuthorizer.setElements(HttpConstants.HTTP_METHOD.DELETE);
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
            checkHttpMethodAuthorizer.getElements().remove(HttpConstants.HTTP_METHOD.DELETE);
        } else {
            result = securityLogic.perform(context, this.config,
                    (ctx, parameters) -> SECURITY_GRANTED_ACCESS, config.getHttpActionAdapter(),
                    this.clients, this.authorizers, this.matchers, this.multiProfile);
        }
        if (result == SECURITY_GRANTED_ACCESS) {
            // It means that the access is granted: continue
            logger.debug("Received SECURITY_GRANTED_ACCESS -> continue");
        } else {
            logger.debug("Halt the request processing");
            // stop the processing if no SECURITY_GRANTED_ACCESS has been received
            throw halt();
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
