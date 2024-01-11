package com.example.kechengsheji;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * MRYviewer 类是 JavaFX 应用程序的入口类。
 */
public class MRYviewer extends Application {
    /**
     * JavaFX 应用程序的入口方法。
     *
     * @param stage JavaFX 主舞台对象
     * @throws Exception 如果在应用程序启动过程中发生异常
     */
    @Override
    public void start(Stage stage) throws Exception {
        //Parent root= FXMLLoader.load(getClass().getResource("/demo/Memview.fxml"));
        Parent root = new FXMLLoader(MRYviewer.class.getResource("Loginview.fxml")).load();
        stage.setTitle("登录界面");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
    /**
     * 主方法，启动 JavaFX 应用程序。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        launch(args);
    }

}