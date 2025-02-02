package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import databasePart1.DatabaseHelper;

/**
 * StaffHomePage class represents the user interface for the staff user.
 * This page displays a simple welcome message for the staff.
 */

public class StaffHomePage {
	/**
     * Displays the staff page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */

	private final DatabaseHelper databaseHelper;

public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(40);
    	Button quitButton = new Button("Back to login");
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the staff
	    Label staffLabel = new Label("Hello, Staff!");
	    
	    staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    quitButton.setOnAction(a -> {
		    new UserLoginPage(databaseHelper).show(primaryStage);
	    });

	    layout.getChildren().addAll(staffLabel, quitButton);
	    Scene staffScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(staffScene);
	    primaryStage.setTitle("Staff Page");
    }
}
