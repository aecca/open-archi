package com.araguacaima.open_archi.web;

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.CommonHelper;
import org.pac4j.http.client.direct.DirectBasicAuthClient;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.http.client.direct.ParameterClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.http.client.indirect.IndirectBasicAuthClient;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.credentials.authenticator.JwtAuthenticator;
import org.pac4j.oauth.client.TwitterClient;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.saml.client.SAML2Client;
import org.pac4j.saml.client.SAML2ClientConfiguration;
import spark.TemplateEngine;

import java.util.LinkedList;
import java.util.List;

public class ConfigFactory implements org.pac4j.core.config.ConfigFactory {

    private final String salt;

    private final TemplateEngine templateEngine;

    @SuppressWarnings("WeakerAccess")
    public ConfigFactory(final String salt, final TemplateEngine templateEngine) {
        this.salt = salt;
        this.templateEngine = templateEngine;
    }

    /**
     * Build a configuration.
     *
     * @param parameters the parameters to build the configuration. The first one means the server name, The second one, the server port, ther third one, the relative port, and the fourth one a comma separated client names
     * @return the built configuration
     */
    @Override
    public Config build(final Object... parameters) {
        String serverName = (String) parameters[0];
        String serverPort = parameters[1].toString();
        String relativeEndpoint = (String) parameters[2];
        String clientNames = (String) parameters[3];

        String[] splittedClientNames = clientNames.split(",");
        List<Client> clientList = new LinkedList<>();
        for (String splittedClientName : splittedClientNames) {
            switch (splittedClientName) {
                case "OidcClient":
                    clientList.add(buildOidcClient());
                    break;
                case "SAML2Client":
                    clientList.add(buildSAML2Client(serverName, serverPort, relativeEndpoint));
                    break;
                case "TwitterClient":
                    clientList.add(buildTwitterClient());
                    break;
                case "CasClient":
                    clientList.add(buildCasClient());
                    break;
                case "FormClient":
                    clientList.add(buildFormClient(serverName, serverPort, relativeEndpoint));
                    break;
                case "ParameterClient":
                    clientList.add(buildParameterClient());
                    break;
                case "IndirectBasicAuthClient":
                    clientList.add(buildIndirectBasicAuthClient());
                    break;
                case "DirectBasicAuthClient":
                    clientList.add(buildDirectBasicAuthClient());
                    break;
                case "HeaderClient":
                    clientList.add(buildHeaderClient());
                    break;
                default:
                    break;
            }
        }

        final Clients clients = new Clients("http://" + serverName + ":" + serverPort + relativeEndpoint + "/callback", clientList);

        final Config config = new Config(clients);
        config.addAuthorizer("admin", new RequireAnyRoleAuthorizer<>("ROLE_ADMIN"));
        config.addAuthorizer("custom", new Authorizer());
        config.setHttpActionAdapter(new HttpActionAdapter(templateEngine));
        return config;
    }

    private HeaderClient buildHeaderClient() {
        return new HeaderClient("Authorization", (credentials, ctx) -> {
            final String token = ((TokenCredentials) credentials).getToken();
            if (CommonHelper.isNotBlank(token)) {
                final CommonProfile profile = new CommonProfile();
                profile.setId(token);
                credentials.setUserProfile(profile);
            }
        });
    }

    private DirectBasicAuthClient buildDirectBasicAuthClient() {
        return new DirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
    }

    private ParameterClient buildParameterClient() {
        ParameterClient parameterClient = new ParameterClient("token", new JwtAuthenticator(new SecretSignatureConfiguration(salt)));
        parameterClient.setSupportGetRequest(true);
        parameterClient.setSupportPostRequest(false);
        return parameterClient;
    }

    private CasClient buildCasClient() {
        final CasConfiguration casConfiguration = new CasConfiguration("https://casserverpac4j.herokuapp.com/login");
        return new CasClient(casConfiguration);
    }

    private IndirectBasicAuthClient buildIndirectBasicAuthClient() {
        return new IndirectBasicAuthClient(new SimpleTestUsernamePasswordAuthenticator());
    }

    private FormClient buildFormClient(String serverName, String serverPort, String relativeEndpoint) {
        return new FormClient("http://" + serverName + ":" + serverPort + relativeEndpoint + "/loginForm", new SimpleTestUsernamePasswordAuthenticator());
    }

    private TwitterClient buildTwitterClient() {
        return new TwitterClient("CoxUiYwQOSFDReZYdjigBA", "2kAzunH5Btc4gRSaMr7D7MkyoJ5u1VzbOOzE8rBofs");
    }

    private SAML2Client buildSAML2Client(String serverName, String serverPort, String relativeEndpoint) {
        final SAML2ClientConfiguration cfg = new SAML2ClientConfiguration("resource:samlKeystore.jks", "pac4j-demo-passwd",
                "pac4j-demo-passwd", "resource:metadata-okta.xml");
        cfg.setMaximumAuthenticationLifetime(3600);

        cfg.setServiceProviderEntityId("http://" + serverName + ":" + serverPort + relativeEndpoint + "/callback?client_name=SAML2Client");
        cfg.setServiceProviderMetadataPath("sp-metadata.xml");
        return new SAML2Client(cfg);
    }

    @SuppressWarnings("unchecked")
    private OidcClient buildOidcClient() {
        final OidcConfiguration oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setClientId("817439124528-1695bhdaiirv4o7rj0fuk20hhep6n4fn.apps.googleusercontent.com");
        oidcConfiguration.setSecret("_2ipySSV6oN3j40XV3tBCOkK");
        oidcConfiguration.setDiscoveryURI("https://accounts.google.com/.well-known/openid-configuration");
        oidcConfiguration.setUseNonce(true);
        //oidcClient.setPreferredJwsAlgorithm(JWSAlgorithm.RS256);
        oidcConfiguration.addCustomParam("prompt", "consent");
        final OidcClient oidcClient = new OidcClient(oidcConfiguration);
        oidcClient.setAuthorizationGenerator((ctx, profile) -> {
            profile.addRole("ROLE_ADMIN");
            return profile;
        });
        return oidcClient;
    }
}
