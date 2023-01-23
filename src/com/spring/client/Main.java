package com.spring.client;

import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {

//    private User loggedUser;
    private Stage stage;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(Main.class, (java.lang.String[]) null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            Locale myLocale = new Locale("ar");
            Locale.setDefault(myLocale);

            stage = primaryStage;
            stage.setTitle("دخول");

            gotoLogin();
            primaryStage.show();

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public User getLoggedUser() {
//        return loggedUser;
//    }
//
//    void userLogout() {
//        loggedUser = null;
//        gotoLogin();
//    }

    void gotoHome() {
        try {
            HomeController homeController = (HomeController) replaceSceneContent("fxml/Home.fxml", 1300, 700);
            stage.setTitle("الصفحة الرئيسية");
            //set to centre position
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setOnHidden(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                  System.exit(0);
                }
            });
            
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
//            homeController.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gotoLogin() {
        try {
            LoginController login = (LoginController) replaceSceneContent("fxml/Login.fxml", 500, 402);
          login.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml, int w, int h) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = getClass().getResourceAsStream(fxml);
        //InputStream in = Main.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, w, h);
        stage.setScene(scene);
        stage.sizeToScene();

        return (Initializable) loader.getController();
    }

}
