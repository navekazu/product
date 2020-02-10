package tools.gitclient.jgit;

import java.io.File;
import java.io.IOException;

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
    public void createLocalRepositoryTest() throws IOException {
        File repoDir = new File(new File(TEST_REPOSITORY, "test01"), ".git");
        repoDir.mkdirs();
        Repository newlyCreatedRepo = FileRepositoryBuilder.create(repoDir);
        //Repository repository = new FileRepositoryBuilder()
        //        .setGitDir(repoDir)
        //        .build();
    }

}
