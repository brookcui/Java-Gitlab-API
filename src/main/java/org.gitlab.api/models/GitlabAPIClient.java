package org.gitlab.api.models;

import org.gitlab.api.core.AuthMethod;
import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class GitlabAPIClient {
    private final Config config;

    public static GitlabAPIClient fromOAuth2Token(String endpoint, String oauth2Token) {
        return new GitlabAPIClient(endpoint, oauth2Token, AuthMethod.OAUTH2);
    }

    public static GitlabAPIClient fromAccessToken(String endpoint, String accessToken) {
        return new GitlabAPIClient(endpoint, accessToken, AuthMethod.ACCESS_TOKEN);
    }


    private GitlabAPIClient(String endpoint, String token, AuthMethod authMethod) {
        config = new Config(endpoint, token, authMethod);
    }

    public Config getConfig() {
        return config;
    }


    public GitlabIssue.Query issues() {
        return new GitlabIssue.Query(config);
    }

    public GitlabUser.Query users() {
        return new GitlabUser.Query(config);
    }

    public GitlabProject.Query projects() {
        return new GitlabProject.Query(config);
    }

    public List<GitlabProject> getUserProjects(String username) {
        return GitlabHttpClient.getList(config, String.format("/users/%s/projects", username), GitlabProject[].class);
    }

    /**
     * GET /projects/:id
     *
     * @param projectId
     * @return

     */
    public GitlabProject getProject(int projectId) {
        return GitlabHttpClient.get(config, "/projects/" + projectId, GitlabProject.class);
    }

    /**
     * GET /projects/:id
     *
     * @param namespace   -
     * @param projectPath - username%2FprojectPath
     * @return

     */
    public GitlabProject getProject(String namespace, String projectPath) {
        try {
            return GitlabHttpClient.get(config, "/projects/" + URLEncoder.encode(
                    namespace + "/" + projectPath, "UTF-8"), GitlabProject.class);
        } catch (UnsupportedEncodingException e) {
            throw new GitlabException(e);
        }
    }

    public GitlabProject newProject(String name) {
        return new GitlabProject(name).withConfig(config);
    }


    public GitlabUser getUser(int userId) {
        return GitlabHttpClient.get(config, "/users/" + userId, GitlabUser.class);
    }

    // FIXME: or name with getCurrentAuthenticatedUser
    public GitlabUser getCurrentUser() {
        return GitlabHttpClient.get(config, "/user", GitlabUser.class);
    }

}
