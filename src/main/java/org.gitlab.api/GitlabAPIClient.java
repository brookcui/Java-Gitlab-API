package org.gitlab.api;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;

/**
 * This class implements Gitlab API client that keeps endpoint, HTTP request
 * configurations, and authentication information (in the form of tokens).
 *
 * To get a instance of API client, use {@code GitlabAPIClient.Builder} to
 * construct needed fields and note that the endpoint must be specified.
 *
 * The can get project by projectId or namespace, and user used by userId or
 * current user who is using the API. This can also get query builder for
 * issues, users, projects, merge-requests, and projected owned by current
 * user.
 */
public final class GitlabAPIClient {
    /**
     * The endpoint to communicate with Gitlab API.
     */
    private final String endpoint;
    /**
     * The authentication method to call Gitlab API.
     */
    private final AuthMethod authMethod;
    /**
     * The token to authenticate requests with Gitlab API.
     */
    private final String token;

    /**
     * The API namespace.
     */
    private final String apiNamespace;
    /**
     * The read timeout of HTTP requests in milliseconds.
     */
    private final int readTimeout;
    /**
     * The write timeout of HTTP requests in milliseconds.
     */
    private final int writeTimeout;
    /**
     * The connection timeout of HTTP requests in milliseconds.
     */
    private final int connectionTimeout;
    /**
     * The proxy for API calls.
     */
    private final Proxy proxy;
    /**
     * The HTTP client helper.
     */
    private final HttpClient httpClient;

    /**
     * Construct the {@link GitlabAPIClient} based on the {@link Builder}.
     *
     * @param builder the builder
     */
    private GitlabAPIClient(Builder builder) {
        this.endpoint = builder.endpoint;
        this.authMethod = builder.authMethod;
        this.token = builder.token;
        this.apiNamespace = builder.apiNamespace;
        this.connectionTimeout = builder.connectionTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.proxy = builder.proxy;
        httpClient = new HttpClient(this);
    }

    /**
     * Returns API endpoint.
     *
     * @return the endpoint
     */
    String getEndpoint() {
        return endpoint;
    }

    /**
     * Returns current {@link AuthMethod}.
     *
     * @return the {@link AuthMethod}
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    /**
     * Returns current token.
     *
     * @return the token
     */
    String getToken() {
        return token;
    }

    /**
     * Returns current API namespace.
     *
     * @return the API namespace
     */
    String getApiNamespace() {
        return apiNamespace;
    }

    /**
     * Returns current {@link Proxy}.
     *
     * @return the {@link Proxy}
     */
    Proxy getProxy() {
        return proxy;
    }

    /**
     * Returns current read timeout in milliseconds.
     *
     * @return read timeout in milliseconds
     */
    int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Returns current write timeout in milliseconds.
     *
     * @return write timeout in milliseconds
     */
    int getWriteTimeout() {
        return writeTimeout;
    }

    /**
     * Returns current connection timeout in milliseconds.
     *
     * @return connection timeout in milliseconds
     */
    int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * Returns a {@link GitlabIssue.Query} that can build query options and
     * execute query for issues related to current authenticated user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-issues
     * <p>
     * GET /issues
     *
     * @return a {@link GitlabIssue.Query}
     */
    public GitlabIssue.Query getIssuesQuery() {
        return new GitlabIssue.Query(httpClient);
    }

    /**
     * Returns a {@link GitlabUser.Query} that can build query options and
     * execute query for users.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-users
     * <p>
     * GET /users
     *
     * @return a {@link GitlabUser.Query}
     */
    public GitlabUser.Query getUsersQuery() {
        return new GitlabUser.Query(httpClient);
    }

    /**
     * Returns a {@link GitlabProject.Query} that can build query options and
     * execute query for projects visible to current authenticated user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-all-projects
     * <p>
     * GET /projects
     *
     * @return a {@link GitlabProject.Query}
     */
    public GitlabProject.Query getProjectsQuery() {
        return new GitlabProject.Query(httpClient);
    }

    /**
     * Returns a {@link GitlabMergeRequest.Query} that can build query options
     * and execute query for merge requests related to current authenticated
     * user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-merge-requests
     * <p>
     * GET /merge_requests
     *
     * @return a {@link GitlabMergeRequest.Query}
     */
    public GitlabMergeRequest.Query getMergeRequestsQuery() {
        return new GitlabMergeRequest.Query(httpClient);
    }

    /**
     * Returns a {@link GitlabProject.UserQuery} that can build query options
     * and execute query for projects owned by user specified by username and
     * who are visible to current authenticated user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
     * <p>
     * GET /users/:user_id/projects
     *
     * @param username the username of the owner the projects to be queried
     * @return a {@link GitlabProject.UserQuery}
     */
    public GitlabProject.UserQuery getUserProjectsQuery(String username) {
        return new GitlabProject.UserQuery(httpClient, username);
    }

    /**
     * Returns the project specified by projectId if exists.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * <p>
     * GET /projects/:id
     *
     * @param projectId the project Id
     * @return the {@link GitlabProject} of given projectId
     */
    public GitlabProject getProject(int projectId) {
        return httpClient.get("/projects/" + projectId, GitlabProject.class);
    }

    /**
     * Returns the project specified by namespace and projectPath if exists.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * <p>
     * GET /projects/:namespace/:project_path
     *
     * @param namespace   the project namespace
     * @param projectPath the project path
     * @return the {@link GitlabProject} of given namespace and path
     */
    public GitlabProject getProject(String namespace, String projectPath) {
        try {
            return httpClient.get("/projects/" + URLEncoder.encode(
                    namespace + "/" + projectPath, "UTF-8"), GitlabProject.class);
        } catch (UnsupportedEncodingException e) {
            throw new GitlabException(e);
        }
    }

    /**
     * Returns a newly created {@link GitlabProject} with given project name
     * that can build parameters for the new project and call
     * {@link GitlabProject#create()} to create this project explicitly.
     *
     * @param projectName name of new project
     * @return a {@link GitlabProject} instance
     */
    public GitlabProject newProject(String projectName) {
        return new GitlabProject(projectName).withHttpClient(httpClient);
    }

    /**
     * Returns the user of given userId if exists.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
     * <p>
     * GET /users
     *
     * @param userId the user id
     * @return {@link GitlabUser} with the given userId
     */
    public GitlabUser getUser(int userId) {
        return httpClient.get("/users/" + userId, GitlabUser.class);
    }

    /**
     * Returns the current authenticated user.
     *
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-current-user-for-normal-users
     * <p>
     * GET /user
     *
     * @return {@link GitlabUser} of current authenticated user
     */
    public GitlabUser getCurrentUser() {
        return httpClient.get("/user", GitlabUser.class);
    }

    /**
     * This {@code Builder} is used to build {@link GitlabAPIClient} instance.
     */
    public static final class Builder {
        /**
         * The default timeout in milliseconds.
         */
        private static final int DEFAULT_TIMEOUT = 5000;
        /**
         * Thee default API namespace.
         */
        private static final String DEFAULT_API_NAMESPACE = "/api/v4";

        /**
         * The Gitlab API endpoint.
         */
        private final String endpoint;
        /**
         * The Gitlab authentication method.
         */
        private AuthMethod authMethod;
        /**
         * The Gitlab token.
         */
        private String token;

        /**
         * The API namespace.
         */
        private String apiNamespace = DEFAULT_API_NAMESPACE;
        /**
         * The read timeout in milliseconds.
         */
        private int readTimeout = DEFAULT_TIMEOUT;
        /**
         * The write timeout in milliseconds.
         */
        private int writeTimeout = DEFAULT_TIMEOUT;
        /**
         * /**
         * The connection timeout in milliseconds.
         */
        private int connectionTimeout = DEFAULT_TIMEOUT;
        /**
         * The proxy.
         */
        private Proxy proxy;

        /**
         * Constructs the {@code GitlabAPIClient.Builder} instance.
         *
         * @param endpoint gitlab api endpoint
         */
        public Builder(String endpoint) {
            this.endpoint = endpoint;
        }

        /**
         * Sets Oauth2 token to the builder
         *
         * @param token Oauth2 token
         * @return {@code Builder} with Oauth2 token
         */
        public Builder withOAuth2Token(String token) {
            this.authMethod = AuthMethod.OAUTH2;
            this.token = token;
            return this;
        }

        /**
         * Sets access token to the builder
         *
         * @param token access token
         * @return {@code Builder} with Oauth2 token
         */
        public Builder withAccessToken(String token) {
            this.authMethod = AuthMethod.ACCESS_TOKEN;
            this.token = token;
            return this;
        }

        /**
         * Sets readTimeout to the builder
         *
         * @param readTimeout read timeout in milliseconds
         * @return {@code Builder} with readTimeout
         */
        public Builder withReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Sets writeTimeout to the builder
         *
         * @param writeTimeout write timeout in milliseconds
         * @return {@code Builder} with writeTimeout
         */
        public Builder withWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        /**
         * Sets connectionTimeout to the builder
         *
         * @param connectionTimeout connection timeout in milliseconds
         * @return {@code Builder} with connectionTimeout
         */
        public Builder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Sets proxy to the builder.
         *
         * @param proxy proxy
         * @return {@code Builder} with proxy
         */
        public Builder withProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * Sets API namespace to the builder.
         *
         * @param apiNamespace api namespace
         * @return {@code Builder} with apiNamespace
         */
        public Builder withApiNamespace(String apiNamespace) {
            this.apiNamespace = apiNamespace;
            return this;
        }

        /**
         * Returns the {@link GitlabAPIClient} instance built from this builder
         * (with fields specified in this builder).
         *
         * @return a {@link GitlabAPIClient} instance
         */
        public GitlabAPIClient build() {
            return new GitlabAPIClient(this);
        }
    }
}
