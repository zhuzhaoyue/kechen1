package com.example.kechengsheji;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
/**
 * 控制器类，用于处理与会员管理、美容品管理、销售记录等相关功能的界面跳转逻辑。
 */
public class controler1 {

    /**
     * 处理点击“会员管理”按钮事件的方法，打开“Memview.fxml”界面。
     * @param event 点击事件
     * @throws IOException 输入/输出异常
     */
    @FXML
    void vip(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Memview.fxml"));
        Stage stage=new Stage();
        stage.setTitle("会员管理");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * 处理点击“美容品管理”按钮事件的方法，打开“PROview.fxml”界面。
     * @param event 点击事件
     * @throws IOException 输入/输出异常
     */
    @FXML
    void pro(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("PROview.fxml"));
        Stage stage=new Stage();
        stage.setTitle("美容品管理");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
    /**
     * 处理点击“销售记录”按钮事件的方法，打开“Saleview.fxml”界面。
     * @param event 点击事件
     * @throws IOException 输入/输出异常
     */
    @FXML
    void sal(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Saleview.fxml"));
        Stage stage=new Stage();
        stage.setTitle("销售记录");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
    }
}
