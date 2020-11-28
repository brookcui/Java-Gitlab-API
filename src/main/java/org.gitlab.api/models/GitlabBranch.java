package models;

import java.io.IOException;
import java.util.Objects;

public class GitlabBranch {
    private final String name;
    private boolean merged;
    private boolean isProtected; // for "protected"
    private boolean isDefault; // for "default"
    private boolean canPush;
    private String webUrl;
    private GitlabCommit commit; // corresponds to branch name or commit SHA to create branch from

    public GitlabBranch(String name, String ref) {
        this.name = name;
        // TODO: convert ref to GitlabCommit and initialize field commit
    }

    public String getName() {
        return name;
    }

    public boolean isMerged() {
        return merged;
    }

    public boolean isProtected() {
        return isProtected;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public boolean canPush() {
        return canPush;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public GitlabCommit getCommit() {
        return commit;
    }

    public GitlabBranch create() throws IOException {
        return this; // TODO
    }

    public GitlabBranch delete() throws IOException {
        return this; // TODO
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GitlabBranch)) {
            return false;
        }
        GitlabBranch that = (GitlabBranch) o;
        return merged == that.merged &&
                       isProtected == that.isProtected &&
                       isDefault == that.isDefault &&
                       canPush == that.canPush &&
                       name.equals(that.name) &&
                       webUrl.equals(that.webUrl) &&
                       commit.equals(that.commit);
    }
}
