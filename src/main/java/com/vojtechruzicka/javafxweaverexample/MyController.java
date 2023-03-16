package com.vojtechruzicka.javafxweaverexample;



import com.vojtechruzicka.javafxweaverexample.service.UserService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static javafx.scene.control.ButtonType.CANCEL;

@Component
@FxmlView("main-stage.fxml")
public class MyController {

    @FXML
    private Button bSave;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TextField tSurname;

    @FXML
    private TableView<?> table;

    @FXML
    void onSave(ActionEvent event) {

    }

   private  UserService userService;

   //конструктор, используется для определения сервисного слоя
    public MyController(UserService userService) {
        this.userService = userService;
    }

   //настройка программы в зависимости от роли
   private void isRol(int rol){

   }
    // создание диалогового окна логин/пароль
    private void loginDialog(){

        // Создание диалогового окна.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Окно логина");
        dialog.setHeaderText("Введите логин пароль для входа в программу");

        // Создание кнопок(OK, Cancel).
        ButtonType loginButtonType = new ButtonType("Вход", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, CANCEL);

        // Разметка окна через GridPane
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        //Поля ввода
        TextField username = new TextField();
        username.setPromptText("Логин");
        PasswordField password = new PasswordField();
        password.setPromptText("Пароль");
        // надписи
        grid.add(new Label("Логин"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Пароль"), 0, 1);
        grid.add(password, 1, 1);

        // Включить/выключить кнопку входа в зависимости от того, было ли введено имя пользователя.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Фокус на поле имени пользователя по умолчанию.
        Platform.runLater(() -> username.requestFocus());

        // Выполните некоторую проверку (используя лямбда-синтаксис Java 8).
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Преобразование результата в пару "имя пользователя-пароль" при нажатии кнопки входа.
        // И смотрим на правильность ввода логина пароля
        // если правильно то узнаем роль


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()){
            if(userService.logIn(username.getText(), password.getText())){
                System.out.println("Username=" + username.getText() + ", Password=" + password.getText());
                isRol(userService.getRol(username.getText(), password.getText()));
            }


        }

/*
        result.ifPresent(usernamePassword -> {
            if(userService.logIn(usernamePassword.getValue(),usernamePassword.getKey()))
            System.out.println("Username=" + usernamePassword.getKey() + ", Password=" + usernamePassword.getValue());
        });*/
    }
    @FXML

    private void initialize() {
        loginDialog(); // вызов диалогового окна логин/пароль
    }

}
