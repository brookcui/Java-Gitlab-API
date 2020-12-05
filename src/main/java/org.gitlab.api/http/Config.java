package org.gitlab.api.http;

import org.gitlab.api.core.AuthMethod;
import org.gitlab.api.core.GitlabAPIClient;

import java.net.Proxy;

public class Config {
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_API_NAMESPACE = "/api/v4";
    private final String endpoint;
    private final AuthMethod authMethod;
    private final String token;
    private final String apiNamespace;
    private int responseReadTimeout = DEFAULT_TIMEOUT;
    private int connectionTimeout = DEFAULT_TIMEOUT;
    private String userAgent = GitlabAPIClient.class.getCanonicalName() + "/" + System.getProperty("java.version");
    private boolean ignoreCertificateErrors = false;
    private Proxy proxy;

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

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public AuthMethod authMethod() {
        return authMethod;
    }

    public int getResponseReadTimeout() {
        return responseReadTimeout;
    }

    public void setResponseReadTimeout(int responseReadTimeout) {
        this.responseReadTimeout = responseReadTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isIgnoreCertificateErrors() {
        return ignoreCertificateErrors;
    }

    public void setIgnoreCertificateErrors(boolean ignoreCertificateErrors) {
        this.ignoreCertificateErrors = ignoreCertificateErrors;
    }

    public String token() {
        return token;
    }
}
