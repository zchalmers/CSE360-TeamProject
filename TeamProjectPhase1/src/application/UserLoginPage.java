package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
				new UserHomePage(databaseHelper).show(primaryStage);
				break;
			}
		} else {
			databaseHelper.setUserCurrentRole("user");
			new UserHomePage(databaseHelper).show(primaryStage);
		}
	}

	public void show(Stage primaryStage) {
		// Input field for the user's userName, password
		TextField userNameField = new TextField();
		userNameField.setPromptText("Enter Username");
		userNameField.setStyle("-fx-text-fill: black; -fx-font-weight: bold;-fx-border-color: black, gray;"
				+ "-fx-border-width: 2, 1; -fx-border-radius: 3, 1; -fx-border-inset: 0, 4;");
		userNameField.setMaxWidth(200);
		userNameField.setAlignment(Pos.CENTER);

		PasswordField passwordField = new PasswordField();
		passwordField.setStyle("-fx-text-fill: black; -fx-font-weight: bold;-fx-border-color: black, gray;"
				+ "-fx-border-width: 2, 1; -fx-border-radius: 3, 1; -fx-border-inset: 0, 4;");
		passwordField.setPromptText("Enter Password");
		passwordField.setMaxWidth(200);
		passwordField.setAlignment(Pos.CENTER);

		// Label to display title to user
		Label prompt = new Label("Login");
		prompt.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");
		prompt.setAlignment(Pos.CENTER);

		// Label to display error messages
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 12px;");

		// Button to login
		Button loginButton = new Button("Login");
		loginButton.setStyle(
				"-fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black, gray; -fx-border-width: 2, 1;"
						+ "-fx-border-radius: 6, 5; -fx-border-inset: 0, 4;");

		// Button to register a new user
		Button setupButton = new Button("New User");
		setupButton.setStyle(
				"-fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black, gray; -fx-border-width: 2, 1;"
						+ "-fx-border-radius: 6, 5; -fx-border-inset: 0, 4;");

		// Set login button as default to allow pressing Enter to activate
		loginButton.setDefaultButton(true);

		loginButton.setOnAction(a -> {
			// Retrieve user inputs
			String userName = userNameField.getText();
			String password = passwordField.getText();

			String userNameValidate = UserNameRecognizer.checkForValidUserName(userName);
			String passwordValidate = PasswordEvaluator.evaluatePassword(password);

			if (!userNameValidate.isEmpty()) {
				errorLabel.setText("Invalid username");
				return;
			}

			if (!passwordValidate.isEmpty()) {
				errorLabel.setText(" Invalid password");
				return;
			}

			try {
				User user = databaseHelper.login(userName, password);
				System.out.println(user.toString());

				System.out.print("\n The otp value is " + user.getOTPFlag() + "\n");// debug

				if (user.getOTPFlag()) {
					System.out.print("Your if statement works\n");// debug
					new NewPasswordPage(databaseHelper).show(primaryStage, user);
					return;
				}

				// If user has more than one role, send them to RoleSelectPage otherwise call
				// roleHomePage() method
				if (user.getRoles().size() > 1) {
					new RoleSelectPage(databaseHelper).show(primaryStage);
				} else {
					roleHomePage(user, primaryStage);
				}
			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});

		// Button to register a new account
		setupButton.setOnAction(a -> {
			new SetupAccountPage(databaseHelper).show(primaryStage);
		});

		HBox hbox = new HBox(5, loginButton, setupButton);
		hbox.setAlignment(Pos.CENTER);

		// Container to hold UI elements in a nice border
		VBox layout = new VBox(10);
		layout.setMinSize(400, 220);
		layout.setMaxSize(400, 220);
		layout.setStyle("-fx-padding: 20; -fx-background-color: gray; -fx-background-radius: 100;"
				+ "-fx-background-insets: 4; -fx-border-color: gray, gray, black;"
				+ "-fx-border-width: 2, 2, 1; -fx-border-radius: 100, 100, 100;" + "-fx-border-insets: 0, 2, 4");
		layout.getChildren().addAll(prompt, userNameField, passwordField, hbox, errorLabel);
		layout.setAlignment(Pos.CENTER);

		StackPane root = new StackPane(layout);

		primaryStage.setScene(new Scene(root, 940, 400));
		primaryStage.setTitle("User Login");
		primaryStage.show();
	}
}
