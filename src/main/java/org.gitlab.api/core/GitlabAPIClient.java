package core;

import models.GitlabIssue;
import models.GitlabProject;
import models.GitlabUser;

import java.io.IOException;
import java.util.List;

public class GitlabAPIClient {
    private final String endpoint;
    private AuthMethod authMethod;
    private final String oauth2Token;
    private final String accessToken;
    private final String username;
    private final String password;
    private boolean useHeader;

    public static class Builder {
        private final String endpoint;
        private AuthMethod authMethod;
        private String oauth2Token;
        private String accessToken;
        private String username;
        private String password;
        private boolean useHeader;

        public Builder(String endpoint) {
            this.endpoint = endpoint;
        }

        public Builder withOAuth2Token(String oauth2Token) {
            this.authMethod = AuthMethod.OAuth2;
            this.oauth2Token = oauth2Token;
            return this;
        }
        public Builder withAccessToken(String accessToken) {
            this.authMethod = AuthMethod.AccessToken;
            this.accessToken = accessToken;
            return this;
        }

        public Builder withPassword(String username, String password) {
            this.authMethod = AuthMethod.Password;
            this.username = username;
            this.password = password;
            return this;
        }

        public Builder useHeader() {
            this.useHeader = true;
            return this;
        }

        public GitlabAPIClient build() {
            return new GitlabAPIClient(this);
        }
    }

    private GitlabAPIClient(Builder builder) {
        this.endpoint = builder.endpoint;
        this.authMethod = builder.authMethod;
        this.oauth2Token = builder.oauth2Token;
        this.accessToken = builder.accessToken;
        this.username = builder.username;
        this.password = builder.password;
        this.useHeader = builder.useHeader;
    }

    /*
     * Issues
     */

    // Returns all issues the authenticated user has access to.
    public List<GitlabIssue> getAllIssues() throws IOException {
        return null; // TODO
    }

    public GitlabIssue getSingleIssue(int issueId) throws IOException {
        return null; // TODO
    }

    /*
     * Projects
     */

    // "get" == "List" in Gitlab API
    public List<GitlabProject> getAllProjects() throws IOException {
        return null; // TODO
    }

    public List<GitlabProject> getUserProjects(GitlabUser user) throws IOException {
        return null; // TODO
    }

    public GitlabProject getSingleProject(int projectId) throws IOException {
        return null; // TODO
    }

    public void createProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    // FIXME: do we need to create a project like this?
    // FIXME: also, naming here can be tricky, and we should not name it like createProject().
    public GitlabProject.Builder buildProject(String projectName) throws IOException {
        return new GitlabProject.Builder(projectName);
    }

    public void createProjectForUser(GitlabProject project, GitlabUser user) throws IOException {
        return; // TODO
    }

    // "update" == "Edit" in Gitlab API
    public void updateProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    public void forkProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    public void deleteProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    /*
     * Users
     */

    /*
     * methods for create/edit/delete users are intentionally left blank since
     * they are only available for administrators, but normal users for this
     * API cannot have administrator access.
     */

    public List<GitlabUser> getAllUsers() throws IOException {
        return null; // TODO
    }

    public GitlabUser getSingleUser(int userId) throws IOException {
        return null; // TODO
    }

    // FIXME: or name with getCurrentAuthenticatedUser
    public GitlabUser getCurrentUser() throws IOException {
        return null; // TODO
    }
}
