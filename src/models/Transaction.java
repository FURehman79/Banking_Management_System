package models;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String transactionId;
    private String accountNumber;
    private TransactionType type;
    private double amount;
    private double balanceAfter;
    private Date timestamp;
    private String description;
    private String transferToAccount; // For transfer transactions
    private String status;
    
    public enum TransactionType {
        DEPOSIT("Deposit"),
        WITHDRAWAL("Withdrawal"),
        TRANSFER_IN("Transfer In"),
        TRANSFER_OUT("Transfer Out"),
        BALANCE_INQUIRY("Balance Inquiry");
        
        private String displayName;
        
        TransactionType(String displayName) {
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
    public Transaction() {
        this.timestamp = new Date();
        this.status = "SUCCESS";
    }
    
    // Parameterized constructor
    public Transaction(String accountNumber, TransactionType type, 
                      double amount, double balanceAfter, String description) {
        this.transactionId = generateTransactionId();
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = new Date();
        this.description = description;
        this.status = "SUCCESS";
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis() + (int)(Math.random() * 1000);
    }
    
    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public void setType(TransactionType type) {
        this.type = type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTransferToAccount() {
        return transferToAccount;
    }
    
    public void setTransferToAccount(String transferToAccount) {
        this.transferToAccount = transferToAccount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", balanceAfter=" + balanceAfter +
                ", timestamp=" + timestamp +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}