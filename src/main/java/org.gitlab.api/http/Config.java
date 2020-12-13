package org.gitlab.api.http;

import org.gitlab.api.core.AuthMethod;

import java.net.Proxy;

/**
 * This class is used to represent a Gitlab configuration. It must be initialized with the endpoint, auth method and token.
 * It can be configurable parameters like Gitlab API namespace, read, write, connection timeout as well as proxy
 */
public class Config {
    /**
     * The default timeout in milliseconds
     */
    private static final int DEFAULT_TIMEOUT = 1000;
    /**
     * Thee default API namespace
     */
    private static final String DEFAULT_API_NAMESPACE = "/api/v4";

    /**
     * The Gitlab endpoint to be used
     */
    private final String endpoint;
    /**
     * The Gitlab authenticated method to be used
     */
    private final AuthMethod authMethod;
    /**
     * The Gitlab token to be used
     */
    private final String token;

    /**
     * The api namespace to be used
     */
    private String apiNamespace = DEFAULT_API_NAMESPACE;
    /**
     * The read timeout to be used in milliseconds
     */
    private int readTimeout = DEFAULT_TIMEOUT;
    /**
     * The write timeout to be used in milliseconds
     */
    private int writeTimeout = DEFAULT_TIMEOUT;
    /**
     * The connection timeout to be used in milliseconds
     */
    private int connectionTimeout = DEFAULT_TIMEOUT;
    /**
     * The proxy to be used
     */
    private Proxy proxy;

    /**
     * Initialize the configuration with Gitlab endpoint, authenticated method and the token
     *
     * @param endpoint - the Gitlab endpoint
     * @param method   - the authenticated method
     * @param token    - the token
     */
    public Config(String endpoint, AuthMethod method, String token) {
        this.endpoint = endpoint.endsWith("/") ? endpoint.replaceAll("/$", "") : endpoint;
        this.authMethod = method;
        this.token = token;
    }

    /**
     * Given the tailUrl, e.g. /projects/1234,
     * return the entire API url based on {@link #endpoint} and {@link #apiNamespace},
     * e.g. https://gitlab.com/api/v4/projects/1234
     * @param tailUrl the API tail Url, e.g.  /projects/1234
     * @return the entire API url based on {@link #endpoint} and {@link #apiNamespace},
     * e.g. https://gitlab.com/api/v4/projects/1234
     */
    public String getAPIUrl(String tailUrl) {
        if (!tailUrl.startsWith("/")) {
            tailUrl = "/" + tailUrl;
        }
        return endpoint + apiNamespace + tailUrl;
    }

    /**
     * Get the current used {@link AuthMethod}
     * @return the current used {@link AuthMethod}
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }
    /**
     * Get the current used token
     * @return the current used token
     */
    public String getToken() {
        return token;
    }

    /**
     * Get the current used api namespace
     * @return the current used api namespace
     */
    public String getApiNamespace() {
        return apiNamespace;
    }

    /**
     * Set the api namespace to be used
     * @param apiNamespace the api namespace to be used
     */
    public void setApiNamespace(String apiNamespace) {
        this.apiNamespace = apiNamespace;
    }

    /**
     * Get the current used {@link Proxy}
     * @return the current used {@link Proxy}
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Set the {@link Proxy} to be used
     * @param proxy the {@link Proxy} to be used
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the current used read timeout in milliseconds
     * @return the current used read timeout in milliseconds
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Set the read timeout to be used in milliseconds
     * @param readTimeout the read timeout to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    /**
     * Get the current used write timeout in milliseconds
     * @return the current used write timeout in milliseconds
     */
    public int getWriteTimeout() {
        return writeTimeout;
    }
    /**
     * Set the write timeout to be used in milliseconds
     * @param writeTimeout the write timeout to be used in milliseconds
     */
    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    /**
     * Get the current used write timeout in milliseconds
     * @return the current used write timeout in milliseconds
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    /**
     * Set the connection timeout to be used in milliseconds
     * @param connectionTimeout the connection timeout to be used in milliseconds
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }


}