/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Service;

/**
 *
 * @author ahmed
 */
@Service
public class StageFactoryImpl implements StageFactory {

    @Override
    public CostumStage build(String stage) {

        switch (stage) {
            case "users":
                return createSatge("/com/spring/client/fxml/Users.fxml", "إدارة المستخدمين");
            case "adddoc":
                return createSatge("/com/spring/client/fxml/Document.fxml", "إضافة ملف");
            case "finddoc":
                return createSatge("/com/spring/client/fxml/FindDocument.fxml", "البحث في الملفات");

            case "types":
                return createSatge("/com/spring/client/fxml/Type.fxml", "إدارة الأنواع");
            case "findRend":
                return createSatge("/com/spring/client/fxml/FindRendezvous.fxml", "البحث في المواعيد");
            case "addRendezvous":
                return createSatge("/com/spring/client/fxml/Rendezvous.fxml", "البحث في المواعيد");

            case "alert":
                return createSatge("/com/spring/client/fxml/AlertDialog.fxml", "تنبيه");

        }
        return null;
    }

    private CostumStage createSatge(String fxmlFile, String stageTitlle) {

        try {
            FXMLLoader loader = new FXMLLoader();
            VBox page;
            try (InputStream in = getClass().getResourceAsStream(fxmlFile)) {
                loader.setBuilderFactory(new JavaFXBuilderFactory());
                loader.setLocation(getClass().getResource(fxmlFile));
                page = (VBox) loader.load(in);
            }
            Scene scene = new Scene(page);
            CostumStage localStage = new CostumStage();
            Initializable controller = loader.getController();
            localStage.setController(controller);
            localStage.setTitle(stageTitlle);
            localStage.setScene(scene);
            return localStage;

        } catch (IOException ex) {
            Logger.getLogger(StageFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
