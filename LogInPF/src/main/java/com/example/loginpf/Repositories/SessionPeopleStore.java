package com.example.loginpf.Repositories;

import com.example.loginpf.Model.Admin;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Model.StaffCashier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public final class SessionPeopleStore {

    private static final ObservableList<Client> clients = FXCollections.observableArrayList();
    private static final ObservableList<StaffCashier> cashiers = FXCollections.observableArrayList();
    private static final ObservableList<Admin> admins = FXCollections.observableArrayList();

    private SessionPeopleStore() { }


    public static synchronized void syncFromUserRepository() {
        clients.setAll(UserRepository.obtenerTodosLosUsuarios());
        cashiers.setAll(UserRepository.obtenerTodosLosCashiers());
        admins.setAll(UserRepository.obtenerTodosLosAdmins());
    }


    public static synchronized void clear() {
        clients.clear();
        cashiers.clear();
        admins.clear();
    }

    public static ObservableList<Client> getClients() { return clients; }
    public static ObservableList<StaffCashier> getCashiers() { return cashiers; }
    public static ObservableList<Admin> getAdmins() { return admins; }
}
