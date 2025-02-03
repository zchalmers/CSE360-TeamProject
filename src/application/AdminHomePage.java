package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// adding comments for testing

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private final DatabaseHelper helper;
	
	public AdminHomePage(DatabaseHelper helper) {
		this.helper = helper;
	}
	
	
    public void show(Stage primaryStage, User user) {
    	TableView<User> table = new TableView<>();
    	    
	    List<User> users = new ArrayList<>();
	    
	    try {
	    	users = helper.getAllUsers();
	    	
	    } catch (SQLException e) {
	    	System.out.println("Should never reach here, can't get all users");
	    }
	    
	    
	    TableColumn<User, String> usernames = new TableColumn<>("Username");
        usernames.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<User, String> names = new TableColumn<>("Name");
        names.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> emails = new TableColumn<>("Email");
        emails.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> roles = new TableColumn<>("Roles");
        roles.setCellValueFactory(new PropertyValueFactory<>("roles"));
        
        // Add columns to the table
        table.getColumns().addAll(usernames, names, emails, roles);
    	
    	System.out.println("USERS:" + users);
    	
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        table.setItems(userObservableList);
	    
	    
        Button backButton = new Button("Back to login");
        backButton.setOnAction(a -> {
        	new UserLoginPage(helper).show(primaryStage);
        });

	// "Invite" button for admin to generate invitation codes
	Button inviteButton = new Button("Invite");
	    
	inviteButton.setOnAction(a -> {
	new InvitationPage().show(helper, primaryStage);
	});
	    
        
	    // PUT BUTTONS HERE 
	    
	    TableColumn<User, Void> deleteColumn = new TableColumn<>("Delete User");

	    deleteColumn.setCellFactory(tc -> new TableCell<>() {
	        private final Button button = new Button("Delete");
	        
	        {   
	        	button.setOnAction(event -> {
	                User user = getTableView().getItems().get(getIndex());
	                
	                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	                alert.setTitle("Confirm Deletion");
	                alert.setHeaderText("Are you sure you want to delete this user?");
	                alert.setContentText("User: " + user.getUsername());

	                Optional<ButtonType> result = alert.showAndWait();
	                
	                if (result.isPresent() && result.get() == ButtonType.OK) {
	                	
	                	if (helper.deleteUser(user.getUsername())) {
	                		System.out.println("User deleted: " + user.getUsername());

	                		getTableView().getItems().remove(user);
	                        
	                		//getTableView().refresh();

	                	}
	                }
	            });
	        }
	        
	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                setGraphic(button);
	            }
	        }
	        
	    });
	    
	    TableColumn<User, Void> changeRole = new TableColumn<>("Change Role");
	    changeRole.setCellFactory(tc -> new TableCell<>() {
	        private final Button button = new Button("Change");
	        
	        {   
	        	button.setOnAction(event -> {
	        		User personBeingChanged = getTableView().getItems().get(getIndex());
	        		new EditRolesPage(helper).show(primaryStage,personBeingChanged);
	                
	            });
	        }
	        
	        @Override
	        protected void updateItem(Void item, boolean empty) {
	            super.updateItem(item, empty);
	            if (empty) {
	                setGraphic(null);
	            } else {
	                setGraphic(button);
	            }
	        }
	        
	    });
	    
	    table.getColumns().add(changeRole);
	    table.getColumns().add(deleteColumn);
	    
	    VBox vbox = new VBox(table);
	    vbox.getChildren().addAll(backButton, inviteButton);
        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX TableView with List<Users>");
        primaryStage.show();
        
 
    }
       
}
