package core;
import org.gitlab.api.core.*;
import java.util.List;

public class GitlabCommitExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient CLIENT =
                GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
        GitlabProject project = CLIENT.newProject("example-project").create();

        // get a specific commit
        String sha = "some valid sha";
        project.getCommit(sha);

        // query all merge requests under a project
        List<GitlabCommit> commitsInProject = project.commits().withRefName("master").query();
        System.out.println(project.getName() + " has " + commitsInProject.size() + " commits under master.");
    }
}
