package de.hpi.isg.io;

/**
 * Load an excel file in csv format that is most similar to the one that the user has just processed.
 *
 * @author Lan Jiang
 * @since 8/23/19
 */
public class LoadExcelFile {

    private final String currentTable;

    public LoadExcelFile(String currentTable) {
        this.currentTable = currentTable;
    }
}
