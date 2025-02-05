package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their
 * accounts. It validates the user's credentials and navigates to the
 * appropriate page upon successful login.
 */
public class NewPasswordPage {
	private final DatabaseHelper databaseHelper;

	public NewPasswordPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage, User user) {
		// Input field for the user's userName, password
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter New Password");
		passwordField.setMaxWidth(250);

		PasswordField passwordValidField = new PasswordField();
		passwordValidField.setPromptText("Re-Enter New Password");
		passwordValidField.setMaxWidth(250);

		// Label to display error messages
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		Button saveChanges = new Button("Save");		

		// Set saveChanges button as default to allow pressing Enter to activate
		saveChanges.setDefaultButton(true);

		saveChanges.setOnAction(a -> {
			// Retrieve user inputs
			String password = passwordField.getText();
			String passwordValid = passwordValidField.getText();

			String passwordValidate = PasswordEvaluator.evaluatePassword(password);


			if (!passwordValidate.isEmpty()) {
				errorLabel.setText("***ERROR*** Invalid password");
				return;
			} else {
				if (passwordValid.equals(password)) {
					
					errorLabel.setText("***Success*** Password Changed");
					databaseHelper.updatePassword(user.getUsername(),password);
					
					databaseHelper.updateOTPFlag(user.getUsername(), false);
					
					new UserLoginPage(databaseHelper).show(primaryStage);
					return;
				}
			}			
		});

			
		VBox layout = new VBox(10);
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
		layout.getChildren().addAll(passwordField, passwordValidField, saveChanges, errorLabel);

		primaryStage.setScene(new Scene(layout, 940, 400));
		primaryStage.setTitle("Password Change");
		primaryStage.show();
	}
}
