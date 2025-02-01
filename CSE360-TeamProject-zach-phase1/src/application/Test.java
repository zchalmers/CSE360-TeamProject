package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javafx.application.Platform;

import java.sql.SQLException;
import java.util.List;

import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class Test {
	
	private final DatabaseHelper databaseHelper;
	//private final User user;

    public Test(DatabaseHelper databaseHelper) {// User user
        this.databaseHelper = databaseHelper;
        //this.user = user;
    }
    public void show( Stage primaryStage, User user) {
    	
    	VBox layout = new VBox(5);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    Label userLable = new Label("Roles:");
	    userLable.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    List<String> roles = user.getRoles();
	    StringBuilder userRoleBuilder = new StringBuilder();

	    for (String role : roles) {
	        userRoleBuilder.append(role).append("\n");
	    }

	    Label userRoleLabel = new Label(userRoleBuilder.toString());
	    userRoleLabel.setStyle("-fx-font-size: 16px;");
	    
	    ObservableList<String> options = FXCollections.observableArrayList("admin", "student", "instructor", "staff", "reviewer" );
	    @SuppressWarnings({ })
		final ComboBox<String> comboBox = new ComboBox<String>(options);
	   
	    Button addOrRemove = new Button("Add/Remove");
	    addOrRemove.setOnAction(a -> {
	    	try{
	    		String roleToAdd = (String)comboBox.getSelectionModel().getSelectedItem();
	    	if(!user.getRoles().contains(roleToAdd)) {
	    	//databaseHelper.addRoles(user.getUsername(), (String)comboBox.getSelectionModel().getSelectedItem());}
	    	//else{
		    	//databaseHelper.removeRoles(user.getUsername(), (String)comboBox.getSelectionModel().getSelectedItem());
	    		System.out.println(user.getRoles() + "\n" + roleToAdd);
	    		//System.out.println(!user.getRoles().contains(roleToAdd));
	    		databaseHelper.addRoles(user.getUsername(), roleToAdd);
	    		new AdminHomePage(databaseHelper).show(primaryStage); // Exit the JavaFX application
	    	}
	    	else {
	    		System.out.println("It is not a role");
	    		databaseHelper.removeRoles(user.getUsername(), roleToAdd);
	    	}
	    	}
	    	catch (SQLException e) {
		    	System.out.println("Should print this after trying to add the role since I don't know the format for role");
		    }
	    });
	    
	    // Button to quit the application
	    Button goBack = new Button("Go Back");
	    goBack.setOnAction(a -> {
	    	new AdminHomePage(databaseHelper).show(primaryStage); // Exit the JavaFX application
	    });
	    
	    // "Invite" button for admin to generate invitation codes
	    

	    layout.getChildren().addAll(userLable, userRoleLabel,comboBox,addOrRemove, goBack);
	    Scene changeUserScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(changeUserScene);
	    primaryStage.setTitle("Change User Role");
    }
}