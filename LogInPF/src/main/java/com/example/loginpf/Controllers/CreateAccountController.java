package com.example.loginpf.Controllers;

import com.example.loginpf.Model.AccountType;
import com.example.loginpf.Model.Admin;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Model.StaffCashier;
import com.example.loginpf.Repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {

    @FXML private TextField txtCreateUsername;
    @FXML private AnchorPane createPane;
    @FXML private TextField txtCreatePassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnCreateUser;

    @FXML private ComboBox<String> cmbRole;
    @FXML private ComboBox<String> cmbAccountType;
    @FXML private Label lblAccountType;

    private final ObservableList<Client> newUsers = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        cmbRole.setItems(FXCollections.observableArrayList("Client", "Admin", "Cashier"));
        cmbRole.getSelectionModel().selectFirst();


        cmbAccountType.setItems(FXCollections.observableArrayList("Savings", "Checking", "Business"));
        cmbAccountType.getSelectionModel().select("Savings");


        cmbRole.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> toggleAccountType(sel));
        toggleAccountType(cmbRole.getSelectionModel().getSelectedItem());


        UserRepository.cargarUsuariosDePrueba();
    }

    private void toggleAccountType(String role) {
        boolean client = role != null && role.equalsIgnoreCase("Client");
        cmbAccountType.setDisable(!client);
        cmbAccountType.setVisible(client);
        if (lblAccountType != null) lblAccountType.setVisible(client);
    }

    @FXML
    private void onCreateUser(ActionEvent event) {
        try {
            String username = txtCreateUsername.getText().trim();
            String password = txtCreatePassword.getText().trim();
            String confirmPassword = txtConfirmPassword.getText().trim();
            String role = cmbRole.getValue() == null ? "" : cmbRole.getValue();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()) {
                showAlert("Error", "Todos los campos son obligatorios", Alert.AlertType.WARNING);
                return;
            }

            if (!password.equals(confirmPassword)) {
                showAlert("Error", "Las contraseñas no coinciden", Alert.AlertType.WARNING);
                return;
            }

            if (password.length() < 6) {
                showAlert("Error", "La contraseña debe tener al menos 6 caracteres", Alert.AlertType.WARNING);
                return;
            }

            if (UserRepository.existeUsuario(username)) {
                showAlert("Error", "El usuario '" + username + "' ya existe", Alert.AlertType.WARNING);
                return;
            }

            if (role.equalsIgnoreCase("Client")) {
                Client newClient = new Client(username, password);
                String typeStr = cmbAccountType.getValue();
                AccountType type = AccountType.fromDisplay(typeStr);
                newClient.setAccountType(type);
                UserRepository.agregarCliente(newClient);

                showAlert("¡Éxito!",
                        "Cliente creado correctamente.\n" +
                                "Usuario: " + username + "\n\n" +
                                "Tipo de cuenta: " + type.display() + "\n" +
                                "Numero de cuenta: " + newClient.getAccount() + "\n\n" +
                                "Saldo inicial $: " + newClient.getCash() + "\n\n" +
                                "Ahora puede iniciar sesión.",
                        Alert.AlertType.INFORMATION);
            } else if (role.equalsIgnoreCase("Admin")) {
                Admin admin = new Admin(username, password);
                UserRepository.agregarAdmin(admin);
                showAlert("¡Éxito!", "Admin creado correctamente. Ahora puede iniciar sesión.", Alert.AlertType.INFORMATION);
            } else if (role.equalsIgnoreCase("Cashier")) {
                StaffCashier cashier = new StaffCashier(username, password);
                UserRepository.agregarCashier(cashier);
                showAlert("¡Éxito!", "Cashier creado correctamente. Ahora puede iniciar sesión.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Rol no reconocido", Alert.AlertType.ERROR);
                return;
            }

            // Clear fields and go back
            txtCreateUsername.clear();
            txtCreatePassword.clear();
            txtConfirmPassword.clear();
            cmbRole.getSelectionModel().selectFirst();
            cmbAccountType.getSelectionModel().select("Savings");

            volverALogin(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Ocurrió un error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void volverALogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/LogIn.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle("SAD Bank - Login");
    }

    public ObservableList<Client> getNewUsers() { return newUsers; }
}