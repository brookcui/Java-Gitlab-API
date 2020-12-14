package core;
import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.*;
import java.util.List;

public class GitlabIssueExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient CLIENT = new GitlabAPIClient
                .Builder("https://gitlab.com")
                .withAccessToken(System.getenv("TOKEN"))
                .build();
        GitlabProject project = CLIENT.newProject("example-project").create();

        // create a new issue
        GitlabIssue issue1 = project.newIssue("issue1").create();

        // update issue
        issue1.withDescription("description added").update();

        // get issue
        issue1 = project.getIssue(issue1.getIid());

        // delete issue
        issue1.delete();

        // query all issues on Gitlab visible to current client
        List<GitlabIssue> issues = CLIENT.getIssuesQuery().query();
        System.out.println("Title of the first issue is " + issues.get(0).getTitle());

        // query all issues under a project
        List<GitlabIssue> issuesInProject = project.getIssuesQuery().query();
        for (GitlabIssue issue : issuesInProject) {
            System.out.println("IssueID" +issue.getIid() + " : "+ issue.getTitle());
        }

        // query specific issues under a project
        List<GitlabIssue> issuesDueWithinWeek = project.getIssuesQuery().withDueDate("week").query();
        System.out.println("Issues due within a week:");
        for (GitlabIssue issue : issuesDueWithinWeek) {
            System.out.println("IssueID" +issue.getIid() + " : "+ issue.getTitle());
        }
        project.delete();

    }
}