package edu.wpi.first.outlineviewer.plugin;

import java.util.*;

public class TableHandlerRepository {
    private final Map<String, List<TableHandler>> tableHandlers;

    public TableHandlerRepository() {
        tableHandlers = new HashMap<>();
    }

    public void put(String name, TableHandler plugin) {
        if(!tableHandlers.containsKey(name)) {
            tableHandlers.put(name, new ArrayList<>());
        }

        tableHandlers.get(name).add(plugin);
    }

    public Map<String, List<TableHandler>> getAllTableHandlers() {
        return tableHandlers;
    }

    public List<TableHandler> getTableHandlersForName(String name) {
        return tableHandlers.get(name);
    }
}
