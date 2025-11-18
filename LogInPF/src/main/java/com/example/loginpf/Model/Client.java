package com.example.loginpf.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client {
    private String name;
    private String username;
    private String password;
    private String id;


    private String account;
    private Double cash;
    private AccountType accountType = AccountType.SAVINGS;


    private final ObservableList<BankAccount> accounts = FXCollections.observableArrayList();
    private String primaryAccountNumber;

    private static int accountCounter = 0;

    public Client() {}

    public Client(String name, String username, String password, String id, String account, Double cash) {
        this(name, username, password, id, account, cash, AccountType.SAVINGS);
    }

    public Client(String name, String username, String password, String id, String account, Double cash, AccountType type) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.id = id;

        String accNum = account != null ? account : generateAccount();
        BankAccount acc = new BankAccount(accNum, type == null ? AccountType.SAVINGS : type, cash == null ? 0.0 : cash);
        this.accounts.add(acc);
        this.primaryAccountNumber = accNum;

        this.account = accNum;
        this.cash = acc.getBalance();
        this.accountType = acc.getType();
    }

    public Client(String username, String password) {
        this.username = username;
        this.password = password;
        String accNum = generateAccount();
        BankAccount acc = new BankAccount(accNum, AccountType.SAVINGS, 0.0);
        this.accounts.add(acc);
        this.primaryAccountNumber = accNum;

        this.account = accNum;
        this.cash = 0.0;
        this.accountType = AccountType.SAVINGS;
    }

    public static synchronized String generateAccount() {
        accountCounter++;
        return String.format("%04d", accountCounter);
    }


    public ObservableList<BankAccount> getAccounts() { return accounts; }

    public BankAccount getPrimaryAccount() {
        return accounts.stream()
                .filter(a -> a.getNumber().equals(primaryAccountNumber))
                .findFirst()
                .orElse(accounts.isEmpty() ? null : accounts.get(0));
    }

    public void setPrimaryAccount(String number) { this.primaryAccountNumber = number; syncLegacyFromPrimary(); }

    public BankAccount addAccount(AccountType type, double initialBalance) {
        String num = generateAccount();
        BankAccount acc = new BankAccount(num, type == null ? AccountType.SAVINGS : type, initialBalance);
        accounts.add(acc);
        if (getPrimaryAccount() == null) {
            primaryAccountNumber = num;
        }
        syncLegacyFromPrimary();
        return acc;
    }

    private void syncLegacyFromPrimary() {
        BankAccount p = getPrimaryAccount();
        if (p != null) {
            this.account = p.getNumber();
            this.cash = p.getBalance();
            this.accountType = p.getType();
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }


    public String getAccount() { return getPrimaryAccount() != null ? getPrimaryAccount().getNumber() : (account == null ? "" : account); }
    public void setAccount(String account) { this.primaryAccountNumber = account; this.account = account; }

    public AccountType getAccountType() { return getPrimaryAccount() != null ? getPrimaryAccount().getType() : (accountType == null ? AccountType.SAVINGS : accountType); }
    public void setAccountType(AccountType accountType) {
        BankAccount p = getPrimaryAccount();
        if (p != null) p.setType(accountType == null ? AccountType.SAVINGS : accountType);
        this.accountType = accountType;
    }

    public Double getCash() { return getPrimaryAccount() != null ? getPrimaryAccount().getBalance() : (cash == null ? 0.0 : cash); }
    public void setCash(Double cash) {
        BankAccount p = getPrimaryAccount();
        if (p != null) p.setBalance(cash == null ? 0.0 : cash);
        this.cash = cash;
    }

    @Override
    public String toString() {
        return "Cliente";
    }
}