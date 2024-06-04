package ui;

import delegates.DashBoardDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class SelectionPanel extends JPanel {
    private DashBoardDelegate delegate;
    private String operator;
    private String queryString;
    private List<List<Object>> resultData;
    private StringBuilder whereClause = new StringBuilder();
    private JPanel conditionsPanel; // Panel to dynamically add conditions
    private JButton generateButton; // Button to generate the query result
    JComboBox<String> attributeComboBox;
    JTextField valueField;
    JComboBox<String> operatorComboBox;

    public SelectionPanel(DashBoardDelegate delegate) {
        this.delegate = delegate;
        setLayout(new BorderLayout());
        initializeUI();
    }

    private void initializeUI() {
        // Title and Description
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.add(new JLabel("Residence Table and Filters"), BorderLayout.NORTH);
//        titlePanel.add(new JTextArea("Note: If no condition is added, the original table is printed. You need to click '+' at least once to select!"), BorderLayout.NORTH);
        titlePanel.add(new JTextArea("Select filters to apply to the Residence table. You can add multiple conditions."), BorderLayout.CENTER);

        // Conditions Panel
        conditionsPanel = new JPanel();
        conditionsPanel.setLayout(new BoxLayout(conditionsPanel, BoxLayout.Y_AXIS));
        addCondition();

        // Scroll Pane for conditions (in case of many conditions)
        JScrollPane scrollPane = new JScrollPane(conditionsPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Generate Button
        generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> generateQuery());

        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);
    }

    private void addCondition() {
        JPanel conditionPanel = new JPanel(new FlowLayout());

        attributeComboBox = new JComboBox<>(new String[]{"ResID", "Address", "Amenities", "Number of Rooms", "Rent", "Type"}); // Populate with actual attributes
        valueField = new JTextField(10);
        operatorComboBox = new JComboBox<>(new String[]{"AND", "OR"});

        JButton addConditionButton = new JButton("+");
        addConditionButton.addActionListener(e -> {
            String attr = (String)attributeComboBox.getSelectedItem();
            String value = (String)valueField.getText();
            String nextOp = (String)operatorComboBox.getSelectedItem();
            if (!(whereClause.length() > 0)) {
                whereClause.append(attr).append(" = ").append("'").append(value).append("'");
            } else {
                whereClause.append(" ").append(operator).append(" ").append(attr).append(" = '").append(value).append("'");
            }

            operator = nextOp;
            addCondition();
        });

        conditionPanel.add(attributeComboBox);
        conditionPanel.add(valueField);
        conditionPanel.add(operatorComboBox);
        conditionPanel.add(addConditionButton);

        conditionsPanel.add(conditionPanel);
        conditionsPanel.revalidate();
        conditionsPanel.repaint();
    }

    private void generateQuery() {
        queryString = "SELECT * FROM Res" + (whereClause.length() > 0 ? " WHERE " + whereClause + " " + operator + " " + (String)attributeComboBox.getSelectedItem() + " = " + (String)(valueField.getText()) : "");
        System.out.println(queryString);
        resultData = delegate.queryResidences(queryString);
        displayResults(resultData);
    }

    public void getWhereClause() {
        System.out.println(whereClause.toString());
    }

    private void displayResults(List<List<Object>> tuples) {
        // Create the popup window
        JDialog popup = new JDialog();
        popup.setTitle("Query Results");
        popup.setLayout(new BorderLayout());

        if (tuples == null || tuples.isEmpty()) {
            popup.add(new JLabel("No data found."), BorderLayout.CENTER);
            popup.setSize(300, 100);
            popup.setLocationRelativeTo(null); // Center on screen
            popup.setVisible(true);
            return;
        }

        String[] columnNames = new String[]{"ResID", "Address", "Amenities", "Number of Rooms", "Rent", "Type"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (List<Object> row : tuples) {
            tableModel.addRow(row.toArray(new Object[0]));
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        popup.add(scrollPane, BorderLayout.CENTER);
        popup.setSize(800, 400);
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }
}

