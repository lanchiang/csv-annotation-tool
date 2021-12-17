package de.hpi.isg.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lan Jiang
 * @since 8/28/19
 */
public class ClassUtils {

    public final static String SHEET_SIMILARITY_FEATURE_PATH = "de.hpi.isg.features.";

    /**
     * Check whether the classes given by the parameter classNames all exist, given the package path.
     *
     * @param classNames is the names of the classes to be checked
     * @param packagePath is the package path
     */
    public static void checkClassesExistence(String[] classNames, String packagePath) {
        if (!packagePath.endsWith(".")) {
            packagePath += ".";
        }
        List<String> missingClasses = new LinkedList<>();
        for (String className : classNames) {
            String absoluteClassName = packagePath + className;
            try {
                Class.forName(absoluteClassName);
            } catch (ClassNotFoundException e) {
                missingClasses.add(className);
            }
        }
        if (missingClasses.size()>0) {
            throw new RuntimeException("Classes " + missingClasses.toString() + " cannot be found.");
        }
    }
}
