package ru.stqa.pft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

    @Test
    public void testCommits() throws IOException {
        Github github = new RtGithub("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");
        RepoCommits commits = github.repos()
                .get(new Coordinates.Simple("ValeriaReshetina", "java_pft_15_04_23")).commits();
        for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
            System.out.println(new RepoCommit.Smart(commit).message());
        }
    }
}
