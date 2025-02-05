package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * StudentHomePage class represents the user interface for the student user.
 * This page displays a simple welcome message for the student.
 */
public class StudentHomePage {
	/**
	 * Displays the student page in the provided primary stage.
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
	private final DatabaseHelper databaseHelper;

	public StudentHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage) {
		VBox layout = new VBox(40);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Label to display the welcome message for the student
		Label studentLabel = new Label("Hello, Student!");
		studentLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		// Button to return to the login screen
		Button quitButton = new Button("Back to login");

		quitButton.setOnAction(a -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});

		layout.getChildren().addAll(studentLabel, quitButton);
		Scene studentScene = new Scene(layout, 940, 400);

		// Set the scene to primary stage
		primaryStage.setScene(studentScene);
		primaryStage.setTitle("Student Page");
	}
}
