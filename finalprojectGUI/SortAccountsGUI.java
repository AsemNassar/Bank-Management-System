package finalprojectGUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class SortAccountsGUI {

    private static final String ACCOUNTS_FILE = "accounts.txt";

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Sort Accounts");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLayout(new BorderLayout());

        JPanel sortPanel = new JPanel();
        sortPanel.setLayout(new FlowLayout());

        JLabel sortLabel = new JLabel("Sort by:");
        String[] sortOptions = {"Name", "Balance", "Date Opened"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        JButton sortButton = new JButton("Sort");

        sortPanel.add(sortLabel);
        sortPanel.add(sortComboBox);
        sortPanel.add(sortButton);

        String[] columnNames = {"Account Number", "Name", "Email", "Balance", "Phone", "Date Opened", "Account Type"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable accountsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(accountsTable);

        frame.add(sortPanel, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);

        sortButton.addActionListener(e -> {
            int selectedIndex = sortComboBox.getSelectedIndex();
            sortAccounts(selectedIndex, tableModel);
        });

        frame.setVisible(true);
    }

    private static void sortAccounts(int criterion, DefaultTableModel tableModel) {
        List<String[]> accounts = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 7) {
                    accounts.add(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the accounts file: " + e.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        for (int i = 0; i < accounts.size(); i++) {
            for (int j = i + 1; j < accounts.size(); j++) {
                boolean shouldSwap = false;

                try {
                    if (criterion == 0) {
                        shouldSwap = accounts.get(i)[1].compareToIgnoreCase(accounts.get(j)[1]) > 0;
                    } else if (criterion == 1) {
                        double balance1 = Double.parseDouble(accounts.get(i)[3]);
                        double balance2 = Double.parseDouble(accounts.get(j)[3]);
                        shouldSwap = balance1 > balance2;
                    } else if (criterion == 2) {
                        Date date1 = dateFormat.parse(accounts.get(i)[5]);
                        Date date2 = dateFormat.parse(accounts.get(j)[5]);
                        shouldSwap = date1.after(date2);
                    }
                } catch (Exception e) {
                }

                if (shouldSwap) {
                    String[] temp = accounts.get(i);
                    accounts.set(i, accounts.get(j));
                    accounts.set(j, temp);
                }
            }
        }

        tableModel.setRowCount(0);
        for (String[] account : accounts) {
            tableModel.addRow(account);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SortAccountsGUI::createAndShowGUI);
    }
}
