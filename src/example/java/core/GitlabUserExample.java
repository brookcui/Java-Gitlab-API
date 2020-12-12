package core;

import org.gitlab.api.core.*;
import java.util.List;

public class GitlabUserExample {
    public static void main(String[] args) {
        // connect to Gitlab via access token
        GitlabAPIClient CLIENT =
                GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
        GitlabProject project = CLIENT.newProject("example-project").create();
        // get current user info
        GitlabUser current = CLIENT.getCurrentUser();
        System.out.println("current user is " + current.getUsername()
                + ", created at " + current.getCreatedAt().toString());

        // query all visible active users and get their email
        List<GitlabUser> users = CLIENT.users().withActive(true).query();
        for (GitlabUser user : users) {
            System.out.println(user.getUsername() + " " + user.getPublicEmail());
        }

        // query all users in a project and get a list of their projects
        List<GitlabUser> usersInProject = project.users().query();
        for (GitlabUser user : usersInProject) {
            List<GitlabProject> projects = CLIENT.getUserProjects(user.getUsername());
            System.out.println(user.getUsername() + " " + projects.size() + " projects");
            for (GitlabProject p : projects) {
                System.out.print(p.getName() + ",");
            }
            System.out.println();
        }
        project.delete();


    }
}
