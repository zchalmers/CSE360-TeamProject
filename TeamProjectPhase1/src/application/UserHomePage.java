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

	private User user;
	
	public UserHomePage(User user) {
		this.user = user;
	}
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display UserName and Role 
	    Label userLabel = new Label("Hello, " + user.getUsername() + ". Your current role is : " + user.getCurrentRole());
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Logout button to redirect user back to UserLoginPage
	    Button logoutButton = new Button("Logout");
	    	logoutButton.setOnAction(event -> {
	    		//Redirect to login
	    		new UserLoginPage(new DatabaseHelper()).show(primaryStage); 
	    	});

	    layout.getChildren().addAll(userLabel,logoutButton); // Including logout button
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}


