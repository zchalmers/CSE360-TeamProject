package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.*;

/**
 * The SetupAdmin class handles the setup process for creating an administrator account.
 * This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {
	
    private final DatabaseHelper databaseHelper;

    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	// Input fields for userName and password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin userName");
        userNameField.setMaxWidth(250);

        TextField nameField = new TextField();
        nameField.setPromptText("Enter Admin Name");
        nameField.setMaxWidth(250);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Enter Admin email");
        emailField.setMaxWidth(250);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        Button setupButton = new Button("Setup");
        setupButton.setDefaultButton(true);
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");
        
        setupButton.setOnAction(a -> {
        	// Retrieve user input
            String userName = userNameField.getText();
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            String userNameValidate = UserNameRecognizer.checkForValidUserName(userName); // Using FSM to validate Username
            String passwordValidate = PasswordEvaluator.evaluatePassword(password);       // Using FSM to validate Password
            
            if (!userNameValidate.isEmpty()) {  // if there is no error then username is valid
            	userNameValidate = "Username Error:" + userNameValidate;   // make it more descriptive
            	errorLabel.setText(userNameValidate);      // set the errorlabel for username error
            	return;
            }
            
            if (!passwordValidate.isEmpty()) {
            	passwordValidate = "Password Error: \n" + passwordValidate;
            	errorLabel.setText(passwordValidate);
        		return;
            }
            
            try {
            	// Create a new User object with admin role and register in the database
            	List<String> roles = new ArrayList<>();
            	roles.add("Admin");
            
            	User user= new User(userName, name, password, email, roles);
                databaseHelper.register(user);
                System.out.println("Administrator setup completed.");
                
                // Navigate to the Welcome Login Page
                new UserLoginPage(databaseHelper).show(primaryStage);
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        VBox layout = new VBox(10, userNameField, nameField, emailField, passwordField, setupButton, errorLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}

