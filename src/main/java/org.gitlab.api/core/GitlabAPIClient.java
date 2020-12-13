package org.gitlab.api.core;

import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * This class is the top level of all {@link GitlabComponent} and can be created with either OAuth2 token
 * or access token from gitlab.
 */
public class GitlabAPIClient {
    private final Config config;

    private GitlabAPIClient(String endpoint, String token, AuthMethod authMethod) {
        config = new Config(endpoint, authMethod, token);
    }

    /**
     * Static factory to obtain an instance of GitlabAPIClient with OAuth2 Token.
     *
     * @param endpoint    base url of the client
     * @param oauth2Token Oauth2 token for user to authenticate with Gitlab
     * @return A GitlabAPIClient obtained from the OAuth2 Token
     */
    public static GitlabAPIClient fromOAuth2Token(String endpoint, String oauth2Token) {
        return new GitlabAPIClient(endpoint, oauth2Token, AuthMethod.OAUTH2);
    }

    /**
     * Static factory to obtain an instance of GitlabAPIClient with access Token.
     *
     * @param endpoint    base url of the client
     * @param accessToken access token for user to authenticate with Gitlab
     * @return A GitlabAPIClient obtained from the access Token
     */
    public static GitlabAPIClient fromAccessToken(String endpoint, String accessToken) {
        return new GitlabAPIClient(endpoint, accessToken, AuthMethod.ACCESS_TOKEN);
    }


    /**
     * Get all the issues that the current {@link GitlabAPIClient} are authorized to get
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/issues.html#list-issues
     * GET /issues
     *
     * @return list of {@link GitlabIssue} that the current {@link GitlabAPIClient} are authorized to get
     */
    public GitlabIssue.Query issues() {
        return new GitlabIssue.Query(config);
    }

    /**
     * Get all the users that the current {@link GitlabAPIClient} are authorized to get
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html#list-users
     * GET /users
     *
     * @return list of {@link GitlabUser} that the current {@link GitlabAPIClient} are authorized to get
     */
    public GitlabUser.Query users() {
        return new GitlabUser.Query(config);
    }

    /**
     * Get all the projects that the current {@link GitlabAPIClient} are authorized to get
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-all-projects
     * GET /projects
     *
     * @return list of {@link GitlabProject} that the current {@link GitlabAPIClient} are authorized to get
     */
    public GitlabProject.Query projects() {
        return new GitlabProject.Query(config);
    }

    /**
     * Get all the merge requests that the current {@link GitlabAPIClient} are authorized to get
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/merge_requests.html#list-merge-requests
     * GET /merge_requests
     *
     * @return list of {@link GitlabMergeRequest} that the current {@link GitlabAPIClient} are authorized to get
     */
    public GitlabMergeRequest.Query mergeRequests() {
        return new GitlabMergeRequest.Query(config);
    }


    /**
     * Get the the project based on the given projectId
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * GET /projects/:id
     *
     * @param projectId - the ID of the a project
     * @return the {@link GitlabProject} of the given projectId
     */
    public GitlabProject getProject(int projectId) {
        return GitlabHttpClient.get(config, "/projects/" + projectId, GitlabProject.class);
    }

    /**
     * Get the the project based on the given a namespace and the project path
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#get-single-project
     * GET /projects/:namespace/:project_path
     *
     * @param namespace   - namespace of the project
     * @param projectPath - path of the project
     * @return the {@link GitlabProject} of the given namespace and path
     */
    public GitlabProject getProject(String namespace, String projectPath) {
        try {
            return GitlabHttpClient.get(config, "/projects/" + URLEncoder.encode(
                    namespace + "/" + projectPath, "UTF-8"), GitlabProject.class);
        } catch (UnsupportedEncodingException e) {
            throw new GitlabException(e);
        }
    }

    /**
     * Get the all projects by user name.
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/projects.html#list-user-projects
     * GET /users/:user_id/projects
     *
     * @param username - username of all the projects
     * @return A list of {@link GitlabProject} belong to a user
     */
    public List<GitlabProject> getUserProjects(String username) {
        return GitlabHttpClient.getList(config, String.format("/users/%s/projects", username), GitlabProject[].class);
    }

    /**
     * Create a new project instance with a given name from the current GitlabAPIClient.
     *
     * @param name - Name for the new project
     * @return a new {@link GitlabProject} instance with the given name
     */
    public GitlabProject newProject(String name) {
        return new GitlabProject(name).withConfig(config);
    }

    /**
     * Get the user based on the given userId
     * <p>
     * Gitlab Web API: https://docs.gitlab.com/ee/api/users.html
     *
     * @param userId - id of the user
     * @return Get {@link GitlabUser} with the given userId
     */
    public GitlabUser getUser(int userId) {
        return GitlabHttpClient.get(config, "/users/" + userId, GitlabUser.class);
    }

    /**
     * Get the current user from the current GitlabAPIClient
     * @return {@link GitlabUser} of the current GitlabAPIClient
     */
    public GitlabUser getCurrentUser() {
        return GitlabHttpClient.get(config, "/user", GitlabUser.class);
    }

    /**
     * Get the config that is stored in current {@link GitlabAPIClient}
     *
     * @return the config with user detail
     */
    public Config getConfig() {
        return config;
    }
}
