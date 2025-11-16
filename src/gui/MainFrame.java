package gui;

import models.Account;
import services.AccountService;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {
    
    private Account currentAccount;
    private AccountService accountService;
    private JLabel balanceLabel;
    private JLabel accountInfoLabel;
    
    private JButton depositButton;
    private JButton withdrawButton;
    private JButton transferButton;
    private JButton balanceButton;
    private JButton changePinButton;
    private JButton logoutButton;
    
    private static final Color PRIMARY_COLOR = new Color(52, 58, 64);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    private static final Color WARNING_COLOR = new Color(255, 193, 7);
    
    public MainFrame(Account account) {
        this.currentAccount = account;
        this.accountService = new AccountService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
        updateAccountInfo();
    }
    
    private void initializeComponents() {
        balanceLabel = new JLabel();
        accountInfoLabel = new JLabel();
        
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 28));
        balanceLabel.setForeground(SUCCESS_COLOR);
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        accountInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        accountInfoLabel.setForeground(Color.WHITE);
        
        depositButton = new JButton("Deposit Money");
        withdrawButton = new JButton("Withdraw Money");
        transferButton = new JButton("Transfer Money");
        balanceButton = new JButton("Check Balance");
        changePinButton = new JButton("Change PIN");
        logoutButton = new JButton("Logout");
        
        styleButton(depositButton, SUCCESS_COLOR);
        styleButton(withdrawButton, WARNING_COLOR);
        styleButton(transferButton, INFO_COLOR);
        styleButton(balanceButton, new Color(108, 117, 125));
        styleButton(changePinButton, new Color(108, 117, 125));
        styleButton(logoutButton, DANGER_COLOR);
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 60));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        accountInfoLabel.setText("Welcome, " + currentAccount.getCustomerName() + 
                                " | Account: " + currentAccount.getAccountNumber());
        headerPanel.add(accountInfoLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        // Balance Panel
        JPanel balancePanel = new JPanel();
        balancePanel.setBackground(Color.WHITE);
        balancePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR, 2), 
            "Current Balance",
            0, 0, new Font("Arial", Font.BOLD, 16), PRIMARY_COLOR));
        balancePanel.add(balanceLabel);
        
        // Menu Panel
        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        menuPanel.add(depositButton);
        menuPanel.add(withdrawButton);
        menuPanel.add(transferButton);
        menuPanel.add(balanceButton);
        menuPanel.add(changePinButton);
        menuPanel.add(new JLabel()); // Empty space
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(balancePanel, BorderLayout.NORTH);
        contentPanel.add(menuPanel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);
        transferButton.addActionListener(this);
        balanceButton.addActionListener(this);
        changePinButton.addActionListener(this);
        logoutButton.addActionListener(this);
    }
    
    private void setupFrame() {
        setTitle("Banking System - " + currentAccount.getCustomerName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == depositButton) {
            handleDeposit();
        } else if (e.getSource() == withdrawButton) {
            handleWithdraw();
        } else if (e.getSource() == transferButton) {
            handleTransfer();
        } else if (e.getSource() == balanceButton) {
            handleCheckBalance();
        } else if (e.getSource() == changePinButton) {
            handleChangePin();
        } else if (e.getSource() == logoutButton) {
            handleLogout();
        }
    }
    
    private void handleDeposit() {
        String amountStr = JOptionPane.showInputDialog(this, 
            "Enter amount to deposit:", "Deposit Money", JOptionPane.PLAIN_MESSAGE);
        
        if (amountStr == null) return; // User cancelled
        
        if (!ValidationUtils.isValidAmount(amountStr)) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double amount = Double.parseDouble(amountStr.trim());
        
        if (accountService.deposit(currentAccount.getAccountNumber(), amount, "Cash deposit via ATM")) {
            refreshAccountData();
            JOptionPane.showMessageDialog(this, 
                "Successfully deposited ₹" + ValidationUtils.formatAmount(amount), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Deposit failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleWithdraw() {
        String amountStr = JOptionPane.showInputDialog(this, 
            "Enter amount to withdraw:", "Withdraw Money", JOptionPane.PLAIN_MESSAGE);
        
        if (amountStr == null) return;
        
        if (!ValidationUtils.isValidAmount(amountStr)) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double amount = Double.parseDouble(amountStr.trim());
        
        if (!ValidationUtils.canWithdraw(amount, currentAccount.getBalance())) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient balance! Minimum balance of ₹100 must be maintained.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (accountService.withdraw(currentAccount.getAccountNumber(), amount, "Cash withdrawal via ATM")) {
            refreshAccountData();
            JOptionPane.showMessageDialog(this, 
                "Successfully withdrawn ₹" + ValidationUtils.formatAmount(amount), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Withdrawal failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleTransfer() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JTextField toAccountField = new JTextField();
        JTextField amountField = new JTextField();
        
        panel.add(new JLabel("To Account Number:"));
        panel.add(toAccountField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Transfer Money", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) return;
        
        String toAccount = toAccountField.getText().trim();
        String amountStr = amountField.getText().trim();
        
        if (toAccount.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidAccountNumber(toAccount)) {
            JOptionPane.showMessageDialog(this, "Invalid destination account number!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidAmount(amountStr)) {
            JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double amount = Double.parseDouble(amountStr);
        
        if (!ValidationUtils.canWithdraw(amount, currentAccount.getBalance())) {
            JOptionPane.showMessageDialog(this, 
                "Insufficient balance! Minimum balance of ₹100 must be maintained.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (accountService.transfer(currentAccount.getAccountNumber(), toAccount, amount, "Online transfer")) {
            refreshAccountData();
            JOptionPane.showMessageDialog(this, 
                "Successfully transferred ₹" + ValidationUtils.formatAmount(amount) + " to " + toAccount, 
                "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Transfer failed! Check destination account number.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleCheckBalance() {
        refreshAccountData();
        JOptionPane.showMessageDialog(this, 
            "Current Balance: ₹" + ValidationUtils.formatAmount(currentAccount.getBalance()) + "\n" +
            "Account Type: " + currentAccount.getAccountType().getDisplayName() + "\n" +
            "Account Status: " + (currentAccount.isActive() ? "Active" : "Inactive"),
            "Balance Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void handleChangePin() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        JPasswordField oldPinField = new JPasswordField();
        JPasswordField newPinField = new JPasswordField();
        
        panel.add(new JLabel("Current PIN:"));
        panel.add(oldPinField);
        panel.add(new JLabel("New PIN (4 digits):"));
        panel.add(newPinField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, "Change PIN", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result != JOptionPane.OK_OPTION) return;
        
        String oldPin = new String(oldPinField.getPassword()).trim();
        String newPin = new String(newPinField.getPassword()).trim();
        
        if (oldPin.isEmpty() || newPin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!ValidationUtils.isValidPin(newPin)) {
            JOptionPane.showMessageDialog(this, "New PIN must be exactly 4 digits!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (accountService.changePin(currentAccount.getAccountNumber(), oldPin, newPin)) {
            JOptionPane.showMessageDialog(this, "PIN changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "PIN change failed! Check your current PIN.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    private void refreshAccountData() {
        currentAccount = accountService.getAccountByNumber(currentAccount.getAccountNumber());
        updateAccountInfo();
    }
    
    private void updateAccountInfo() {
        balanceLabel.setText("₹ " + ValidationUtils.formatAmount(currentAccount.getBalance()));
    }
}