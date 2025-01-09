package finalproject;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

public class ModifyAccountCode {

    private static final String ACCOUNTS_FILE = "accounts.txt";
    private String accountNumber; 

    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber.trim();
    }

    
    public void modifyAccount(String name, String phoneNumber, String email) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Account number is not set!");
            return;
        }

        ArrayList<String> updatedLines = new ArrayList<>();
        boolean accountFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data[0].equals(accountNumber)) {
                    accountFound = true;

                
                    data[1] = name;
                    data[2] = email;
                    data[4] = phoneNumber;

                    updatedLines.add(String.join(",", data));
                } else {
                    updatedLines.add(line);
                }
            }

            if (!accountFound) {
                JOptionPane.showMessageDialog(null, "Account does not exist!");
                return;
            }

            try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine + "\n");
                }
            }

            JOptionPane.showMessageDialog(null, "Account updated successfully!");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading or writing file: " + e.getMessage());
        }
    }
}
