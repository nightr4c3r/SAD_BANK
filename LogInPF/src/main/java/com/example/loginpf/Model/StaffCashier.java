package com.example.loginpf.Model;

public class StaffCashier {
    private String username;
    private String password;

    public StaffCashier() {}
    public StaffCashier(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Cashier";
    }
}
