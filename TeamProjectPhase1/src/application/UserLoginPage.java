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
    	switch (user.getRoles().get(0).toLowerCase()) {
    	case "admin":
    		databaseHelper.setUserCurrentRole("admin");
    		new AdminHomePage(databaseHelper).show(primaryStage, user);    		
    		break;
    		
    	case "student":
    		databaseHelper.setUserCurrentRole("student");
    		new StudentHomePage(databaseHelper).show(primaryStage);
    		break;
    	
    	case "instructor":
    		databaseHelper.setUserCurrentRole("instructor");
    		new InstructorHomePage(databaseHelper).show(primaryStage);
    		break;
    	
    	case "staff":
    		databaseHelper.setUserCurrentRole("staff");
    		new StaffHomePage(databaseHelper).show(primaryStage);
    		break;
    	
    	case "reviewer":
    		databaseHelper.setUserCurrentRole("reviewer");
    		new ReviewerHomePage(databaseHelper).show(primaryStage);
    		break;
    		
    	default:
    		databaseHelper.setUserCurrentRole("user");
            new UserHomePage(user).show(primaryStage);
            break;
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
	Button setupButton = new Button("Register New User");
	    
	// Set login button as default to allow pressing Enter to activate    
	loginButton.setDefaultButton(true);
        
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
            	
            	User user = databaseHelper.login(userName, password);
            	
            	if (user != null) {
            		if (user.getRoles().size() > 1) {
            			new RoleSelectPage(databaseHelper).show(primaryStage);
            		}
            		else {
            			roleHomePage(user, primaryStage);
            		}
            	}
            	
            	else {
        			 //Display an error if the login fails
                   errorLabel.setText("Error logging in");
        		}
//            	User user=new User(userName, password, "");
//            	
//            	// Retrieve the user's role from the database using userName
//            	String role = databaseHelper.getUserRole(userName);
//            	
//            	if(role!=null) {
//            		if(databaseHelper.login(user)) {
//            			if (databaseHelper.currentUser != null && databaseHelper.currentUser.getRoles().size() == 1) {
//            				// If the user has only one role, go directly to the home page
//            				roleHomePage(databaseHelper.currentUser, primaryStage);
//					
//            			} else if (databaseHelper.currentUser != null && databaseHelper.currentUser.getRoles().size() > 1) {
//            				//If multiple roles, go to role selection
//            				new RoleSelectPage(databaseHelper).show(primaryStage);
//					
//            			} else {
//            				// Display an error if role information is invalid
//            				errorLabel.setText("Error retrieving user roles.");
//            			}		
//            		}
//            		else {
//            			// Display an error if the login fails
//                        errorLabel.setText("Error logging in");
//            		}
//            	}
//            	else {
//            		// Display an error if the account does not exist
//                    errorLabel.setText("user account doesn't exists");
//            	}
            	
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                e.printStackTrace();
            } 
        });

	setupButton.setOnAction(a -> {
        	new SetupAccountPage(databaseHelper).show(primaryStage);
        });    

        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, setupButton, errorLabel);

        primaryStage.setScene(new Scene(layout, 940, 400));
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
