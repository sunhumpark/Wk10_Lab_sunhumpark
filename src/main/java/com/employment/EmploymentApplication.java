
package com.employment;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Types;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class EmploymentApplication extends Application {

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/employment_db";

    private static final String DB_USER = "root";

    private static final String DB_PASSWORD =
            System.getenv("MYSQL_PASSWORD");

    @Override
    public void start(Stage stage) {

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(12);

        Label title = new Label("Employment Application");
        title.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        TextField fullName = new TextField();
        TextField address = new TextField();
        TextField phone = new TextField();
        TextField email = new TextField();
        TextField education = new TextField();

        ComboBox<String> gender = new ComboBox<>();
        gender.getItems().addAll("Male", "Female", "Other");

        DatePicker availableDate = new DatePicker();

        TextField position = new TextField();
        TextField salary = new TextField();

        ComboBox<String> authorized = new ComboBox<>();
        authorized.getItems().addAll("Yes", "No");

        ComboBox<String> relatives = new ComboBox<>();
        relatives.getItems().addAll("Yes", "No");

        TextField relativesDetails = new TextField();
        TextField signature = new TextField();

        Button submitButton = new Button("Submit");

        submitButton.setStyle(
                "-fx-background-color: #2874A6;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;"
        );

        int row = 0;

        grid.add(title, 0, row++, 2, 1);

        grid.add(new Label("Full Name:"), 0, row);
        grid.add(fullName, 1, row++);

        grid.add(new Label("Current Address:"), 0, row);
        grid.add(address, 1, row++);

        grid.add(new Label("Contact Number:"), 0, row);
        grid.add(phone, 1, row++);

        grid.add(new Label("Email Address:"), 0, row);
        grid.add(email, 1, row++);

        grid.add(new Label("Education:"), 0, row);
        grid.add(education, 1, row++);

        grid.add(new Label("Gender:"), 0, row);
        grid.add(gender, 1, row++);

        grid.add(new Label("Date Available:"), 0, row);
        grid.add(availableDate, 1, row++);

        grid.add(new Label("Desired Position:"), 0, row);
        grid.add(position, 1, row++);

        grid.add(new Label("Desired Salary:"), 0, row);
        grid.add(salary, 1, row++);

        grid.add(new Label("Authorized to Work:"), 0, row);
        grid.add(authorized, 1, row++);

        grid.add(new Label("Relatives Working:"), 0, row);
        grid.add(relatives, 1, row++);

        grid.add(new Label("Relatives Details:"), 0, row);
        grid.add(relativesDetails, 1, row++);

        grid.add(new Label("Applicant Signature:"), 0, row);
        grid.add(signature, 1, row++);

        grid.add(submitButton, 1, row);

        submitButton.setOnAction(event -> {

            String sql =
                    "INSERT INTO applications (" +
                    "full_name, current_address, " +
                    "contact_number, email_address, " +
                    "education, gender, date_available, " +
                    "desired_position, desired_salary, " +
                    "authorized_to_work, relatives_working, " +
                    "relatives_details, applicant_signature" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (
                Connection connection =
                        DriverManager.getConnection(
                                DB_URL, DB_USER, DB_PASSWORD
                        );

                PreparedStatement ps =
                        connection.prepareStatement(sql)
            ) {

                ps.setString(1, fullName.getText());
                ps.setString(2, address.getText());
                ps.setString(3, phone.getText());
                ps.setString(4, email.getText());
                ps.setString(5, education.getText());
                ps.setString(6, gender.getValue());

                if (availableDate.getValue() != null) {
                    ps.setDate(
                            7,
                            java.sql.Date.valueOf(
                                    availableDate.getValue()
                            )
                    );
                } else {
                    ps.setNull(7, Types.DATE);
                }

                ps.setString(8, position.getText());

                String salaryText = salary.getText().trim();

                if (salaryText.isEmpty()) {
                    ps.setNull(9, Types.DECIMAL);
                } else {
                    ps.setBigDecimal(
                            9,
                            new BigDecimal(salaryText)
                    );
                }

                ps.setString(10, authorized.getValue());
                ps.setString(11, relatives.getValue());
                ps.setString(12, relativesDetails.getText());
                ps.setString(13, signature.getText());

                int result = ps.executeUpdate();

                if (result > 0) {
                    showMessage(
                            "Success",
                            "Application submitted successfully!"
                    );
                }

            } catch (Exception e) {
                e.printStackTrace();

                showMessage(
                        "Error",
                        "Database Error: " + e.getMessage()
                );
            }
        });

        ScrollPane scrollPane = new ScrollPane(grid);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane, 600, 700);

        stage.setTitle("Employment Application");
        stage.setScene(scene);
        stage.show();
    }

    private void showMessage(String title, String message) {

        Alert alert = new Alert(
                Alert.AlertType.INFORMATION
        );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
