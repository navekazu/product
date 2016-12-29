package tools.mailer.di.container;

import tools.mailer.di.anntation.Autowired;
import tools.mailer.di.anntation.Plugin;
import tools.mailer.di.anntation.Process;
import tools.mailer.di.anntation.ProcessType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DIContainer {
    private static DIContainer diContainer = new DIContainer();
    public static DIContainer getInstance() {
        return diContainer;
    }
    private DIContainer() {
    }

    Map<Class, Object> pluginContainer = new HashMap<>();

    public void loadPlugin() {
        Class<? extends Annotation>[] targetAnnotations = new Class[]{Plugin.class};
        getLoadedJarFiles().stream()
                .forEach(jarFile -> {
                    getClassFiles(jarFile).stream()
                            .map(classFile -> toFqdnClassName(classFile))
                            .filter(classFile -> isDeclaredAnnotations(classFile, targetAnnotations))
                            .forEach(classFile -> registPlugin(classFile));
                });
        autowired();
    }

    List<String> getLoadedJarFiles() {
        String property = System.getProperty("java.class.path");
        String[] paths = property.split(System.getProperty("path.separator"));
        return Arrays.asList(paths).stream()
                .filter(path -> Files.exists(Paths.get(path)))
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

    void registPlugin(String pluginClass) {
        try {
            Class clazz = Class.forName(pluginClass);
            Object instance = clazz.newInstance();
            pluginContainer.put(clazz, instance);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void autowired() {
        pluginContainer.keySet().stream()
                .forEach(clazz -> {
                    for (Field field: clazz.getDeclaredFields()) {
                        if (!field.isAnnotationPresent(Autowired.class)) {
                            continue;
                        }
                        field.setAccessible(true);
                        try {
                            field.set(pluginContainer.get(clazz), pluginContainer.get(field.getType()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public void fireEvent(ProcessType processType) {
        pluginContainer.keySet().stream()
                .forEach(clazz -> {

                    for (Method method: clazz.getDeclaredMethods()) {
                        if (!method.isAnnotationPresent(Process.class)) {
                            continue;
                        }
                        Process processAnnotation =  method.getAnnotation(Process.class);
                        if (processType==processAnnotation.processType()) {
                            try {
                                method.invoke(pluginContainer.get(clazz));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        };
                    }
                });

    }

}
