package utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    
    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    // Phone number pattern (Indian format)
    private static final Pattern PHONE_PATTERN = 
            Pattern.compile("^[6-9]\\d{9}$");
    
    // PIN pattern (4 digits)
    private static final Pattern PIN_PATTERN = 
            Pattern.compile("^\\d{4}$");
    
    /**
     * Validates if the given string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        if (!isNotEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validates phone number format (Indian 10-digit mobile number)
     */
    public static boolean isValidPhoneNumber(String phone) {
        if (!isNotEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validates PIN format (4 digits)
     */
    public static boolean isValidPin(String pin) {
        if (!isNotEmpty(pin)) {
            return false;
        }
        return PIN_PATTERN.matcher(pin.trim()).matches();
    }
    
    /**
     * Validates amount (must be positive and have at most 2 decimal places)
     */
    public static boolean isValidAmount(String amountStr) {
        if (!isNotEmpty(amountStr)) {
            return false;
        }
        
        try {
            double amount = Double.parseDouble(amountStr.trim());
            return amount > 0 && amount <= 999999999.99; // Max limit
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Validates amount for double value
     */
    public static boolean isValidAmount(double amount) {
        return amount > 0 && amount <= 999999999.99;
    }
    
    /**
     * Validates customer name (only letters and spaces, 2-50 characters)
     */
    public static boolean isValidName(String name) {
        if (!isNotEmpty(name)) {
            return false;
        }
        
        String trimmedName = name.trim();
        return trimmedName.length() >= 2 && 
               trimmedName.length() <= 50 && 
               trimmedName.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validates account number format
     */
    public static boolean isValidAccountNumber(String accountNumber) {
        if (!isNotEmpty(accountNumber)) {
            return false;
        }
        
        return accountNumber.trim().matches("^ACC\\d+$");
    }
    
    /**
     * Validates minimum deposit amount based on account type
     */
    public static boolean isValidMinimumDeposit(double amount, String accountType) {
        switch (accountType.toUpperCase()) {
            case "SAVINGS":
                return amount >= 1000.0; // Minimum ₹1000 for savings
            case "CURRENT":
                return amount >= 5000.0; // Minimum ₹5000 for current
            case "FIXED_DEPOSIT":
                return amount >= 10000.0; // Minimum ₹10000 for FD
            default:
                return amount >= 1000.0;
        }
    }
    
    /**
     * Validates address (must be between 10-200 characters)
     */
    public static boolean isValidAddress(String address) {
        if (!isNotEmpty(address)) {
            return false;
        }
        
        String trimmedAddress = address.trim();
        return trimmedAddress.length() >= 10 && trimmedAddress.length() <= 200;
    }
    
    /**
     * Sanitizes input string by trimming and removing extra spaces
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("\\s+", " ");
    }
    
    /**
     * Formats amount to 2 decimal places
     */
    public static String formatAmount(double amount) {
        return String.format("%.2f", amount);
    }
    
    /**
     * Validates withdrawal amount against available balance
     */
    public static boolean canWithdraw(double withdrawalAmount, double availableBalance) {
        return withdrawalAmount > 0 && 
               withdrawalAmount <= availableBalance && 
               (availableBalance - withdrawalAmount) >= 100.0; // Minimum balance ₹100
    }
    
    /**
     * Gets validation error message for different validation types
     */
    public static String getValidationErrorMessage(String field, String value) {
        switch (field.toLowerCase()) {
            case "name":
                return "Name must contain only letters and spaces (2-50 characters)";
            case "email":
                return "Please enter a valid email address";
            case "phone":
                return "Please enter a valid 10-digit mobile number";
            case "pin":
                return "PIN must be exactly 4 digits";
            case "amount":
                return "Amount must be a positive number with at most 2 decimal places";
            case "address":
                return "Address must be between 10-200 characters";
            case "account":
                return "Please enter a valid account number";
            default:
                return "Invalid input for " + field;
        }
    }
}