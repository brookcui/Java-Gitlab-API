package org.gitlab.api.http;

import org.gitlab.api.core.AuthMethod;
import org.gitlab.api.models.GitlabAPIClient;

import java.net.Proxy;

public class Config {
    private final String endpoint;
    private final AuthMethod authMethod;
    private final String token;
    private static final int DEFAULT_TIMEOUT = 0;
    private int responseReadTimeout = DEFAULT_TIMEOUT;
    private int connectionTimeout = DEFAULT_TIMEOUT;
    private String userAgent = GitlabAPIClient.class.getCanonicalName() + "/" + System.getProperty("java.version");
    private boolean ignoreCertificateErrors = false;

    private Proxy proxy;
    private final String apiNamespace;

    private static final String DEFAULT_API_NAMESPACE = "/api/v4";

    public Config(String endpoint, String token, AuthMethod method) {
        this.endpoint = endpoint.endsWith("/") ? endpoint.replaceAll("/$", "") : endpoint;
        this.token = token;
        this.authMethod = method;
        this.apiNamespace = DEFAULT_API_NAMESPACE;
    }


    public String getAPIUrl(String tailAPIUrl) {
        if (!tailAPIUrl.startsWith("/")) {
            tailAPIUrl = "/" + tailAPIUrl;
        }
        return endpoint + apiNamespace + tailAPIUrl;
    }

    public Proxy getProxy() {
        return proxy;
    }


    public AuthMethod authMethod() {
        return authMethod;
    }

    public int getResponseReadTimeout() {
        return responseReadTimeout;
    }


    public int getConnectionTimeout() {
        return connectionTimeout;
    }


    public String getUserAgent() {
        return userAgent;
    }


    public boolean isIgnoreCertificateErrors() {
        return ignoreCertificateErrors;
    }


    public void setResponseReadTimeout(int responseReadTimeout) {
        this.responseReadTimeout = responseReadTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public void setIgnoreCertificateErrors(boolean ignoreCertificateErrors) {
        this.ignoreCertificateErrors = ignoreCertificateErrors;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public String token() {
        return token;
    }
}
