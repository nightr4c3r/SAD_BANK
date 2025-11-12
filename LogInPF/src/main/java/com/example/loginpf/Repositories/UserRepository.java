package com.example.loginpf.Repositories;

import com.example.loginpf.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserRepository {

    // Collections per role
    private static final ObservableList<Client> clients = FXCollections.observableArrayList();
    private static final ObservableList<Admin> admins = FXCollections.observableArrayList();
    private static final ObservableList<StaffCashier> cashiers = FXCollections.observableArrayList();

    // Legacy compatibility names
    public static ObservableList<Client> getUsuarios() { return clients; }
    public static ObservableList<Client> obtenerTodosLosUsuarios() { return clients; }
    public static ObservableList<Admin> obtenerTodosLosAdmins() { return admins; }
    public static ObservableList<StaffCashier> obtenerTodosLosCashiers() { return cashiers; }

    // ---- Adders ----
    public static void agregarCliente(Client cliente) { clients.add(cliente); }
    public static void agregarAdmin(Admin admin) { admins.add(admin); }
    public static void agregarCashier(StaffCashier cashier) { cashiers.add(cashier); }

    // Safe adders (validate uniqueness)
    public static boolean agregarClienteSiUnico(Client cliente) {
        if (cliente == null) return false;
        if (existeUsuario(cliente.getUsername())) return false;
        // Ensure account number unique across clients' primary accounts
        boolean accountTaken = clients.stream().anyMatch(c -> cliente.getAccount().equalsIgnoreCase(c.getAccount()));
        if (accountTaken) return false;
        clients.add(cliente);
        return true;
    }

    public static boolean agregarCashierSiUnico(StaffCashier cashier) {
        if (cashier == null) return false;
        if (existeUsuario(cashier.getUsername())) return false;
        cashiers.add(cashier);
        return true;
    }

    // Legacy method kept for controllers that add clients
    public static void agregarUsuario(Client usuario) { agregarCliente(usuario); }

    // ---- Removal ----
    public static boolean eliminarCliente(String username) {
        return clients.removeIf(c -> c.getUsername().equalsIgnoreCase(username));
    }
    public static boolean eliminarCashier(String username) {
        return cashiers.removeIf(c -> c.getUsername().equalsIgnoreCase(username));
    }
    public static boolean eliminarAdmin(String username) {
        return admins.removeIf(a -> a.getUsername().equalsIgnoreCase(username));
    }

    // ---- Updates ----
    public static boolean actualizarCliente(String originalUsername, Client updated) {
        if (originalUsername == null || updated == null) return false;
        Client existing = obtenerUsuario(originalUsername);
        if (existing == null) return false;
        // If username changes, ensure it's still unique
        if (!originalUsername.equalsIgnoreCase(updated.getUsername()) && existeUsuario(updated.getUsername())) {
            return false;
        }
        // Apply changes
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());
        existing.setName(updated.getName());
        existing.setId(updated.getId());
        existing.setAccount(updated.getAccount());
        existing.setAccountType(updated.getAccountType());
        existing.setCash(updated.getCash());
        return true;
    }

    public static boolean actualizarCashier(String originalUsername, StaffCashier updated) {
        if (originalUsername == null || updated == null) return false;
        StaffCashier existing = obtenerCashier(originalUsername);
        if (existing == null) return false;
        if (!originalUsername.equalsIgnoreCase(updated.getUsername()) && existeUsuario(updated.getUsername())) {
            return false;
        }
        existing.setUsername(updated.getUsername());
        existing.setPassword(updated.getPassword());
        return true;
    }

    // ---- Promote to Admin (multi-role allowed by default) ----
    public static boolean promoverClienteAAdmin(String username) {
        Client c = obtenerUsuario(username);
        if (c == null) return false;
        if (obtenerAdmin(username) != null) return false; // already admin
        agregarAdmin(new Admin(c.getUsername(), c.getPassword()));
        return true;
    }
    public static boolean promoverCashierAAdmin(String username) {
        StaffCashier sc = obtenerCashier(username);
        if (sc == null) return false;
        if (obtenerAdmin(username) != null) return false; // already admin
        agregarAdmin(new Admin(sc.getUsername(), sc.getPassword()));
        return true;
    }

    // ---- Validators ----
    public static boolean validarCliente(String username, String password) {
        return clients.stream().anyMatch(u -> u.getUsername().equals(username) && u.getPassword().equals(password));
    }
    public static boolean validarAdmin(String username, String password) {
        return admins.stream().anyMatch(a -> a.getUsername().equals(username) && a.getPassword().equals(password));
    }
    public static boolean validarCashier(String username, String password) {
        return cashiers.stream().anyMatch(c -> c.getUsername().equals(username) && c.getPassword().equals(password));
    }

    // Legacy method used by LogInController (client validation)
    public static boolean validarCredenciales(String username, String password) {
        return validarCliente(username, password);
    }

    // ---- Queries ----
    public static boolean existeUsuario(String username) {
        boolean existsInClients = clients.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(username));
        boolean existsInAdmins = admins.stream().anyMatch(a -> a.getUsername().equalsIgnoreCase(username));
        boolean existsInCashiers = cashiers.stream().anyMatch(c -> c.getUsername().equalsIgnoreCase(username));
        return existsInClients || existsInAdmins || existsInCashiers;
    }

    // Find owner client by account number (searches across all accounts)
    public static Client buscarPorCuenta(String numeroCuenta) {
        for (Client c : clients) {
            for (BankAccount a : c.getAccounts()) {
                if (a.getNumber().equals(numeroCuenta)) return c;
            }
        }
        return null;
    }

    // Find BankAccount by number
    public static BankAccount buscarCuenta(String numeroCuenta) {
        for (Client c : clients) {
            for (BankAccount a : c.getAccounts()) {
                if (a.getNumber().equals(numeroCuenta)) return a;
            }
        }
        return null;
    }

    // Find account owner by account number
    public static Client buscarPropietario(String numeroCuenta) {
        return buscarPorCuenta(numeroCuenta);
    }

    public static Client obtenerUsuario(String username) {
        return clients.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public static Admin obtenerAdmin(String username) {
        return admins.stream().filter(a -> a.getUsername().equals(username)).findFirst().orElse(null);
    }

    public static StaffCashier obtenerCashier(String username) {
        return cashiers.stream().filter(c -> c.getUsername().equals(username)).findFirst().orElse(null);
    }

    public static int getCantidadUsuarios() { return clients.size(); }

    // ---- Seeding ----
    public static void cargarUsuariosDePrueba() {
        // Ensure demo Admin and Cashier
        ensureAdmin(new Admin("admin", "admin"));
        ensureCashier(new StaffCashier("cashier", "cashier"));


        ensureClient(new Client("Cliente Demo","cliente", "cliente","20", "0001", 2500.0, AccountType.SAVINGS));
        ensureClient(new Client("JuanCa","Juan123", "password123","24", "0002", 5000.0, AccountType.CHECKING));
        ensureClient(new Client("Maria","Maria456", "password456","25", "0003", 3500.50, AccountType.BUSINESS));
        ensureClient(new Client("Pdro Pascal","Pedro789", "password789","26", "0004", 10000.0, AccountType.SAVINGS));
        ensureClient(new Client("Anita","Ana321", "password321","27", "0005", 7500.75, AccountType.CHECKING));
        ensureClient(new Client("Carlos David","Carlos654", "password654","28", "0006", 2000.0, AccountType.BUSINESS));


        Client c1 = obtenerUsuario("cliente");
        if (c1 != null && c1.getAccounts().size() < 2) c1.addAccount(AccountType.CHECKING, 300.0);
        Client c2 = obtenerUsuario("Juan123");
        if (c2 != null && c2.getAccounts().size() < 2) c2.addAccount(AccountType.SAVINGS, 150.0);
    }

    private static void ensureClient(Client candidate) {
        boolean exists = clients.stream().anyMatch(u -> u.getUsername().equalsIgnoreCase(candidate.getUsername())
                || u.getAccount().equals(candidate.getAccount()));
        if (!exists) {
            clients.add(candidate);
        }
    }
    private static void ensureAdmin(Admin admin) {
        boolean exists = admins.stream().anyMatch(a -> a.getUsername().equalsIgnoreCase(admin.getUsername()));
        if (!exists) admins.add(admin);
    }
    private static void ensureCashier(StaffCashier cashier) {
        boolean exists = cashiers.stream().anyMatch(c -> c.getUsername().equalsIgnoreCase(cashier.getUsername()));
        if (!exists) cashiers.add(cashier);
    }
}