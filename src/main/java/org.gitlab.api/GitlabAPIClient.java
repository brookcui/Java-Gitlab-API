package org.gitlab.api;

import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URLEncoder;

/**
 * This class is the top level of all {@link GitlabComponent} can be constructed using builder pattern.
 */
public final class GitlabAPIClient {
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
    private final String apiNamespace;
    /**
     * The read timeout to be used in milliseconds
     */
    private final int readTimeout;
    /**
     * The write timeout to be used in milliseconds
     */
    private final int writeTimeout;
    /**
     * The connection timeout to be used in milliseconds
     */
    private final int connectionTimeout;
    /**
     * The proxy to be used
     */
    private final Proxy proxy;
    /**
     * The http client to be used
     */
    private final HttpClient httpClient;

    /**
     * The builder is used to build a {@link GitlabAPIClient}
     */
    public static final class Builder {
        /**
         * The default timeout in milliseconds
         */
        private static final int DEFAULT_TIMEOUT = 5000;
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
        private AuthMethod authMethod;
        /**
         * The Gitlab token to be used
         */
        private String token;

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
         * /**
         * The connection timeout to be used in milliseconds
         */
        private int connectionTimeout = DEFAULT_TIMEOUT;
        /**
         * The proxy to be used
         */
        private Proxy proxy;

        /**
         * Builder constructor
         *
         * @param endpoint gitlab api endpoint
         */
        public Builder(String endpoint) {
            this.endpoint = endpoint;
        }

        /**
         * Set Oauth2 token to the builder
         *
         * @param token Oauth2 token
         * @return Builder with Oauth2 token
         */
        public Builder withOAuth2Token(String token) {
            this.authMethod = AuthMethod.OAUTH2;
            this.token = token;
            return this;
        }

        /**
         * Set access token to the builder
         *
         * @param token access token
         * @return Builder with Oauth2 token
         */
        public Builder withAccessToken(String token) {
            this.authMethod = AuthMethod.ACCESS_TOKEN;
            this.token = token;
            return this;
        }

        /**
         * Set readTimeout to the builder
         *
         * @param readTimeout read timeout in milliseconds
         * @return Builder with readTimeout
         */
        public Builder withReadTimeout(int readTimeout) {
            this.readTimeout = readTimeout;
            return this;
        }

        /**
         * Set writeTimeout to the builder
         *
         * @param writeTimeout write timeout in milliseconds
         * @return Builder with writeTimeout
         */
        public Builder withWriteTimeout(int writeTimeout) {
            this.writeTimeout = writeTimeout;
            return this;
        }

        /**
         * Set connectionTimeout to the builder
         *
         * @param connectionTimeout connection timeout in milliseconds
         * @return Builder with connectionTimeout
         */
        public Builder withConnectionTimeout(int connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        /**
         * Set proxy to the builder
         *
         * @param proxy proxy
         * @return Builder with proxy
         */
        public Builder withProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * Set apiNamespace to the builder
         *
         * @param apiNamespace api namespace
         * @return Builder with apiNamespace
         */
        public Builder withApiNamespace(String apiNamespace) {
            this.apiNamespace = apiNamespace;
            return this;
        }

        /**
         * Build the {@link GitlabAPIClient} with current builder
         *
         * @return a {@link GitlabAPIClient} obtained from the builder
         */
        public GitlabAPIClient build() {
            return new GitlabAPIClient(this);
        }
    }

    /**
     * Construct the {@link GitlabAPIClient} based on the {@link Builder}
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
     * Get the current endpoint
     *
     * @return endpoint
     */
    String getEndpoint() {
        return endpoint;
    }


    /**
     * Get the current used {@link AuthMethod}
     *
     * @return the current used {@link AuthMethod}
     */
    public AuthMethod getAuthMethod() {
        return authMethod;
    }

    /**
     * Get the current used token
     *
     * @return the current used token
     */
    String getToken() {
        return token;
    }

    /**
     * Get the current used api namespace
     *
     * @return the current used api namespace
     */
    String getApiNamespace() {
        return apiNamespace;
    }


    /**
     * Get the current used {@link Proxy}
     *
     * @return the current used {@link Proxy}
     */
    Proxy getProxy() {
        return proxy;
    }

    /**
     * Get the current used read timeout in milliseconds
     *
     * @return the current used read timeout in milliseconds
     */
    int getReadTimeout() {
        return readTimeout;
    }


    /**
     * Get the current used write timeout in milliseconds
     *
     * @return the current used write timeout in milliseconds
     */
    int getWriteTimeout() {
        return writeTimeout;
    }

    /**
     * Get the current used write timeout in milliseconds
     *
     * @return the current used write timeout in milliseconds
     */
    int getConnectionTimeout() {
        return connectionTimeout;
    }


    /**
     * Get a {@link GitlabIssue.Query} that can be used to query issues of the current authenticated user
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
     * Get a {@link GitlabUser.Query} that can be used to query users
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
     * Get a {@link GitlabProject.Query} that can be used to query projects of the current authenticated user
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
     * Get a {@link GitlabMergeRequest.Query} that can be used to query merge requests of the current authenticated user
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
     * Get a {@link GitlabProject.UserQuery} that can be used to query projects owned by the given user
     * that are accessible by the current authenticated user
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
     * Get the project based on the given projectId
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * <p>
     * GET /projects/:id
     *
     * @param projectId the ID of the a project
     * @return the {@link GitlabProject} of the given projectId
     */
    public GitlabProject getProject(int projectId) {

        return httpClient.get("/projects/" + projectId, GitlabProject.class);
    }

    /**
     * Get the project based on the given a namespace and the project path
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * <p>
     * GET /projects/:namespace/:project_path
     *
     * @param namespace   namespace of the project
     * @param projectPath path of the project
     * @return the {@link GitlabProject} of the given namespace and path
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
     * Create a new project instance with a given name
     *
     * @param name Name for the new project
     * @return a new {@link GitlabProject} instance with the given name
     */
    public GitlabProject newProject(String name) {
        return new GitlabProject(name).withHttpClient(httpClient);
    }

    /**
     * Get the user based on the given userId
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
     * <p>
     * GET /users
     *
     * @param userId id of the user
     * @return Get {@link GitlabUser} with the given userId
     */
    public GitlabUser getUser(int userId) {
        return httpClient.get("/users/" + userId, GitlabUser.class);
    }

    /**
     * Get the current authenticated user
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-current-user-for-normal-users
     * <p>
     * GET /user
     *
     * @return the current authenticated {@link GitlabUser}
     */
    public GitlabUser getCurrentUser() {
        return httpClient.get("/user", GitlabUser.class);
    }


}
