package com.example.loginpf.Model;

public class Admin extends Person {
    private String username;
    private String password;
    private String report;

    public Admin() {}

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Admin(String username, String password, String report) {
        this.username = username;
        this.password = password;
        this.report = report;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getReport() {
        return report;
    }
    public void setReport(String report) { this.report = report; }

    @Override
    public String toString() {
        return "Admin";
    }
}
