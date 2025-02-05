package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator
 * account. This is intended to be used by the first user to initialize the
 * system with admin credentials.
 */
public class AdminSetupPage {
	private final DatabaseHelper databaseHelper;

	public AdminSetupPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage) {

		// Label to explain the first page to the user
		Label userLabel = new Label("             Hello..You are the first person here."
				+ "\nPlease register an administrator account to continue");
		userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		// Input fields for account info
		TextField userNameField = new TextField();
		userNameField.setPromptText("Enter Admin userName");
		userNameField.setMaxWidth(250);

		TextField nameField = new TextField();
		nameField.setPromptText("Enter Admin Name");
		nameField.setMaxWidth(250);

		TextField emailField = new TextField();
		emailField.setPromptText("Enter Admin email");
		emailField.setMaxWidth(250);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter Password");
		passwordField.setMaxWidth(250);

		Button setupButton = new Button("Setup");
		setupButton.setDefaultButton(true);
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		setupButton.setOnAction(a -> {
			
			// Retrieve user input
			String userName = userNameField.getText();
			String name = nameField.getText();
			String email = emailField.getText();
			String password = passwordField.getText();
			
			// Using FSM to validate
			String userNameValidate = UserNameRecognizer.checkForValidUserName(userName);
			
			// Using FSM to validate name
			String nameValidate = NameValidator.checkForValidName(name);
			
			// Using FSM to validate Password
			String passwordValidate = PasswordEvaluator.evaluatePassword(password);
			
			// Using FSM to validate email syntax
			String emailValidate = EmailValidator.checkForValidEmail(email);

			if (!userNameValidate.isEmpty()) {
				errorLabel.setText(userNameValidate);
				return;
			}

			if (!nameValidate.isEmpty()) {
				errorLabel.setText(nameValidate);
				return;
			}

			if (!emailValidate.isEmpty()) {
				errorLabel.setText(emailValidate);
				return;
			}

			if (!passwordValidate.isEmpty()) {
				errorLabel.setText(passwordValidate);
				return;
			}

			try {
				// Create a new User object with admin role and register in the database
				List<String> roles = new ArrayList<>();
				roles.add("Admin");

				User user = new User(userName, name, password, email, roles);
				databaseHelper.register(user);
				System.out.println("Administrator setup completed.");

				// Navigate to the Welcome Login Page
				new UserLoginPage(databaseHelper).show(primaryStage);
			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});

		VBox layout = new VBox(10, userLabel, userNameField, nameField, emailField, passwordField, setupButton,
				errorLabel);
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

		primaryStage.setScene(new Scene(layout, 940, 400));
		primaryStage.setTitle("Administrator Setup");
		primaryStage.show();
	}
}
