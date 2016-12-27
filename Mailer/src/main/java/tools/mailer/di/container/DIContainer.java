package tools.mailer.di.container;

import tools.mailer.di.anntation.Plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
//                .filter(path -> !Files.isDirectory(Paths.get(path)))
                .collect(Collectors.toList());
    }

    List<String> getClassFiles(String file) {
        if (file.endsWith(".jar")) {
            return getClassFilesFromJarFile(file);
        } else
        if (file.endsWith(".zip")) {
            return getClassFilesFromZipFile(file);
        } else
        if (Files.isDirectory(Paths.get(file))) {
            return getClassFilesFromDirectory(file);
        }
        throw new IllegalArgumentException();
    }

    List<String> getClassFilesFromJarFile(String file) {
        List<String> list = new ArrayList<>();

        try (JarInputStream in = new JarInputStream(new FileInputStream(file))){
            JarEntry entry;

            while ((entry=in.getNextJarEntry())!=null) {
                list.add(entry.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list.stream()
                .filter(fileName -> fileName.endsWith(".class"))
                .collect(Collectors.toList());
    }

    List<String> getClassFilesFromZipFile(String file) {
        List<String> list = new ArrayList<>();

        try (ZipInputStream in = new ZipInputStream(new FileInputStream(file))){
            ZipEntry entry;

            while ((entry=in.getNextEntry())!=null) {
                list.add(entry.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list.stream()
                .filter(fileName -> fileName.endsWith(".class"))
                .collect(Collectors.toList());
    }

    List<String> getClassFilesFromDirectory(String directory) {
        List<String> list = new ArrayList<>();

        try {
            try (Stream<Path> stream = Files.walk(Paths.get(directory))) {
                stream.forEach(path -> list.add(path.toString()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list.stream()
                .filter(fileName -> fileName.endsWith(".class"))
                .map(fileName -> fileName.substring(directory.length()+1))      // パスの部分を削除してFQDNを作成
                .collect(Collectors.toList());
    }

    String toFqdnClassName(String classFile) {
        classFile = classFile.substring(0, classFile.length()-".class".length());
        classFile = classFile.replace('/', '.');        // JarやZipなら/区切り
        classFile = classFile.replace('\\', '.');       // directoryでwindowsなら\区切り
        classFile = classFile.startsWith(".")? classFile.substring(1): classFile;
        return classFile;
    }

    boolean isDeclaredAnnotations(String className, Class<? extends Annotation>[] targetAnnotations) {
//        ClassLoader loader = DIContainer.class.getClassLoader();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = null;
        try {
            clazz = loader.loadClass(className);
        } catch (NoClassDefFoundError e) {
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }

        Annotation[] annotations = clazz.getAnnotations();

//        System.out.println("*****"+className+"*****");
//        Arrays.asList(annotations).stream().forEach(System.out::println);
        for (Annotation annotation: annotations) {
            for (Class<? extends Annotation> targetAnnotation: targetAnnotations) {
                if (annotation.annotationType().equals(targetAnnotation)) {
                    return true;
                }
            }
        }

        return false;
    }
}
