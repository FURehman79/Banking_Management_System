package gui;

import models.Account;
import services.AccountService;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateAccountFrame extends JFrame implements ActionListener {
    
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JTextArea addressArea;
    private JComboBox<Account.AccountType> accountTypeCombo;
    private JTextField initialDepositField;
    private JPasswordField pinField;
    private JPasswordField confirmPinField;
    private JButton createButton;
    private JButton cancelButton;
    private JLabel statusLabel;
    
    private AccountService accountService;
    private LoginFrame parentFrame;
    
    private static final Color PRIMARY_COLOR = new Color(52, 58, 64);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    
    public CreateAccountFrame(LoginFrame parent) {
        this.parentFrame = parent;
        this.accountService = new AccountService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        addressArea = new JTextArea(4, 20);
        initialDepositField = new JTextField(20);
        pinField = new JPasswordField(20);
        confirmPinField = new JPasswordField(20);
        
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setBorder(BorderFactory.createLoweredBevelBorder());
        
        accountTypeCombo = new JComboBox<>(Account.AccountType.values());
        
        createButton = new JButton("Create Account");
        cancelButton = new JButton("Cancel");
        
        styleButton(createButton, SUCCESS_COLOR);
        styleButton(cancelButton, DANGER_COLOR);
        
        statusLabel = new JLabel(" ");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }
    
    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("CREATE NEW ACCOUNT");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        int row = 0;
        
        // Full Name
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nameField, gbc);
        row++;
        
        // Phone Number
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Phone Number:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(phoneField, gbc);
        row++;
        
        // Email
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(emailField, gbc);
        row++;
        
        // Address
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        mainPanel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(200, 100));
        mainPanel.add(addressScroll, gbc);
        row++;
        
        // Account Type
        gbc.gridx = 0; gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(new JLabel("Account Type:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(accountTypeCombo, gbc);
        row++;
        
        // Initial Deposit
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Initial Deposit:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(initialDepositField, gbc);
        row++;
        
        // PIN
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("PIN (4 digits):"), gbc);
        gbc.gridx = 1;
        mainPanel.add(pinField, gbc);
        row++;
        
        // Confirm PIN
        gbc.gridx = 0; gbc.gridy = row;
        mainPanel.add(new JLabel("Confirm PIN:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(confirmPinField, gbc);
        row++;
        
        // Status
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(statusLabel, gbc);
        row++;
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridy = row;
        mainPanel.add(buttonPanel, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(new JScrollPane(mainPanel), BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        createButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }
    
    private void setupFrame() {
        setTitle("Create New Account");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == createButton) {
            handleCreateAccount();
        } else if (e.getSource() == cancelButton) {
            handleCancel();
        }
    }
    
    private void handleCreateAccount() {
        // Get all field values
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();
        String address = addressArea.getText().trim();
        Account.AccountType accountType = (Account.AccountType) accountTypeCombo.getSelectedItem();
        String depositStr = initialDepositField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        String confirmPin = new String(confirmPinField.getPassword()).trim();
        
        // Validation
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || 
            address.isEmpty() || depositStr.isEmpty() || pin.isEmpty() || confirmPin.isEmpty()) {
            showStatus("Please fill all fields", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidName(name)) {
            showStatus("Invalid name. Use only letters and spaces (2-50 chars)", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidPhoneNumber(phone)) {
            showStatus("Invalid phone number. Enter 10-digit mobile number", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            showStatus("Invalid email address format", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidAddress(address)) {
            showStatus("Address must be between 10-200 characters", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidAmount(depositStr)) {
            showStatus("Invalid deposit amount", DANGER_COLOR);
            return;
        }
        
        double deposit = Double.parseDouble(depositStr);
        if (!ValidationUtils.isValidMinimumDeposit(deposit, accountType.name())) {
            showStatus("Minimum deposit: ₹1000 (Savings), ₹5000 (Current), ₹10000 (FD)", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidPin(pin)) {
            showStatus("PIN must be exactly 4 digits", DANGER_COLOR);
            return;
        }
        
        if (!pin.equals(confirmPin)) {
            showStatus("PINs do not match", DANGER_COLOR);
            return;
        }
        
        // Create account
        try {
            Account account = accountService.createAccount(name, phone, email, address, accountType, deposit, pin);
            System.out.println("DEBUG: New account created: " + account.getAccountNumber());
            System.out.println("DEBUG: New account PIN: " + account.getPin());


            showStatus("Account created successfully! Account Number: " + account.getAccountNumber(), SUCCESS_COLOR);
            
            // Show success dialog
            JOptionPane.showMessageDialog(this, 
                "Account Created Successfully!\n" +
                "Account Number: " + account.getAccountNumber() + "\n" +
                "Please remember your account number for login.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Return to login
            this.dispose();
            parentFrame.showAccountCreatedMessage(account.getAccountNumber());
            parentFrame.setVisible(true);
            
        } catch (Exception ex) {
            showStatus("Error creating account: " + ex.getMessage(), DANGER_COLOR);
        }
    }
    
    private void handleCancel() {
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel account creation?",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            this.dispose();
            parentFrame.refresh();
            parentFrame.setVisible(true);
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
}
