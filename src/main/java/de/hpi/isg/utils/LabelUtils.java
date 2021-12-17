package de.hpi.isg.utils;

/**
 * @author Lan Jiang
 * @since 9/19/19
 */
public class LabelUtils {

    public final static String PREAMBLE = "Preamble (P)";

    public final static String HEADER = "Header (H)";

    public final static String DATA = "Data (D)";

    public final static String AGGREGATION = "Aggregation (A)";

    public final static String FOOTNOTE = "Footnote (F)";

    public final static String GROUP_HEADER = "Group header (G)";

    public final static String EMPTY = "Empty (E)";

    /* -----------------------------------related work cell types------------------------------ */
    public final static String METADATA_RNN = "metadata";
    public final static String HEADER_RNN = "header";
    public final static String DATA_RNN = "data";
    public final static String DERIVED_RNN = "derived";
    public final static String ATTRIBUTE_RNN = "group";
    public final static String NOTES_RNN = "notes";
    public final static String EMPTY_RNN = "empty";

    /* -----------------------------------aggregator type------------------------------ */
    public final static String SUM = "Sum";
    public final static String AVERAGE = "Average";
    public final static String RELATIVE_CHANGE = "RelativeChange";
    public final static String DIVISION = "Division";
    public final static String OTHERS = "Others";
}
