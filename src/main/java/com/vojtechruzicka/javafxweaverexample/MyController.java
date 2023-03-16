package com.vojtechruzicka.javafxweaverexample;



import com.vojtechruzicka.javafxweaverexample.entyti.JournalEntity;
import com.vojtechruzicka.javafxweaverexample.service.JournalService;
import com.vojtechruzicka.javafxweaverexample.service.UserService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
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
    private AnchorPane adminPane;
    @FXML
    private TextField dob;

    @FXML
    private TableColumn<JournalEntity, String> dobCol;

    @FXML
    private TextField phone;

    @FXML
    private TableColumn<JournalEntity, String> phoneCol;

    @FXML
    private TextField surFirstSecondName;

    @FXML
    private TableColumn<JournalEntity, String> surFirstSecondNameCol;

    @FXML
    private TableView<JournalEntity> table;

    private UserService userService;
    private JournalService journalService;
    private ObservableList<JournalEntity> journalList = FXCollections.observableArrayList();

   //конструктор, используется для определения сервисного слоя
    public MyController(UserService userService, JournalService journalService) {
        this.userService = userService;
        this.journalService = journalService;
    }

   //настройка программы в зависимости от роли
   private void isRol(int rol){
        adminPane.setVisible(rol == 0);
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
        journalService.getAll().forEach(journalList::add);

        dobCol.setCellValueFactory(new PropertyValueFactory<JournalEntity, String>("dob"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<JournalEntity, String>("phone"));
        surFirstSecondNameCol.setCellValueFactory(new PropertyValueFactory<JournalEntity, String>("surFirstSecondName"));
        table.setItems(journalList);

        table.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showJournalEntityDetails(newValue)
        );
    }

    @FXML
    void onDelete(ActionEvent event) {
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            journalService.delete(table.getItems().get(selectedIndex).getId());
            table.getItems().remove(selectedIndex);
        }   else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Нет выбора");
            alert.setHeaderText("Никто не выбран");
            alert.setContentText("Пожалуйста выберите человека в таблице");
            alert.showAndWait();
        }
    }

    @FXML
    void onSave(ActionEvent event) {
        if (!dob.equals("") && !surFirstSecondName.equals("")) {
            journalList.add(journalService.save(new JournalEntity(dob.getText(), phone.getText(), surFirstSecondName.getText())));
            dob.setText("");
            phone.setText("");
            surFirstSecondName.setText("");
        }
    }

    private void showJournalEntityDetails(JournalEntity journal) {
        if (null != journal) {
            dob.setText(journal.getDob());
            phone.setText(journal.getPhone());
            surFirstSecondName.setText(journal.getSurFirstSecondName());
        }   else {
            dob.setText("");
            phone.setText("");
            surFirstSecondName.setText("");
        }
    }

}
