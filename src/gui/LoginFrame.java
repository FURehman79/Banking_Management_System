package gui;

import models.Account;
import services.AccountService;
import utils.ValidationUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame implements ActionListener {
    
    private JTextField accountField;
    private JPasswordField pinField;
    private JButton loginButton;
    private JButton createAccountButton;
    private JButton exitButton;
    private JLabel statusLabel;
    
    private AccountService accountService;
    
    private static final Color PRIMARY_COLOR = new Color(52, 58, 64);
    private static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    private static final Color DANGER_COLOR = new Color(220, 53, 69);
    private static final Color INFO_COLOR = new Color(23, 162, 184);
    
    public LoginFrame() {
        accountService = new AccountService();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupFrame();
    }
    
    private void initializeComponents() {
        accountField = new JTextField(15);
        pinField = new JPasswordField(15);
        
        accountField.setFont(new Font("Arial", Font.PLAIN, 14));
        pinField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        loginButton = new JButton("Login");
        createAccountButton = new JButton("Create New Account");
        exitButton = new JButton("Exit");
        
        styleButton(loginButton, SUCCESS_COLOR);
        styleButton(createAccountButton, INFO_COLOR);
        styleButton(exitButton, DANGER_COLOR);
        
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
        
        JLabel titleLabel = new JLabel("BANKING MANAGEMENT SYSTEM");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Account Number:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(accountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("PIN:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(pinField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        mainPanel.add(statusLabel, gbc);
        
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(loginButton, gbc);
        
        gbc.gridy = 4;
        mainPanel.add(createAccountButton, gbc);
        
        gbc.gridy = 5;
        mainPanel.add(exitButton, gbc);
        
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        loginButton.addActionListener(this);
        createAccountButton.addActionListener(this);
        exitButton.addActionListener(this);
    }
    
    private void setupFrame() {
        setTitle("Banking Management System - Login");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            handleLogin();
        } else if (e.getSource() == createAccountButton) {
            openCreateAccountFrame();
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }
    
    private void handleLogin() {
        String accountNumber = accountField.getText().trim();
        String pin = new String(pinField.getPassword()).trim();
        
        if (accountNumber.isEmpty() || pin.isEmpty()) {
            showStatus("Please fill all fields", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidAccountNumber(accountNumber)) {
            showStatus("Invalid account number format", DANGER_COLOR);
            return;
        }
        
        if (!ValidationUtils.isValidPin(pin)) {
            showStatus("PIN must be 4 digits", DANGER_COLOR);
            return;
        }
        
        Account account = accountService.authenticateUser(accountNumber, pin);
        if (account != null) {
            showStatus("Login successful!", SUCCESS_COLOR);
            openMainFrame(account);
        } else {
            showStatus("Invalid account number or PIN", DANGER_COLOR);
            pinField.setText("");
        }
    }
    
    private void openCreateAccountFrame() {
        this.setVisible(false);
        new CreateAccountFrame(this).setVisible(true);
    }
    
    private void openMainFrame(Account account) {
        this.dispose();
        new MainFrame(account).setVisible(true);
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    public void refresh() {
        accountField.setText("");
        pinField.setText("");
        statusLabel.setText(" ");
    }
    
    public void showAccountCreatedMessage(String accountNumber) {
        accountField.setText(accountNumber);
        showStatus("Account created successfully! Please login.", SUCCESS_COLOR);
    }
}