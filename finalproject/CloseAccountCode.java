package finalproject;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CloseAccountCode extends javax.swing.JFrame{
    private static final String ACCOUNTS_FILE = "accounts.txt";

    public CloseAccountCode(String accountNum) {

        Scanner amicia = new Scanner(System.in);

      

        if(!isValidAccountNumber(accountNum)) {
            JOptionPane.showMessageDialog(this,"Invalid account number. It must be exactly 10 numeric digits. ");
            return;
        }

        
        ArrayList<String> updatedLines = new ArrayList<>();
        boolean accountFound = false;

        try (BufferedReader br = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;

  
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

            
                if (data[0].equals(accountNum)) {
                    accountFound = true;
                    double balance = Double.parseDouble(data[3]);

                    if (balance == 0.0) {
                        JOptionPane.showMessageDialog(this,"Account closed successfully!");
                  
                        continue;
                    } else {
                       JOptionPane.showMessageDialog(this,"Cannot close account. Balance must be 0. Current balance: $" + balance);
                        updatedLines.add(line);
                        continue;
                    }
                }

    
                updatedLines.add(line);
            }

     
            if (!accountFound) {
                JOptionPane.showMessageDialog(this,"Account does not exist");
                return; 
            }

           
            try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
                for (String updatedLine : updatedLines) {
                    writer.write(updatedLine + "\n");
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error reading or writing file: " + e.getMessage());
        }
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
}
