package finalproject;

import javax.swing.JOptionPane;
import java.io.*;
import java.util.*;

public class TransferCode {
    private static final String ACCOUNTS_FILE = "accounts.txt";

  
    public void transfer(String senderAccount, String receiverAccount, String transferAmountStr) {
        double transferAmount;
        try {
            transferAmount = Double.parseDouble(transferAmountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid transfer amount. Please enter a valid number.");
            return;
        }

        if (!isValidAccountNumber(senderAccount) || !isValidAccountNumber(receiverAccount)) {
            JOptionPane.showMessageDialog(null, "Invalid account number(s). Each must be exactly 10 numeric digits.");
            return;
        }

        if (senderAccount.equals(receiverAccount)) {
            JOptionPane.showMessageDialog(null, "Sender and receiver account numbers cannot be the same.");
            return;
        }

        if (transferAmount <= 0) {
            JOptionPane.showMessageDialog(null, "Transfer amount must be greater than 0.");
            return;
        }

        boolean senderFound = false;
        boolean receiverFound = false;
        List<String> updatedLines = new ArrayList<>();
        String senderType = "";
        double senderBalance = 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(senderAccount)) {
                    senderFound = true;
                    senderType = data[6];
                    senderBalance = Double.parseDouble(data[3]);

                  
                    double totalDeduction = transferAmount;
                    if (senderType.equalsIgnoreCase("Current")) {
                        JOptionPane.showMessageDialog(null, "a 10$ fee will be deducted from the account for each transfer.");
                        totalDeduction += 10;
                    }

                    if (senderBalance < totalDeduction) {
                        JOptionPane.showMessageDialog(null, "Insufficient balance in sender's account.");
                        return;
                    }

            
                    senderBalance -= totalDeduction;
                    data[3] = String.format("%.2f", senderBalance);
                    updatedLines.add(String.join(",", data));
                } else if (data[0].equals(receiverAccount)) {
                    receiverFound = true;

             
                    double receiverBalance = Double.parseDouble(data[3]);
                    receiverBalance += transferAmount;
                    data[3] = String.format("%.2f", receiverBalance);
                    updatedLines.add(String.join(",", data));
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading the accounts file: " + e.getMessage());
            return;
        }

        if (!senderFound) {
            JOptionPane.showMessageDialog(null, "Sender account not found.");
            return;
        }

        if (!receiverFound) {
            JOptionPane.showMessageDialog(null, "Receiver account not found.");
            return;
        }

       
        try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating the accounts file: " + e.getMessage());
        }

      
        recordTransaction(senderAccount, "Transfer Out", transferAmount);
        if (senderType.equalsIgnoreCase("Current")) {
            recordTransaction(senderAccount, "Fee Deduction", 10.0);
        }
        recordTransaction(receiverAccount, "Transfer In", transferAmount);

        JOptionPane.showMessageDialog(null, "Transfer successful! New sender balance: $" + String.format("%.2f", senderBalance));
    }


    private static boolean isValidAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() != 10) {
            return false;
        }
        for (char c : accountNumber.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private static void recordTransaction(String accountNumber, String type, double amount) {
        String transactionFile = accountNumber + ".txt";
        try (FileWriter writer = new FileWriter(transactionFile, true)) {
            writer.write(type + ": $" + amount + " on " + new Date() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error recording the transaction: " + e.getMessage());
        }
    }
}
