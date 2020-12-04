package org.gitlab.api.models.query;

import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabMergeRequest;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectMergeRequestQuery extends NewQuery<GitlabMergeRequest> {
    private final int projectId;

    public ProjectMergeRequestQuery(int projectId, Config config) {
        super(GitlabMergeRequest[].class, config);
        this.projectId = projectId;
    }

    public ProjectMergeRequestQuery withId(String id) {
        appendString("id", id);
        return this;
    }

    public ProjectMergeRequestQuery withIids(List<Integer> iids) {
        appendInts("iids[]", iids);
        return this;
    }

    public ProjectMergeRequestQuery withState(String state) {
        appendString("state", state);
        return this;
    }

    public ProjectMergeRequestQuery withOrderBy(String orderBy) {
        appendString("order_by", orderBy);
        return this;
    }

    public ProjectMergeRequestQuery withSort(String sort) {
        appendString("sort", sort);
        return this;
    }

    public ProjectMergeRequestQuery withMilestone(String milestone) {
        appendString("milestone", milestone);
        return this;
    }

    public ProjectMergeRequestQuery withView(String view) {
        appendString("view", view);
        return this;
    }

    public ProjectMergeRequestQuery withLabels(List<String> labels) {
        appendStrings("labels", labels);
        return this;
    }

    public ProjectMergeRequestQuery withWithLabelsDetails(boolean withLabelsDetails) {
        appendBoolean("with_labels_details", withLabelsDetails);
        return this;
    }

    public ProjectMergeRequestQuery withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
        appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
        return this;
    }

    public ProjectMergeRequestQuery withCreatedAfter(LocalDateTime createdAfter) {
        appendDateTime("created_after", createdAfter);
        return this;
    }

    public ProjectMergeRequestQuery withCreatedBefore(LocalDateTime createdBefore) {
        appendDateTime("created_before", createdBefore);
        return this;
    }

    public ProjectMergeRequestQuery withUpdatedAfter(LocalDateTime updatedAfter) {
        appendDateTime("updated_after", updatedAfter);
        return this;
    }

    public ProjectMergeRequestQuery withUpdatedBefore(LocalDateTime updatedBefore) {
        appendDateTime("updated_before", updatedBefore);
        return this;
    }

    public ProjectMergeRequestQuery withScope(String scope) {
        appendString("scope", scope);
        return this;
    }


    public ProjectMergeRequestQuery withAuthorId(int authorId) {
        appendInt("author_id", authorId);
        return this;
    }

    public ProjectMergeRequestQuery withAuthorUsername(String authorUsername) {
        appendString("author_username", authorUsername);
        return this;
    }

    public ProjectMergeRequestQuery withAssigneeId(int assigneeId) {
        appendInt("assignee_id", assigneeId);
        return this;
    }

    public ProjectMergeRequestQuery withApproverIds(List<Integer> approverIds) {
        appendInts("approver_ids", approverIds);
        return this;
    }

    public ProjectMergeRequestQuery withApprovedByIds(String approvedByIds) {
        appendString("approved_by_ids", approvedByIds);
        return this;
    }

    public ProjectMergeRequestQuery withMyReactionEmoji(String myReactionEmoji) {
        appendString("my_reaction_emoji", myReactionEmoji);
        return this;
    }

    public ProjectMergeRequestQuery withSourceBranch(String sourceBranch) {
        appendString("source_branch", sourceBranch);
        return this;
    }

    public ProjectMergeRequestQuery withTargetBranch(String targetBranch) {
        appendString("target_branch", targetBranch);
        return this;
    }

    public ProjectMergeRequestQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    public ProjectMergeRequestQuery withWip(String wip) {
        appendString("wip", wip);
        return this;
    }

    @Override
    public String getUrlPrefix() {
        return String.format("/projects/%d/merge_requests", projectId);
    }
}
