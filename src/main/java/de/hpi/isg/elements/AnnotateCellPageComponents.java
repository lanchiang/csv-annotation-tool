package de.hpi.isg.elements;

import javax.swing.*;

/**
 * @author lan
 * @since 2021/1/17
 */
public class AnnotateCellPageComponents extends PageComponents {

    private JTable sheetDisplayCellTable;
    private JTabbedPane menuTab;
    private JScrollPane sheetDisplayCellPane;
    private JPanel operatingCellPanel;
    private JButton loadAnnotationButton;
    private JLabel preambleCellLabel;
    private JLabel headerCellLabel;
    private JLabel dataCellLabel;
    private JLabel aggregationCellLabel;
    private JLabel footnoteCellLabel;
    private JLabel groupHeaderCellLabel;
    private JLabel emptyCellLabel;
    private JTable annoReviewCellTable;
    private JLabel numOfColumnsCell;
    private JLabel numOfLinesCell;
    private JLabel topLeftCellText;
    private JLabel bottomRightCellText;
    private JLabel cellBlockTypeText;
    private JScrollPane annoReviewCellScrollPane;
    private JButton storeAllResultsButton;
    private JLabel selectedCellValue;
    private JButton submitResultsButton;

    public AnnotateCellPageComponents(JTable sheetDisplayCellTable, JTabbedPane menuTab, JScrollPane sheetDisplayCellPane, JPanel operatingCellPanel, JButton loadAnnotationButton, JLabel preambleCellLabel, JLabel headerCellLabel, JLabel dataCellLabel, JLabel aggregationCellLabel, JLabel footnoteCellLabel, JLabel groupHeaderCellLabel, JLabel emptyCellLabel, JTable annoReviewCellTable, JLabel numOfColumnsCell, JLabel numOfLinesCell, JLabel topLeftCellText, JLabel bottomRightCellText, JLabel cellBlockTypeText, JScrollPane annoReviewCellScrollPane, JButton storeAllResultsButton, JLabel selectedCellValue, JButton submitResultsButton) {
        this.sheetDisplayCellTable = sheetDisplayCellTable;
        this.menuTab = menuTab;
        this.sheetDisplayCellPane = sheetDisplayCellPane;
        this.operatingCellPanel = operatingCellPanel;
        this.loadAnnotationButton = loadAnnotationButton;
        this.preambleCellLabel = preambleCellLabel;
        this.headerCellLabel = headerCellLabel;
        this.dataCellLabel = dataCellLabel;
        this.aggregationCellLabel = aggregationCellLabel;
        this.footnoteCellLabel = footnoteCellLabel;
        this.groupHeaderCellLabel = groupHeaderCellLabel;
        this.emptyCellLabel = emptyCellLabel;
        this.annoReviewCellTable = annoReviewCellTable;
        this.numOfColumnsCell = numOfColumnsCell;
        this.numOfLinesCell = numOfLinesCell;
        this.topLeftCellText = topLeftCellText;
        this.bottomRightCellText = bottomRightCellText;
        this.cellBlockTypeText = cellBlockTypeText;
        this.annoReviewCellScrollPane = annoReviewCellScrollPane;
        this.storeAllResultsButton = storeAllResultsButton;
        this.selectedCellValue = selectedCellValue;
        this.submitResultsButton = submitResultsButton;
    }

    public JTable getSheetDisplayCellTable() {
        return sheetDisplayCellTable;
    }

    public JTabbedPane getMenuTab() {
        return menuTab;
    }

    public JScrollPane getSheetDisplayCellPane() {
        return sheetDisplayCellPane;
    }

    public JPanel getOperatingCellPanel() {
        return operatingCellPanel;
    }

    public JButton getLoadAnnotationButton() {
        return loadAnnotationButton;
    }

    public JLabel getPreambleCellLabel() {
        return preambleCellLabel;
    }

    public JLabel getHeaderCellLabel() {
        return headerCellLabel;
    }

    public JLabel getDataCellLabel() {
        return dataCellLabel;
    }

    public JLabel getAggregationCellLabel() {
        return aggregationCellLabel;
    }

    public JLabel getFootnoteCellLabel() {
        return footnoteCellLabel;
    }

    public JLabel getGroupHeaderCellLabel() {
        return groupHeaderCellLabel;
    }

    public JLabel getEmptyCellLabel() {
        return emptyCellLabel;
    }

    public JTable getAnnoReviewCellTable() {
        return annoReviewCellTable;
    }

    public JLabel getNumOfColumnsCell() {
        return numOfColumnsCell;
    }

    public JLabel getNumOfLinesCell() {
        return numOfLinesCell;
    }

    public JLabel getTopLeftCellText() {
        return topLeftCellText;
    }

    public JLabel getBottomRightCellText() {
        return bottomRightCellText;
    }

    public JLabel getCellBlockTypeText() {
        return cellBlockTypeText;
    }

    public JScrollPane getAnnoReviewCellScrollPane() {
        return annoReviewCellScrollPane;
    }

    public JButton getStoreAllResultsButton() {
        return storeAllResultsButton;
    }

    public JLabel getSelectedCellValue() {
        return selectedCellValue;
    }

    public JButton getSubmitResultsButton() {
        return submitResultsButton;
    }
}
