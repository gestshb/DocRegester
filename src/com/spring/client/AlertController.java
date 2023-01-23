/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.spring.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author ahmed
 */
public class AlertController implements Initializable {

    @FXML
    Label msg;
    @FXML
    Label longMsg;

    @FXML
    public void ok(ActionEvent a) {
        msg.getScene().getWindow().hide();

    }

    public void setMsg(String msg, String longMsg) {
        this.msg.setText(msg);
        this.longMsg.setText(longMsg);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
