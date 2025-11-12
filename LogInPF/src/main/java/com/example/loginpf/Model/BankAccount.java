package com.example.loginpf.Model;

public class BankAccount {
    private String number;
    private AccountType type;
    private double balance;

    public BankAccount(String number, AccountType type, double balance) {
        this.number = number;
        this.type = type == null ? AccountType.SAVINGS : type;
        this.balance = balance;
    }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public AccountType getType() { return type; }
    public void setType(AccountType type) { this.type = type; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

    @Override
    public String toString() {
        return number + " (" + (type == null ? "Savings" : type.display()) + ")";
    }
}