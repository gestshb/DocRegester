/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import static com.spring.client.HomeController.addDocumentStage;
import static com.spring.client.HomeController.alert;
import static com.spring.client.HomeController.loggedUser;
import static com.spring.client.HomeController.permission;
import com.spring.client.util.CostumStage;
import com.spring.config.ConfigBeans;
import com.spring.entity.Document;
import com.spring.entity.Procedure;
import com.spring.entity.Type;
import com.spring.entity.User;
import com.spring.service.interfaces.DocumentService;
import com.spring.service.interfaces.TypeService;
import com.spring.util.SpringUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author pc
 */
public class FindDocumentController implements Initializable {

    private Stage stage;
    private DocumentService docService;
    private TypeService typeServie;
    private Chronology chronology = IsoChronology.INSTANCE;

    @FXML
    CheckBox hidjri;
    
    
    

    @FXML
    TextField iddoc;
    @FXML
    TextField sender;
    @FXML
    TextField recipient;
    @FXML
    TextField registrationNumber;
    @FXML
    TextField orderNumber;
    @FXML
    TextField description;
    @FXML
    ComboBox<Type> types;
    @FXML
    DatePicker docDate1;
    @FXML
    DatePicker docDate2;
    @FXML
    TableView<Document> documentTable;
    @FXML
    TableColumn<Document, String> iddocCol;
    @FXML
    TableColumn<Document, String> senderCol;
    @FXML
    TableColumn<Document, String> recipientCol;
    @FXML
    TableColumn<Document, String> registrationNumberCol;
    @FXML
    TableColumn<Document, String> orderNumberCol;
    @FXML
    TableColumn<Document, String> descriptionCol;
    @FXML
    TableColumn<Document, String> typeCol;
    @FXML
    TableColumn<Document, String> docDateCol;
    @FXML
    TableColumn<Document, String> relatedDoc;
    @FXML
    TableColumn<Document, Boolean> lockedCol;
    @FXML
    TableColumn<Document, Boolean> treatedCol;

    @FXML
    TableView<Procedure> procedureTable;
    @FXML
    TableColumn<Procedure, String> idProcedure;
    @FXML
    TableColumn<Procedure, String> detail;
    @FXML
    TableColumn<Procedure, String> dateProcedure;

    private User user = HomeController.loggedUser;
    @FXML
    private ImageView header;
    private Document selectedDocument;

    class Serveur extends Thread {

        List<String> list;

        Serveur(List<String> list) {
            this.list = list;
        }

        @Override
        public void run() {
            //client=================================================
            String host = null;
            Properties pro = null;
            Path path = Paths.get(System.getProperty("user.home"), ".setting", "setting.properties");

            try {
                if (Files.exists(path)) {
                    BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
                    Properties props = new Properties();
                    props.load(reader);
                    host = props.getProperty("host2");
                }

            } catch (Exception e) {
                Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, e);
            }
            Socket socket = null;
            ObjectOutputStream out = null;

            try {
                int port = 2319;
                InetAddress address = InetAddress.getByName(host);
                socket = new Socket(address, port);
                out = new ObjectOutputStream(socket.getOutputStream());

                out.writeObject(list);

            } catch (UnknownHostException ex) {
                Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, ex);

            } catch (IOException ex) {
                Logger.getLogger(ConfigBeans.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                    socket.close();
                } catch (Exception e) {
                }
            }
        }
    };

    @FXML
    private void getDoc(ActionEvent a) {

        if (selectedDocument == null) {
            //ERROR Message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("تنبيه");
            alert.setContentText("يجب عليك اختيار ملف");
            alert.showAndWait();
            //=========================================================================
            return;
        };
        List<String> list = new ArrayList<>();
        StringBuilder st = new StringBuilder();
        st.append("يطلب منك الموظف :  ");
        st.append(HomeController.loggedUser.getFirstname());
        st.append(" \nأن تحضر الملف الذي رقمه ");
        st.append(selectedDocument.getIddoc());
        list.add(st.toString());
        Serveur thread = new Serveur(list);
        thread.setDaemon(true);
        thread.start();

    }
    
    @FXML
    private void returnDoc(ActionEvent a) {

        if (selectedDocument == null) {
            //ERROR Message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("تنبيه");
            alert.setContentText("يجب عليك اختيار ملف");
            alert.showAndWait();
            //=========================================================================
            return;
        };
        List<String> list = new ArrayList<>();
        StringBuilder st = new StringBuilder();
        st.append("يطلب منك الموظف :  ");
        st.append(HomeController.loggedUser.getFirstname());
        st.append(" \nأن ترد الملف الذي رقمه ");
        st.append(selectedDocument.getIddoc());
        list.add(st.toString());
        Serveur thread = new Serveur(list);
        thread.setDaemon(true);
        thread.start();

    }

    @FXML
    public void search(ActionEvent action) throws Exception {

        Map<String, Object> map = new HashMap<>();

        if (!this.iddoc.getText().equals("")) {
            map.put("idDoc", this.iddoc.getText());

        }

        if (!this.registrationNumber.getText().equals("")) {
            map.put("registrationNumber", this.registrationNumber.getText());

        }
        if (!this.orderNumber.getText().equals("")) {
            map.put("orderNumber", this.orderNumber.getText());

        }
        if (!this.description.getText().equals("")) {
            map.put("description", this.description.getText());

        }
        if (!this.sender.getText().equals("")) {
            map.put("sender", this.sender.getText());

        }
        if (!this.recipient.getText().equals("")) {
            map.put("recipient", this.recipient.getText());

        }
        if (this.docDate1.getValue() != null) {
            map.put("creationDate1", this.docDate1.getValue());

        }
        if (this.docDate2.getValue() != null) {
            map.put("creationDate2", this.docDate2.getValue());

        }

        if (types.getSelectionModel().getSelectedIndex() > -1) {
            map.put("type", types.getSelectionModel().getSelectedItem());

        }

        documentTable.getItems().clear();
        documentTable.getItems().addAll(docService.getBy(map));

    }

    @FXML
    public void clear(ActionEvent action) {
        onClose();
    }

    @FXML
    private void makeRendezvous() {
        if ((!permission.contains("addRendezvous")) && !loggedUser.isSysAdmin()) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        selectedDocument = documentTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            CostumStage addRend = HomeController.factory.build("addRendezvous");
            RendezvousController controller = (RendezvousController) addRend.getController();
            controller.setSelectedDoc(selectedDocument);
            addRend.setOnShown(new EventHandler<WindowEvent>() {

                @Override
                public void handle(WindowEvent event) {
                    controller.onShow();
                }
            });
            addRend.showAndWait();
        } else {

            //ERROR Message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("تنبيه");
            alert.setContentText("يجب عليك اختيار ملف");
            alert.showAndWait();
            //=========================================================================

        }

    }

    void onClose() {

        hidjri.setSelected(false);

        iddoc.clear();

        sender.clear();

        recipient.clear();
        registrationNumber.clear();
        orderNumber.clear();
        description.clear();

        types.getSelectionModel().clearSelection();

        docDate1.setValue(null);

        docDate2.setValue(null);
        documentTable.getItems().clear();

    }

    @Override

    public void initialize(URL location, ResourceBundle resources) {
        try {
            ApplicationContext context = SpringUtil.getApplicationContext();

            docService = (DocumentService) context.getBean("documentServiceImpl");
            typeServie = (TypeService) context.getBean("typeServiceImpl");
            types.getItems().clear();
            types.getItems().addAll(typeServie.getAll());

            documentTable.setOnMouseClicked((MouseEvent click) -> {
                if ((click.getClickCount() == 2) && (documentTable.getSelectionModel().getSelectedItem() != null)) {
                    selectedDocument = documentTable.getSelectionModel().getSelectedItem();

                    DocumentController docController = ((DocumentController) HomeController.addDocumentStage.getController());
                    docController.setSelectedDoc(selectedDocument);
                    docController.onEditionCase();
                    HomeController.addDocumentStage.show();
                    addDocumentStage.setOnHidden((WindowEvent eve) -> {
                        docController.clearDocument();
                        docController.clearProcedure();
                    });
                }
            });
            //init tables   ==============================================

            documentTable.getSelectionModel().selectedItemProperty().
                    addListener((ObservableValue<? extends Document> observable,
                                    Document oldValue, Document newValue) -> {
                        selectedDocument = newValue;
                        if (selectedDocument != null) {
                            procedureTable.getItems().clear();
                            procedureTable.getItems().addAll(selectedDocument.getProcedures());
                        }

                    });

            iddocCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getIddoc().toString());
            });
            senderCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getSender());
            });

            recipientCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getRecipient());
            });

            registrationNumberCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getRegistrationNumber());
            });

            orderNumberCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getOrderNumber());
            });

            descriptionCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getDescription());
            });

            docDateCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getCreationDate() == null ? "" : p.getCreationDate().toString());
            });
            typeCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getType() == null ? "" : p.getType().toString());
            });
            
             relatedDoc.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getDoc()== null ? "" : p.getDoc().getIddoc().toString());
            });

            lockedCol.setCellFactory(p -> new BooleanCell());
            lockedCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(p.getLocked());

            });
            treatedCol.setCellFactory(p -> new BooleanCell());
            treatedCol.setCellValueFactory(cellData -> {
                Document p = cellData.getValue();
                return new ReadOnlyObjectWrapper<>(p.getTreated());
            });

            idProcedure.setCellValueFactory(cellData -> {
                Procedure p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getIdProcedure().toString());
            });
            detail.setCellValueFactory(cellData -> {
                Procedure p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getDiscription());
            });
            dateProcedure.setCellValueFactory(cellData -> {
                Procedure p = cellData.getValue();
                return new ReadOnlyStringWrapper(p.getDateProcedure() == null ? "" : p.getDateProcedure().toString());
            });

        } catch (Exception ex) {
            Logger.getLogger(FindDocumentController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        //date Picker configuration================================================================start
        //hidjri switcher
        docDate1.setChronology(chronology);
        docDate2.setChronology(chronology);

        hidjri.selectedProperty().
                addListener((ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        chronology = HijrahChronology.INSTANCE;
                        docDate1.setChronology(chronology);
                        docDate2.setChronology(chronology);

                    } else {
                        chronology = IsoChronology.INSTANCE;
                        docDate1.setChronology(chronology);
                        docDate2.setChronology(chronology);

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

        docDate1.setConverter(new CostumeConverter());
        docDate2.setConverter(new CostumeConverter());
        //date Picker configuration================================================================start

    }

//    Callback<TableColumn<Document, Boolean>, TableCell<Document, Boolean>> booleanCellFactory = (TableColumn<Document, Boolean> p) -> new BooleanCell();
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;

    }

}

class BooleanCell extends TableCell<Document, Boolean> {

    private CheckBox checkBox;

    public BooleanCell() {
        checkBox = new CheckBox();
        checkBox.setDisable(true);

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isEditing()) {
                    commitEdit(newValue == null ? false : newValue);
                }
            }

        });
        this.setGraphic(checkBox);
        this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        this.setEditable(true);
    }

    @Override
    public void startEdit() {
        super.startEdit();
        if (isEmpty()) {
            return;
        }
        checkBox.setDisable(false);
        checkBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        checkBox.setDisable(true);
    }

    public void commitEdit(Boolean value) {
        super.commitEdit(value);
        checkBox.setDisable(true);
    }

    @Override
    public void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {
            checkBox.setSelected(item);
        }
    }

}
