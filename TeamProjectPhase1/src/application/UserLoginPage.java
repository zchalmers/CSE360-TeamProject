package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their
 * accounts. It validates the user's credentials and navigates to the
 * appropriate page upon successful login.
 */
public class UserLoginPage {
	private final DatabaseHelper databaseHelper;

	public UserLoginPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	// Method to include direction to the necessary role pages
	private void roleHomePage(User user, Stage primaryStage) {
		if (user.getRoles().size() > 0) {
			
			switch (user.getRoles().get(0).toLowerCase()) {
			case "admin":
				databaseHelper.setUserCurrentRole("admin");
				new AdminHomePage(databaseHelper).show(primaryStage, user);
				break;

			case "student":
				databaseHelper.setUserCurrentRole("student");
				new StudentHomePage(databaseHelper).show(primaryStage);
				break;

			case "instructor":
				databaseHelper.setUserCurrentRole("instructor");
				new InstructorHomePage(databaseHelper).show(primaryStage);
				break;

			case "staff":
				databaseHelper.setUserCurrentRole("staff");
				new StaffHomePage(databaseHelper).show(primaryStage);
				break;

			case "reviewer":
				databaseHelper.setUserCurrentRole("reviewer");
				new ReviewerHomePage(databaseHelper).show(primaryStage);
				break;

			default:
				databaseHelper.setUserCurrentRole("user");
				new UserHomePage(user).show(primaryStage);
				break;
			}
		}
		else {
			databaseHelper.setUserCurrentRole("user");
			new UserHomePage(user).show(primaryStage);
		}
	}

	public void show(Stage primaryStage) {
		// Input field for the user's userName, password
		TextField userNameField = new TextField();
		userNameField.setPromptText("Enter userName");
		userNameField.setMaxWidth(250);

		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter Password");
		passwordField.setMaxWidth(250);

		// Label to display error messages
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		Button loginButton = new Button("Login");
		Button setupButton = new Button("Register New User");

		// Set login button as default to allow pressing Enter to activate
		loginButton.setDefaultButton(true);

		loginButton.setOnAction(a -> {
			// Retrieve user inputs
			String userName = userNameField.getText();
			String password = passwordField.getText();

			String userNameValidate = UserNameRecognizer.checkForValidUserName(userName);
			String passwordValidate = PasswordEvaluator.evaluatePassword(password);

			if (!userNameValidate.isEmpty()) {
				errorLabel.setText("***ERROR*** Invalid username");
				return;
			}

			if (!passwordValidate.isEmpty()) {
				errorLabel.setText("***ERROR*** Invalid password");
				return;
			}

			try {
				User user = databaseHelper.login(userName, password);
				

				
				System.out.print("\n The otp value is " + user.getOTPFlag(true) + "\n");//debug
				
				if(user.getOTPFlag(true)) {
					
					System.out.print("Your if statement works\n");//debug

					new NewPasswordPage(databaseHelper).show(primaryStage, user);
					return;

					
				}

				if (user != null) {
					if (user.getRoles().size() > 1) {
						new RoleSelectPage(databaseHelper).show(primaryStage);
					} else {
						roleHomePage(user, primaryStage);
					}
				}

				else {
					// Display an error if the login fails
					errorLabel.setText("Error logging in. Contact an Administrator.");
				}

			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});

		setupButton.setOnAction(a -> {
			new SetupAccountPage(databaseHelper).show(primaryStage);
		});

		VBox layout = new VBox(10);
		layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
		layout.getChildren().addAll(userNameField, passwordField, loginButton, setupButton, errorLabel);

		primaryStage.setScene(new Scene(layout, 940, 400));
		primaryStage.setTitle("User Login");
		primaryStage.show();
	}
}
