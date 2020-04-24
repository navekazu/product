package tools.gitclient;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import tools.gitclient.config.CredentialsConfigManager;
import tools.gitclient.config.CredentialsConfigManager.Credentials;
import tools.gitclient.config.OpeningRepositoryConfigManager;
import tools.gitclient.config.RecentRepositoryConfigManager;
import tools.gitclient.config.RepositoryCredentialsConfigManager;
import tools.gitclient.config.RepositoryCredentialsConfigManager.RepositoryCredentials;
import tools.gitclient.ui.MainFrame;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App implements OperationMessage {
    private MainFrame mainFrame;

    public void openMainFrame() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        // MetalLookAndFeel.setCurrentTheme(new DefaultTheme());
        // LafManager.setTheme(new DarculaTheme());
        // UIManager.setLookAndFeel(DarkLaf.class.getCanonicalName());
        // LafManager.install();
        UIManager.put( "control", new Color( 128, 128, 128) );
        UIManager.put( "info", new Color(128,128,128) );
        UIManager.put( "nimbusBase", new Color( 18, 30, 49) );
        UIManager.put( "nimbusAlertYellow", new Color( 248, 187, 0) );
//        UIManager.put( "nimbusDisabledText", new Color( 128, 128, 128) );
        UIManager.put( "nimbusDisabledText", new Color( 150, 150, 150) );
        UIManager.put( "nimbusFocus", new Color(115,164,209) );
        UIManager.put( "nimbusGreen", new Color(176,179,50) );
        UIManager.put( "nimbusInfoBlue", new Color( 66, 139, 221) );
        UIManager.put( "nimbusLightBackground", new Color( 18, 30, 49) );
        UIManager.put( "nimbusOrange", new Color(191,98,4) );
        UIManager.put( "nimbusRed", new Color(169,46,34) );
        UIManager.put( "nimbusSelectedText", new Color( 255, 255, 255) );
        UIManager.put( "nimbusSelectionBackground", new Color( 104, 93, 156) );
        UIManager.put( "text", new Color( 230, 230, 230) );
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }

        mainFrame = new MainFrame(this);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        new App().openMainFrame();
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

    @Override
    public void updateCredencialsConfig() {
        mainFrame.updateCredencialsConfig();
    }

    @Override
    public RepositoryCredentials getRepositoryCredentials(String repository) {
        RepositoryCredentialsConfigManager config = new RepositoryCredentialsConfigManager();
        List<String> list = config.readConfig();

        if (list==null) {
            return null;
        }

        List<RepositoryCredentials> configList = list.stream()
                .map(s -> config.deserialize(s))
                .filter(c -> repository.equals(c.repository))
                .collect(Collectors.toList());

        if (configList.size()==0) {
            return null;
        }
        return configList.get(0);
    }

    @Override
    public void setRepositoryCredentials(RepositoryCredentials repositoryCredentials) {
        RepositoryCredentialsConfigManager config = new RepositoryCredentialsConfigManager();
        List<String> list = config.readConfig();

        List<RepositoryCredentials> configList;
        if (list==null) {
            configList = new ArrayList<>();
        } else {
            configList = list.stream()
                .map(s -> config.deserialize(s))
                .collect(Collectors.toList());
        }

        Optional<RepositoryCredentials> opt = configList.stream()
                .filter(c -> repositoryCredentials.repository.equals(c.repository))
                .findFirst();
        RepositoryCredentials rc = opt.orElse(new RepositoryCredentials());
        rc.repository = repositoryCredentials.repository;
        rc.credentials = repositoryCredentials.credentials;

        if (!opt.isPresent()) {
            configList.add(rc);
        }

        list = configList.stream()
            .map(c -> config.serialize(c))
            .collect(Collectors.toList());
        config.writeConfig(list);
    }

    @Override
    public void clearRepositoryCredentials(String repository) {
        RepositoryCredentialsConfigManager config = new RepositoryCredentialsConfigManager();
        List<String> list = config.readConfig();

        if (list==null) {
            return ;
        }

        Optional<RepositoryCredentials> opt = list.stream()
                .map(s -> config.deserialize(s))
                .filter(c -> repository.equals(c.repository))
                .findFirst();

        if (!opt.isPresent()) {
            return ;
        }

        List<RepositoryCredentials> configList = list.stream()
                .map(s -> config.deserialize(s))
                .collect(Collectors.toList());

        RepositoryCredentials rc = opt.get();
        for (int index=0; index<configList.size(); index++) {
            if (configList.get(index).repository.equals(rc.repository)) {
                configList.remove(index);
            }
        }

        list = configList.stream()
                .map(c -> config.serialize(c))
                .collect(Collectors.toList());
        config.writeConfig(list);
    }
}
