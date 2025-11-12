package com.example.loginpf.Model;

public enum AccountType {
    SAVINGS,
    CHECKING,
    BUSINESS;

    public static AccountType fromDisplay(String display) {
        if (display == null) return SAVINGS;
        switch (display.toLowerCase()) {
            case "savings":
            case "ahorros":
                return SAVINGS;
            case "checking":
            case "corriente":
                return CHECKING;
            case "business":
            case "negocios":
                return BUSINESS;
            default:
                return SAVINGS;
        }
    }

    public String display() {
        switch (this) {
            case SAVINGS: return "Savings";
            case CHECKING: return "Checking";
            case BUSINESS: return "Business";
            default: return name();
        }
    }
}
