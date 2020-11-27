package models;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class GitlabCommit {
    private String id;
    private String shortId;
    private String title;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private LocalDateTime createdAt;
    private String message;
    private LocalDateTime committedDate;
    private LocalDateTime authoredDate;
    private List<String> parentIds;
    private String status;

    // TODO: getters for all necessary public fields
    // TODO: public constructor
    // TODO: withXXX() for all necessary public and modifiable fields

    @Override
    public String toString() {
        return title;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    // TODO: compare all fields for equals
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabCommit)) {
            return false;
        }
        GitlabCommit commit = (GitlabCommit) o;
        return commit.id == this.id;
    }
}
