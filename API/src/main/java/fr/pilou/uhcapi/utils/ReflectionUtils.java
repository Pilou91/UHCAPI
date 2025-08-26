package fr.pilou.uhcapi.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@UtilityClass
public class ReflectionUtils {
    public List<Class<?>> getClassWithAnnotation(JavaPlugin javaPlugin, Class<? extends Annotation> annotation) {
        List<Class<?>> annotatedClasses = new ArrayList<>();
        String packageName = javaPlugin.getClass().getPackage().getName();
        String path = packageName.replace('.', '/');

        try {
            Enumeration<URL> resources = javaPlugin.getClass().getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                if (resource.getProtocol().equals("jar")) {
                    JarURLConnection conn = (JarURLConnection) resource.openConnection();
                    try (JarFile jarFile = conn.getJarFile()) {
                        for (JarEntry entry : java.util.Collections.list(jarFile.entries())) {
                            String name = entry.getName();
                            if (name.endsWith(".class") && name.startsWith(path)) {
                                String className = name.replace('/', '.').substring(0, name.length() - 6);
                                try {
                                    Class<?> clazz = Class.forName(className, false, javaPlugin.getClass().getClassLoader());
                                    if (clazz.isAnnotationPresent(annotation)) {
                                        annotatedClasses.add(clazz);
                                    }
                                } catch (Throwable ignored) {
                                    // Class may fail to load – ignore or log
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return annotatedClasses;
    }
}
