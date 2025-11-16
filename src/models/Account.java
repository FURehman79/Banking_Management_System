package models;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String accountNumber;
    private String customerName;
    private String phoneNumber;
    private String email;
    private String address;
    private AccountType accountType;
    private double balance;
    private Date dateCreated;
    private String pin;
    private boolean isActive;
    
    public enum AccountType {
        SAVINGS("Savings Account"),
        CURRENT("Current Account"),
        FIXED_DEPOSIT("Fixed Deposit Account");
        
        private String displayName;
        
        AccountType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // Default constructor
    public Account() {
        this.dateCreated = new Date();
        this.isActive = true;
    }
    
    // Parameterized constructor
    public Account(String customerName, String phoneNumber, String email, 
                   String address, AccountType accountType, double initialDeposit) {
        this.accountNumber = generateAccountNumber();
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.accountType = accountType;
        this.balance = initialDeposit;
        this.dateCreated = new Date();
        this.isActive = true;
    }
    
    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public AccountType getAccountType() {
        return accountType;
    }
    
    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public Date getDateCreated() {
        return dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public String getPin() {
        return pin;
    }
    
    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", customerName='" + customerName + '\'' +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", dateCreated=" + dateCreated +
                ", isActive=" + isActive +
                '}';
    }
}