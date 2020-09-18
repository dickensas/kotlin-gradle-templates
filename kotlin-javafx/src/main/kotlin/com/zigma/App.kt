package com.zigma

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage


class App : Application() {
    public override fun start(primaryStage:Stage) {
        try {
            var root:Parent = FXMLLoader.load(App::class.java.getResource("App.fxml"));
            var scene:Scene = Scene(root)
            scene.getStylesheets().add(App::class.java.getResource("App.css").toExternalForm())
            primaryStage.setTitle("Example")
            primaryStage.setScene(scene)
            primaryStage.show()
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
    companion object {
        fun main(args: Array<String>) {
        }
    }
}
