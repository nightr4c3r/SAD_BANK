package com.example.loginpf.Controllers;

import com.example.loginpf.Model.Admin;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import com.example.loginpf.App;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {

    @FXML
    public PasswordField txtPassword;


    @FXML
    private AnchorPane createPane;

    @FXML
    private AnchorPane pane;

    @FXML
    private AnchorPane anchorContainer;

    @FXML
    private Label lblUser;

    @FXML
    private Label lblPassword;

    @FXML
    private Label lblUserName;

    @FXML
    private ComboBox<Object> comboUser;

    @FXML
    private TextField txtUserName;

    @FXML
    private AnchorPane anchorView;

    @FXML
    private Hyperlink hypCreate;

    @FXML
    private Button btnLogIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Object> users = FXCollections.observableArrayList();
        users.add("Client");
        users.add("Admin");
        users.add("Cashier");
        comboUser.setItems(users);
        comboUser.getSelectionModel().selectFirst();
        UserRepository.cargarUsuariosDePrueba();

    }

    private boolean validateCredentials(String username, String password) {
        return UserRepository.validarCredenciales(username, password);
    }

    @FXML
    private void onLogIn(ActionEvent event) {
        try {

            if (txtUserName == null || txtUserName.getText().trim().isEmpty()) {
                showAlert("Campo Vacío", "Por favor ingrese un nombre de usuario", Alert.AlertType.WARNING);
                return;
            }

            if (txtPassword == null || txtPassword.getText().trim().isEmpty()) {
                showAlert("Campo Vacío", "Por favor ingrese una contraseña", Alert.AlertType.WARNING);
                return;
            }

            if (comboUser == null || comboUser.getValue() == null) {
                showAlert("Selección Requerida", "Por favor seleccione un tipo de usuario", Alert.AlertType.WARNING);
                return;
            }

            String userName = txtUserName.getText().trim();
            String password = txtPassword.getText().trim();

            Object selectedRole = comboUser.getValue();
            String role = selectedRole != null ? selectedRole.toString() : "";

            boolean ok;
            if ("Admin".equalsIgnoreCase(role)) {
                ok = UserRepository.validarAdmin(userName, password);
            } else if ("Cashier".equalsIgnoreCase(role) || "Cajero".equalsIgnoreCase(role)) {
                ok = UserRepository.validarCashier(userName, password);
            } else {
                ok = validateCredentials(userName, password);
            }

            if (!ok) {
                showAlert("Credenciales Incorrectas",
                        "Usuario o contraseña incorrectos.\n\n" +
                                "Usuarios de prueba:\n" +
                                "Admin: admin/admin\n" +
                                "Cliente: cliente/cliente\n\n" +
                                "O use una cuenta creada previamente.",
                        Alert.AlertType.ERROR);
                return;
            }

            // selected role already captured above
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            String userType = selectedRole != null ? selectedRole.toString() : "";
            if ("Cashier".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/CashierView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 800, 600);
                // Apply global stylesheet for consistent readable UI
                String css = com.example.loginpf.App.class.getResource("styles.css").toExternalForm();
                if (!scene.getStylesheets().contains(css)) {
                    scene.getStylesheets().add(css);
                }
                stage.setScene(scene);
                stage.setTitle("SAD Bank - Caja");
                return;
            } else if ("Admin".equalsIgnoreCase(userType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/AdminView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root, 900, 600);
                // Apply global stylesheet for consistent readable UI
                String css = com.example.loginpf.App.class.getResource("styles.css").toExternalForm();
                if (!scene.getStylesheets().contains(css)) {
                    scene.getStylesheets().add(css);
                }
                stage.setScene(scene);
                stage.setTitle("SAD Bank - Admin");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/User.fxml"));
            Parent root = loader.load();

            UserController userController = loader.getController();

            // If role is Client, pass the full Client object; otherwise, just set welcome text
            Client loggedClient = UserRepository.obtenerUsuario(userName);
            if (loggedClient != null) {
                userController.setLoggedInClient(loggedClient);
            }
            userController.setUserInfo(userName, userType);

            Scene scene = new Scene(root, 600, 600);
            // Apply global stylesheet for consistent readable UI
            String css = com.example.loginpf.App.class.getResource("styles.css").toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
            stage.setScene(scene);
            stage.setTitle("SAD Bank - Área de Usuario");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar la vista: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void hoverOn() {
        if (btnLogIn != null) {
            btnLogIn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
        }
    }

    @FXML
    private void hoverOff() {
        if (btnLogIn != null) {
            btnLogIn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-cursor: hand;");
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
    private void onCreateAccount(ActionEvent event) throws IOException {
        AnchorPane createAccount = FXMLLoader.load(getClass().getResource("/com/example/loginpf/CreateAccount.fxml"));
        pane.getChildren().setAll(createAccount);
    }
}