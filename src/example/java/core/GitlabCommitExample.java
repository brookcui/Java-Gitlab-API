package core;
import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.*;
import java.util.List;

public class GitlabCommitExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient client = new GitlabAPIClient
                .Builder("https://gitlab.com")
                .withAccessToken(System.getenv("TOKEN"))
                .build();
        GitlabProject project = client.newProject("example-project").create();

        // get a specific commit
        String sha = "some valid sha";
        project.getCommit(sha);

        // query all merge requests under a project
        List<GitlabCommit> commitsInProject = project.getCommitsQuery().withRefName("master").query();
        System.out.println(project.getName() + " has " + commitsInProject.size() + " commits under master.");
    }
}
