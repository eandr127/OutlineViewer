package edu.wpi.first.outlineviewer.plugin;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.outlineviewer.model.TreeRow;

import java.util.List;

public interface TableHandler {

    String getTableName();

    void init(List<TreeRow> entries);

    default void entryUpdated(TreeRow entryDisplay, int updateTypes) {}

    default void destroy() {}
}
