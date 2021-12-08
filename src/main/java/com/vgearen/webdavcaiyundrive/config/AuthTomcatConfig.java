package com.vgearen.webdavcaiyundrive.config;

import org.apache.catalina.CredentialHandler;
import org.apache.catalina.authenticator.AuthenticatorBase;
import org.apache.catalina.authenticator.BasicAuthenticator;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.MessageDigestCredentialHandler;
import org.apache.catalina.realm.RealmBase;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collections;

@Component
@ConditionalOnProperty(prefix = "caiyun.auth", name = "enable", matchIfMissing = true)
public class AuthTomcatConfig implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>, Ordered {

    @Autowired
    private CaiyunProperties caiyunProperties;

    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {

        TomcatServletWebServerFactory tomcatServletWebServerFactory = (TomcatServletWebServerFactory) factory;

        tomcatServletWebServerFactory.addContextCustomizers(context -> {

            RealmBase realm = new RealmBase() {
                @Override
                protected String getPassword(String username) {
                    if (caiyunProperties.getAuth().getUserName().equals(username)) {
                        return caiyunProperties.getAuth().getPassword();
                    }
                    return null;
                }

                @Override
                protected Principal getPrincipal(String username) {
                    return new GenericPrincipal(username, caiyunProperties.getAuth().getPassword(), Collections.singletonList("**"));
                }
            };

            CredentialHandler credentialHandler = new MessageDigestCredentialHandler();
            realm.setCredentialHandler(credentialHandler);
            context.setRealm(realm);

            AuthenticatorBase digestAuthenticator = new BasicAuthenticator();
            SecurityConstraint securityConstraint = new SecurityConstraint();
            securityConstraint.setAuthConstraint(true);
            securityConstraint.addAuthRole("**");
            SecurityCollection collection = new SecurityCollection();
            collection.addPattern("/*");
            securityConstraint.addCollection(collection);
            context.addConstraint(securityConstraint);
            context.getPipeline().addValve(digestAuthenticator);
        });

    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
