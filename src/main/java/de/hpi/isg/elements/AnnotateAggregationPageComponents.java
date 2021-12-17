package de.hpi.isg.elements;

import lombok.Getter;

import javax.swing.*;

/**
 * @author lan
 * @since 2021/1/17
 */
@Getter
public class AnnotateAggregationPageComponents extends PageComponents {

    private final JRadioButton sumRadioButton;
    private final JRadioButton relativeChangeRadioButton;
    private final JRadioButton averageRadioButton;
    private final JRadioButton divisionRadioButton;
    private final JRadioButton othersRadioButton;
    private final JPanel operatingAggrPanel;
    private final JButton aggrAnnotationLoadDatasetButton;
    private final JTable fileReviewTable;
    private final JScrollPane fileDisplayAggrPane;
    private final JTextField errorTextField;
    private final JPanel aggrAnnotationMainPanel;
    private final JLabel numRowsAggr;
    private final JLabel numColumnsAggr;
    private final JLabel topleftIndexAggr;
    private final JLabel bottomRightIndexAggr;
    private final JTable fileDisplayTableAggr;
    private final JRadioButton operandOneRadioButton;
    private final JRadioButton operandTwoRadioButton;
    private final JLabel operandOneCellRange;
    private final JLabel operandTwoCellRange;
    private final JLabel errorMessageLabel;
    private final JCheckBox hopSelectionModeCheckBox;
    private final JButton calculateHopsButton;
    private final JLabel modeHintLabel;
    private final JButton nextAnnotationPreviewButton;
    private final JButton lastAnnotationPreviewButton;
    private final JRadioButton commaPointNumberFormat;
    private final JRadioButton pointCommaNumberFormat;
    private final JRadioButton spacePointNumberFormat;
    private final JRadioButton spaceCommaNumberFormat;
    private final JRadioButton nonePointNumberFormat;
    private final JRadioButton noneCommaNumberFormat;

    public AnnotateAggregationPageComponents(JRadioButton sumRadioButton, JRadioButton relativeChangeRadioButton, JRadioButton averageRadioButton,
                                             JRadioButton divisionRadioButton, JRadioButton othersRadioButton,
                                             JPanel operatingAggrPanel, JButton aggrAnnotationLoadDatasetButton, JTable fileReviewTable,
                                             JScrollPane fileDisplayAggrPane, JTextField errorTextField, JPanel aggrAnnotationMainPanel,
                                             JLabel numRowsAggr, JLabel numColumnsAggr, JLabel topleftIndexAggr,
                                             JLabel bottomRightIndexAggr, JTable fileDisplayTableAggr, JRadioButton operandOneRadioButton,
                                             JRadioButton operandTwoRadioButton, JLabel operandOneCellRange, JLabel operandTwoCellRange,
                                             JLabel errorMessageLabel, JCheckBox hopSelectionModeCheckBox, JButton calculateHopsButton,
                                             JLabel modeHintLabel, JButton nextAnnotationPreviewButton, JButton lastAnnotationPreviewButton,
                                             JRadioButton commaPointNumberFormat, JRadioButton pointCommaNumberFormat,
                                             JRadioButton spacePointNumberFormat, JRadioButton spaceCommaNumberFormat,
                                             JRadioButton nonePointNumberFormat, JRadioButton noneCommaNumberFormat) {
        this.sumRadioButton = sumRadioButton;
        this.relativeChangeRadioButton = relativeChangeRadioButton;
        this.averageRadioButton = averageRadioButton;
        this.divisionRadioButton = divisionRadioButton;
        this.othersRadioButton = othersRadioButton;
        this.operatingAggrPanel = operatingAggrPanel;
        this.aggrAnnotationLoadDatasetButton = aggrAnnotationLoadDatasetButton;
        this.fileReviewTable = fileReviewTable;
        this.fileDisplayAggrPane = fileDisplayAggrPane;
        this.errorTextField = errorTextField;
        this.aggrAnnotationMainPanel = aggrAnnotationMainPanel;
        this.numRowsAggr = numRowsAggr;
        this.numColumnsAggr = numColumnsAggr;
        this.topleftIndexAggr = topleftIndexAggr;
        this.bottomRightIndexAggr = bottomRightIndexAggr;
        this.fileDisplayTableAggr = fileDisplayTableAggr;
        this.operandOneRadioButton = operandOneRadioButton;
        this.operandTwoRadioButton = operandTwoRadioButton;
        this.operandOneCellRange = operandOneCellRange;
        this.operandTwoCellRange = operandTwoCellRange;
        this.errorMessageLabel = errorMessageLabel;
        this.hopSelectionModeCheckBox = hopSelectionModeCheckBox;
        this.calculateHopsButton = calculateHopsButton;
        this.modeHintLabel = modeHintLabel;
        this.nextAnnotationPreviewButton = nextAnnotationPreviewButton;
        this.lastAnnotationPreviewButton = lastAnnotationPreviewButton;
        this.commaPointNumberFormat = commaPointNumberFormat;
        this.pointCommaNumberFormat = pointCommaNumberFormat;
        this.spacePointNumberFormat = spacePointNumberFormat;
        this.spaceCommaNumberFormat = spaceCommaNumberFormat;
        this.nonePointNumberFormat = nonePointNumberFormat;
        this.noneCommaNumberFormat = noneCommaNumberFormat;
    }

}
