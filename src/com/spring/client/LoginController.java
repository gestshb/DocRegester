package com.spring.client;

import com.spring.entity.Action;
import com.spring.entity.User;
import com.spring.security.AuthenticatorInterface;
import com.spring.service.interfaces.ActionService;
import com.spring.service.interfaces.UserService;
import com.spring.util.SpringUtil;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

/**
 * Login Controller.
 */
public class LoginController implements Initializable {

    @FXML
    TextField user;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    Button connect;
    @FXML
    Label errorMessage;
    @FXML
    TextField host;

    @FXML
    ProgressIndicator indicator;

    private Stage stage;
    private Main application;
    private ApplicationContext context;
    private UserService userService;
    private ActionService actionService;
    private AuthenticatorInterface auth;

    public void setApp(Main application) {
        this.application = application;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMessage.setText("");
        login.setVisible(false);
        connect.setVisible(false);
        indicator.setVisible(true);
        connect.setDisable(true);
        login.setDisable(true);
        errorMessage.setTextFill(Paint.valueOf("green"));
        errorMessage.setText("جاري الاتصال بالسيرفر...");

        Task worker = createWorker();
        worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent eve) {
                indicator.setVisible(false);
                connect.setVisible(false);
                login.setVisible(true);

                login.setDisable(false);
                errorMessage.setTextFill(Paint.valueOf("green"));
                errorMessage.setText("تم الاتصال بنجاح");

            }
        });
        worker.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                indicator.setVisible(false);
                login.setVisible(false);
                connect.setVisible(true);
                errorMessage.setText("لا يتمكن البرنامج  من الاتصال");
                errorMessage.setTextFill(Paint.valueOf("red"));

                connect.setDisable(false);
                login.setDisable(true);

            }
        });

        Thread thread = new Thread(worker);
        thread.setDaemon(true);
        thread.start();

    }

    public void reconnect(ActionEvent a) {

        errorMessage.setText("");
        login.setVisible(false);
        connect.setVisible(false);
        indicator.setVisible(true);
        errorMessage.setTextFill(Paint.valueOf("green"));
        errorMessage.setText("جاري الاتصال بالسيرفر...");
        connect.setDisable(true);
        login.setDisable(true);

        Task worker = createWorker();
        worker.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            public void handle(WorkerStateEvent eve) {
                indicator.setVisible(false);
                connect.setVisible(false);
                login.setVisible(true);

                login.setDisable(false);
                errorMessage.setTextFill(Paint.valueOf("green"));
                errorMessage.setText("تم الاتصال بنجاح");

            }
        });
        worker.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent event) {
                indicator.setVisible(false);
                login.setVisible(false);
                connect.setVisible(true);
                errorMessage.setText("لا يتمكن البرنامج  من الاتصال");
                errorMessage.setTextFill(Paint.valueOf("red"));
                connect.setVisible(true);
                connect.setDisable(false);
                login.setDisable(true);

            }
        });

        Thread thread = new Thread(worker);

        thread.start();
        password.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    if (!password.getText().equals("") && login.isVisible()) {
                        HomeController.loggedUser = userService.getByName(user.getText());
                        {
                            if (HomeController.loggedUser != null) {
                                if (HomeController.loggedUser.isSysAdmin() || HomeController.loggedUser.isActive()) {
                                    if (userLogging(user.getText(), password.getText())) {

                                    } else {
                                        errorMessage.setText(" كلمة السر غير صحيحة");
                                    }
                                } else {
                                    errorMessage.setText("المستخدم غير مفعل");
                                }
                            } else {
                                errorMessage.setText("المستخدم غير موجود");
                            }

                        }
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    public void processLogin(ActionEvent event) throws Exception {
        HomeController.loggedUser = userService.getByName(user.getText());
        {
            if (HomeController.loggedUser != null) {
                if (HomeController.loggedUser.isSysAdmin() || HomeController.loggedUser.isActive()) {
                    if (userLogging(user.getText(), password.getText())) {

                    } else {
                        errorMessage.setText(" كلمة السر غير صحيحة");
                    }
                } else {
                    errorMessage.setText("المستخدم غير مفعل");
                }
            } else {
                errorMessage.setText("المستخدم غير موجود");
            }

        }
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @return the auth
     */
    public AuthenticatorInterface getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(AuthenticatorInterface auth) {
        this.auth = auth;
    }

    public boolean userLogging(String user, String password) throws Exception {

        if (getAuth().validate(user, password)) {

            application.gotoHome();

            return true;
        } else {
            return false;
        }
    }

    private Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {

                context = SpringUtil.getApplicationContext();

                userService = (UserService) context.getBean("userServiceImpl");
                actionService = (ActionService) context.getBean("actionServiceImpl");

                setAuth((AuthenticatorInterface) context.getBean("authenticator"));

                //initialisation of Actions...
                try {

                    if (actionService.getAll().isEmpty()) {
                        actionService.createOrUpdate(new Action("addRendezvous", "إضافة موعد "));
                        actionService.createOrUpdate(new Action("editRendezvous", "تعديل  موعد "));
                        actionService.createOrUpdate(new Action("removeDocument", "حذف الملفات"));
                        actionService.createOrUpdate(new Action("findDoc", "البحث في الملفات"));

                        actionService.createOrUpdate(new Action("addDocument", "إضافة ملف"));
                        actionService.createOrUpdate(new Action("editDocument", "التعديل على الملفات"));

                    }
                    //=================================================================================================
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                try {
                    User user = new User();
                    user.setUsername("sa");
                    user.setPassword("sa");
                    user.setFirstname("مدير النظام");
                    user.setLastname("system admin");
                    user.setSysAdmin(true);
                    user.setCreationDate(new Date());

                    userService.createOrUpdate(user);
                } catch (Exception e) {

                }

                return true;

            }

        };
    }
}
