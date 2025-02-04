package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * InstructorHomePage class represents the user interface for the instructor user.
 * This page displays a simple welcome message for the instructor.
 */

public class InstructorHomePage {
	/**
     * Displays the instructor page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private final DatabaseHelper databaseHelper;

public InstructorHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(40);
    	Button quitButton = new Button("Back to login");
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the instructor
	    Label instructorLabel = new Label("Hello, Instructor!");
	    
	    instructorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    quitButton.setOnAction(a -> {
		    new UserLoginPage(databaseHelper).show(primaryStage);
	    });

	    layout.getChildren().addAll(instructorLabel, quitButton);
	    Scene instructorScene = new Scene(layout, 940, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(instructorScene);
	    primaryStage.setTitle("Instructor Page");
    }
}
