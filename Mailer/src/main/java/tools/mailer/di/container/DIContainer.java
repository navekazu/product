package tools.mailer.di.container;

import tools.mailer.di.anntation.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DIContainer {
    private static List<Plugin> pluginContainer;
    public static void premain(String agentArgs, Instrumentation instrumentation) {

        Class[] classes = instrumentation.getAllLoadedClasses();
        for (Class cls : classes) {
            System.out.println(cls);
        }
    }
    public void loadPlugin() {
        pluginContainer = new ArrayList<>();

        System.out.println(DIContainer.class.getClassLoader());
        final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        final URL root = classLoader.getResource("/");
        System.out.println(System.getProperty("java.class.path"));
/*
        Class[] cls = Plugin.class.getClasses();
        for (int i = 0; i < cls.length; i++) {
            System.out.println("aaa:"+cls[i]);
        }
*/
/*
        try {
            URL res = DIContainer.class.getResource("/");
            Path classPath = new File(res.toURI()).toPath();
            Files.walk(classPath)
                        .forEach(name -> System.out.println(name));

            DIContainer diContainer = new DIContainer();
            ClassLoader cl = diContainer.getClass().getClassLoader();
            cl.getResource("*");


        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
    }

    List<String> getLoadedJarFiles() {
        String property = System.getProperty("java.class.path");
        String[] paths = property.split(System.getProperty("path.separator"));
        return Arrays.asList(paths).stream()
                .filter(path -> Files.exists(Paths.get(path)))
                .filter(path -> !Files.isDirectory(Paths.get(path)))
                .collect(Collectors.toList());
    }
}
