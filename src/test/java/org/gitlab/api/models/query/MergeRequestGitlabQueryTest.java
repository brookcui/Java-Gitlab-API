package org.gitlab.api.models.query;

import org.gitlab.api.models.GitlabAPIClient;
import org.junit.jupiter.api.Test;

class MergeRequestGitlabQueryTest {
    private static final GitlabAPIClient CLIENT = GitlabAPIClient
            .fromAccessToken("https://gitlab.com", (System.getenv("TOKEN")));

    @Test
    public void test() {
//        MergeRequestQuery query = new MergeRequestQuery(CLIENT.getConfig()).withCreatedBefore(LocalDateTime.now())
//                                                         .withLabels(Collections.singletonList("aaaaa"));
//        List<GitlabMergeRequest> mrs = CLIENT.query(query);
//        mrs.forEach(System.out::println);
    }

}