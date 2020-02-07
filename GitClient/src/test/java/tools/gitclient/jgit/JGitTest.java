package tools.gitclient.jgit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

public class JGitTest {
    private static String TEST_REPOSITORY = "test_repository";

    @Before
    public void before() throws IOException {
        Path localRepositoryDir = Paths.get(TEST_REPOSITORY);
        deleteFile(localRepositoryDir);
        Files.createDirectories(localRepositoryDir);
    }

    public void deleteFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            return;
        }

        if (!Files.isDirectory(path)) {
            Files.deleteIfExists(path);
            return;
        }

        // ディレクトリ内を再帰
        Files.list(path)
            .forEach(p -> {
                try {
                    deleteFile(p);
                } catch (IOException e) {
                    // TODO 自動生成された catch ブロック
                    e.printStackTrace();
                }
            });

        // ディレクトリを削除
        Files.deleteIfExists(path);
    }

    @Test
    public void createLocalRepositoryTest() {
//        Repository repository = = new FileRepositoryBuilder()
//                .setGitDir(local)
//                .build();
    }

}
