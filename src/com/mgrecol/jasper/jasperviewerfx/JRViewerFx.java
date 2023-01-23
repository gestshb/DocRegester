package com.mgrecol.jasper.jasperviewerfx;

import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperPrint;

/**
 *
 */
/**
 * @author Michael Grecol
 * @project JasperViewerFx
 * @filename JRViewerFx.java
 * @date Mar 23, 2015
 */
public class JRViewerFx extends Application {

    private JasperPrint jasperPrint;
    private JRViewerFxMode printMode;

    @Override
    public void start(Stage primaryStage) throws Exception {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getResourceAsStream("/com/spring/client/fxml/FRViewerFx.fxml");
            FXMLLoader loader = new FXMLLoader();
            Parent page = (Parent) loader.load(fxmlStream);
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Jasper Viewer");

            primaryStage.show();
            JRViewerFxController o = (JRViewerFxController) loader.getController();

            if (o instanceof JRViewerFxController) {
                JRViewerFxController jrViewerFxController = (JRViewerFxController) o;
                jrViewerFxController.setJasperPrint(jasperPrint);
                jrViewerFxController.show();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JRViewerFx(JasperPrint jasperPrint, JRViewerFxMode printMode, Stage primaryStage) {
        this.jasperPrint = jasperPrint;
        this.printMode = printMode;
        try {
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
