package tools.mailer.di.container;

import tools.mailer.di.anntation.Plugin;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DIContainer {
    private static List<Plugin> pluginContainer;

    public void loadPlugin() {
        pluginContainer = new ArrayList<>();

        try {
            URL res = DIContainer.class.getResource("/");
            Path classPath = new File(res.toURI()).toPath();
            Files.walk(classPath)
                        .forEach(name -> System.out.println(name));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
