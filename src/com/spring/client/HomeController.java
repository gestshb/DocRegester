package com.spring.client;

import com.spring.client.util.CostumStage;
import com.spring.client.util.StageFactory;
import com.spring.entity.User;
import com.spring.util.SpringUtil;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;
import org.springframework.context.ApplicationContext;

public class HomeController implements Initializable {

    //==================================================stages
    public static StageFactory factory;
    public static CostumStage addDocumentStage;
    public static CostumStage findDocumentStage;
    public static CostumStage findRendezVousStage;
    public static CostumStage RendezVousStage;
   
   

    //==================================================

    ;
    public static User loggedUser;
    public static Set<String> permission = new HashSet<>();

    @FXML
    Label dateLabel;
    @FXML
    Label loggedEmployer;
    @FXML
    ImageView logo;
    
    @FXML
    public void findRendezvous(ActionEvent event){
    if ((!permission.contains("addRendezvous")) && !loggedUser.isSysAdmin() ) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
    findRendezVousStage = factory.build("findRend");
    FindRendezvous controller = (FindRendezvous) findRendezVousStage.getController();
         findRendezVousStage.setOnShown(new EventHandler<WindowEvent>() {

        @Override
        public void handle(WindowEvent event) {
           controller.clear();
        }
    });
         findRendezVousStage.show();
    
    }

    

    @FXML
    public void addDocumentButton(ActionEvent event) {

        if ((!permission.contains("addDocument")) && !loggedUser.isSysAdmin() ) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        DocumentController docController = (DocumentController) addDocumentStage.getController();
        addDocumentStage.setOnHidden((WindowEvent eve) -> {
            
            docController.clearDocument();
        });

       
        addDocumentStage.show();
    }

    @FXML
    public void findDocumentButton(ActionEvent action) {
        if ((!permission.contains("findDoc")) && !loggedUser.isSysAdmin() ) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        FindDocumentController controller = (FindDocumentController) findDocumentStage.getController();
        
        findDocumentStage.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {
                 controller.onClose();
            }
        });
        findDocumentStage.show();
    }

    
    @FXML
    public void userButton(ActionEvent action) {
        if (!loggedUser.isSysAdmin()) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        CostumStage userStage = factory.build("users");
        userStage.show();

    }

    

    @FXML
    public void addTypeButton(ActionEvent action) {
        if ( !loggedUser.isSysAdmin()) {
            alert("تنبيه", "لا تملك الصلاحيات لأداء هذا الإجراء");
            return;
        }
        CostumStage addTypeStage = factory.build("types");
        addTypeStage.show();

    }
    
  




    //alert message
    public static void alert(String message, String longMsg) {
        CostumStage alert;
        alert = factory.build("alert");
        AlertController controller = (AlertController) alert.getController();
        controller.setMsg(message, longMsg);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //=======================================================
        ApplicationContext context = SpringUtil.getApplicationContext();
        factory = (StageFactory) context.getBean("stageFactoryImpl");

//        docService = (DocumentService) context.getBean("documentServiceImpl");

        loggedUser.getActions().stream().forEach((a) -> {
            permission.add(a.getActionEn());
        });

        addDocumentStage = factory.build("adddoc");
        findDocumentStage = factory.build("finddoc");


        
        //======init employee
        loggedEmployer.setText("الموظف المتصل : "+loggedUser.getUsername()+"  "+loggedUser.getFirstname());

       
        Task timer = new Task() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                   
                    updateMessage(new SimpleDateFormat(" yyyy/MM/dd HH:mm").format(System.currentTimeMillis()));
                     Thread.sleep(60000);
                }
            }
        };

        dateLabel.textProperty().bind(timer.messageProperty());

        Thread timerService = new Thread(timer);
        timerService.setDaemon(true);
        timerService.start();

    }

}
