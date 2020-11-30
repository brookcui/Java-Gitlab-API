package org.gitlab.api.core;

import org.gitlab.api.http.Config;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabComponent;
import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabUser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class GitlabAPIClient extends GitlabComponent {


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

//        public Builder withPassword(String username, String password) {
//            this.authMethod = AuthMethod.PASSWORD;
//            this.username = username;
//            this.password = password;
//            return this;
//        }

        public GitlabAPIClient build() {
            return new GitlabAPIClient(this);
        }
    }

    private GitlabAPIClient(Builder builder) {
        super(new GitlabHTTPRequestor(new Config(builder.endpoint, builder.token, builder.authMethod)));
    }

    /*
     * Issues
     */

    // Returns all issues the authenticated user has access to.
    public List<GitlabIssue> getAllIssues() throws IOException {
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

    /**
     * GET /projects/:id
     *
     * @param projectId
     * @return
     * @throws IOException
     */
    public GitlabProject getProject(int projectId) throws IOException {
        return getHTTPRequestor().get("/projects/" + projectId, GitlabProject.class)
                                 .withHTTPRequestor(getHTTPRequestor());
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
        return getHTTPRequestor()
                .get("/projects/" + URLEncoder.encode(namespace + "/" + projectPath, "UTF-8"), GitlabProject.class)
                .withHTTPRequestor(getHTTPRequestor());
    }

    // Deprecated. Instead, use newProject(). See below.
    @Deprecated
    public void createProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    public GitlabProject newProject(String name) {
        return new GitlabProject(name).withHTTPRequestor(getHTTPRequestor());
    }

    // FIXME: do we need to create a project like this?
    // FIXME: also, naming here can be tricky, and we should not name it like createProject().
    //
    // Client Code:
    //
    // User might just want to obtain a project instance:
    // GitlabProject project = client.newProject("abcd"); // newProject returns a newly created GitlabProject object
    // project.withDescription("").create(); // withDescription is a setter GitlabProject
    //
    // User might also want to talk the the server and actually a create a project on gitlab
    // GitlabProject project = client.newProject("abcd").withDescription("what a good project").create();
    // project.withDescription("desc").update();
    // project.delete();
    // GitlabIssue issue = project.newIssue("a bug").withDescription("a big bug").create()
    //


    @Deprecated
    public void createProjectForUser(GitlabProject project, GitlabUser user) throws IOException {
        return; // TODO
    }


    @Deprecated
    // Deprecated. Added to Project class, project.update()
    // "update" == "Edit" in Gitlab API
    public void updateProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    @Deprecated
    // add this method to Project class, project.fork()
    public void forkProject(GitlabProject project) throws IOException {
        return; // TODO
    }

    @Deprecated
    // Deprecated, Added to Project class, project.delete()
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

    // FIXME: do normal users need get all users?
    // for admin
    @Deprecated
    public List<GitlabUser> getAllUsers() throws IOException {
        return null; // TODO
    }

    public GitlabUser getUser(int userId) throws IOException {
        return getHTTPRequestor().get("/users/" + userId, GitlabUser.class).withHTTPRequestor(getHTTPRequestor());
    }

    // FIXME: or name with getCurrentAuthenticatedUser
    public GitlabUser getCurrentUser() throws IOException {
        return getHTTPRequestor().get("/user", GitlabUser.class).withHTTPRequestor(getHTTPRequestor());
    }
}
