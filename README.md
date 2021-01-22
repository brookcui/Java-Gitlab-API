# Java-Gitlab-API (Refactored)

This project is a Java wrapper library for communicating with [Gitlab v4 REST API](https://docs.gitlab.com/ee/api/). This aims to fix the broken [Java-Gitlab-API](https://github.com/timols/java-gitlab-api) and provides new features to make it easy to use and learn for Gitlab API users. This supports querying, creating, updating, and deleting Gitlab components like Project, User, Issue, Branch, Commit, Merge-Request.

Documentation is available in the form of [JavaDoc](https://apiteam4.gitlab.io/f20-project-team4/).

## Example Code

### Authentication

```java
// Build Gitlab API client with endpoint and access token and use this client to communicate with Gitlab REST API.
GitlabAPIClient client = new GitlabAPIClient
    .Builder("https://gitlab.com")
    .withAccessToken(System.getenv("TOKEN"))
    .build();

// Or, use OAuth2 token for authentication and set connection timeouts.
GitlabAPIClient client = new GitlabAPIClient
    .Builder("https://gitlab.com")
    .withOAuth2Token(System.getenv("TOKEN"))
    .withReadTimeout(500)
    .withWriteTimeout(500)
    .build();
```

### Create/Read/Update/Delete a Project

```java
// Create a new project with project name, description, issues status, and jobs status set.
GitlabProject project = client.newProject("projectName")
                                .withDescription("description")
                                .withIssuesEnabled(true)
                                .withJobsEnabled(false)
                                .create();
// Read attributes of the project.
System.out.println(project.getDescription());
System.out.println(project.getForksCount());
System.out.println(project.getStarCount());
// Update the project with specified fields.
GitlabProject updatedProject = project.withDescription("new description").update();
// Delete the project.
GitlabProject deletedProject = project.delete();
```

### Get Projects for Current User

```java
// Get all projects that are visible to current user.
// Note that current user means the owner of the token used for authentication.
// Remember this query will use default Pagination, which returns 20 items in the first page.
List<GitlabProject> projects = client.getCurrentUser().getUserProjectsQuery().query();
for (GitlabProject proj : projects) {
    System.out.println(String.format("project %d: %s", proj.getId(), proj.getName()));
}
```

### Fork a Project

```java
// Get a project by namespace.
GitlabProject project = client.getProject("api", "team4");
// Fork a project.
GitlabProject forkedProject = project.fork();
// Update the description of the forked project.
forkedProject.withDescription("new description").update();
System.out.println(String.format("Project %d is forked from project %d", forkedProject.getId(), project.getId()));
System.out.println(String.format("Project %d has been forked for %d times", project.getId(), project.getForksCount()));
```

### Query Issues

```java
// Query all issues visible to current user and obtains these issues ordered by creation date.
List<GitlabIssue> allIssues = client.getIssuesQuery().withOrderBy("created_at").query();
// Query all issues under the project.
List<GitlabIssue> allProjectIssues = project.getIssuesQuery().query();
// Query all issues visible to current user with specified pagination.
Pagination pagination = Pagination.of(3, 50);
List<GitlabIssue> issues = client.getIssuesQuery().withPagination(pagination).query();
```

### Approve/Decline a Merge Request

```java
// Get and approve or decline a merge request explicitly.
int mergeRequestId = 123;
GitlabMergeRequest req = project.getMergeRequest(mergeRequestId);
req.approve(); // to approve
req.decline(); // to decline
```

### More Examples

See more example code in [Client Code](src/example/java/core).
