package de.hpi.isg.utils;

import de.hpi.isg.swing.SheetDisplayLineTypeRowRenderer;

import java.awt.*;

/**
 *
 * @author Lan Jiang
 * @since 8/26/19
 */
public class ColorSolution {

    public static Color EMPTY_LINE_BACKGROUND_COLOR = new Color(255, 255, 0, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color DEFAULT_BACKGROUND_COLOR = new Color(255, 255, 255, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color BLACK = new Color(0, 0, 0, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color PREAMBLE_BACKGROUND_COLOR = new Color(0, 0, 255, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color AGGREGATION_BACKGROUND_COLOR = new Color(0, 255, 255, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color HEADER_BACKGROUND_COLOR = new Color(255, 0, 255, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color DATA_BACKGROUND_COLOR = new Color(0, 255, 0, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color GROUND_HEADER_BACKGROUND_COLOR = new Color(255, 64, 0, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color FOOTNOTE_BACKGROUND_COLOR = new Color(64, 128, 192, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color BROWN = new Color(128, 64, 64, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color DARKGREY = new Color(64, 64, 64, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color GREY  = new Color(128, 128, 128, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);

    public static Color NULL_BORDER_COLOR = new Color(255, 255, 255, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color HIGHLIGHT_BORDER_COLOR = Color.RED;
    public static Color OPERAND_BORDER_COLOR = Color.BLUE;
    public static Color SELECT_AGGREGATEE_COLOR = Color.ORANGE;
    public static Color ANNOTATED_AGGREGATEE_COLOR = Color.PINK;
    public static Color SUM_AGGREGATOR_COLOR = new Color(16, 165, 140, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color AVERAGE_AGGREGATOR_COLOR = new Color(178, 100,30, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color RELATIVE_CHANGE_AGGREGATOR_COLOR = new Color(192, 32, 172, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color DIVISION_AGGREGATOR_COLOR = new Color(30, 72, 200, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);
    public static Color OTHERS_AGGREGATOR_COLOR = new Color(69, 10, 100, SheetDisplayLineTypeRowRenderer.OPACITY_PARAMETER);

    public static Color getColor(String string) {
        Color color;
        switch (string) {
            case LabelUtils.PREAMBLE: {
                color = PREAMBLE_BACKGROUND_COLOR;
                break;
            }
            case LabelUtils.HEADER: {
                color = HEADER_BACKGROUND_COLOR;
                break;
            }
            case LabelUtils.DATA: {
                color = DATA_BACKGROUND_COLOR;
                break;
            }
            case LabelUtils.AGGREGATION: {
                color = AGGREGATION_BACKGROUND_COLOR;
                break;
            }
            case LabelUtils.FOOTNOTE: {
                color = FOOTNOTE_BACKGROUND_COLOR;
                break;
            }
            case LabelUtils.GROUP_HEADER: {
                color = GROUND_HEADER_BACKGROUND_COLOR;
                break;
            }
            default: {
                color = EMPTY_LINE_BACKGROUND_COLOR;
            }
        }
        return color;
    }

    public static Color getAggregatorColor(String string) {
        Color color;
        switch (string) {
            case LabelUtils.SUM: {
                color = SUM_AGGREGATOR_COLOR;
                break;
            }
            case LabelUtils.RELATIVE_CHANGE: {
                color = RELATIVE_CHANGE_AGGREGATOR_COLOR;
                break;
            }
            case LabelUtils.AVERAGE: {
                color = AVERAGE_AGGREGATOR_COLOR;
                break;
            }
            case LabelUtils.DIVISION: {
                color = DIVISION_AGGREGATOR_COLOR;
                break;
            }
            case LabelUtils.OTHERS: {
                color = OTHERS_AGGREGATOR_COLOR;
            }
            default: {
                color = DEFAULT_BACKGROUND_COLOR;
            }
        }
        return color;
    }

    public static Color getColorRNN(String string) {
        Color color;
        if (LabelUtils.METADATA_RNN.equals(string)) {
            color = ColorSolution.PREAMBLE_BACKGROUND_COLOR;
        } else if (LabelUtils.HEADER_RNN.equals(string)) {
            color = ColorSolution.HEADER_BACKGROUND_COLOR;
        } else if (LabelUtils.ATTRIBUTE_RNN.equals(string)) {
            color = ColorSolution.GROUND_HEADER_BACKGROUND_COLOR;
        } else if (LabelUtils.DATA_RNN.equals(string)) {
            color = ColorSolution.DATA_BACKGROUND_COLOR;
        } else if (LabelUtils.DERIVED_RNN.equals(string)) {
            color = ColorSolution.AGGREGATION_BACKGROUND_COLOR;
        } else if (LabelUtils.NOTES_RNN.equals(string)) {
            color = ColorSolution.FOOTNOTE_BACKGROUND_COLOR;
        } else {
            color = ColorSolution.DEFAULT_BACKGROUND_COLOR;
        }
        return color;
    }

    public static String getLineType(Color color) {
        String lineType;
        if (color.equals(PREAMBLE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.PREAMBLE;
        } else if (color.equals(HEADER_BACKGROUND_COLOR)) {
            lineType = LabelUtils.HEADER;
        } else if (color.equals(DATA_BACKGROUND_COLOR)) {
            lineType = LabelUtils.DATA;
        } else if (color.equals(AGGREGATION_BACKGROUND_COLOR)) {
            lineType = LabelUtils.AGGREGATION;
        } else if (color.equals(FOOTNOTE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.FOOTNOTE;
        } else if (color.equals(GROUND_HEADER_BACKGROUND_COLOR)) {
            lineType = LabelUtils.GROUP_HEADER;
        } else if (color.equals(EMPTY_LINE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.EMPTY;
        } else {
            return null;
        }
        return lineType;
    }

    public static String getLineTypeRNN(Color color) {
        String lineType;
        if (color.equals(PREAMBLE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.METADATA_RNN;
        } else if (color.equals(HEADER_BACKGROUND_COLOR)) {
            lineType = LabelUtils.HEADER_RNN;
        } else if (color.equals(DATA_BACKGROUND_COLOR)) {
            lineType = LabelUtils.DATA_RNN;
        } else if (color.equals(AGGREGATION_BACKGROUND_COLOR)) {
            lineType = LabelUtils.DERIVED_RNN;
        } else if (color.equals(FOOTNOTE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.NOTES_RNN;
        } else if (color.equals(GROUND_HEADER_BACKGROUND_COLOR)) {
            lineType = LabelUtils.ATTRIBUTE_RNN;
        } else if (color.equals(EMPTY_LINE_BACKGROUND_COLOR)) {
            lineType = LabelUtils.EMPTY_RNN;
        } else {
            return null;
        }
        return lineType;
    }
}
