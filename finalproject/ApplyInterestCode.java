package finalproject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.JOptionPane;

public class ApplyInterestCode extends javax.swing.JFrame {
    private static final String ACCOUNTS_FILE = "accounts.txt";
    private static final double INTEREST_RATE = 0.05;
    private static final int ACTIVE_MONTHS_THRESHOLD = 4;

    public  void applyInterest() {
        List<String> updatedLines = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNTS_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

             
                if (data.length < 7) {
                   
                    continue;
                }

                String accountType = data[6];
                String dateOpened = data[5];

            
                if (accountType.equalsIgnoreCase("Savings")) {
                    try {
                        Date openedDate = dateFormat.parse(dateOpened);
                        if (isActiveForFourMonthsOrMore(openedDate)) {
                            double balance = Double.parseDouble(data[3]);
                            double interest = balance * INTEREST_RATE;
                            balance += interest;
                            data[3] = String.format("%.2f", balance);

                            recordTransaction(data[0], "Interest Applied", interest);
                        }
                    } catch (ParseException e) {
                        
                    } catch (NumberFormatException e) {
                        
                    }
                }

              
                updatedLines.add(String.join(",", data));
            }
        } catch (IOException e) {
    
            return;
        }

   
        try (FileWriter writer = new FileWriter(ACCOUNTS_FILE)) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine + "\n");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error updating the accounts file: " + e.getMessage());
        }

        JOptionPane.showMessageDialog(this,"Interest applied to eligible accounts successfully!");
    }

    private static boolean isActiveForFourMonthsOrMore(Date dateOpened) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -ACTIVE_MONTHS_THRESHOLD);
        Date fourMonthsAgo = calendar.getTime();
        return dateOpened.before(fourMonthsAgo);
    }

    private  void recordTransaction(String accountNumber, String type, double amount) {
        String transactionFile = accountNumber + ".txt";
        try (FileWriter writer = new FileWriter(transactionFile, true)) {
            writer.write(type + ": $" + amount + " on " + new Date() + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,"Error recording the transaction: " + e.getMessage());
        }
    }
}
