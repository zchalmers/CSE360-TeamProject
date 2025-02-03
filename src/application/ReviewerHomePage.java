package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * ReviewerHomePage class represents the user interface for the reviewer user.
 * This page displays a simple welcome message for the reviewer.
 */

public class ReviewerHomePage {
	/**
     * Displays the reviewer page in the provided primary stage.
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
	    
	    // label to display the welcome message for the reviewer
	    Label reviewerLabel = new Label("Hello, Reviewer!");
	    
	    reviewerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    quitButton.setOnAction(a -> {
		    new UserLoginPage(databaseHelper).show(primaryStage);
	    });

	    layout.getChildren().addAll(reviewerLabel, quitButton);
	    Scene reviewerScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(reviewerScene);
	    primaryStage.setTitle("Reviewer Page");
    }
}
