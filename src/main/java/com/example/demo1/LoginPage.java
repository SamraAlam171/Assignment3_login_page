package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoginPage extends Application {
    private Label resultLabel = new Label();

    @Override
    public void start(Stage primaryStage) {

        Image bannerImage = new Image(this.getClass().getResource("/office.jpg").toExternalForm());
        ImageView banner = new ImageView(bannerImage);
        banner.setFitHeight(100);
        banner.setPreserveRatio(true);
        banner.setSmooth(true);


        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter Username");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");


        Button signInButton = new Button("Sign In");
        Button loginButton = new Button("Log In");
        Button exitButton = new Button("Exit");


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));


        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);


        HBox buttonBox = new HBox(10, signInButton, loginButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);
        grid.add(buttonBox, 0, 2, 2, 1);
        grid.add(resultLabel, 0, 3, 2, 1);

        resultLabel.setStyle("-fx-text-fill: red;");

        VBox layout = new VBox(10, banner, grid);
        layout.setAlignment(Pos.TOP_CENTER);

        layout.setStyle("-fx-background-color: #D3D3D3;");

        Scene scene = new Scene(layout, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Page");
        primaryStage.show();


        signInButton.setOnAction(e -> handleSignIn(usernameField.getText(), passwordField.getText()));
        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
        exitButton.setOnAction(e -> primaryStage.close());

    }

    private void handleSignIn(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            resultLabel.setText("Username and Password cannot be empty.");
            return;
        }

        List<String[]> users = readUsers();
        for (String[] user : users) {
            if (user[0].equals(username)) {
                resultLabel.setText("User already exists! Please log in.");
                return;
            }
        }

        users.add(new String[]{username, password});
        writeUsers(users);
        resultLabel.setText("Sign In Successful!");
        resultLabel.setStyle("-fx-text-fill: green;");
    }

    private void handleLogin(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            resultLabel.setText("Username and Password cannot be empty.");
            return;
        }

        List<String[]> users = readUsers();
        for (String[] user : users) {
            if (user[0].equals(username) && user[1].equals(password)) {
                resultLabel.setText("Login Successful! Welcome " + username + "!");
                resultLabel.setStyle("-fx-text-fill: green;");
                return;
            }
        }

        resultLabel.setText("Invalid Username or Password.");
    }

    private List<String[]> readUsers() {
        List<String[]> users = new ArrayList<>();
        File file = new File("users.txt");
        if (!file.exists()) return users;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.add(parts);
                }
            }
        } catch (IOException e) {
            resultLabel.setText("Error reading user data.");
        }
        return users;
    }

    private void writeUsers(List<String[]> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (String[] user : users) {
                writer.write(user[0] + "," + user[1]);
                writer.newLine();
            }
        } catch (IOException e) {
            resultLabel.setText("Error writing user data.");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

