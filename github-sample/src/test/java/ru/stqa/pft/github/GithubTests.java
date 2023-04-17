package ru.stqa.pft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

public class GithubTests {

    @Test
    public void testCommits() {
        Github github = new RtGithub("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");
        RepoCommits commits = github.repos()
                .get(new Coordinates.Simple("Valeria Reshetina", "java_pft_15_04_23")).commits();
        for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            System.out.println(commit);
        }
    }
}
