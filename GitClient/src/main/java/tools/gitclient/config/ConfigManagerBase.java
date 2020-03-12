package tools.gitclient.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ConfigManagerBase {
    public abstract String getConfigName();

    private static String APP_HOME_DIR = ".GitClient";
    private static String CONFIG_DELIMITER = "=";

    private File getAppHomeDir() {
        String home = System.getProperty("user.home");
        return new File(home, APP_HOME_DIR);
    }

    Path getConfigPath() {
        String configName = getConfigName();
        return Paths.get(getAppHomeDir().getPath(), configName);
    }

    public List<String> readConfig() {
        Path path = getConfigPath();
        if (!Files.exists(path)) {
            return null;
        }

        List<String> list = null;
        try {
            list = Files.readAllLines(path);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
            return null;
        }

        if (list.isEmpty()) {
            return null;
        }

        return list;
    }

    public void writeConfig(List<String> list) {
        Path appHomeDir = Paths.get(getAppHomeDir().getPath());
        if (!Files.exists(appHomeDir)) {
            try {
                Files.createDirectories(appHomeDir);
            } catch (IOException e) {
                // TODO 自動生成された catch ブロック
                e.printStackTrace();
                return ;
            }
        }

        Path configPath = getConfigPath();
        try {
            Files.write(configPath, list);
        } catch (IOException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

    public void addConfig(String config) {
        List<String> list = readConfig();

        if (list==null) {
            list = new ArrayList<>();
        }

        list = list.stream()
                .filter(v -> {
                    if (v.indexOf(CONFIG_DELIMITER)==-1) {
                        return !v.equals(config);
                    }
                    v = v.substring(0, v.indexOf(CONFIG_DELIMITER));
                    return !config.startsWith(v);
                })
                .collect(Collectors.toList());
        list.add(config);
        writeConfig(list);
    }

    public void clearConfig() {
        writeConfig(new ArrayList<String>());
    }

    public static String base64Decode(String value) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] b = decoder.decode(value);
        return new String(b);
    }

    public static String base64Encode(String value) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(value.getBytes());
    }

    public static String[] split(String value, char delimiter) {
        if (value.length()==0) {
            return new String[0];
        }

        int from = 0;
        int to = 1;
        List<String> list = new ArrayList<>();

        while(from<=value.length()) {
            if (value.indexOf(delimiter, to)==-1) {
                to = value.length();
            } else {
                to = value.indexOf(delimiter, to);
            }

            String data = value.substring(from, to);
            if (data.length()==1 && data.charAt(0)==delimiter) {
                data = "";
                to--;
            }

            list.add(data);
            from = to+1;
            to = from+1;
        }

        return list.toArray(new String[list.size()]);
    }

}
