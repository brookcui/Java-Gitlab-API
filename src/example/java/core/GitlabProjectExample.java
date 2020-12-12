package core;
import org.gitlab.api.core.*;

import java.util.List;

public class GitlabProjectExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient CLIENT =
                GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
        // create a new project
        GitlabProject project = CLIENT.newProject("project1").create();

        // get all projects of a specific user
        List<GitlabProject> projects = CLIENT.getUserProjects(CLIENT.getCurrentUser().getUsername());
        for (GitlabProject p : projects) {
            System.out.println("ProjectID: " + p.getId() + " Title: " + p.getName());
        }

        // update project field, add a default branch
        project.withDefaultBranch("master").update();
        project.newBranch("branch1").create("master");

        // fork a project
        GitlabProject projectForked = project.fork();
        System.out.println("project " + projectForked.getId() + "forked from project " + project.getId());
        System.out.println("Project " +project.getId() + "is forked " + project.getForksCount() + " times");

        // query issues, branch, mergeRequest, commits under a project
        List<GitlabIssue> issues = project.issues().query();
        List<GitlabBranch> branches = project.branches().query();
        List<GitlabMergeRequest> mergeRequests = project.mergeRequests().query();
        List<GitlabCommit> commits = project.commits().query();

        System.out.println(project.getName() + " has " + issues.size() + " issues.");
        System.out.println(project.getName() + " has " + branches.size() + " branches.");
        System.out.println(project.getName() + " has " + mergeRequests.size() + " merge requests.");
        System.out.println(project.getName() + " has " + commits.size() + " commits.");

        // delete a project
        projectForked.delete();
        project.delete();
    }
}
