package org.gitlab.api.models.query;

import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabIssue;

import java.time.LocalDateTime;
import java.util.List;

public class ProjectIssueQuery extends NewQuery<GitlabIssue> {

    private final int projectId;

    public ProjectIssueQuery(int projectId, Config config) {
        super(GitlabIssue[].class, config);
        this.projectId = projectId;
    }

    public ProjectIssueQuery withAssigneeId(int assigneeId) {
        appendInt("assignee_id", assigneeId);
        return this;
    }

    public ProjectIssueQuery withAssigneeUsernames(List<String> assigneeUsernames) {
        appendStrings("assignee_username", assigneeUsernames);
        return this;
    }

    public ProjectIssueQuery withAuthorId(int authorId) {
        appendInt("author_id", authorId);
        return this;
    }

    public ProjectIssueQuery withAuthorUsername(String authorUsername) {
        appendString("author_username", authorUsername);
        return this;
    }

    public ProjectIssueQuery withConfidential(boolean confidential) {
        appendBoolean("confidential", confidential);
        return this;
    }

    public ProjectIssueQuery withCreatedAfter(LocalDateTime createdAfter) {
        appendDateTime("created_after", createdAfter);
        return this;
    }

    public ProjectIssueQuery withCreatedBefore(LocalDateTime createdBefore) {
        appendDateTime("created_before", createdBefore);
        return this;
    }

    /**
     * Due date could be 0(no due date), overdue, week, month or next_month_and_previous_two_weeks according to
     * gitlab api.
     *
     * @param dueDate due date
     * @return
     */
    public ProjectIssueQuery withDueDate(String dueDate) {
        appendString("due_date", dueDate);
        return this;
    }

    public ProjectIssueQuery withIids(List<Integer> iids) {
        appendInts("iids", iids);
        return this;

    }

    public ProjectIssueQuery withMilestone(String milestone) {
        appendString("milestone", milestone);
        return this;

    }

    public ProjectIssueQuery withLabels(List<String> labels) {
        appendStrings("labels", labels);
        return this;

    }

    public ProjectIssueQuery withMyReactionEmoji(String myReactionEmoji) {
        appendString("my_reaction_emoji", myReactionEmoji);
        return this;
    }

    public ProjectIssueQuery withNonArchived(boolean nonArchived) {
        appendBoolean("non_archived", nonArchived);
        return this;
    }

    public ProjectIssueQuery withNot(String not) {
        appendString("not", not);
        return this;
    }

    public ProjectIssueQuery withOrderBy(String orderBy) {
        appendString("order_by", orderBy);
        return this;
    }

    public ProjectIssueQuery withScope(String scope) {
        appendString("scope", scope);
        return this;
    }

    public ProjectIssueQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    public ProjectIssueQuery withSort(String sort) {
        appendString("sort", sort);
        return this;
    }

    public ProjectIssueQuery withUpdatedAfter(LocalDateTime updatedAfter) {
        appendDateTime("updated_after", updatedAfter);
        return this;
    }

    public ProjectIssueQuery withUpdatedBefore(LocalDateTime updatedBefore) {
        appendDateTime("updated_before", updatedBefore);
        return this;
    }

    public ProjectIssueQuery withWeight(int weight) {
        appendInt("weight", weight);
        return this;
    }

    public ProjectIssueQuery withLabelsDetails(boolean labelsDetails) {
        appendBoolean("with_labels_details", labelsDetails);
        return this;
    }


    @Override
    public String getUrlPrefix() {
        return String.format("/projects/%d/issues", projectId);
    }

}
