package com.example.loginpf.Controllers;

import com.example.loginpf.Model.AccountType;
import com.example.loginpf.Model.Admin;
import com.example.loginpf.Model.Client;
import com.example.loginpf.Model.StaffCashier;
import com.example.loginpf.Repositories.UserRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminController implements Initializable {


    @FXML private TableView<Client> tblClients;
    @FXML private TableColumn<Client, String> colCAccount;
    @FXML private TableColumn<Client, String> colCUsername;
    @FXML private TableColumn<Client, String> colCName;
    @FXML private TableColumn<Client, String> colCId;
    @FXML private TableColumn<Client, Double> colCBalance;
    @FXML private TableColumn<Client, AccountType> colCType;
    @FXML private Button btnRemoveClient;
    @FXML private Button btnPromoteClient;

    @FXML private ComboBox<Client> cmbClients;
    @FXML private TextField txtCUsername;
    @FXML private PasswordField txtCPassword;
    @FXML private TextField txtCName;
    @FXML private TextField txtCId;
    @FXML private TextField txtCAccount;
    @FXML private TextField txtCBalance;
    @FXML private ComboBox<AccountType> cmbCType;


    @FXML private TableView<StaffCashier> tblCashiers;
    @FXML private TableColumn<StaffCashier, String> colCashierUser;
    @FXML private Button btnRemoveCashier;
    @FXML private Button btnPromoteCashier;

    @FXML private ComboBox<StaffCashier> cmbCashiers;
    @FXML private TextField txtCashierUser;
    @FXML private PasswordField txtCashierPassword;


    @FXML private TableView<Admin> tblAdmins;
    @FXML private TableColumn<Admin, String> colAdminUser;
    @FXML private Button btnRemoveAdmin;

    private ObservableList<Client> clients;
    private ObservableList<StaffCashier> cashiers;
    private ObservableList<Admin> admins;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UserRepository.cargarUsuariosDePrueba();


        colCAccount.setCellValueFactory(new PropertyValueFactory<>("account"));
        colCUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colCName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCBalance.setCellValueFactory(new PropertyValueFactory<>("cash"));
        colCType.setCellValueFactory(new PropertyValueFactory<>("accountType"));


        tblClients.setEditable(false);
        colCAccount.setEditable(false);
        colCUsername.setEditable(false);
        colCName.setEditable(false);
        colCId.setEditable(false);
        colCBalance.setEditable(false);
        colCType.setEditable(false);

        colCAccount.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });

        colCAccount.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setStyle("-fx-text-fill: black;"); // 🔹 fuerza texto visible
            }
        });

        colCUsername.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        colCName.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        colCId.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });
        colCBalance.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(item));
            }
        });

        colCType.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(AccountType item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : new AccountTypeStringConverter().toString(item));
            }
        });


        clients = UserRepository.obtenerTodosLosUsuarios();
        cashiers = UserRepository.obtenerTodosLosCashiers();
        admins = UserRepository.obtenerTodosLosAdmins();

        tblClients.setItems(clients);


        if (cmbClients != null) {
            cmbClients.setItems(clients);
            cmbClients.setConverter(new StringConverter<>() {
                @Override public String toString(Client object) { return object == null ? "" : object.getUsername(); }
                @Override public Client fromString(String string) { return clients.stream().filter(c -> c.getUsername().equalsIgnoreCase(string)).findFirst().orElse(null); }
            });
        }
        if (cmbCType != null) {
            cmbCType.setItems(FXCollections.observableArrayList(AccountType.values()));
            cmbCType.setConverter(new AccountTypeStringConverter());
        }

        colCashierUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblCashiers.setItems(cashiers);
        tblCashiers.setEditable(false);
        colCashierUser.setEditable(false);
        colCashierUser.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });


        if (cmbCashiers != null) {
            cmbCashiers.setItems(cashiers);
            cmbCashiers.setConverter(new StringConverter<>() {
                @Override public String toString(StaffCashier object) { return object == null ? "" : object.getUsername(); }
                @Override public StaffCashier fromString(String string) { return cashiers.stream().filter(c -> c.getUsername().equalsIgnoreCase(string)).findFirst().orElse(null); }
            });
        }


        colAdminUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblAdmins.setItems(admins);
        tblAdmins.setEditable(false);
        colAdminUser.setEditable(false);
        colAdminUser.setCellFactory(col -> new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
            }
        });


        if (btnRemoveClient != null) btnRemoveClient.disableProperty().bind(tblClients.getSelectionModel().selectedItemProperty().isNull());
        if (btnPromoteClient != null) btnPromoteClient.disableProperty().bind(tblClients.getSelectionModel().selectedItemProperty().isNull());
        if (btnRemoveCashier != null) btnRemoveCashier.disableProperty().bind(tblCashiers.getSelectionModel().selectedItemProperty().isNull());
        if (btnPromoteCashier != null) btnPromoteCashier.disableProperty().bind(tblCashiers.getSelectionModel().selectedItemProperty().isNull());
        if (btnRemoveAdmin != null) btnRemoveAdmin.disableProperty().bind(tblAdmins.getSelectionModel().selectedItemProperty().isNull());
    }

    private String nonNull(String v) { return v == null ? "" : v; }
    private Double safeDouble(Double v) { return v == null ? 0.0 : v; }

    public static class AccountTypeStringConverter extends StringConverter<AccountType> {
        @Override public String toString(AccountType object) {
            return object == null ? "" : object.display();
        }
        @Override public AccountType fromString(String string) {
            return AccountType.fromDisplay(string);
        }
    }

    @FXML
    private void onRefresh() {
        tblClients.refresh();
        tblCashiers.refresh();
        tblAdmins.refresh();
    }

    @FXML
    private void onRemoveClient() {
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        if (sel == null && cmbClients != null) sel = cmbClients.getValue();
        if (sel == null) return;
        if (!confirm("¿Eliminar cliente '" + sel.getUsername() + "'?")) return;
        boolean ok = UserRepository.eliminarCliente(sel.getUsername());
        if (ok) {
            info("Cliente eliminado");
            refreshClientsViews();
        } else warn("No se pudo eliminar");
    }

    @FXML
    private void onPromoteClientToAdmin() {
        Client sel = tblClients.getSelectionModel().getSelectedItem();
        if (sel == null && cmbClients != null) sel = cmbClients.getValue();
        if (sel == null) return;
        if (!confirm("¿Convertir a Admin al cliente '" + sel.getUsername() + "'? (conservará su rol de cliente)")) return;
        boolean ok = UserRepository.promoverClienteAAdmin(sel.getUsername());
        if (ok) info("Cliente promovido a Admin"); else warn("No se pudo promover (ya es admin o error)");
        tblAdmins.refresh();
    }

    @FXML
    private void onRemoveCashier() {
        StaffCashier sel = tblCashiers.getSelectionModel().getSelectedItem();
        if (sel == null && cmbCashiers != null) sel = cmbCashiers.getValue();
        if (sel == null) return;
        if (!confirm("¿Eliminar cajero '" + sel.getUsername() + "'?")) return;
        boolean ok = UserRepository.eliminarCashier(sel.getUsername());
        if (ok) {
            info("Cajero eliminado");
            refreshCashiersViews();
        } else warn("No se pudo eliminar");
    }

    @FXML
    private void onPromoteCashierToAdmin() {
        StaffCashier sel = tblCashiers.getSelectionModel().getSelectedItem();
        if (sel == null && cmbCashiers != null) sel = cmbCashiers.getValue();
        if (sel == null) return;
        if (!confirm("¿Convertir a Admin al cajero '" + sel.getUsername() + "'? (conservará su rol de cajero)")) return;
        boolean ok = UserRepository.promoverCashierAAdmin(sel.getUsername());
        if (ok) info("Cajero promovido a Admin"); else warn("No se pudo promover (ya es admin o error)");
        tblAdmins.refresh();
    }

    @FXML
    private void onRemoveAdmin() {
        Admin sel = tblAdmins.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        if (!confirm("¿Eliminar admin '" + sel.getUsername() + "'?")) return;
        boolean ok = UserRepository.eliminarAdmin(sel.getUsername());
        if (ok) info("Admin eliminado"); else warn("No se pudo eliminar");
    }

    private boolean confirm(String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText(null);
        return a.showAndWait().filter(ButtonType.OK::equals).isPresent();
    }
    private void info(String msg) { Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }
    private void warn(String msg) { Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK); a.setHeaderText(null); a.showAndWait(); }


    private void refreshClientsViews() {
        tblClients.refresh();
        if (cmbClients != null) {
            cmbClients.setItems(null);
            cmbClients.setItems(clients);
        }
    }
    private void refreshCashiersViews() {
        tblCashiers.refresh();
        if (cmbCashiers != null) {
            cmbCashiers.setItems(null);
            cmbCashiers.setItems(cashiers);
        }
    }


    @FXML
    private void onLoadClientFromDropdown() {
        Client c = cmbClients == null ? null : cmbClients.getValue();
        if (c == null) return;
        if (txtCUsername != null) txtCUsername.setText(nonNull(c.getUsername()));
        if (txtCPassword != null) txtCPassword.setText(nonNull(c.getPassword()));
        if (txtCName != null) txtCName.setText(nonNull(c.getName()));
        if (txtCId != null) txtCId.setText(nonNull(c.getId()));
        if (txtCAccount != null) txtCAccount.setText(nonNull(c.getAccount()));
        if (txtCBalance != null) txtCBalance.setText(String.valueOf(safeDouble(c.getCash())));
        if (cmbCType != null) cmbCType.setValue(c.getAccountType());
    }

    @FXML
    private void onAddClient() {
        if (txtCUsername == null) return;
        String username = nonNull(txtCUsername.getText()).trim();
        String password = txtCPassword == null ? "" : nonNull(txtCPassword.getText());
        String name = txtCName == null ? "" : nonNull(txtCName.getText());
        String id = txtCId == null ? "" : nonNull(txtCId.getText());
        String account = txtCAccount == null ? null : nonNull(txtCAccount.getText());
        double balance;
        try { balance = txtCBalance == null ? 0.0 : Double.parseDouble(nonNull(txtCBalance.getText())); }
        catch (NumberFormatException e) { warn("Saldo inválido"); return; }
        AccountType type = cmbCType == null ? AccountType.SAVINGS : (cmbCType.getValue() == null ? AccountType.SAVINGS : cmbCType.getValue());

        if (username.isEmpty()) { warn("Usuario es requerido"); return; }
        if (password.isEmpty()) { warn("Contraseña es requerida"); return; }

        Client nuevo = new Client(name, username, password, id, account, balance, type);
        boolean ok = UserRepository.agregarClienteSiUnico(nuevo);
        if (ok) { info("Cliente agregado"); refreshClientsViews(); clearClientForm(); }
        else { warn("No se pudo agregar: usuario o cuenta ya existe"); }
    }

    @FXML
    private void onUpdateClient() {
        String originalUsername = null;
        Client selected = null;
        if (cmbClients != null) selected = cmbClients.getValue();
        if (selected == null && tblClients != null) selected = tblClients.getSelectionModel().getSelectedItem();
        if (selected == null) { warn("Seleccione un cliente (combo o tabla)"); return; }
        originalUsername = selected.getUsername();

        String username = txtCUsername == null ? originalUsername : nonNull(txtCUsername.getText()).trim();
        String password = txtCPassword == null ? selected.getPassword() : nonNull(txtCPassword.getText());
        String name = txtCName == null ? selected.getName() : nonNull(txtCName.getText());
        String id = txtCId == null ? selected.getId() : nonNull(txtCId.getText());
        String account = txtCAccount == null ? selected.getAccount() : nonNull(txtCAccount.getText());
        double balance;
        try { balance = txtCBalance == null ? safeDouble(selected.getCash()) : Double.parseDouble(nonNull(txtCBalance.getText())); }
        catch (NumberFormatException e) { warn("Saldo inválido"); return; }
        AccountType type = cmbCType == null ? selected.getAccountType() : (cmbCType.getValue() == null ? selected.getAccountType() : cmbCType.getValue());

        Client updated = new Client(name, username, password, id, account, balance, type);
        boolean ok = UserRepository.actualizarCliente(originalUsername, updated);
        if (ok) { info("Cliente actualizado"); refreshClientsViews(); clearClientForm(); }
        else { warn("No se pudo actualizar (usuario duplicado o no encontrado)"); }
    }

    private void clearClientForm() {
        if (txtCUsername != null) txtCUsername.clear();
        if (txtCPassword != null) txtCPassword.clear();
        if (txtCName != null) txtCName.clear();
        if (txtCId != null) txtCId.clear();
        if (txtCAccount != null) txtCAccount.clear();
        if (txtCBalance != null) txtCBalance.clear();
        if (cmbCType != null) cmbCType.setValue(null);
        if (cmbClients != null) cmbClients.setValue(null);
    }

    @FXML
    private void onLoadCashierFromDropdown() {
        StaffCashier c = cmbCashiers == null ? null : cmbCashiers.getValue();
        if (c == null) return;
        if (txtCashierUser != null) txtCashierUser.setText(nonNull(c.getUsername()));
        if (txtCashierPassword != null) txtCashierPassword.setText(nonNull(c.getPassword()));
    }

    @FXML
    private void onAddCashier() {
        if (txtCashierUser == null) return;
        String username = nonNull(txtCashierUser.getText()).trim();
        String password = txtCashierPassword == null ? "" : nonNull(txtCashierPassword.getText());
        if (username.isEmpty()) { warn("Usuario es requerido"); return; }
        if (password.isEmpty()) { warn("Contraseña es requerida"); return; }
        StaffCashier nuevo = new StaffCashier(username, password);
        boolean ok = UserRepository.agregarCashierSiUnico(nuevo);
        if (ok) { info("Cajero agregado"); refreshCashiersViews(); clearCashierForm(); }
        else { warn("No se pudo agregar: usuario ya existe"); }
    }

    @FXML
    private void onUpdateCashier() {
        StaffCashier selected = null;
        if (cmbCashiers != null) selected = cmbCashiers.getValue();
        if (selected == null && tblCashiers != null) selected = tblCashiers.getSelectionModel().getSelectedItem();
        if (selected == null) { warn("Seleccione un cajero (combo o tabla)"); return; }
        String originalUsername = selected.getUsername();
        String username = txtCashierUser == null ? originalUsername : nonNull(txtCashierUser.getText()).trim();
        String password = txtCashierPassword == null ? selected.getPassword() : nonNull(txtCashierPassword.getText());
        StaffCashier updated = new StaffCashier(username, password);
        boolean ok = UserRepository.actualizarCashier(originalUsername, updated);
        if (ok) { info("Cajero actualizado"); refreshCashiersViews(); clearCashierForm(); }
        else { warn("No se pudo actualizar (usuario duplicado o no encontrado)"); }
    }

    private void clearCashierForm() {
        if (txtCashierUser != null) txtCashierUser.clear();
        if (txtCashierPassword != null) txtCashierPassword.clear();
        if (cmbCashiers != null) cmbCashiers.setValue(null);
    }

    @FXML
    private void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/loginpf/LogIn.fxml"));
            AnchorPane root = loader.load();
            Stage stage = (Stage) tblClients.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);

            String css = com.example.loginpf.App.class.getResource("styles.css").toExternalForm();
            if (!scene.getStylesheets().contains(css)) {
                scene.getStylesheets().add(css);
            }
            stage.setScene(scene);
            stage.setTitle("SAD Bank - Login");
        } catch (IOException e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "No se pudo volver al login: " + e.getMessage(), ButtonType.OK);
            a.showAndWait();
        }
    }
}
