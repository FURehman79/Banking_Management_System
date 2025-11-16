package services;

import models.Account;
import models.Transaction;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class FileManager {
    
    private static final String DATA_DIRECTORY = "data";
    
    private void createDataDirectoryIfNotExists() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }
    
    public boolean saveAccounts(List<Account> accounts, String filename) {
        createDataDirectoryIfNotExists();
        
        System.out.println("DEBUG: Saving " + accounts.size() + " accounts to " + filename);
        
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(accounts);
            System.out.println("DEBUG: Accounts saved successfully");
            return true;
            
        } catch (IOException e) {
            System.err.println("DEBUG: Error saving accounts: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Account> loadAccounts(String filename) {
        File file = new File(filename);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            return (List<Account>) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean saveTransactions(List<Transaction> transactions, String filename) {
        createDataDirectoryIfNotExists();
        
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            
            oos.writeObject(transactions);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving transactions: " + e.getMessage());
            return false;
        }
    }
    
    @SuppressWarnings("unchecked")
    public List<Transaction> loadTransactions(String filename) {
        File file = new File(filename);
        
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            
            return (List<Transaction>) ois.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean exportTransactionsToCSV(List<Transaction> transactions, String filename) {
        createDataDirectoryIfNotExists();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("Transaction ID,Account Number,Type,Amount,Balance After,Timestamp,Description,Status");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            
            for (Transaction transaction : transactions) {
                writer.printf("%s,%s,%s,%.2f,%.2f,%s,\"%s\",%s%n",
                    transaction.getTransactionId(),
                    transaction.getAccountNumber(),
                    transaction.getType().getDisplayName(),
                    transaction.getAmount(),
                    transaction.getBalanceAfter(),
                    dateFormat.format(transaction.getTimestamp()),
                    transaction.getDescription().replace("\"", "\"\""),
                    transaction.getStatus()
                );
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error exporting transactions to CSV: " + e.getMessage());
            return false;
        }
    }
}
