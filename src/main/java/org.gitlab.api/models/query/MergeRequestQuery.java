package org.gitlab.api.models.query;

import org.gitlab.api.models.GitlabMergeRequest;

import java.time.LocalDateTime;
import java.util.List;

public class MergeRequestQuery extends NewQuery<GitlabMergeRequest> {
    public MergeRequestQuery() {
        super(GitlabMergeRequest[].class);
    }

    public MergeRequestQuery withState(String state) {
        appendString("state", state);
        return this;
    }

    public MergeRequestQuery withOrderBy(String orderBy) {
        appendString("order_by", orderBy);
        return this;
    }

    public MergeRequestQuery withSort(String sort) {
        appendString("sort", sort);
        return this;
    }

    public MergeRequestQuery withMilestone(String milestone) {
        appendString("milestone", milestone);
        return this;
    }

    public MergeRequestQuery withView(String view) {
        appendString("view", view);
        return this;
    }

    public MergeRequestQuery withLabels(List<String> labels) {
        appendStrings("labels", labels);
        return this;
    }

    public MergeRequestQuery withWithLabelsDetails(boolean withLabelsDetails) {
        appendBoolean("with_labels_details", withLabelsDetails);
        return this;
    }

    public MergeRequestQuery withWithMergeStatusRecheck(boolean withMergeStatusRecheck) {
        appendBoolean("with_merge_status_recheck", withMergeStatusRecheck);
        return this;
    }

    public MergeRequestQuery withCreatedAfter(LocalDateTime createdAfter) {
        appendDateTime("created_after", createdAfter);
        return this;
    }

    public MergeRequestQuery withCreatedBefore(LocalDateTime createdBefore) {
        appendDateTime("created_before", createdBefore);
        return this;
    }

    public MergeRequestQuery withUpdatedAfter(LocalDateTime updatedAfter) {
        appendDateTime("updated_after", updatedAfter);
        return this;
    }

    public MergeRequestQuery withUpdatedBefore(LocalDateTime updatedBefore) {
        appendDateTime("updated_before", updatedBefore);
        return this;
    }

    public MergeRequestQuery withScope(String scope) {
        appendString("scope", scope);
        return this;
    }

    public MergeRequestQuery withAuthorId(int authorId) {
        appendInt("author_id", authorId);
        return this;
    }

    public MergeRequestQuery withAuthorUsername(String authorUsername) {
        appendString("author_username", authorUsername);
        return this;
    }

    public MergeRequestQuery withAssigneeId(int assigneeId) {
        appendInt("assignee_id", assigneeId);
        return this;
    }

    public MergeRequestQuery withApproverIds(List<Integer> approverIds) {
        appendInts("approver_ids", approverIds);
        return this;
    }

    public MergeRequestQuery withApprovedByIds(List<Integer>  approvedByIds) {
        appendInts("approved_by_ids", approvedByIds);
        return this;
    }

    public MergeRequestQuery withMyReactionEmoji(String myReactionEmoji) {
        appendString("my_reaction_emoji", myReactionEmoji);
        return this;
    }

    public MergeRequestQuery withSourceBranch(String sourceBranch) {
        appendString("source_branch", sourceBranch);
        return this;
    }

    public MergeRequestQuery withTargetBranch(String targetBranch) {
        appendString("target_branch", targetBranch);
        return this;
    }

    public MergeRequestQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    public MergeRequestQuery withIn(String in) {
        appendString("in", in);
        return this;
    }

    public MergeRequestQuery withWip(String wip) {
        appendString("wip", wip);
        return this;
    }

    public MergeRequestQuery withNot(String not) {
        appendString("not", not);
        return this;
    }

    public MergeRequestQuery withEnvironment(String environment) {
        appendString("environment", environment);
        return this;
    }

    public MergeRequestQuery withDeployedBefore(LocalDateTime deployedBefore) {
        appendDateTime("deployed_before", deployedBefore);
        return this;
    }

    public MergeRequestQuery withDeployedAfter(LocalDateTime deployedAfter) {
        appendDateTime("deployed_after", deployedAfter);
        return this;
    }

    @Override
    public String getUrlPrefix() {
        return "/merge_requests";
    }
}
