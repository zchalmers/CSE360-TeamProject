package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * SetupAccountPage class handles the account setup process for new users. Users
 * provide their userName, password, and a valid invitation code to register.
 */
public class SetupAccountPage {

	private final DatabaseHelper databaseHelper;

	// DatabaseHelper to handle database operations.
	public SetupAccountPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	/**
	 * Displays the Setup Account page in the provided stage.
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
	public void show(Stage primaryStage) {

		// Input fields for userName, password, and invitation code
		TextField userNameField = new TextField();
		userNameField.setPromptText("Enter UserName");
		userNameField.setMaxWidth(250);

		TextField nameField = new TextField();
		nameField.setPromptText("Enter Name");
		nameField.setMaxWidth(250);

		TextField emailField = new TextField();
		emailField.setPromptText("Enter Email");
		emailField.setMaxWidth(250);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter Password");
		passwordField.setMaxWidth(250);

		TextField inviteCodeField = new TextField();
		inviteCodeField.setPromptText("Enter InvitationCode");
		inviteCodeField.setMaxWidth(250);

		// Label to display error messages for invalid input or registration issues
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		Button setupButton = new Button("Setup");
		setupButton.setDefaultButton(true);

		// Create button to return to login screen
		Button quitButton = new Button("Back to login");

		// Quit button to return to User Login Screen
		quitButton.setOnAction(a -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});

		setupButton.setOnAction(a -> {
			// Retrieve user input
			String userName = userNameField.getText();
			String name = nameField.getText();
			String email = emailField.getText();
			String password = passwordField.getText();
			String code = inviteCodeField.getText();

			String userNameValidate = UserNameRecognizer.checkForValidUserName(userName);

			String passwordValidate = PasswordEvaluator.evaluatePassword(password);

			if (!userNameValidate.isEmpty()) {
				errorLabel.setText(userNameValidate);
				return;
			}

			if (!passwordValidate.isEmpty()) {
				errorLabel.setText(passwordValidate);
				return;
			}

			try {
				// Check if the user already exists
				if (!databaseHelper.doesUserExist(userName)) {

					// Validate the invitation code
					if (databaseHelper.validateInvitationCode(code)) {

						// Create a new user and register them in the database
						List<String> roles = new ArrayList<>();

						User user = new User(userName, name, password, email, roles);
						databaseHelper.register(user);

						// Navigate to the Welcome Login Page
						new UserLoginPage(databaseHelper).show(primaryStage);
					} else {
						errorLabel.setText("Please enter a valid invitation code.");
					}
				} else {
					errorLabel.setText("This useruserName is taken!!.. Please use another to setup an account");
				}

			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});

		HBox hbox = new HBox(5, setupButton, quitButton);
		hbox.setStyle("-fx-padding: 20; -fx-alignment: center;");

		VBox layout = new VBox(10);
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
		layout.getChildren().addAll(userNameField, nameField, emailField, passwordField, inviteCodeField, hbox,
				errorLabel);

		primaryStage.setScene(new Scene(layout, 940, 400));
		primaryStage.setTitle("Account Setup");
		primaryStage.show();
	}
}
