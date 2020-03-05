package tools.gitclient;

import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.plaf.metal.MetalLookAndFeel;

import tools.gitclient.config.CredentialsConfigManager;
import tools.gitclient.config.CredentialsConfigManager.Credentials;
import tools.gitclient.config.OpeningRepositoryConfigManager;
import tools.gitclient.config.RecentRepositoryConfigManager;
import tools.gitclient.ui.MainFrame;
import tools.gitclient.ui.theme.DefaultTheme;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App implements OperationMessage {
    private MainFrame mainFrame;

    public void openMainFrame() {
        MetalLookAndFeel.setCurrentTheme(new DefaultTheme());

        mainFrame = new MainFrame(this);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new App().openMainFrame();
/*
        Repository repo = new FileRepositoryBuilder()
            .setGitDir(new File("C:\\repositories\\repositories-github\\omotenashi\\.git"))
            .build();

        // 参照を取得する
        Ref master = repo.getRefDatabase().findRef("master");

        // 参照の指すオブジェクトを取得する
        ObjectId masterTip = master.getObjectId();

        // Rev-parse文法を使う
        ObjectId obj = repo.resolve("HEAD^{tree}");

        // オブジェクトの生の内容をロードする
        ObjectLoader loader = repo.open(masterTip);
        loader.copyTo(System.out);
*/
    }

    @Override
    public void openRepository(File local) {
        mainFrame.addRepositoryTab(local);
    }

    @Override
    public void cloneRepository(URL remote) {
    }

    @Override
    public void startLocalRepository(File local) {
    }

    @Override
    public void addRecentOpenRepository(File local) {
        RecentRepositoryConfigManager config = new RecentRepositoryConfigManager();
        config.addConfig(local.getPath());
    }

    @Override
    public File getRecentOpenRepository() {
        List<File> list = getRecentOpenRepositoryList();

        if (list==null) {
            return null;
        }

        return list.get(list.size()-1);
    }

    @Override
    public List<File> getRecentOpenRepositoryList() {
        RecentRepositoryConfigManager config = new RecentRepositoryConfigManager();
        return stringListToFileList(config.readConfig());
    }

    @Override
    public void addOpeningRepository(File local) {
        OpeningRepositoryConfigManager config = new OpeningRepositoryConfigManager();
        config.addConfig(local.getPath());
    }

    @Override
    public List<File> getOpeningRepositoryList() {
        OpeningRepositoryConfigManager config = new OpeningRepositoryConfigManager();
        return stringListToFileList(config.readConfig());
    }

    private List<File> stringListToFileList(List<String> list) {
        if (list==null) {
            return null;
        }

        return list.stream()
                .map(f -> new File(f))
                .collect(Collectors.toList());
    }

    @Override
    public Frame getMainFrame() {
        return mainFrame;
    }

    @Override
    public List<Credentials> getCredentialsConfig() {
        CredentialsConfigManager config = new CredentialsConfigManager();
        List<String> list = config.readConfig();

        if (list==null) {
            return null;
        }

        return list.stream()
                .map(s -> config.deserialize(s))
                .collect(Collectors.toList());
    }

    @Override
    public void setCredentialsConfig(List<Credentials> list) {
        CredentialsConfigManager config = new CredentialsConfigManager();
        List<String> outputList = list.stream()
                .map(s -> config.serialize(s))
                .collect(Collectors.toList());
        config.writeConfig(outputList);
    }

}
