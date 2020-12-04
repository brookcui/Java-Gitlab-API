package org.gitlab.api.models.query;

import org.gitlab.api.http.Config;
import org.gitlab.api.models.GitlabIssue;

import java.time.LocalDateTime;
import java.util.List;

public class IssueQuery extends NewQuery<GitlabIssue> {

    public IssueQuery(Config config) {
        super(GitlabIssue[].class, config);
    }

    public IssueQuery withAssigneeId(int assigneeId) {
        appendInt("assignee_id", assigneeId);
        return this;
    }

    public IssueQuery withAssigneeUsernames(List<String> assigneeUsernames) {
        appendStrings("assignee_username", assigneeUsernames);
        return this;
    }

    public IssueQuery withAuthorId(int authorId) {
        appendInt("author_id", authorId);
        return this;
    }

    public IssueQuery withAuthorUsername(String authorUsername) {
        appendString("author_username", authorUsername);
        return this;
    }

    public IssueQuery withConfidential(boolean confidential) {
        appendBoolean("confidential", confidential);
        return this;
    }

    public IssueQuery withCreatedAfter(LocalDateTime createdAfter) {
        appendDateTime("created_after", createdAfter);
        return this;
    }

    public IssueQuery withCreatedBefore(LocalDateTime createdBefore) {
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
    public IssueQuery withDueDate(String dueDate) {
        appendString("due_date", dueDate);
        return this;
    }

    public IssueQuery withIids(List<Integer> iids) {
        appendInts("iids", iids);
        return this;

    }

    public IssueQuery withIn(String in) {
        appendString("in", in);
        return this;

    }

    public IssueQuery withIterationId(int iterationId) {
        appendInt("iteration_id", iterationId);
        return this;

    }

    public IssueQuery withIterationTitle(String iterationTitle) {

        appendString("iteration_title", iterationTitle);
        return this;

    }

    public IssueQuery withMilestone(String milestone) {
        appendString("milestone", milestone);
        return this;

    }

    public IssueQuery withLabels(List<String> labels) {
        appendStrings("labels", labels);
        return this;

    }

    public IssueQuery withMyReactionEmoji(String myReactionEmoji) {
        appendString("my_reaction_emoji", myReactionEmoji);
        return this;
    }

    public IssueQuery withNonArchived(boolean nonArchived) {
        appendBoolean("non_archived", nonArchived);
        return this;
    }

    public IssueQuery withNot(String not) {
        appendString("not", not);
        return this;
    }

    public IssueQuery withOrderBy(String orderBy) {
        appendString("order_by", orderBy);
        return this;
    }

    public IssueQuery withScope(String scope) {
        appendString("scope", scope);
        return this;
    }

    public IssueQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    public IssueQuery withSort(String sort) {
        appendString("sort", sort);
        return this;
    }

    public IssueQuery withUpdatedAfter(LocalDateTime updatedAfter) {
        appendDateTime("updated_after", updatedAfter);
        return this;
    }

    public IssueQuery withUpdatedBefore(LocalDateTime updatedBefore) {
        appendDateTime("updated_before", updatedBefore);
        return this;
    }

    public IssueQuery withWeight(int weight) {
        appendInt("weight", weight);
        return this;
    }

    public IssueQuery withLabelsDetails(boolean labelsDetails) {
        appendBoolean("with_labels_details", labelsDetails);
        return this;
    }



    @Override
    public String getUrlPrefix() {
        return "/issues";
    }

}
