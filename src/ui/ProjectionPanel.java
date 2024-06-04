package ui;

import delegates.DashBoardDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProjectionPanel extends JPanel {
    private JCheckBox userIdCheckBox, nameCheckBox, genderCheckBox, bioCheckBox, ageCheckBox;
    private JButton generateButton;
    private DashBoardDelegate delegate;

    public ProjectionPanel(DashBoardDelegate delegate) {
        this.delegate = delegate;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Select User Attributes");
        add(titleLabel);

        userIdCheckBox = new JCheckBox("UserID");
        nameCheckBox = new JCheckBox("Name");
        genderCheckBox = new JCheckBox("Gender");
        bioCheckBox = new JCheckBox("Bio");
        ageCheckBox = new JCheckBox("Age");

        add(userIdCheckBox);
        add(nameCheckBox);
        add(genderCheckBox);
        add(bioCheckBox);
        add(ageCheckBox);

        generateButton = new JButton("Generate");
        generateButton.addActionListener(e -> generateQueryAndDisplayResults());
        add(generateButton);
    }

    private void generateQueryAndDisplayResults() {
        List<String> selectedAttributes = new ArrayList<>();
        if (userIdCheckBox.isSelected()) selectedAttributes.add("UserID");
        if (nameCheckBox.isSelected()) selectedAttributes.add("Name");
        if (genderCheckBox.isSelected()) selectedAttributes.add("Gender");
        if (bioCheckBox.isSelected()) selectedAttributes.add("Bio");
        if (ageCheckBox.isSelected()) selectedAttributes.add("Age");

        String query = "SELECT " + (selectedAttributes.isEmpty() ? "*" : String.join(", ", selectedAttributes)) + " FROM Users";
        System.out.println(query);

        List<List<Object>> results = delegate.projectUser(query);
        displayResultsInNewWindow(results, selectedAttributes.isEmpty() ? new String[]{"All Attributes"} : selectedAttributes.toArray(new String[0]));
    }

    private void displayResultsInNewWindow(List<List<Object>> results, String[] columnNames) {
        JFrame resultsFrame = new JFrame("Query Results");
        resultsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTable resultsTable = new JTable(convertResultsToObjectArray(results), columnNames);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsFrame.add(scrollPane, BorderLayout.CENTER);
        resultsFrame.pack();
        resultsFrame.setLocationRelativeTo(null);
        resultsFrame.setVisible(true);
    }

    private Object[][] convertResultsToObjectArray(List<List<Object>> results) {
        Object[][] dataArray = new Object[results.size()][results.get(0).size()];
        for (int i = 0; i < results.size(); i++) {
            dataArray[i] = results.get(i).toArray();
        }
        return dataArray;
    }
}

