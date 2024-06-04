package ui;

import delegates.DashBoardDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JoinPanel extends JPanel {
    private DashBoardDelegate delegate;
    private List<List<Object>> resultData;
    private JComboBox<String> userDropdown;
    private JComboBox<String> preferencesDropdown;
    private JComboBox<String> requirementsDropdown;
    private JButton generateButton;

    public JoinPanel(DashBoardDelegate delegate) {
        this.delegate = delegate;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initializeUI();
    }

    private void initializeUI() {
        JLabel headerLabel = new JLabel("Join Users, Preferences, Requirements");
        headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea description = new JTextArea("Join the Users, Preferences and Requirements Tables to view users' preferences and requirements.");
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setAlignmentX(Component.CENTER_ALIGNMENT);

        String[] userAttributes = {"UserID"};
        String[] preferencesAttributes = {"PreferencesID"};
        String[] requirementsAttributes = {"RequirementsID"};

        userDropdown = new JComboBox<>(userAttributes);
        preferencesDropdown = new JComboBox<>(preferencesAttributes);
        requirementsDropdown = new JComboBox<>(requirementsAttributes);

        generateButton = new JButton("Generate");
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateJoinedResults();
            }
        });

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(headerLabel);
        add(Box.createRigidArea(new Dimension(0, 5)));
        add(description);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(new JLabel("User:"));
        add(userDropdown);
        add(new JLabel("Preferences:"));
        add(preferencesDropdown);
        add(new JLabel("Requirements:"));
        add(requirementsDropdown);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(generateButton);
    }

    private void generateJoinedResults() {
        resultData = delegate.join();
        displayResults(resultData);
    }

    private void displayResults(java.util.List<java.util.List<Object>> tuples) {
        JDialog popup = new JDialog();
        popup.setTitle("Query Results");
        popup.setLayout(new BorderLayout());

        if (tuples == null || tuples.isEmpty()) {
            popup.add(new JLabel("No data found."), BorderLayout.CENTER);
            popup.setSize(300, 100);
            popup.setLocationRelativeTo(null);
            popup.setVisible(true);
            return;
        }

        String[] columnNames = new String[]{"UserID", "Name", "Gender", "Bio", "Age", "Preferences ID", "Preferred Gender", "Preferred Age Range", "Preferred Lifestyle", "Preferred Location", "RequirementID", "Required Gender", "Required Age Range", "Required Lifestyle", "Required Location"};

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

