package core;
import org.gitlab.api.core.*;

import java.util.List;

public class GitlabMergeRequestExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient CLIENT =
                GitlabAPIClient.fromAccessToken("https://gitlab.com", System.getenv("TOKEN"));
        GitlabProject project = CLIENT.newProject("example-project").create();

        GitlabBranch branch1 = project.newBranch("branch1").create("master");
        GitlabBranch branch2 = project.newBranch("branch2").create("branch1");

        project.withDefaultBranch("master").update();

        // create a new merge request
        GitlabMergeRequest req1 =
                project.newMergeRequest(branch1.getName(), project.getDefaultBranch(), "req1").create();
        GitlabMergeRequest req2 =
                project.newMergeRequest(branch2.getName(), project.getDefaultBranch(), "req2").create();

        // check merge request state (opened, closed, merged)
        System.out.println(req1.getTitle() + " status is now " + req1.getState());

        // get all commits in this merge request
        Pagination page = Pagination.getDefault();
        List<GitlabCommit> commits = req1.getAllCommits(page); //getAllCommits now return null
        System.out.println(req1.getTitle() + " has " + commits.size() + " commits.");

        // approve a request
        req1.approve();
        System.out.println(req1.getTitle() + " are approved by " + req1.getUpvotes());

        // merge a request
        req1.accept();
        System.out.println(req1.getTitle() + " status is now " + req1.getState());

        // decline a request
        req2.decline();
        System.out.println(req2.getTitle() + " are declined by " + req1.getDownvotes());

        // update a request to a new target branch
        req1.withTargetBranch(branch2.getName()).update();

        // query all merge requests
        List<GitlabMergeRequest> requests = CLIENT.mergeRequests().query();
        System.out.println("Visible merge requests: " + requests.size());
        // query all merge requests under a project
        List<GitlabMergeRequest> requestsInProject = project.mergeRequests().withState("opened").query();
        System.out.println(project.getName() + " has " + requestsInProject.size() + " open merge requests");

        // delete a request
        req2.delete();
        req1.delete();

        project.delete();


    }
}
