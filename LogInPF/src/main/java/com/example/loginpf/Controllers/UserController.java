package com.example.loginpf.Controllers;

import com.example.loginpf.Model.BankAccount;
import com.example.loginpf.Model.Cashier;
import com.example.loginpf.Repositories.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    private com.example.loginpf.Model.Client loggedInClient;

    @FXML
    public AnchorPane paneTransferencia;

    @FXML
    private AnchorPane userPane;

    @FXML
    private Label lblMessage;

    @FXML
    private Label lblChoose;

    @FXML
    private ImageView imgDollar;

    @FXML
    private Button btnTransferencia;

    @FXML
    private ImageView imgBanner;

    @FXML
    private Button btnDepositar;

    @FXML
    private Label lblAccountNumber;

    @FXML
    private Label lblCash;

    @FXML
    private Label lblAccountType;

    @FXML
    private ComboBox<BankAccount> cmbAccount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (lblMessage != null) {
            lblMessage.setText("¡Bienvenido a SAD Bank!");
        }
        // cargar datos demo si es la primera vez
        UserRepository.cargarUsuariosDePrueba();
    }

    private BankAccount getSelectedAccount() {
        if (cmbAccount != null && cmbAccount.getSelectionModel() != null) {
            BankAccount sel = cmbAccount.getSelectionModel().getSelectedItem();
            if (sel != null) return sel;
        }
        return loggedInClient != null ? loggedInClient.getPrimaryAccount() : null;
    }

    @FXML
    private void onDepositar(ActionEvent event){
        if (loggedInClient == null) {
            showAlert("Error", "No hay un usuario en sesión", Alert.AlertType.ERROR);
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setHeaderText("Ingrese el monto a depositar");
        dialog.setContentText("Monto ($):");
        dialog.showAndWait().ifPresent(value -> {
            try {
                double amount = Double.parseDouble(value.trim());
                BankAccount target = getSelectedAccount();
                if (target == null) { showAlert("Error", "Seleccione una cuenta", Alert.AlertType.WARNING); return; }
                Cashier.Result r = Cashier.deposit(target, amount);
                if (r.ok) {
                    showAlert("Éxito", r.message, Alert.AlertType.INFORMATION);
                    updateAccountInfoLabels();
                } else {
                    showAlert("Error", r.message, Alert.AlertType.WARNING);
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "El monto debe ser un número válido", Alert.AlertType.ERROR);
            }
        });
    }

    @FXML
    private void onTransferencia(ActionEvent event) {
        try {
            URL fxmlLocation = getClass().getResource("/com/example/loginpf/Transferencia.fxml");

            if (fxmlLocation == null) {
                System.err.println("ERROR: No se encontró Transferencia.fxml");
                System.err.println("Ruta esperada: /com/example/loginpf/Transferencia.fxml");
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            AnchorPane vistaTransferencia = loader.load();
            TransactionController controller = loader.getController();
            if (controller != null) {
                if (loggedInClient != null) {
                    controller.setClienteActual(loggedInClient);
                }
                controller.setCuentaOrigen(getSelectedAccount());
                controller.setOnBack(this::restoreDefaultContent);
            }

            if (paneTransferencia == null) {
                System.err.println("ERROR: paneTransferencia es null");
                return;
            }

            paneTransferencia.getChildren().setAll(vistaTransferencia);
            System.out.println("Transferencia cargada exitosamente");

        } catch (IOException e) {
            System.err.println("ERROR al cargar Transferencia.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setUserInfo(String userName, String userType) {
        if (lblMessage != null) {
            lblMessage.setText("¡Bienvenido a SAD Bank " + userName + "!");
        }
        updateAccountInfoLabels();
    }

    public void setLoggedInClient(com.example.loginpf.Model.Client client) {
        this.loggedInClient = client;
        if (cmbAccount != null) {
            cmbAccount.getItems().setAll(client.getAccounts());
            if (client.getPrimaryAccount() != null) {
                cmbAccount.getSelectionModel().select(client.getPrimaryAccount());
            }
            cmbAccount.getSelectionModel().selectedItemProperty().addListener((o, old, sel) -> updateAccountInfoLabels());
        }
        updateAccountInfoLabels();
    }

    private void updateAccountInfoLabels() {
        if (loggedInClient != null) {
            BankAccount acc = getSelectedAccount();
            if (acc == null) acc = loggedInClient.getPrimaryAccount();
            if (acc != null) {
                if (lblAccountType != null) {
                    String type = acc.getType() == null ? "Savings" : acc.getType().display();
                    lblAccountType.setText("Tipo de cuenta: " + type);
                }
                if (lblAccountNumber != null) {
                    lblAccountNumber.setText("Numero de cuenta: " + acc.getNumber());
                }
                if (lblCash != null) {
                    lblCash.setText("Saldo: $" + String.format("%.2f", acc.getBalance()));
                }
            }
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
    private void onLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/LogIn.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) userPane.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 600));
            stage.setTitle("SAD Bank - Login");
        } catch (IOException e) {
            showAlert("Error", "No se pudo volver al login: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void restoreDefaultContent() {
        if (paneTransferencia != null) {
            paneTransferencia.getChildren().clear();
            // Create minimal default content showing account and balance
            Label account = new Label();
            Label cash = new Label();
            BankAccount acc = getSelectedAccount();
            if (acc == null && loggedInClient != null) acc = loggedInClient.getPrimaryAccount();
            if (acc != null) {
                account.setText("Numero de cuenta: " + acc.getNumber());
                cash.setText("Saldo: $" + String.format("%.2f", acc.getBalance()));
            } else {
                account.setText("Numero de cuenta: -");
                cash.setText("Saldo: -");
            }
            javafx.scene.layout.HBox h1 = new javafx.scene.layout.HBox(account);
            h1.setLayoutX(54);
            h1.setLayoutY(121);
            javafx.scene.layout.HBox h2 = new javafx.scene.layout.HBox(cash);
            h2.setLayoutX(54);
            h2.setLayoutY(220);
            paneTransferencia.getChildren().addAll(h1, h2);
        }
    }
}