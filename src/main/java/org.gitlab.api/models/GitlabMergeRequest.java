package models;

import core.Pagination;

import java.io.IOException;
import java.util.List;

public class GitlabMergeRequest {

    // create a new gitlab issue
    public GitlabMergeRequest create() throws IOException {
        return this; // TODO
    }

    public GitlabMergeRequest delete() throws IOException {
        return this; // TODO
    }

    public GitlabMergeRequest update() throws IOException {
        return this; // TODO
    }

    public List<GitlabUser> getAllParticipants() throws IOException {
        return null; // TODO
    }

    public List<GitlabUser> getAllParticipants(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabCommit> getAllCommits() throws IOException {
        return null; // TODO
    }

    public List<GitlabCommit> getAllCommits(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public List<GitlabIssue> getAllIssues() throws IOException {
        return null; // TODO
    }

    public List<GitlabIssue> getAllIssues(Pagination pagination) throws IOException {
        return null; // TODO
    }

    public GitlabMergeRequest accept() throws IOException {
        return this;
    }

    public GitlabMergeRequest approve() throws IOException {
        return this;
    }

    public GitlabMergeRequest decline() throws IOException {
        return this;
    }

    //TODO: diff??

}
