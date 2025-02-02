package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;

/**
 * RoleSelectPage class represents the user interface for the role-selection page.
 * This page displays only if a user has multiple roles and allows the user to 
 * select which role they which to play.
 */
public class RoleSelectPage {
	/**
     * Displays the role-selection page.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	private final DatabaseHelper databaseHelper;
	String selectedRole;
	User user = databaseHelper.currentUser;
	
    public RoleSelectPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) { 
    	
    	// Create Next and Back buttons
    	Button nextButton = new Button("Next");
    	Button quitButton = new Button("Back");

    	// Create layout for buttons and combobox
    	VBox layout = new VBox(20);    	
    	layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    	    
	    // Create combobox for selecting roles
	    ComboBox<String> comboBox = new ComboBox<>();
	    
	    // Add roles text to comboBox
	    comboBox.getItems().addAll(user.getRoles());
	    // comboBox.getItems().addAll(userRoles);
	    
	    // Set default comboBox text
	    comboBox.setPromptText("Please select a role");	    
	    
	    // ComboBox allows selection of roles. Deactivates Next button until selection is made
	    comboBox.setOnAction(a -> {
	    	selectedRole = comboBox.getValue();
	    	if (selectedRole != null) {
	    		nextButton.setDisable(false);
	    	} else {
	    		nextButton.setDisable(true);
	    	}
	    });

		// Next button to proceed with selection. Is inactive unless selection is made
		// in ComboBox
		// Based on role selected, go to that role's home page.
		nextButton.setOnAction(a -> {
			try {
				switch (selectedRole) {
				case "admin":
					new AdminHomePage(databaseHelper).show(primaryStage);
					break;
				case "student":
					new StudentHomePage(/* databaseHelper */).show(primaryStage);
					break;
				case "instructor":
					new InstructorHomePage(/* databaseHelper */).show(primaryStage);
					break;
				case "staff":
					new StaffHomePage(/* databaseHelper */).show(primaryStage);
					break;
				case "reviewer":
					new ReviewerHomePage(/* databaseHelper */).show(primaryStage);
					break;
				}
			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});
	    
		// Quit button to return to User Login Screen
		quitButton.setOnAction(a -> {
			try {
				new UserLoginPage(databaseHelper).show(primaryStage);
			} catch (SQLException e) {
				System.err.println("Database error: " + e.getMessage());
				e.printStackTrace();
			}
		});

	    // Attach buttons and combobox to layout
	    layout.getChildren().addAll(comboBox, nextButton, quitButton);
	    Scene roleSelectScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(roleSelectScene);
	    primaryStage.setTitle("Role-Selection Page");
    }
}
