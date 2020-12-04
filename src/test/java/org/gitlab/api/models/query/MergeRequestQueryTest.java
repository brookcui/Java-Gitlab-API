package org.gitlab.api.models.query;

import org.gitlab.api.core.GitlabAPIClient;
import org.gitlab.api.models.GitlabMergeRequest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

class MergeRequestQueryTest {
    private static final GitlabAPIClient CLIENT = new GitlabAPIClient
            .Builder("https://gitlab.com")
            .withAccessToken(System.getenv("TOKEN"))
            .build();

    @Test
    public void test() throws IOException {
        MergeRequestQuery query = new MergeRequestQuery(CLIENT.getConfig()).withCreatedBefore(LocalDateTime.now())
                                                         .withLabels(Collections.singletonList("aaaaa"));
        List<GitlabMergeRequest> mrs = CLIENT.query(query);
        mrs.forEach(System.out::println);
    }

}