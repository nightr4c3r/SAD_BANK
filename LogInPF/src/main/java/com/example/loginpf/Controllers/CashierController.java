package com.example.loginpf.Controllers;

import com.example.loginpf.Model.BankAccount;
import com.example.loginpf.Model.Cashier;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Repositories.UserRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class CashierController implements Initializable {

    @FXML private TableView<Client> tblClients;
    @FXML private TableColumn<Client, String> colAccount;
    @FXML private TableColumn<Client, String> colUsername;
    @FXML private TableColumn<Client, String> colId;

    @FXML private Label lblSelAccount;
    @FXML private Label lblSelUser;
    @FXML private Label lblSelBalance;

    @FXML private ComboBox<BankAccount> cmbAccounts;

    @FXML private TextField txtAmount;
    @FXML private TextField txtToAccount;
    @FXML private TextArea txtMessage;

    private ObservableList<Client> clients;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UserRepository.cargarUsuariosDePrueba();

        colAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));


        colAccount.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        colUsername.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        colId.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        clients = UserRepository.obtenerTodosLosUsuarios();
        tblClients.setItems(clients);

        // Update details on selection
        tblClients.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> updateDetails(sel));
        if (!clients.isEmpty()) {
            tblClients.getSelectionModel().selectFirst();
        }

        if (cmbAccounts != null) {
            cmbAccounts.getSelectionModel().selectedItemProperty().addListener((o, old, sel) -> refreshSelectedAccountInfo());
        }
    }

    private void updateDetails(Client sel) {
        if (sel == null) {
            lblSelAccount.setText("Cuenta: -");
            lblSelUser.setText("Usuario: -");
            lblSelBalance.setText("Saldo: -");
            if (cmbAccounts != null) cmbAccounts.getItems().setAll(Collections.emptyList());
            return;
        }
        lblSelUser.setText("Usuario: " + sel.getUsername());
        if (cmbAccounts != null) {
            cmbAccounts.getItems().setAll(sel.getAccounts());
            if (sel.getPrimaryAccount() != null) {
                cmbAccounts.getSelectionModel().select(sel.getPrimaryAccount());
            }
        }
        refreshSelectedAccountInfo();
    }

    private void refreshSelectedAccountInfo() {
        BankAccount acc = selectedAccount();
        if (acc == null) {
            lblSelAccount.setText("Cuenta: -");
            lblSelBalance.setText("Saldo: -");
        } else {
            lblSelAccount.setText("Cuenta: " + acc.getNumber());
            lblSelBalance.setText("Saldo: $" + String.format("%.2f", acc.getBalance()));
        }
    }

    private BankAccount selectedAccount() {
        BankAccount a = cmbAccounts != null ? cmbAccounts.getSelectionModel().getSelectedItem() : null;
        if (a != null) return a;
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        return sel == null ? null : sel.getPrimaryAccount();
    }

    @FXML
    private void onDeposit() {
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Seleccione un cliente", Alert.AlertType.WARNING); return; }
        Double amount = parseAmount(); if (amount == null) return;
        BankAccount acc = selectedAccount(); if (acc == null) { showAlert("Seleccione una cuenta", Alert.AlertType.WARNING); return; }
        Cashier.Result r = Cashier.deposit(acc, amount);
        handleResult(r);
    }

    @FXML
    private void onWithdraw() {
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Seleccione un cliente", Alert.AlertType.WARNING); return; }
        Double amount = parseAmount(); if (amount == null) return;
        BankAccount acc = selectedAccount(); if (acc == null) { showAlert("Seleccione una cuenta", Alert.AlertType.WARNING); return; }
        Cashier.Result r = Cashier.withdraw(acc, amount);
        handleResult(r);
    }

    @FXML
    private void onTransfer() {
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        if (sel == null) { showAlert("Seleccione un cliente", Alert.AlertType.WARNING); return; }
        Double amount = parseAmount(); if (amount == null) return;
        String toAcc = txtToAccount.getText() == null ? "" : txtToAccount.getText().trim();
        String msg = txtMessage.getText() == null ? "" : txtMessage.getText().trim();
        BankAccount acc = selectedAccount(); if (acc == null) { showAlert("Seleccione una cuenta", Alert.AlertType.WARNING); return; }
        Cashier.Result r = Cashier.transfer(acc, toAcc, amount, msg);
        handleResult(r);
    }

    private Double parseAmount() {
        String text = txtAmount.getText();
        if (text == null || text.trim().isEmpty()) {
            showAlert("Ingrese un monto válido", Alert.AlertType.WARNING);
            return null;
        }
        try {
            return Double.parseDouble(text.trim());
        } catch (NumberFormatException e) {
            showAlert("El monto debe ser numérico", Alert.AlertType.ERROR);
            return null;
        }
    }

    private void handleResult(Cashier.Result r) {
        if (r.ok) {
            showInfo(r.message);
            tblClients.refresh();
            refreshSelectedAccountInfo();
        } else {
            showAlert(r.message, Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/LogIn.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) tblClients.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            stage.setScene(scene);
            stage.setTitle("SAD Bank - Login");
        } catch (IOException e) {
            showAlert("No se pudo volver al login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String message, Alert.AlertType type) {
        Alert a = new Alert(type);
        a.setHeaderText(null);
        a.setTitle(type == Alert.AlertType.ERROR ? "Error" : "Aviso");
        a.setContentText(message);
        a.showAndWait();
    }

    private void showInfo(String message) {
        showAlert(message, Alert.AlertType.INFORMATION);
    }
}
