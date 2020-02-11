package tools.gitclient.jgit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.junit.Before;
import org.junit.Test;

public class JGitTest {
    private static String TEST_REPOSITORY = "test_repository";

    @Before
    public void before() throws IOException {
        File localRepositoryDir = new File(TEST_REPOSITORY);
        deleteFile(localRepositoryDir);
        localRepositoryDir.mkdir();
    }

    public void deleteFile(File path) throws IOException {
        if (!path.exists()) {
            return;
        }

        for (File f: path.listFiles()) {
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        path.delete();
    }

    @Test
    public void createLocalRepositoryTest() throws IOException, GitAPIException {
        File repoDir = new File(new File(TEST_REPOSITORY, "test01"), ".git");
        repoDir.mkdirs();

        // リポジトリ作成
        Repository newlyCreatedRepo = FileRepositoryBuilder.create(repoDir);
        newlyCreatedRepo.create();

        // initial commit
        // コミットしないとmasterブランチがない
        // masterブランチがないと別のブランチが作れない
        Git git = new Git(newlyCreatedRepo);
        git.commit().setMessage("initial commit").call();

        // ブランチ一覧
        git = new Git(newlyCreatedRepo);
        List<Ref> call = git.branchList().call();
        assertThat(call.size(), is(1));
        assertThat(call.get(0).getName(), is("refs/heads/master"));

        // releaseブランチを作成
        git.branchCreate().setName("release").call();
        git = new Git(newlyCreatedRepo);
        call = git.branchList().call();
        assertThat(call.size(), is(2));
        assertThat(call.get(0).getName(), is("refs/heads/master"));
        assertThat(call.get(1).getName(), is("refs/heads/release"));

        // developブランチを作成
        git.branchCreate().setName("develop").call();
        git = new Git(newlyCreatedRepo);
        call = git.branchList().call();
        assertThat(call.size(), is(3));
        assertThat(call.get(0).getName(), is("refs/heads/develop"));
        assertThat(call.get(1).getName(), is("refs/heads/master"));
        assertThat(call.get(2).getName(), is("refs/heads/release"));

        // featureブランチを作成
        git.branchCreate().setName("feature/some_feature01").call();
        git = new Git(newlyCreatedRepo);
        call = git.branchList().call();
        assertThat(call.size(), is(4));
        assertThat(call.get(0).getName(), is("refs/heads/develop"));
        assertThat(call.get(1).getName(), is("refs/heads/feature/some_feature01"));
        assertThat(call.get(2).getName(), is("refs/heads/master"));
        assertThat(call.get(3).getName(), is("refs/heads/release"));
    }

}
