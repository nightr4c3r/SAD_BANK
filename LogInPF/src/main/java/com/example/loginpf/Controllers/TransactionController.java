package com.example.loginpf.Controllers;

import com.example.loginpf.Model.BankAccount;
import com.example.loginpf.Model.Cashier;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Repositories.UserRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    private Runnable onBackAction;

    public void setOnBack(Runnable onBackAction) {
        this.onBackAction = onBackAction;
    }

    @FXML
    private TableView<Client> tblAccounts;

    @FXML
    private TableColumn<Client, String> colAccount;

    @FXML
    private TableColumn<Client, String> colName;

    @FXML
    private TableColumn<Client, String> colId;

    @FXML
    private TextField txtCuenta;

    @FXML
    private TextField txtMonto;

    @FXML
    private TextArea txtMensaje;

    @FXML
    private Button btnTransferir;

    @FXML
    private Button btnInfo;

    @FXML
    private AnchorPane layout;

    private Client clienteActual;
    private BankAccount cuentaOrigen;

    private ObservableList<Client> clientList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
        colName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        // Force black text in table cells for readability
        colAccount.setCellFactory(col -> {
            TableCell<Client, String> cell = new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setStyle("-fx-text-fill: black;");
            return cell;
        });
        colName.setCellFactory(col -> {
            TableCell<Client, String> cell = new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setStyle("-fx-text-fill: black;");
            return cell;
        });
        colId.setCellFactory(col -> {
            TableCell<Client, String> cell = new TableCell<>() {
                @Override protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };
            cell.setStyle("-fx-text-fill: black;");
            return cell;
        });

        clientList = UserRepository.obtenerTodosLosUsuarios();
        tblAccounts.setItems(clientList);
    }

    public void setClienteActual(Client cliente) {
        this.clienteActual = cliente;
        System.out.println("✓ Cliente recibido en TransactionController: " + cliente.getUsername());
        System.out.println("  ID: " + cliente.getId());
        System.out.println("  Cuenta primaria: " + cliente.getAccount());
        System.out.println("  Saldo primario: $" + cliente.getCash());
    }

    public void setCuentaOrigen(BankAccount cuenta) {
        this.cuentaOrigen = cuenta;
    }

    @FXML
    private void onInfo() {
        UserRepository.cargarUsuariosDePrueba();
        cargarDatos();
    }

    private void cargarDatos() {
        tblAccounts.setItems(UserRepository.obtenerTodosLosUsuarios());
        System.out.println("✓ Datos cargados en la tabla: " + tblAccounts.getItems().size() + " usuarios");
    }

    @FXML
    private void onTransferir(ActionEvent event) {
        if (clienteActual == null) {
            showAlert("Error", "No hay un usuario en sesión", Alert.AlertType.ERROR);
            System.err.println("✗ Error: clienteActual es null");
            return;
        }

        BankAccount from = cuentaOrigen != null ? cuentaOrigen : clienteActual.getPrimaryAccount();
        if (from == null) {
            showAlert("Error", "Seleccione una cuenta de origen", Alert.AlertType.WARNING);
            return;
        }

        String cuentaDestino = txtCuenta.getText().trim();
        String montoStr = txtMonto.getText().trim();
        String mensaje = txtMensaje.getText().trim();

        if (cuentaDestino.isEmpty() || montoStr.isEmpty()) {
            showAlert("Error", "Debe completar la cuenta y el monto", Alert.AlertType.WARNING);
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr);
            Cashier.Result r = Cashier.transfer(from, cuentaDestino, monto, mensaje);
            if (r.ok) {
                showAlert("¡Éxito!", r.message, Alert.AlertType.INFORMATION);
                txtCuenta.clear();
                txtMonto.clear();
                txtMensaje.clear();
                if (tblAccounts.getItems() != null && !tblAccounts.getItems().isEmpty()) {
                    tblAccounts.refresh();
                }
            } else {
                showAlert("Error", r.message, Alert.AlertType.WARNING);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "El monto debe ser un número válido", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void onBack() {
        if (onBackAction != null) {
            onBackAction.run();
        }
    }
}