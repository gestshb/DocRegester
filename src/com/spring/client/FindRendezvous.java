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
import com.spring.client.util.CostumStage;
import com.spring.entity.Rendezvous;
import com.spring.entity.Type;
import com.spring.service.interfaces.RendezvousService;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import net.sf.jasperreports.engine.JRParameter;
import org.springframework.context.ApplicationContext;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class FindRendezvous implements Initializable {

    @FXML
    private CheckBox preview;
    @FXML
    private ImageView header;
    @FXML
    private TextField iddoc;
    @FXML
    private TextField sender;
    @FXML
    private ComboBox<Type> types;
    @FXML
    private DatePicker rendDate1;
    @FXML
    private DatePicker rendDate2;
    @FXML
    private CheckBox hidjri;
    @FXML
    private TextField registrationNumber;
    @FXML
    private TextField recipient;
    @FXML
    private TextField orderNumber;
    @FXML
    private TextField idRend;
    @FXML
    private TableView<Rendezvous> rendezvousTable;
    @FXML
    private TableColumn<Rendezvous, String> idRendCol;
    @FXML
    private TableColumn<Rendezvous, String> iddocCol;
    @FXML
    private TableColumn<Rendezvous, String> senderCol;
    @FXML
    private TableColumn<Rendezvous, String> recipientCol;
    @FXML
    private TableColumn<Rendezvous, String> registrationNumberCol;
    @FXML
    private TableColumn<Rendezvous, String> orderNumberCol;
    @FXML
    private TableColumn<Rendezvous, String> docDateCol;
    private RendezvousService rendezvousService;
    private TypeService typeServie;
    private Rendezvous selectedRendezvous;
    private Chronology chronology = IsoChronology.INSTANCE;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ApplicationContext context = SpringUtil.getApplicationContext();

        rendezvousService = (RendezvousService) context.getBean("rendezvousServiceImpl");
        typeServie = (TypeService) context.getBean("typeServiceImpl");

        types.getItems().clear();
        types.getItems().addAll(typeServie.getAll());
        //init tables   ==============================================
        rendezvousTable.setOnMouseClicked((MouseEvent click) -> {
            if ((click.getClickCount() == 2) && (rendezvousTable.getSelectionModel().getSelectedItem() != null)) {
                if ((!permission.contains("editRendezvous")) && !loggedUser.isSysAdmin()) {
                    alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
                    return;
                }

                selectedRendezvous = rendezvousTable.getSelectionModel().getSelectedItem();
                CostumStage addRend = HomeController.factory.build("addRendezvous");
                RendezvousController rendController = ((RendezvousController) addRend.getController());
                rendController.setSelectedRendezvous(selectedRendezvous);
                rendController.setSelectedDoc(selectedRendezvous.getDocument());
                rendController.onShow();
                addRend.show();
                addRend.setOnHidden((WindowEvent eve) -> {
                    rendController.clear();
                    search();

                });

            }
        });

        idRendCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getIdRendezvous().toString());
        });
        iddocCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDocument().getIddoc().toString());
        });
        senderCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDocument().getSender());
        });

        recipientCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDocument().getRecipient());
        });

        registrationNumberCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDocument().getRegistrationNumber());
        });

        orderNumberCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDocument().getOrderNumber());
        });

        docDateCol.setCellValueFactory(cellData -> {
            Rendezvous p = cellData.getValue();
            return new ReadOnlyStringWrapper(p.getDate() == null ? "" : p.getDate().toString());
        });

        //date Picker configuration================================================================start
        //hidjri switcher
        rendDate1.setChronology(chronology);
        rendDate2.setChronology(chronology);

        hidjri.selectedProperty()
                .
                addListener((ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        chronology = HijrahChronology.INSTANCE;
                        rendDate1.setChronology(chronology);
                        rendDate2.setChronology(chronology);

                    } else {
                        chronology = IsoChronology.INSTANCE;
                        rendDate1.setChronology(chronology);
                        rendDate2.setChronology(chronology);

                    }
                }
                );

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

        rendDate1.setConverter(
                new CostumeConverter());
        rendDate2.setConverter(
                new CostumeConverter());
        //date Picker configuration================================================================start
    }

    @FXML
    public void print(ActionEvent a) throws Exception {
        Map<String, Object> map = new HashMap<>();

        if (rendDate2.getValue() != null) {
            map.put("dt2", DateTimeFormatter.ofPattern("yyyy/MM/dd").format(HijrahChronology.INSTANCE.date(rendDate2.getValue())));
            map.put("dt", ConverterDate.getDate(rendDate2.getValue()));
        }
        map.put("logo", getClass().getResource("/com/spring/client/Images/logo2.png").toExternalForm());
        map.put(JRParameter.REPORT_LOCALE, new Locale("ar"));
        int index = types.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            map.put("type", types.getSelectionModel().getSelectedItem());
        }

        new JRViewerMain().start(map, "/com/spring/client/fxml/rendezvous.jasper", preview.isSelected());
    }

    @FXML
    private void search() {
        Map<String, Object> map = new HashMap<>();
        if (!this.idRend.getText().equals("")) {
            map.put("idRendezvous", this.idRend.getText());

        }

        if (!this.iddoc.getText().equals("")) {
            map.put("idDoc", this.iddoc.getText());

        }

        if (!this.registrationNumber.getText().equals("")) {
            map.put("registrationNumber", this.registrationNumber.getText());

        }
        if (!this.orderNumber.getText().equals("")) {
            map.put("orderNumber", this.orderNumber.getText());

        }

        if (!this.sender.getText().equals("")) {
            map.put("sender", this.sender.getText());

        }
        if (!this.recipient.getText().equals("")) {
            map.put("recipient", this.recipient.getText());

        }
        if (this.rendDate1.getValue() != null) {
            map.put("creationDate1", ConverterDate.getDate(rendDate1.getValue()));

        }
        if (this.rendDate2.getValue() != null) {
            map.put("creationDate2", ConverterDate.getDate(rendDate2.getValue()));

        }

        if (types.getSelectionModel().getSelectedIndex() > -1) {
            map.put("type", types.getSelectionModel().getSelectedItem());

        }

        rendezvousTable.getItems().clear();
        rendezvousTable.getItems().addAll(rendezvousService.getBy(map));

    }

    @FXML
    public void clear() {

        iddoc.clear();
        sender.clear();
        types.getSelectionModel().clearSelection();
        rendDate1.setValue(null);
        rendDate2.setValue(null);
        hidjri.setSelected(false);
        registrationNumber.clear();
        recipient.clear();
        orderNumber.clear();
        idRend.clear();
        rendezvousTable.getItems().clear();
    }

}
