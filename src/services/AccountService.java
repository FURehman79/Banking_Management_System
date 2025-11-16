package services;

import models.Account;
import models.Transaction;
import utils.ValidationUtils;
import java.util.*;
import java.util.stream.Collectors;

public class AccountService {
    private List<Account> accounts;
    private FileManager fileManager;
    private TransactionService transactionService;
    private static final String ACCOUNTS_FILE = "data/accounts.dat";
    
//    public AccountService() {
//        fileManager = new FileManager();
//        transactionService = new TransactionService();
//        loadAccounts();
        
    public AccountService() {
        fileManager = new FileManager();
        transactionService = new TransactionService();
        loadAccounts();
        
        // ADD THIS FOR TESTING
        if (accounts.isEmpty()) {
            System.out.println("Creating test account...");
            try {
                Account testAccount = new Account("Test User", "9999999999", "test@test.com", 
                                                "Test Address", Account.AccountType.SAVINGS, 1000.0);
                testAccount.setPin("1234");
                accounts.add(testAccount);
                saveAccounts();
                System.out.println("Test account created: " + testAccount.getAccountNumber() + " PIN: 1234");
            } catch (Exception e) {
                System.out.println("Error creating test account: " + e.getMessage());
            }
        }
     
        
         
        
    }
    
    
    
    
    public Account createAccount(String customerName, String phoneNumber, String email, 
                               String address, Account.AccountType accountType, 
                               double initialDeposit, String pin) {
        
        if (!ValidationUtils.isValidName(customerName)) {
            throw new IllegalArgumentException("Invalid customer name");
        }
        
        if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
        
        if (!ValidationUtils.isValidAddress(address)) {
            throw new IllegalArgumentException("Invalid address");
        }
        
        if (!ValidationUtils.isValidPin(pin)) {
            throw new IllegalArgumentException("Invalid PIN format");
        }
        
        if (!ValidationUtils.isValidMinimumDeposit(initialDeposit, accountType.name())) {
            throw new IllegalArgumentException("Insufficient initial deposit for account type");
        }
        
        if (isEmailExists(email)) {
            throw new IllegalArgumentException("Email address already exists");
        }
        
        if (isPhoneExists(phoneNumber)) {
            throw new IllegalArgumentException("Phone number already exists");
        }
        
        Account account = new Account(
            ValidationUtils.sanitizeInput(customerName),
            ValidationUtils.sanitizeInput(phoneNumber),
            ValidationUtils.sanitizeInput(email),
            ValidationUtils.sanitizeInput(address),
            accountType,
            initialDeposit
        );
        account.setPin(pin);
     // After account.setPin(pin); line, add:
        System.out.println("DEBUG: Account created with PIN: " + pin);
        System.out.println("DEBUG: Account number: " + account.getAccountNumber());
        System.out.println("DEBUG: Account saved in list: " + accounts.size() + " accounts total");
        
        accounts.add(account);
        saveAccounts();
        
        Transaction initialTransaction = new Transaction(
            account.getAccountNumber(),
            Transaction.TransactionType.DEPOSIT,
            initialDeposit,
            initialDeposit,
            "Initial deposit - Account opening"
        );
        transactionService.recordTransaction(initialTransaction);
        
        return account;
    }
    
    public void verifyAccountExists(String accountNumber) {
        Account found = getAccountByNumber(accountNumber);
        if (found != null) {
            System.out.println("Account EXISTS: " + accountNumber + " PIN: " + found.getPin());
        } else {
            System.out.println("Account NOT FOUND: " + accountNumber);
            System.out.println("Available accounts:");
            for (Account acc : accounts) {
                System.out.println("  - " + acc.getAccountNumber() + " (PIN: " + acc.getPin() + ")");
            }
        }
    }
    
    
    public Account authenticateUser(String accountNumber, String pin) {
        System.out.println("DEBUG: Login attempt - Account: " + accountNumber + ", PIN: " + pin);
        
        if (!ValidationUtils.isNotEmpty(accountNumber) || !ValidationUtils.isNotEmpty(pin)) {
            System.out.println("DEBUG: Empty account number or PIN");
            return null;
        }
        
        // Add this loop to see all accounts
        for (Account acc : accounts) {
            System.out.println("DEBUG: Checking account " + acc.getAccountNumber() + 
                             " with PIN " + acc.getPin() + " (Active: " + acc.isActive() + ")");
        }
        
        Account result = accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber.trim()))
                .filter(account -> account.getPin().equals(pin.trim()))
                .filter(Account::isActive)
                .findFirst()
                .orElse(null);
        
        System.out.println("DEBUG: Authentication result: " + (result != null ? "SUCCESS" : "FAILED"));
        return result;
    }
    
    public boolean deposit(String accountNumber, double amount, String description) {
        if (!ValidationUtils.isValidAmount(amount)) return false;
        
        Account account = getAccountByNumber(accountNumber);
        if (account == null || !account.isActive()) return false;
        
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        
        if (saveAccounts()) {
            Transaction transaction = new Transaction(
                accountNumber,
                Transaction.TransactionType.DEPOSIT,
                amount,
                newBalance,
                description != null ? description : "Cash deposit"
            );
            transactionService.recordTransaction(transaction);
            return true;
        }
        
        return false;
    }
    
    public boolean withdraw(String accountNumber, double amount, String description) {
        if (!ValidationUtils.isValidAmount(amount)) return false;
        
        Account account = getAccountByNumber(accountNumber);
        if (account == null || !account.isActive()) return false;
        
        if (!ValidationUtils.canWithdraw(amount, account.getBalance())) return false;
        
        double newBalance = account.getBalance() - amount;
        account.setBalance(newBalance);
        
        if (saveAccounts()) {
            Transaction transaction = new Transaction(
                accountNumber,
                Transaction.TransactionType.WITHDRAWAL,
                amount,
                newBalance,
                description != null ? description : "Cash withdrawal"
            );
            transactionService.recordTransaction(transaction);
            return true;
        }
        
        return false;
    }
    
    public boolean transfer(String fromAccountNumber, String toAccountNumber, double amount, String description) {
        if (!ValidationUtils.isValidAmount(amount)) return false;
        if (fromAccountNumber.equals(toAccountNumber)) return false;
        
        Account fromAccount = getAccountByNumber(fromAccountNumber);
        Account toAccount = getAccountByNumber(toAccountNumber);
        
        if (fromAccount == null || toAccount == null || 
            !fromAccount.isActive() || !toAccount.isActive()) return false;
        
        if (!ValidationUtils.canWithdraw(amount, fromAccount.getBalance())) return false;
        
        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        
        if (saveAccounts()) {
            String transferDescription = description != null ? description : "Transfer between accounts";
            
            Transaction debitTransaction = new Transaction(
                fromAccountNumber,
                Transaction.TransactionType.TRANSFER_OUT,
                amount,
                fromAccount.getBalance(),
                transferDescription + " - Transfer to " + toAccountNumber
            );
            debitTransaction.setTransferToAccount(toAccountNumber);
            
            Transaction creditTransaction = new Transaction(
                toAccountNumber,
                Transaction.TransactionType.TRANSFER_IN,
                amount,
                toAccount.getBalance(),
                transferDescription + " - Transfer from " + fromAccountNumber
            );
            
            transactionService.recordTransaction(debitTransaction);
            transactionService.recordTransaction(creditTransaction);
            return true;
        }
        
        return false;
    }
    
    public boolean changePin(String accountNumber, String oldPin, String newPin) {
        if (!ValidationUtils.isValidPin(newPin)) return false;
        
        Account account = authenticateUser(accountNumber, oldPin);
        if (account == null) return false;
        
        account.setPin(newPin);
        return saveAccounts();
    }
    
    public Account getAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }
    
    public boolean isEmailExists(String email) {
        return accounts.stream()
                .anyMatch(account -> account.getEmail().equalsIgnoreCase(email.trim()));
    }
    
    public boolean isPhoneExists(String phoneNumber) {
        return accounts.stream()
                .anyMatch(account -> account.getPhoneNumber().equals(phoneNumber.trim()));
    }
    
    private void loadAccounts() {
        accounts = fileManager.loadAccounts(ACCOUNTS_FILE);
        if (accounts == null) {
            accounts = new ArrayList<>();
        }
    }
    
    private boolean saveAccounts() {
        return fileManager.saveAccounts(accounts, ACCOUNTS_FILE);
    }
}