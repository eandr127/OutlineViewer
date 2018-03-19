package edu.wpi.first.outlineviewer.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class PluginLoader extends ClassLoader {
    private final TableHandlerRepository repo;
    private final File folder;

    public PluginLoader(File folder, TableHandlerRepository repo) {
        this.folder = folder;
        this.repo = repo;
    }

    public File getFolder() {
        return folder;
    }

    public TableHandlerRepository getRepo() {
        return repo;
    }

    public void loadPlugin(String name) throws IOException {
        File f = new File(folder, name + ".jar");

        JarFile jar = new JarFile(f);
        jar.stream()
            .parallel()
            .filter(t -> t.getName().endsWith(".class"))
            .map(t -> read(jar, t))
            .filter(t -> TableHandler.class.isAssignableFrom(t) && Stream.of(t.getDeclaredConstructors()).anyMatch(s -> s.getParameterCount() == 0))
            .map(this::instantiateEmptyConstructor)
            .forEach(t -> repo.put(name, (TableHandler)t));
    }

    private Object instantiateEmptyConstructor(Class<?> clazz) {
        try {
            return Stream.of(clazz.getDeclaredConstructors()).filter(s -> s.getParameterCount() == 0).findAny().orElseThrow(() -> new ExceptionInInitializerError("No empty constructor")).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Class<?> read(JarFile jar, JarEntry entry) {
        try {
            byte[] data = jar.getInputStream(entry).readAllBytes();
            return defineClass(entry.getName().substring(0, entry.getName().length() - 6).replace("/", "."), data, 0, data.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
