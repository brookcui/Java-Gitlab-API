package org.gitlab.api.models.query;

import org.gitlab.api.models.GitlabIssue;
import org.gitlab.api.models.GitlabProject;

import java.time.LocalDateTime;

public class ProjectQuery extends NewQuery<GitlabProject> {

    public ProjectQuery() {
        super(GitlabProject[].class);
    }

    public ProjectQuery withArchived(boolean archived) {
        appendBoolean("archived", archived);
        return this;
    }


    public ProjectQuery withIdAfter(int idAfter) {
        appendInt("id_after", idAfter);
        return this;
    }

    public ProjectQuery withIdBefore(int idBefore) {
        appendInt("id_before", idBefore);
        return this;
    }

    public ProjectQuery withLastActivityAfter(LocalDateTime lastActivityAfter) {
        appendDateTime("last_activity_after", lastActivityAfter);
        return this;
    }

    public ProjectQuery withLastActivityBefore(LocalDateTime lastActivityBefore) {
        appendDateTime("last_activity_before", lastActivityBefore);
        return this;
    }

    public ProjectQuery withMembership(boolean membership) {
        appendBoolean("membership", membership);
        return this;
    }

    public ProjectQuery withMinAccessLevel(int minAccessLevel) {
        appendInt("min_access_level", minAccessLevel);
        return this;
    }

    public ProjectQuery withOrderBy(String orderBy) {
        appendString("order_by", orderBy);
        return this;
    }

    public ProjectQuery withOwned(boolean owned) {
        appendBoolean("owned", owned);
        return this;
    }

    public ProjectQuery withRepositoryChecksumFailed(boolean repositoryChecksumFailed) {
        appendBoolean("repository_checksum_failed", repositoryChecksumFailed);
        return this;
    }

    public ProjectQuery withRepositoryStorage(String repositoryStorage) {
        appendString("repository_storage", repositoryStorage);
        return this;
    }

    public ProjectQuery withSearchNamespaces(boolean searchNamespaces) {
        appendBoolean("search_namespaces", searchNamespaces);
        return this;
    }

    public ProjectQuery withSearch(String search) {
        appendString("search", search);
        return this;
    }

    public ProjectQuery withSimple(boolean simple) {
        appendBoolean("simple", simple);
        return this;
    }

    public ProjectQuery withSort(String sort) {
        appendString("sort", sort);
        return this;
    }

    public ProjectQuery withStarred(boolean starred) {
        appendBoolean("starred", starred);
        return this;
    }

    public ProjectQuery withStatistics(boolean statistics) {
        appendBoolean("statistics", statistics);
        return this;
    }

    public ProjectQuery withVisibility(String visibility) {
        appendString("visibility", visibility);
        return this;
    }

    public ProjectQuery withCheckSumFailed(boolean checkSumFailed) {
        appendBoolean("wiki_checksum_failed", checkSumFailed);
        return this;
    }

    public ProjectQuery withCustomAttributes(boolean customAttributes) {
        appendBoolean("with_custom_attributes", customAttributes);
        return this;
    }

    public ProjectQuery withIssuesEnabled(boolean issuesEnabled) {
        appendBoolean("with_issues_enabled", issuesEnabled);
        return this;
    }

    public ProjectQuery withMergeRequestsEnabled(boolean mergeRequestsEnabled) {
        appendBoolean("with_merge_requests_enabled", mergeRequestsEnabled);
        return this;
    }

    public ProjectQuery withProgrammingLanguage(String programmingLanguage) {
        appendString("with_programming_language", programmingLanguage);
        return this;
    }

    @Override
    public String getUrlPrefix() {
        return "/projects";
    }


}
