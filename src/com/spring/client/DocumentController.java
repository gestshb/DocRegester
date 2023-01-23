/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import com.mgrecol.jasper.jasperviewerfx.JRViewerMain;
import static com.spring.client.HomeController.alert;
import static com.spring.client.HomeController.loggedUser;
import static com.spring.client.HomeController.permission;
import com.spring.entity.Document;
import com.spring.entity.Procedure;
import com.spring.entity.Type;
import com.spring.entity.User;
import com.spring.service.interfaces.DocumentService;
import com.spring.service.interfaces.ProcedureService;
import com.spring.service.interfaces.TypeService;
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
import java.util.ArrayList;
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
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.context.ApplicationContext;

/**
 * @author ahmed
 */
public class DocumentController implements Initializable {

    private DocumentService docService;

    private TypeService typeServie;

    private ProcedureService procedureService;

    private Chronology chronology = IsoChronology.INSTANCE;

    private Document selectedDoc;

    private Stage stage;

    User user = HomeController.loggedUser;

    @FXML
    Label errorMessage;

    //=============================
    @FXML
    TextField iddoc;

    @FXML
    TextField idDocRelated;
    @FXML
    TextField description;
    @FXML
    TextField registrationNumber;
    @FXML
    TextField orderNumber;
    @FXML
    TextField sender;
    @FXML
    TextField recipient;
    @FXML
    DatePicker docDate;
    @FXML
    DatePicker creationDateProcedure;
    @FXML
    CheckBox isLocked;
    @FXML
    CheckBox isTreated;
    @FXML
    ComboBox<Type> types;
    @FXML
    TextField idProcedure;
    @FXML
    TextArea detail;

    @FXML
    ListView<Procedure> procedures;

    @FXML
    CheckBox hidjri1;
    @FXML
    CheckBox preview;
    private Procedure selectedProcedure;

    @FXML
    public void print(ActionEvent a) throws Exception {

        Map<String, Object> map = new HashMap<>();
        map.put("iddoc", selectedDoc.getIddoc());
        map.put("orderNumber", selectedDoc.getOrderNumber());
        map.put("registrationNumber", selectedDoc.getRegistrationNumber());
        map.put("sender", selectedDoc.getSender());
        map.put("recipient", selectedDoc.getRecipient());
        map.put("logo", getClass().getResource("/com/spring/client/Images/logo2.png").toExternalForm());
        map.put(JRParameter.REPORT_LOCALE, new Locale("ar"));
        int index = types.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            map.put("type", types.getSelectionModel().getSelectedItem().toString());
        }

        new JRViewerMain().start(map, "/com/spring/client/fxml/doc.jasper", preview.isSelected());
    }

    @FXML
    public void addOrUpdateOperation(ActionEvent action) {

        if (registrationNumber.getText().equals("")
                || orderNumber.getText().equals("")
                || sender.getText().equals("")
                || recipient.getText().equals("")) {
            errorMessage.setText("يجب ملء الحقول الضرورية ...رقم القيد-رقم الطلب -طالب التنفيذ -المنفذ ضده");
            return;
        }
        if (selectedDoc == null) {
            if ((!permission.contains("addDocument")) && !loggedUser.isSysAdmin()) {
                alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
                return;
            }
            selectedDoc = new Document();
        } else if ((!permission.contains("editDocument")) && !loggedUser.isSysAdmin()) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        selectedDoc.setOrderNumber(orderNumber.getText());
        selectedDoc.setRegistrationNumber(registrationNumber.getText());
        selectedDoc.setSender(sender.getText());
        selectedDoc.setRecipient(recipient.getText());

        if (docDate.getValue() != null) {
            selectedDoc.setCreationDate(ConverterDate.getDate(docDate.getValue()));
        }
        selectedDoc.setLocked(isLocked.isSelected());
        selectedDoc.setTreated(isTreated.isSelected());
        selectedDoc.setDescription(description.getText());
        int index = types.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            selectedDoc.setType(types.getSelectionModel().getSelectedItem());
        }
        if (!idDocRelated.getText().equals("")) {
            try {
                Document d = docService.get(Long.parseLong(idDocRelated.getText()));
                if (d == null) {
                    throw new RuntimeException();
                } else {
                    selectedDoc.setDoc(d);
                }
            } catch (Exception e) {
                //ERROR Message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("خطأ");
                alert.setContentText("رقم الملف ذي العلاقة خطأ أو غير موجود");
                alert.showAndWait();

            }
        }

        try {
            docService.createOrUpdate(selectedDoc);
            iddoc.setText(selectedDoc.getIddoc().toString());
            alert("تنبيه", "تمت عملية الحفظ بنجاح");
        } catch (Exception ex) {
            Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void getPrimaryData(ActionEvent a) {
        getPrimaryDataProxy();
    }

    public void getPrimaryDataProxy() {
        types.getItems().clear();
        types.getItems().addAll(typeServie.getAll());
    }

    @FXML
    public void removeDocument() throws Exception {
        if ((!permission.contains("removeDocument")) && !loggedUser.isSysAdmin()) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }

        if (selectedDoc != null) {
            docService.delete(selectedDoc);

            clearDocument();

        }
    }

    @FXML
    public void clearDocument() {
        iddoc.clear();
        sender.clear();
        recipient.clear();
        docDate.setValue(LocalDate.now());
        description.clear();
        isTreated.setSelected(false);
        isLocked.setSelected(false);
        orderNumber.clear();
        registrationNumber.clear();
        types.getSelectionModel().select(-1);
        procedures.getItems().clear();
        idProcedure.clear();
        detail.clear();
        idDocRelated.clear();
        creationDateProcedure.setValue(LocalDate.now());
        selectedProcedure = null;
        selectedDoc = null;
        errorMessage.setText("");

    }

    @FXML
    public void addOrUpdateProcedure() {
        if (selectedDoc != null) {
            if (!detail.getText().equals("")) {
                if (selectedProcedure == null) {
                    selectedProcedure = new Procedure();
                }
                selectedProcedure.setDateProcedure(ConverterDate.getDate(creationDateProcedure.getValue()));
                selectedProcedure.setDiscription(detail.getText());
                selectedProcedure.setDocument(selectedDoc);
                List<Procedure> tmpProcedure = selectedDoc.getProcedures();
                if (tmpProcedure == null) {
                    tmpProcedure = new ArrayList<>();
                }
                tmpProcedure.add(selectedProcedure);
                selectedDoc.setProcedures(tmpProcedure);
                try {
                    docService.createOrUpdate(selectedDoc);

                    selectedProcedure.setDocument(selectedDoc);
                    procedureService.createOrUpdate(selectedProcedure);
                    procedures.getItems().clear();
                    procedures.getItems().addAll(selectedDoc.getProcedures());
                    clearProcedure();

                } catch (Exception ex) {
                    Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {

            //ERROR Message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("خطأ");
            alert.setContentText("يجب حفظ السجل أولا قبل إضافة الإجراء");
            alert.showAndWait();
            //=========================================================================
        }

    }

    @FXML
    public void clearProcedure() {
        idProcedure.clear();
        detail.clear();
        creationDateProcedure.setValue(LocalDate.now());
        selectedProcedure = null;
    }

    @FXML
    public void removeProcedure() {
        if (selectedProcedure != null) {
            procedureService.delete(selectedProcedure);
            selectedDoc.getProcedures().remove(selectedProcedure);
            try {
                docService.createOrUpdate(selectedDoc);
            } catch (Exception ex) {
                Logger.getLogger(DocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }

            procedures.getItems().clear();
            procedures.getItems().addAll(selectedDoc.getProcedures());
            clearProcedure();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources
    ) {
        ApplicationContext context = SpringUtil.getApplicationContext();
        docService = (DocumentService) context.getBean("documentServiceImpl");
        typeServie = (TypeService) context.getBean("typeServiceImpl");
        procedureService = (ProcedureService) context.getBean("procedureServiceImpl");
        getPrimaryDataProxy();
        docDate.setValue(LocalDate.now());
        creationDateProcedure.setValue(LocalDate.now());
        procedures.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends Procedure> observable, Procedure oldValue, Procedure newValue) -> {
                    selectedProcedure = newValue;
                    if (selectedProcedure != null) {
                        idProcedure.setText(selectedProcedure.getIdProcedure().toString());
                        creationDateProcedure.setValue(ConverterDate.getLocalDate(selectedProcedure.getDateProcedure()));
                        detail.setText(selectedProcedure.getDiscription());
                    }
                });
        //date Picker configuration================================================================start
        //hidjri switcher
        docDate.setChronology(chronology);
        creationDateProcedure.setChronology(chronology);
        hidjri1.selectedProperty().
                addListener((ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        chronology = HijrahChronology.INSTANCE;
                        docDate.setChronology(chronology);
                        creationDateProcedure.setChronology(chronology);
                    } else {
                        chronology = IsoChronology.INSTANCE;
                        docDate.setChronology(chronology);
                        creationDateProcedure.setChronology(chronology);
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

        docDate.setConverter(new CostumeConverter());
        creationDateProcedure.setConverter(new CostumeConverter());
        //date Picker configuration================================================================end

    }

    public void onEditionCase() {

        if (selectedDoc != null) {

            iddoc.setText(selectedDoc.getIddoc().toString());
            sender.setText(selectedDoc.getSender());
            recipient.setText(selectedDoc.getRecipient());
            docDate.setValue(ConverterDate.getLocalDate(selectedDoc.getCreationDate()));
            description.setText(selectedDoc.getDescription());
            isTreated.setSelected(selectedDoc.getLocked());
            isLocked.setSelected(selectedDoc.getTreated());
            orderNumber.setText(selectedDoc.getOrderNumber());
            registrationNumber.setText(selectedDoc.getRegistrationNumber());
            types.getSelectionModel().select(selectedDoc.getType());
            try {
              idDocRelated.setText(selectedDoc.getDoc().getIddoc().toString());  
            } catch (Exception e) {
            }
            
            procedures.getItems().clear();
            procedures.getItems().addAll(selectedDoc.getProcedures());

        }

    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Document getSelectedDoc() {
        return selectedDoc;
    }

    public void setSelectedDoc(Document selectedDoc) {
        this.selectedDoc = selectedDoc;
    }

}
