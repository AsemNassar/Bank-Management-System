package finalprojectGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class TransactionGUI {

    public static void displayTransactionHistory(String accountNumber) {
        JFrame frame = new JFrame("Transactions History");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 400);

        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Acc No", "Type", "Amount", "Date", "Description"}, 0);
        JTable transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);

        try (BufferedReader reader = new BufferedReader(new FileReader(accountNumber + ".txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" on ");
                String[] typeAndAmount = parts[0].split(": ");
                tableModel.addRow(new Object[]{accountNumber, typeAndAmount[0].trim(), typeAndAmount[1].trim(), parts[1].trim(), line});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> frame.dispose());

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(backButton, BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
