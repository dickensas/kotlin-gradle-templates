package com.zigma

import java.net.URL
import java.util.ResourceBundle

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextArea

public class Controller : Initializable {

    public override fun initialize(url:URL, resourceBundle:ResourceBundle?)
    {
        println("controller init");
    }

}