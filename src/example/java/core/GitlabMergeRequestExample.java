package core;
import org.gitlab.api.GitlabAPIClient;
import org.gitlab.api.*;

import java.util.List;

public class GitlabMergeRequestExample {
    public static void main(String[] args) {
        // Connect to Gitlab via access token
        GitlabAPIClient CLIENT = new GitlabAPIClient
                .Builder("https://gitlab.com")
                .withAccessToken(System.getenv("TOKEN"))
                .build();
        GitlabProject project = CLIENT.newProject("example-project").create();

        GitlabBranch branch1 = project.newBranch("branch1", "master").create();
        GitlabBranch branch2 = project.newBranch("branch2", branch1.getName()).create();

        project.withDefaultBranch("master").update();

        // create a new merge request
        GitlabMergeRequest req1 =
                project.newMergeRequest(branch1.getName(), project.getDefaultBranch(), "req1").create();
        GitlabMergeRequest req2 =
                project.newMergeRequest(branch2.getName(), project.getDefaultBranch(), "req2").create();

        // check merge request state (opened, closed, merged)
        System.out.println(req1.getTitle() + " status is now " + req1.getState());

        // get all commits in this merge request
        List<GitlabCommit> commits = req1.getAllCommits(); //getAllCommits now return null
        System.out.println(req1.getTitle() + " has " + commits.size() + " commits.");

        // get all participants in this merge request
        List<GitlabUser> participants = req1.getAllParticipants(); //getAllCommits now return null
        System.out.println(req1.getTitle() + " has " + participants.size() + " participants.");
        for (GitlabUser participant : participants) {
            System.out.println(participant.getUsername() + " " + participant.getLinkedin());
        }
        
        // approve a request
        req1.approve();

        // merge a request (empty merge request cannot be accepted)
        try {
            req1.accept();
            System.out.println(req1.getTitle() + " status is now " + req1.getState());
        } catch (GitlabException e) {
            System.out.println("Merge request cannot be performed, " + e.getMessage());
        }

        // decline a request
        req1.decline();

        // update a request to a new target branch
        req1.withTargetBranch(branch2.getName()).update();

        // query all merge requests
        List<GitlabMergeRequest> requests = CLIENT.getMergeRequestsQuery().query();
        System.out.println("Visible merge requests: " + requests.size());
        // query all merge requests under a project
        List<GitlabMergeRequest> requestsInProject = project.getMergeRequestsQuery().withState("opened").query();
        System.out.println(project.getName() + " has " + requestsInProject.size() + " open merge requests");

        // delete a request
        req2.delete();
        req1.delete();

        project.delete();


    }
}
