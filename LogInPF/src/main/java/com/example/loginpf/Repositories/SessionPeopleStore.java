package com.example.loginpf.Repositories;

import com.example.loginpf.Model.Admin;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Model.StaffCashier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Temporary in-memory store for people visible during the current app session.
 *
 * This store mirrors data from {@link UserRepository} for read-only presentation
 * needs (e.g., Admin/Cashier tables) without changing the source of truth.
 * Use {@link #syncFromUserRepository()} to refresh its content from
 * {@link UserRepository} at any time.
 *
 * Notes:
 * - This data is NOT persisted.
 * - Mutations should continue to be performed via {@link UserRepository}.
 * - After a mutation, call {@link #syncFromUserRepository()} and update UI lists.
 */
public final class SessionPeopleStore {

    private static final ObservableList<Client> clients = FXCollections.observableArrayList();
    private static final ObservableList<StaffCashier> cashiers = FXCollections.observableArrayList();
    private static final ObservableList<Admin> admins = FXCollections.observableArrayList();

    private SessionPeopleStore() { }

    /** Replace the contents of this store with the current data in UserRepository. */
    public static synchronized void syncFromUserRepository() {
        clients.setAll(UserRepository.obtenerTodosLosUsuarios());
        cashiers.setAll(UserRepository.obtenerTodosLosCashiers());
        admins.setAll(UserRepository.obtenerTodosLosAdmins());
    }

    /** Clears all lists in this session store (does not affect UserRepository). */
    public static synchronized void clear() {
        clients.clear();
        cashiers.clear();
        admins.clear();
    }

    public static ObservableList<Client> getClients() { return clients; }
    public static ObservableList<StaffCashier> getCashiers() { return cashiers; }
    public static ObservableList<Admin> getAdmins() { return admins; }
}
