package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

import databasePart1.*;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {
	
    private final DatabaseHelper databaseHelper;

    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }
    
    //Method to include direction to the necessary role pages
    private void roleHomePage(User user, Stage primaryStage) {
    	switch (user.getRole().toLowerCase()) {
    	case "admin":
    		new AdminHomePage(databaseHelper).show(primaryStage);
    		break;
    		
    	case "student":
    		new StudentHomePage().show(primaryStage);
    		break;
    	
    	case "instructor":
    		new InstructorHomePage().show(primaryStage);
    		break;
    	
    	case "staff":
    		new StaffHomePage().show(primaryStage);
    		break;
    	
    	case "reviewer":
    		new ReviewerHomePage().show(primaryStage);
    	}
    }

    public void show(Stage primaryStage) {
    	// Input field for the user's userName, password
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter userName");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);
        
        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");


        Button loginButton = new Button("Login");
        
        loginButton.setOnAction(a -> {
        	// Retrieve user inputs
            String userName = userNameField.getText();
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
            	User user=new User(userName, password, "");
            	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
            	
            	// Retrieve the user's role from the database using userName
            	String role = databaseHelper.getUserRole(userName);
            	
            	if(role!=null) {
            		user.setRole(role);
            		if(databaseHelper.login(user)) {
            			int roleCount = databaseHelper.getUserRolesCount(userName); //Count roles
            			welcomeLoginPage.show(primaryStage,user);
            			if (roleCount > 1) {
            				// If multiple roles, go to RoleSelectPage
            				new RoleSelectPage(databaseHelper).show(primaryStage);
            			} else if (roleCount == 1) {
            				//If the user has only role, go to their home page
            				roleHomePage(user, primaryStage);
       
            			}
            		}
            		else {
            			// Display an error if the login fails
                        errorLabel.setText("Error logging in");
            		}
            	}
            	else {
            		// Display an error if the account does not exist
                    errorLabel.setText("user account doesn't exists");
            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 800, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
