package services;

import models.Transaction;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionService {
    private List<Transaction> transactions;
    private FileManager fileManager;
    private static final String TRANSACTIONS_FILE = "data/transactions.dat";
    
    public TransactionService() {
        fileManager = new FileManager();
        loadTransactions();
    }
    
    public boolean recordTransaction(Transaction transaction) {
        if (transaction == null) return false;
        transactions.add(transaction);
        return saveTransactions();
    }
    
    public List<Transaction> getTransactionsByAccount(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }
    
    public List<Transaction> getRecentTransactions(String accountNumber, int limit) {
        return getTransactionsByAccount(accountNumber).stream()
                .limit(limit)
                .collect(Collectors.toList());
    }
    
    public double getTotalDeposited(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType() == Transaction.TransactionType.DEPOSIT || 
                           t.getType() == Transaction.TransactionType.TRANSFER_IN)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public double getTotalWithdrawn(String accountNumber) {
        return transactions.stream()
                .filter(t -> t.getAccountNumber().equals(accountNumber))
                .filter(t -> t.getType() == Transaction.TransactionType.WITHDRAWAL || 
                           t.getType() == Transaction.TransactionType.TRANSFER_OUT)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    
    public boolean exportTransactionsToCSV(String accountNumber, String filename) {
        List<Transaction> accountTransactions = getTransactionsByAccount(accountNumber);
        return fileManager.exportTransactionsToCSV(accountTransactions, filename);
    }
    
    private void loadTransactions() {
        transactions = fileManager.loadTransactions(TRANSACTIONS_FILE);
        if (transactions == null) {
            transactions = new ArrayList<>();
        }
    }
    
    private boolean saveTransactions() {
        return fileManager.saveTransactions(transactions, TRANSACTIONS_FILE);
    }
}
