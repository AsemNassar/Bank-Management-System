package finalprojectGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LiveSearch {

    private static final String ACCOUNTS_FILE = "accounts.txt";

    public static void Search() {
        JFrame frame = new JFrame("Search Accounts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);
        frame.setLayout(new BorderLayout());

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 18));
        frame.add(searchField, BorderLayout.NORTH);

        String[] columnNames = {"Acc No", "Name", "Email", "Balance", "Phone Number", "Date", "Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultsTable = new JTable(tableModel);
        resultsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsTable.setRowHeight(20);

        JScrollPane scrollPane = new JScrollPane(resultsTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTable(searchField.getText(), tableModel);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTable(searchField.getText(), tableModel);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTable(searchField.getText(), tableModel);
            }

            private void updateTable(String keyword, DefaultTableModel tableModel) {
                tableModel.setRowCount(0);

                if (keyword.trim().isEmpty()) {
                    return;
                }

                try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");
                        if (data.length > 1 && data[1].toLowerCase().contains(keyword.toLowerCase())) {
                            tableModel.addRow(data);
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error reading the accounts file: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}
