package tools.mailer.di.container;

import org.junit.*;
import tools.mailer.di.anntation.Plugin;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DIContainerTest {
    @BeforeClass
    public static void beforeClass() throws Exception {
    }

    @AfterClass
    public static void afterClass() throws Exception {
    }

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void loadPluginTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        diContainer.loadPlugin();
        assertEquals(2, diContainer.pluginContainer.size());
        assertTrue(diContainer.pluginContainer.containsKey("tools.mailer.plugin.SendMailPlugin"));
        assertTrue(diContainer.pluginContainer.containsKey("tools.mailer.processor.MailProcessor"));
    }

    @Test
    public void getLoadedJarFilesTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        List<String> jars = diContainer.getLoadedJarFiles();
        jars.forEach(System.out::println);
        assertTrue(true);
    }

    @Test
    public void getClassFilesTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        List<String> jars = diContainer.getLoadedJarFiles();
        for (String jarFile: jars) {
            System.out.println("jarFile=>"+jarFile);
            diContainer.getClassFiles(jarFile)
                    .forEach(System.out::println);
        }
        assertTrue(true);
    }

    @Test
    public void toFqdnClassNameTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        assertEquals("ccc", diContainer.toFqdnClassName("ccc.class"));
        assertEquals("aaa.bbb.ccc", diContainer.toFqdnClassName("aaa/bbb/ccc.class"));
        assertEquals("aaa.bbb.ccc", diContainer.toFqdnClassName("/aaa/bbb/ccc.class"));

        List<String> jars = diContainer.getLoadedJarFiles();
        List<String> classFiles = new ArrayList<>();
        for (String jarFile: jars) {
            classFiles.addAll(diContainer.getClassFiles(jarFile));
        }
        classFiles.stream()
            .map(diContainer::toFqdnClassName)
            .forEach(System.out::println);
    }

    @Test
    public void isDeclaredAnnotationsTest() throws Exception {
        DIContainer diContainer = new DIContainer();
        List<String> jars = diContainer.getLoadedJarFiles();
        List<String> classFiles = new ArrayList<>();
        for (String jarFile: jars) {
            classFiles.addAll(diContainer.getClassFiles(jarFile));
        }

        Class<? extends Annotation>[] targetAnnotations = new Class[]{Plugin.class};

        List<String> annotationClasses = classFiles.stream()
                .map(diContainer::toFqdnClassName)
                .filter(className -> diContainer.isDeclaredAnnotations(className, targetAnnotations))
                .collect(Collectors.toList());
        annotationClasses.forEach(System.out::println);
    }

}
