/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import com.spring.entity.Action;
import com.spring.entity.User;
import com.spring.service.interfaces.ActionService;
import com.spring.service.interfaces.UserService;
import com.spring.util.SpringUtil;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class UsersController implements Initializable {

    //service
    private UserService userService;
    private ActionService actionService;
    private User selectedUser;
    private Set<Action> permissions = new HashSet<>();
    //ControlUI
    @FXML
    Label errorMessage;
    @FXML
    ImageView imageuser;

    @FXML
    TextField idUser;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextField password2;
    @FXML
    TextField firstname;
    @FXML
    TextField lastname;
    @FXML
    TextField tel;
    @FXML
    TextField addres;
    @FXML
    TextField email;
    @FXML
    TextField creationDate;
    @FXML
    CheckBox isActive;
    @FXML
    CheckBox isSysadmin;

    @FXML
    TableView<User> usersTable;
    @FXML
    TableColumn<User, String> usernameColumn;
    @FXML
    TableColumn<User, String> firstNameColumn;
    @FXML
    TableColumn<User, String> lastNameColumn;
    @FXML
    ListView<Action> userListView;
    @FXML
    ListView<Action> permissionListView;

    private Path imageUrl;

    @FXML
    public void browsImage(ActionEvent event) throws FileNotFoundException, IOException {

        FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        imageUrl = fileChooser.showOpenDialog(stage).toPath();
        InputStream in = Files.newInputStream(imageUrl);
        if (in != null) {
            imageuser.setImage(new Image(in));
        }
        in.close();

    }

    @FXML
    public void saveButton(ActionEvent event) {

        if (!firstname.getText().equals("")) {
            if (!username.getText().equals("")) {
                if ( //                    (!password.getText().equals("")) && 
                        (password2.getText().equals(password.getText()))) {

                    if (selectedUser == null) {
                        selectedUser = new User();
                    }

                    selectedUser.setUsername(username.getText());
                    selectedUser.setPassword(password.getText());
                    selectedUser.setFirstname(firstname.getText());
                    selectedUser.setLastname(lastname.getText());

                    selectedUser.setCreationDate(new Date());
                    selectedUser.setActive(isActive.isSelected());
                    selectedUser.setSysAdmin(isSysadmin.isSelected());

                    selectedUser.setAddres(addres.getText());
                    selectedUser.setEmail(email.getText());
                    selectedUser.setTelephone(tel.getText());

                    try {
                        if (imageUrl != null) {
                            selectedUser.setPhoto(Files.readAllBytes(imageUrl));
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Set<Action> actions = new HashSet<>();
                    actions.addAll(userListView.getItems());
//                        userListView.getItems().sort(new UserComparator());
                    selectedUser.setActions(actions);

                    try {
                        //save ...
                        userService.createOrUpdate(selectedUser);

                    } catch (Exception ex) {
                        //ERROR Message
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("خطأ");
                        alert.setContentText("حدث خطا عند أداء هذه العملية  \n يرجى مراجعة ملفات الأخطاء لمعرفة التفاصيل");
                        alert.showAndWait();
                        //=========================================================================
                        Logger.getLogger(UsersController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    clearField();

                } else {
                    errorMessage.setText("خطأ في إدخال كلمة السر");
                }
            } else {
                errorMessage.setText("يجب إدخال اسم المستخدم");
            }
        } else {
            errorMessage.setText("يجب إدخال اسم المستخدم الأول ");
        }

    }

    private void clearField() {
        idUser.setText("");
        username.setText("");
        password.setText("");
        password2.setText("");
        firstname.setText("");
        lastname.setText("");
        creationDate.setText("");
        isActive.setSelected(false);
        addres.setText("");
        email.setText("");
        tel.setText("");
        isActive.setSelected(false);
        isSysadmin.setSelected(false);

        userListView.getItems().clear();
        permissionListView.getItems().clear();
        permissionListView.getItems().addAll(permissions);
        permissionListView.getItems().sort(new UserComparator());
        usersTable.getItems().clear();
        usersTable.getItems().addAll(userService.getAll());
        isActive.setDisable(false);

        isSysadmin.setDisable(false);

        permissionListView.setDisable(false);
        userListView.setDisable(false);
        imageuser.setImage(new Image("/com/spring/client/Images/default.gif"));

        usersTable.getItems().clear();
        usersTable.getItems().addAll(userService.getAll());
        errorMessage.setText(null);
        selectedUser = null;

    }

    ;

    @FXML
    private void refreshButton(ActionEvent action) {
        clearField();
        permissions.clear();
        permissions.addAll(actionService.getAll());
        permissionListView.getItems().clear();
        permissionListView.getItems().addAll(permissions);
        permissionListView.getItems().sort(new UserComparator());

    }

    @FXML
    private void deleteButton(ActionEvent action) {
        if (selectedUser != null && !selectedUser.getUsername().equals("sa")) {
            if (!idUser.getText().equals("")) {

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("تأكيد");
                alert.setHeaderText("تأكيد الحذف");
                alert.setContentText("هل أنت متأكد من حذف هذا المستخدم ؟");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    userService.delete(selectedUser);
                    usersTable.getItems().clear();
                    usersTable.getItems().addAll(userService.getAll());
                    clearField();
                } else {

                }

            } else {
                errorMessage.setText("يجب اختيار المستخدم المراد حذفه بالضغط عليه مرتين");
            }
        }
    }

    @FXML
    public void addPermissionButton(ActionEvent action) {
        if (selectedUser != null) {
            Action selectedAction = permissionListView.getSelectionModel().getSelectedItem();
            if (selectedAction != null) {
                userListView.getItems().add(selectedAction);
                permissionListView.getItems().remove(selectedAction);
            }
        } else {
            errorMessage.setText("يجب اختيار المستخدم بالضغط عليه مرتين");
        }
    }

    @FXML
    public void removePermissionButton(ActionEvent action) {
        if (selectedUser != null) {
            Action selectedAction = userListView.getSelectionModel().getSelectedItem();
            if (selectedAction != null) {
                permissionListView.getItems().add(selectedAction);
                permissionListView.getItems().sort(new UserComparator());
                userListView.getItems().remove(selectedAction);
            } else {
                errorMessage.setText("يجب اختيار المستخدم بالضغط عليه مرتين");
            }
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Set the column resize policy to constrained resize policy
        usersTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        //usersTable.autosize();

        //Load context==========================================================
        ApplicationContext context = SpringUtil.getApplicationContext();

        userService = (UserService) context.getBean("userServiceImpl");
        actionService = (ActionService) context.getBean("actionServiceImpl");

        //creation Tabel========================================================
        usersTable.getItems().clear();
        usersTable.getItems().addAll(userService.getAll());
        // Create columns
        usernameColumn.setCellValueFactory(cellData -> {
            User p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getUsername());
        });

        firstNameColumn.setCellValueFactory(cellData -> {
            User p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getFirstname());
        });

        lastNameColumn.setCellValueFactory(cellData -> {
            User p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getLastname());
        });

        usersTable.setOnMouseClicked((MouseEvent click) -> {
            if ((click.getClickCount() == 2) && (usersTable.getSelectionModel().getSelectedItem() != null)) {
                selectedUser = usersTable.getSelectionModel().getSelectedItem();
                idUser.setText(selectedUser.getIdUser().toString());
                username.setText(selectedUser.getUsername());
                password.setText("");
                password2.setText("");
                firstname.setText(selectedUser.getFirstname());
                lastname.setText(selectedUser.getLastname());

                creationDate.setText(new SimpleDateFormat("yyyy-MM-dd ").format(selectedUser.getCreationDate()));
                isActive.setSelected(selectedUser.isActive());
                isSysadmin.setSelected(selectedUser.isSysAdmin());

                addres.setText(selectedUser.getAddres());
                email.setText(selectedUser.getEmail());
                tel.setText(selectedUser.getTelephone());
                permissionListView.getItems().clear();
                permissionListView.getItems().addAll(permissions);
                permissionListView.getItems().sort(new UserComparator());
                userListView.getItems().clear();
                userListView.getItems().addAll(selectedUser.getActions());
                userListView.getItems().sort(new UserComparator());
                permissionListView.getItems().removeAll(userListView.getItems());
                try {
                    imageuser.setImage(new Image(new ByteArrayInputStream(selectedUser.getPhoto())));
                } catch (Exception e) {
                    imageuser.setImage(new Image("/com/spring/client/Images/default.gif"));
                }
                if (selectedUser.getUsername().equals("sa")) {
                    isActive.setDisable(true);

                    isSysadmin.setDisable(true);

                    permissionListView.setDisable(true);
                    userListView.setDisable(true);
                } else {
                    isActive.setDisable(false);

                    isSysadmin.setDisable(false);

                    permissionListView.setDisable(false);
                    userListView.setDisable(false);
                }

            }
        });

        isSysadmin.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    permissionListView.setDisable(true);
                    userListView.setDisable(true);

                } else {
                    permissionListView.setDisable(false);
                    userListView.setDisable(false);
                }
            }
        });

        permissions.addAll(actionService.getAll());
        permissionListView.getItems().addAll(permissions);
        permissionListView.getItems().sort(new UserComparator());

    }

}

class ActionCell extends ListCell<Action> {

    @Override
    public void updateItem(Action item, boolean empty) {

        super.updateItem(item, empty);

        if (empty) {
            setText(null);

        } else {
            setText(item.getAction());

        }
    }
}

class UserComparator implements Comparator<Action> {

    @Override
    public int compare(Action o1, Action o2) {
        return o1.getIdAction().compareTo(o2.getIdAction());
    }
}
