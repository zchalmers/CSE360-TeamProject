package application;

import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * This page displays a simple welcome message for the user.
 */
public class UserHomePage {
	private final DatabaseHelper databaseHelper;
	private User user;

	public UserHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
		user = databaseHelper.currentUser;
	}

	public void show(Stage primaryStage) {
		VBox layout = new VBox(10);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Label to display UserName and Role
		Label userLabel = new Label(
				"Hello, " + user.getUsername() + ". Your current role is : " + user.getCurrentRole());
		userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		// Button to return to the login screen
		Button quitButton = new Button("Back to login");
		quitButton.setOnAction(event -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});

		layout.getChildren().addAll(userLabel, quitButton); // Including logout button
		Scene userScene = new Scene(layout, 940, 400);

		// Set the scene to primary stage
		primaryStage.setScene(userScene);
		primaryStage.setTitle("User Page");
	}
}
