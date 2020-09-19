package com.zigma

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import javafx.scene.control.ListView
import javafx.collections.ObservableList
import javafx.collections.FXCollections
import java.util.List
import java.util.ArrayList
import javafx.scene.Node


class App : Application() {
    public override fun start(primaryStage:Stage) {
        try {
            var root:Parent = FXMLLoader.load(App::class.java.getResource("App.fxml"))
            var scene:Scene = Scene(root)
            scene.getStylesheets().add(App::class.java.getResource("App.css").toExternalForm())

            primaryStage.setTitle("Example")
            primaryStage.setScene(scene)
            primaryStage.show()
            
            var i=0
            var j=0
            var gridPane:GridPane = scene.lookup("#mygrid") as GridPane
            for(i in 0..30) {
                for(j in 0..12) {
                    var button1:Button = Button(" ")
                    gridPane.add(button1, i, j, 1, 1)
                }
            }
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
    companion object {
        fun main(args: Array<String>) {
        }
    }
}
