package tools.mailer.di.container;

import tools.mailer.di.anntation.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

        Class[] cls = Plugin.class.getClasses();
        for (int i = 0; i < cls.length; i++) {
            System.out.println("aaa:"+cls[i]);
        }
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
}
