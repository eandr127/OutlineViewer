package edu.wpi.first.outlineviewer.plugin;

import edu.wpi.first.outlineviewer.NetworkTableUtilities;
import edu.wpi.first.outlineviewer.model.TreeRow;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class PluginHandler {

    private final File folder;
    private final PluginLoader loader;

    public PluginHandler() {
        File folder = new File("plugins");
        folder.mkdir();

        System.out.println("Folder: " + folder.getAbsolutePath());

        if(folder.isDirectory()) {
            this.folder = folder;
        }
        else {
            this.folder = null;
        }

        this.loader = new PluginLoader(folder, new TableHandlerRepository());
    }

    public void load() {
        if(folder != null) {
            Stream.of(folder.list())
                    .parallel()
                    .filter(t -> t.endsWith(".jar"))
                    .map(t -> t.substring(0, t.length() - 4))
                    .forEach(this::loadJar);
        }
    }

    private void loadJar(String name) {
        try {
            loader.loadPlugin(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        loader.getRepo().getAllTableHandlers().values().stream().flatMap(t -> t.stream()).forEach((t -> t.init(new ArrayList<TreeRow>())));
    }

    public void destroy() {
        loader.getRepo().getAllTableHandlers().values().stream().flatMap(t -> t.stream()).forEach((t -> t.destroy()));
    }
}
