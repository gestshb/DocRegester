/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import com.spring.entity.Type;
import com.spring.service.interfaces.TypeService;
import com.spring.util.SpringUtil;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.springframework.context.ApplicationContext;

/**
 * @author ahmed
 */
public class TypeController implements Initializable   {

    TypeService service;
    Type selectedType;

    @FXML
    private ListView<Type> typeList;

    @FXML
    private TextField text;
    

    @FXML
    public void addTypeButton(ActionEvent action) {
        try {
            if (!text.getText().equals("")) {
                if (selectedType == null) {
                    selectedType = new Type();
                }
                selectedType.setType(text.getText());
                service.createOrUpdate(selectedType);
              
                typeList.getItems().clear();
                typeList.getItems().addAll(service.getAll());
                selectedType = null;
                text.clear();
            }
        } catch (Exception e) {
            Logger.getLogger(TypeController.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    @FXML
    public void deleteTypeButton(ActionEvent action) {
        if (selectedType != null) {
            try {
                service.delete(typeList.getSelectionModel().getSelectedItem());
              
                typeList.getItems().clear();
                typeList.getItems().addAll(service.getAll());
                selectedType = null;
                text.clear();

            } catch (Exception e) {
                Logger.getLogger(TypeController.class.getName()).log(Level.ALL, null, e);
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ApplicationContext context = SpringUtil.getApplicationContext();
        service = (TypeService) context.getBean("typeServiceImpl");
        typeList.getItems().addAll(service.getAll());

        typeList.getSelectionModel().selectedItemProperty().
                addListener(new ChangeListener<Type>() {

            public void changed(ObservableValue<? extends Type> observable, Type oldValue, Type newValue) {
                selectedType = newValue;
                if (selectedType!=null)
                text.setText(selectedType.getType());
            }
        });


        text.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    if (!text.getText().equals("")) {
                        if (selectedType == null) {
                            selectedType = new Type();
                        }
                        selectedType.setType(text.getText());
                        service.createOrUpdate(selectedType);
                        typeList.getItems().clear();
                        typeList.getItems().addAll(service.getAll());
                        selectedType = null;
                        text.clear();
                    }
                } catch (Exception e) {
                    Logger.getLogger(TypeController.class.getName()).log(Level.SEVERE, null, e);
                }
            }
        });
    }

}
