package tools.gitclient.jgit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.UserConfig;
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

    @Test
    public void configTest() throws IOException {
        File repoDir = new File(new File(TEST_REPOSITORY, "test01"), ".git");
        repoDir.mkdirs();

        // リポジトリ作成
        Repository newlyCreatedRepo = FileRepositoryBuilder.create(repoDir);
        newlyCreatedRepo.create();

        // config
        Config config = newlyCreatedRepo.getConfig();
        String value;
        Set<String> sections = config.getSections();
        for (String section: sections) {
//            value = config.getString(section, null, null);
//            System.out.println("section:"+section+" subsection:"+null+" name:"+null+" value:"+value);

            Set<String> subsections = config.getSubsections(section);
            for (String subsection: subsections) {
//                value = config.getString(section, subsection, null);
//                System.out.println("section:"+section+" subsection:"+subsection+" name:"+null+" value:"+value);

                Set<String> names = config.getNames(subsection);
                for (String name: names) {
                    value = config.getString(section, subsection, name);
                    System.out.println("section:"+section+" subsection:"+subsection+" name:"+name+" value:"+value);
                }
            }
        }

        String name = config.getString("user", null, "name");
        String name2 = config.get(UserConfig.KEY).getAuthorName();
        String name3 = config.get(UserConfig.KEY).getCommitterName();
        assertThat(name.length(), is(not(0)));
        assertThat(name, is(name2));
        assertThat(name, is(name3));

        String email = config.getString("user", null, "email");
        String email2 = config.get(UserConfig.KEY).getAuthorEmail();
        String email3 = config.get(UserConfig.KEY).getCommitterEmail();
        assertThat(email.length(), is(not(0)));
        assertThat(email, is(email2));
        assertThat(email, is(email3));
    }


    @Test
    public void listBranchCommandTest() throws IOException, GitAPIException {
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

        // git-flowブランチを作成
        git.branchCreate().setName("release").call();
        git.branchCreate().setName("develop").call();
        git.branchCreate().setName("feature/some_feature01").call();

        // ローカルブランチリスト取得
        List<Ref> list = git.branchList().call();
        assertThat(list.size(), is(4));
        assertThat(list.get(0).getName(), is("refs/heads/develop"));
        assertThat(list.get(1).getName(), is("refs/heads/feature/some_feature01"));
        assertThat(list.get(2).getName(), is("refs/heads/master"));
        assertThat(list.get(3).getName(), is("refs/heads/release"));
    }

    @Test
    public void statusCommandTest() throws IOException, GitAPIException {
        File repoDir = new File(new File(TEST_REPOSITORY, "test01"), ".git");
        repoDir.mkdirs();

        // リポジトリ作成
        Repository newlyCreatedRepo = FileRepositoryBuilder.create(repoDir);
        newlyCreatedRepo.create();

        try (Git git = Git.open(repoDir.getParentFile())) {
            Status status = git.status().call();

            // 追加
            Set<String> added = status.getAdded();
            assertThat(added.size(), is(0));

            File newFile = new File(new File(TEST_REPOSITORY, "test01"), "test01");
            newFile.createNewFile();

            status = git.status().call();
            added = status.getAdded();
//            assertThat(added.size(), is(1));
//            assertThat(added.iterator(), is("test01"));

            // 変更
            Set<String> changed = status.getChanged();
            assertThat(changed.size(), is(0));

        }
    }
}
