package org.gitlab.api.core.query;

import org.gitlab.api.GitlabAPIClient;
import org.junit.jupiter.api.Test;

class MergeRequestGitlabQueryTest {
    private static final GitlabAPIClient CLIENT =
            new GitlabAPIClient.Builder("https://gitlab.com").withAccessToken(System.getenv("TOKEN")).build();

    @Test
    public void test() {
//        MergeRequestQuery query = new MergeRequestQuery(CLIENT.getConfig()).withCreatedBefore(LocalDateTime.now())
//                                                         .withLabels(Collections.singletonList("aaaaa"));
//        List<GitlabMergeRequest> mrs = CLIENT.query(query);
//        mrs.forEach(System.out::println);
    }

}