package finalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


import finalprojectGUI.AccountNumber ;

import javax.swing.JOptionPane;


public class Transaction extends  javax.swing.JFrame  {

    private static final String ACCOUNTS_FILE = "accounts.txt";

    public  void deposit( String accNumber , String amount)  { 

 
        if (!isValidAccountNumber(accNumber)) {
               JOptionPane.showMessageDialog( this , "Invalid account number. It must be exactly 10 numeric digits.");
            return;
        }

        
        double depositAmount ; 
      

       
        try {
            depositAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
               JOptionPane.showMessageDialog(this,"Invalid deposit amount Please enter a valid number");
            return;
        }

    
        if (depositAmount > 10000) {
               JOptionPane.showMessageDialog(this,"Deposit amount exceeds the maximum limit of $10,000 per transaction");
            return;
        } else if (depositAmount <= 0) {
               JOptionPane.showMessageDialog(this,"enter a valid deposit amount");
            return;
        }

     
        boolean accountFound = false;
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accNumber)) {
                    accountFound = true;

                   
                    double currentBalance = Double.parseDouble(data[3]);
                    currentBalance += depositAmount;
                    data[3] = String.format("%.2f", currentBalance);

                       JOptionPane.showMessageDialog(this,"Deposit successful! New balance: $" + currentBalance);

                   
                    updatedLines.add(String.join(",", data));

                
                    recordTransaction(accNumber, "Deposit", depositAmount);
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
               JOptionPane.showMessageDialog(this,"Error reading the accounts file: " );
            return;
        }

        if (!accountFound) {
               JOptionPane.showMessageDialog(this,"Account number not found.");
            return;
        }

     
        try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
               JOptionPane.showMessageDialog(this,"Error updating the accounts file: " );
        }
    }

    public void Withdraw(String accNumber, String amount) {

        if (!isValidAccountNumber(accNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid account number. It must be exactly 10 numeric digits.");
            return;
        }

        double withdrawAmount;

        try {
            withdrawAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid withdraw amount. Please enter a valid number.");
            return;
        }

        if (withdrawAmount > 10000) {
            JOptionPane.showMessageDialog(this, "Withdraw amount exceeds the maximum limit of $10,000 per transaction.");
            return;
        } else if (withdrawAmount <= 0) {
            JOptionPane.showMessageDialog(this, "Enter a valid withdraw amount.");
            return;
        }

        boolean accountFound = false;
        List<String> updatedLines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;
            double currentBalance;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(accNumber)) {
                    currentBalance = Double.parseDouble(data[3]);
                    accountFound = true;

                    if (withdrawAmount > currentBalance) {
                        JOptionPane.showMessageDialog(this, "Insufficient funds to complete the withdrawal.");
                        updatedLines.add(line);
                        continue;
                    }

                    if (withdrawAmount < 1000 && data[6].equalsIgnoreCase("current")) {
                        if (currentBalance < withdrawAmount + 10) {
                            JOptionPane.showMessageDialog(this, "Insufficient funds to cover the additional $10 fee for current accounts.");
                            updatedLines.add(line);
                            continue;
                        }
                        JOptionPane.showMessageDialog(this, "Your account is of type 'current'. An extra $10 will be deducted.");
                        currentBalance -= withdrawAmount + 10;
                    } else {
                        currentBalance -= withdrawAmount;
                    }

                    data[3] = String.format("%.2f", currentBalance);
                    JOptionPane.showMessageDialog(this, "Withdraw successful! New balance: $" + currentBalance);

                    updatedLines.add(String.join(",", data));

                    recordTransaction(accNumber, "withdraw", withdrawAmount);
                } else {
                    updatedLines.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading the accounts file: " + e.getMessage());
            return;
        }

        if (!accountFound) {
            JOptionPane.showMessageDialog(this, "Account number not found.");
            return;
        }

        try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating the accounts file: " + e.getMessage());
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

    private void recordTransaction(String accountNumber, String type, double amount) {
        String transactionFile = accountNumber + ".txt";
        try (FileWriter writer = new FileWriter(transactionFile, true)) {
            writer.write(type + ": $" + amount + " on " + new Date() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error recording the transaction: " + e.getMessage());
        }
    }
}


//    public static void main(String[] args) {
//        Scanner amicia = new Scanner(System.in);
//           JOptionPane.showMessageDialog(this,"would you like to deposit or withdraw");
//           JOptionPane.showMessageDialog(this,"choose (1) for deposit");
//           JOptionPane.showMessageDialog(this,"choose (2) for withdraw");
//        String choice = amicia.nextLine();
//        if (choice.equals("1")) {
//            deposit();
//        }
//        else if (choice.equals("2")) {
//            Withdraw();
//        }
//    }
    
   