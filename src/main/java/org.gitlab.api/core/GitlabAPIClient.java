package org.gitlab.api.core;

import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabRestClient;
import org.gitlab.api.http.Query;
import org.gitlab.api.models.AuthComponent;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;
import org.gitlab.api.models.query.NewQuery;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class GitlabAPIClient implements AuthComponent {
    private final Config config;

    @Override
    public Config getConfig() {
        return null;
    }

    @Override
    public AuthComponent withConfig(Config config) {
        return null;
    }

    public static class Builder {
        private final String endpoint;
        private AuthMethod authMethod;
        private String token;


        public Builder(String endpoint) {
            this.endpoint = endpoint;
        }

        public Builder withOAuth2Token(String oauth2Token) {
            this.authMethod = AuthMethod.OAUTH2;
            this.token = oauth2Token;
            return this;
        }

        public Builder withAccessToken(String accessToken) {
            this.authMethod = AuthMethod.ACCESS_TOKEN;
            this.token = accessToken;
            return this;
        }


        public GitlabAPIClient build() {
            return new GitlabAPIClient(this);
        }
    }

    private GitlabAPIClient(Builder builder) {
        config = new Config(builder.endpoint, builder.token, builder.authMethod);
    }

    /*
     * Issues
     */

    // Returns all issues the authenticated user has access to.
    public List<GitlabIssue> getAllIssues() throws IOException {
        return GitlabRestClient.getList(config, "/issues", GitlabIssue[].class);
    }

    /*
     * Projects
     */

    // "get" == "List" in Gitlab API

    /**
     * Get <b>ALL</b> projects on Gitlab on the first page
     * It's not only the projects owned by the user, but also public projects accessible to the user
     *
     * @return
     * @throws IOException
     */
    public List<GitlabProject> getAllProjects() throws IOException {
        return GitlabRestClient.getList(config, "/projects", GitlabProject[].class);
    }

    public List<GitlabProject> getOwnedProjects() throws IOException {
        Query query = new Query().append("owned", "true");
        return GitlabRestClient.getList(config, "/projects" + query, GitlabProject[].class);
    }

    public List<GitlabProject> getUserProjects(String username) throws IOException {
        return GitlabRestClient.getList(config, String.format("/users/%s/projects", username), GitlabProject[].class);
    }

    public List<GitlabProject> getGroupProjects(String username) throws IOException {
        return GitlabRestClient.getList(config, String.format("/groups/%s/projects", username), GitlabProject[].class);
    }

    /**
     * GET /projects/:id
     *
     * @param projectId
     * @return
     * @throws IOException
     */
    public GitlabProject getProject(int projectId) throws IOException {
        return GitlabRestClient.get(config, "/projects/" + projectId, GitlabProject.class);
    }

    /**
     * GET /projects/:id
     *
     * @param namespace   -
     * @param projectPath - username%2FprojectPath
     * @return
     * @throws IOException
     */
    public GitlabProject getProject(String namespace, String projectPath) throws IOException {
        return GitlabRestClient.get(config, "/projects/" + URLEncoder.encode(
                namespace + "/" + projectPath, "UTF-8"), GitlabProject.class);
    }

    public GitlabProject newProject(String name) {
        return new GitlabProject(name).withConfig(config);
    }


    public GitlabUser getUser(int userId) throws IOException {
        return GitlabRestClient.get(config, "/users/" + userId, GitlabUser.class);
    }

    // FIXME: or name with getCurrentAuthenticatedUser
    public GitlabUser getCurrentUser() throws IOException {
        return GitlabRestClient.get(config, "/user", GitlabUser.class);
    }


    public <T extends AuthComponent> List<T> query(NewQuery<T> query) throws IOException {
        return GitlabRestClient.getList(config, query.getEntireUrl(), query.getType());
    }
}
