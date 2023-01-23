/**
 *
 */
package com.mgrecol.jasper.jasperviewerfx;

import com.spring.util.SpringUtil;
import java.io.InputStream;
import java.util.Map;
import javafx.stage.Stage;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import org.springframework.context.ApplicationContext;

/**
 * @author Michael Grecol
 * @project JasperViewerFx
 * @filename JRViewerMain.java
 * @date Mar 23, 2015
 */
public class JRViewerMain {

    public void start(Map par, String report, boolean showReport) throws Exception {
        ApplicationContext context = SpringUtil.getApplicationContext();
        DataSource dataSource = (DataSource) context.getBean("dataSource");

        Stage primaryStage = new Stage();
        JasperPrint jasperPrint = null;
        try {
            InputStream in = getClass().getResourceAsStream(report);

            jasperPrint = JasperFillManager.fillReport(in, par, dataSource.getConnection());
            
            if (!showReport) {
                JasperPrintManager.printReport(jasperPrint, true);
            } else {
                JRViewerFx viewer = new JRViewerFx(jasperPrint, JRViewerFxMode.REPORT_VIEW, primaryStage);
                viewer.start(primaryStage);
            }

        } catch (JRException e) {

            e.printStackTrace();
        };

    }
}
