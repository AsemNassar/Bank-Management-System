package finalproject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Accounts extends baseAccount {
    private static final String FILE_NAME = "accounts.txt";

    public Accounts(String name, String mobile, String email, String accountType) {
        super(name, mobile, email, accountType);
        saveToFile();
        readFromFile();
    }

    @Override
    public void saveToFile() {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(toFileFormat() + "\n");
            System.out.println("\nAccount saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    @Override
    public void readFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
