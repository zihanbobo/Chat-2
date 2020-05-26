package cn.roy.chat.controller;

import cn.roy.chat.cache.CacheUtil;
import cn.roy.chat.call.CallChatServer;
import cn.roy.chat.enity.LoginEntity;
import cn.roy.chat.enity.ResultData;
import cn.roy.chat.enity.UserEntity;
import cn.roy.chat.http.HttpRequestTask;
import cn.roy.chat.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import io.netty.util.internal.StringUtil;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController {
    @FXML
    private TextField userNameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label tipLabel;
    @FXML
    private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        final UserEntity user = CacheUtil.getCacheFromProp("user", UserEntity.class);
        if (user != null) {
            userNameField.setText(user.getName());
            passwordField.setText(user.getPassword());

            userNameField.positionCaret(user.getName().length());
        }
    }

    @Override
    public Scene config(Stage stage, Parent root) {
        Scene scene = new Scene(root);
        stage.setTitle("Chat");
        stage.setMinWidth(300.0);
        stage.setMaxWidth(400.0);
        stage.setMinHeight(550.0);
        stage.setMaxHeight(750.0);
        stage.setWidth(350.0);
        stage.setHeight(700.0);
        stage.setResizable(true);
        return scene;
    }

    public void login(ActionEvent actionEvent) {
        String userName = userNameField.getText().trim();
        String password = passwordField.getText().trim();

        if (StringUtil.isNullOrEmpty(userName)) {
            tipLabel.setText("用户名不能为空");
            return;
        }

        if (StringUtil.isNullOrEmpty(password)) {
            tipLabel.setText("密码不能为空");
            return;
        }

        doLogin(userName, password);
    }

    private void doLogin(String userName, String password) {
        tipLabel.setText("");
        userNameField.setEditable(false);
        passwordField.setEditable(false);
        loginButton.setText("登录中...");
        loginButton.setDisable(true);

        final LoginEntity entity = new LoginEntity();
        entity.setUserName(userName);
        entity.setPassword(password);

        HttpUtil.execute(new HttpRequestTask() {
            @Override
            public ResultData doInBackground() {
                final CallChatServer callChatServer = getApplicationContext()
                        .getBean(CallChatServer.class);
                final ResultData resultData = callChatServer.login(entity);
                return resultData;
            }

            @Override
            public void success(String data) {
                final UserEntity userEntity = JSON.parseObject(data, UserEntity.class);
                userEntity.setPassword(password);
                CacheUtil.cacheData("user", userEntity);
                CacheUtil.cacheToProp("user", userEntity);

                userNameField.setEditable(true);
                passwordField.setEditable(true);
                loginButton.setText("登录");
                loginButton.setDisable(false);
                jump2Main();

            }

            @Override
            public void fail(String msg) {
                userNameField.setEditable(true);
                passwordField.setEditable(true);
                tipLabel.setText(msg);
                loginButton.setText("登录");
                loginButton.setDisable(false);
            }
        });
    }

    private void jump2Main() {
        jump2NewScene("main.fxml", "main.css");
    }

}
