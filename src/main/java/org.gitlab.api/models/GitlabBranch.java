package models;

import java.io.IOException;
import java.util.Objects;

public class GitlabBranch {
    private String name;
    private boolean merged;
    private boolean isProtected; // for "protected"
    private boolean isDefault; // for "default"
    private boolean canPush;
    private String webUrl;
    private GitlabCommit commit;

    @Deprecated
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

    @Deprecated
    private GitlabBranch(Builder builder) {
        this.name = builder.branchName;
        // TODO: ref
    }

    // TODO: getters for all necessary public fields
    // TODO: public constructor
    // TODO: withXXX() for all necessary public and modifiable fields
    // GitlabBranch branch = project.newBranch("branch1","master").create();

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
    // TODO: compare all fields for equals
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
