package com.example.kechengsheji;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;
import javafx.scene.Node;
/**
 * 控制器类，用于处理登录界面的逻辑。
 */
public class controler0 {
    @FXML
    private TextField AssWord;

    @FXML
    private TextField AssID;
    /**
     * 处理登录按钮点击事件的方法。
     * @param event 点击事件
     * @throws IOException 输入/输出异常
     */
    @FXML
    void log(ActionEvent event) throws IOException {
        String enteredID = AssID.getText();
        String enteredPassword = AssWord.getText();

        if ("123".equals(enteredID) && "123".equals(enteredPassword)) {
            // 登录成功，跳转到 MRYview.fxml

            // 获取按钮所属的 Stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 关闭当前登录界面
            currentStage.close();

            // 打开新的界面 MRYview.fxml
            Parent root = FXMLLoader.load(getClass().getResource("MRYview.fxml"));
            Stage stage = new Stage();
            stage.setTitle("美容院管理系统");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        } else {
            // 密码错误，跳转到 Errview.fxml

            // 获取按钮所属的 Stage
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 关闭当前登录界面
            currentStage.close();

            // 打开新的界面 Errview.fxml
            Parent root = FXMLLoader.load(getClass().getResource("Errview.fxml"));
            Stage stage = new Stage();
            stage.setTitle("错误提示");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
        }
    }
}
