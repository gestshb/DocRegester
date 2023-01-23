/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import com.mgrecol.jasper.jasperviewerfx.JRViewerMain;
import static com.spring.client.HomeController.alert;
import static com.spring.client.HomeController.loggedUser;
import com.spring.entity.Document;
import com.spring.entity.Rendezvous;
import com.spring.service.interfaces.RendezvousService;
import com.spring.util.ConverterDate;
import com.spring.util.SpringUtil;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.context.ApplicationContext;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class RendezvousController implements Initializable {

    @FXML
    private TextField idRendezvous;

    @FXML
    private TextField idDoc;
    @FXML
    private TextField sender;
    @FXML
    private TextField recipient;
    @FXML
    private TextField orderNumber;
    @FXML
    private DatePicker rendezvousDate;
    @FXML
    private CheckBox hidjri;

    @FXML
    private Label msg;
    private Chronology chronology = IsoChronology.INSTANCE;

    private Document selectedDoc;
    private Rendezvous selectedRendezvous;

    private RendezvousService rendezvousService;
    @FXML
    private CheckBox preview;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        rendezvousService = (RendezvousService) context.getBean("rendezvousServiceImpl");

        rendezvousDate.setValue(LocalDate.now());
        //date Picker configuration================================================================start
        //hidjri switcher
        rendezvousDate.setChronology(chronology);

        hidjri.selectedProperty().
                addListener((ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        chronology = HijrahChronology.INSTANCE;
                        rendezvousDate.setChronology(chronology);

                    } else {
                        chronology = IsoChronology.INSTANCE;
                        rendezvousDate.setChronology(chronology);

                    }
                });

        class CostumeConverter extends StringConverter<LocalDate> {

            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

            @Override
            public String toString(LocalDate value) {

                if (value instanceof LocalDate) {
                    ChronoLocalDate cDate;
                    try {
                        cDate = chronology.date(value);
                    } catch (DateTimeException ex) {

                        chronology = IsoChronology.INSTANCE;
                        cDate = (LocalDate) value;
                    }
                    return formatter.format(cDate);
                }
                return "";
            }

            @Override
            public LocalDate fromString(String text) {
                if (text == null || text.isEmpty()) {
                    return null;
                }
                text = text.trim();

                TemporalAccessor temporal = formatter.parse(text);
                return LocalDate.from(chronology.date(temporal));

            }
        };

        rendezvousDate.setConverter(new CostumeConverter());

        //date Picker configuration================================================================end
    }

    @FXML
    private void print(ActionEvent event) throws Exception {
        if (selectedRendezvous == null) {
            save(event);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("idRendezvous", selectedRendezvous.getIdRendezvous().toString());
        map.put("idDoc", selectedDoc.getIddoc().toString());
        map.put("orderNumber", selectedDoc.getOrderNumber());
        map.put("sender", selectedDoc.getSender());
        map.put("date", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(HijrahChronology.INSTANCE.date(rendezvousDate.getValue())));
        map.put("logo", getClass().getResource("/com/spring/client/Images/logo2.png").toExternalForm());
        map.put(JRParameter.REPORT_LOCALE, new Locale("ar"));
        new JRViewerMain().start(map, "/com/spring/client/fxml/rendezvousMini.jasper", preview.isSelected());
    }

    @FXML
    private void remove(ActionEvent event) {
        rendezvousService.delete(selectedRendezvous);
        clear();

    }

    @FXML
    private void save(ActionEvent event) {

        if (selectedRendezvous == null) {
            //================================================التأكد ان عدد المواعيد لا يتجاوز العشرين
            Map<String, Object> map = new HashMap<>();
            map.put("creationDate2", new Date());
            List list = rendezvousService.getBy(map);

            if (list.size() > 20 && !loggedUser.isSysAdmin()) {
                alert("تنبيه", "وصلت إلى الحد المسموح به من المواعيد");
                return;
            }
            //================================================
            selectedRendezvous = new Rendezvous();
        }
        selectedRendezvous.setDate(ConverterDate.getDate(rendezvousDate.getValue()));
        selectedRendezvous.setDocument(selectedDoc);
        try {
            rendezvousService.createOrUpdate(selectedRendezvous);
            idRendezvous.setText(selectedRendezvous.getIdRendezvous().toString());
            msg.setText("تم الحفظ بنجاح");
        } catch (Exception ex) {
            Logger.getLogger(RendezvousController.class.getName()).log(Level.SEVERE, null, ex);
            msg.setText("خطأ في الحفظ");
        }

    }

    @FXML
    public void clear() {
        idDoc.clear();
        sender.clear();
        recipient.clear();
        orderNumber.clear();
        idRendezvous.clear();
        rendezvousDate.setValue(LocalDate.now());
        idRendezvous.clear();
        selectedRendezvous = null;
        selectedDoc = null;
    }

    public void onShow() {

        idDoc.setText(selectedDoc.getIddoc().toString());
        sender.setText(selectedDoc.getSender());
        recipient.setText(selectedDoc.getRecipient());
        orderNumber.setText(selectedDoc.getOrderNumber());
        Date date = null;
        if (selectedRendezvous != null) {
            date = selectedRendezvous.getDate();
            idRendezvous.setText(selectedRendezvous.getIdRendezvous().toString());
        } else {
            date = new Date();
        }
        rendezvousDate.setValue(ConverterDate.getLocalDate(date));

    }

    public Document getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(Document selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

    public Rendezvous getSelectedRendezvous() {
        return selectedRendezvous;
    }

    public void setSelectedRendezvous(Rendezvous selectedRendezvous) {
        this.selectedRendezvous = selectedRendezvous;
    }

}
