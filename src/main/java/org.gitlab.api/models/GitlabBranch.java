package models;

import java.util.Objects;

public class GitlabBranch {
    private String name;
    private boolean merged;
    private boolean isProtected; // for "protected"
    private boolean isDefault; // for "default"
    private boolean canPush;
    private String webUrl;
    private GitlabCommit commit;

    public static class Builder {
        private String branchName;
        private String ref;

        public Builder(String branchName, String ref) {
            this.branchName = branchName;
            this.ref = ref;
        }

        public GitlabBranch build() {
            return new GitlabBranch(this);
        }
    }

    private GitlabBranch(Builder builder) {
        this.name = builder.branchName;
        // TODO: ref
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
        if (o == this) {
            return true;
        }
        if (!(o instanceof GitlabBranch)) {
            return false;
        }
        GitlabBranch branch = (GitlabBranch) o;
        return branch.name.equals(this.name);
    }
}
