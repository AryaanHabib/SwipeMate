package ui;

import delegates.DashBoardDelegate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DivisionPanel extends JPanel {
    private DashBoardDelegate delegate;
    private List<List<Object>> resultData;
    public DivisionPanel(DashBoardDelegate delegate) {
        this.delegate = delegate;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Users Looking for both On and Off Campus");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea descriptionArea = new JTextArea("find users who are seeking all types of listings available in the Res table. This might mean finding users who are looking for both 'On Campus' and 'Off Campus' residencies");
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton generateButton = new JButton("Generate");
        generateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resultData = delegate.performDivisionQuery();
                displayResults(resultData);
            }
        });

        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 5))); // Spacer
        add(descriptionArea);
        add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        add(generateButton);
    }

    private void openNewWindowWithTable() {
        JFrame tableFrame = new JFrame("Grouped Data");
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        tableFrame.setLayout(new BorderLayout());

        JTable dataTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tableFrame.add(scrollPane, BorderLayout.CENTER);

        tableFrame.setSize(new Dimension(400, 300));
        tableFrame.setLocationRelativeTo(null);
        tableFrame.setVisible(true);

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

        String[] columnNames = new String[]{"UserID", "Name"};

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
