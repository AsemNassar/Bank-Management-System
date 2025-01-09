package finalproject;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class baseAccount {
    protected String accountNumber;
    protected String name;
    protected String mobile;
    protected String email;
    protected double balance;
    protected String dateOpened;
    protected String accountType;

    public baseAccount(String name, String mobile, String email, String accountType) {
        this.accountNumber = generateAccountNumber();
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.balance = 0.0;
        this.dateOpened = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        this.accountType = accountType;
    }

    private String generateAccountNumber() {
        return String.valueOf((long) (Math.random() * 9000000000L) + 1000000000L);
    }

    public abstract void saveToFile();

    public abstract void readFromFile();

    public String toFileFormat() {
        return accountNumber + "," + name + "," + email + "," + String.format("%.2f", balance) + "," + mobile + "," + dateOpened + "," + accountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public String getAccountType() {
        return accountType;
    }
}
