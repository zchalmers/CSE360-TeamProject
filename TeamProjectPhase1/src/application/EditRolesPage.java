package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//import javafx.application.Platform;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import databasePart1.*;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or
 * quit the application.
 */
public class EditRolesPage {

	private final DatabaseHelper databaseHelper;
	private User user;

	public EditRolesPage(DatabaseHelper databaseHelper) {// User user
		this.databaseHelper = databaseHelper;
		// this.user = user;
	}

	public void show(Stage primaryStage, User user) {
		this.user = user;
		VBox layout = new VBox(5);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		Label userLable = new Label("Current Roles:");
		userLable.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		List<String> roles = user.getRoles();
		StringBuilder userRoleBuilder = new StringBuilder();

		for (String role : roles) {
			userRoleBuilder.append(role).append("\n");
		}

		Label userRoleLabel = new Label(userRoleBuilder.toString());
		userRoleLabel.setStyle("-fx-font-size: 16px;");

		ObservableList<String> options = FXCollections.observableArrayList("Admin", "Student", "Instructor", "Staff",
				"Reviewer");
		@SuppressWarnings({})
		final ComboBox<String> comboBox = new ComboBox<String>(options);

		Button addOrRemove = new Button("Add/Remove");
        
        comboBox.setOnAction(a -> {
            String check = comboBox.getValue();
            addOrRemove.setDisable(check == null);
        });
        addOrRemove.setOnAction(a -> {
        	try {
                String roleToAdd = comboBox.getSelectionModel().getSelectedItem();
                if (roleToAdd == null) {
                    return;
                }
                if (!user.getRoles().contains(roleToAdd)) {
                    System.out.println(user.getRoles() + "\n" + roleToAdd);
                    // System.out.println(!user.getRoles().contains(roleToAdd));
                    databaseHelper.addRoles(user.getUsername(), roleToAdd);
                    new EditRolesPage(databaseHelper).show(primaryStage, user); // Exit
                    } else {
                        if(roleToAdd.equals("Admin")) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("STOP!!");
                            alert.setHeaderText("You cannot delete the 'Admin' role");
                            alert.setContentText("An admin role cannot be deleted");

                            Optional<ButtonType> result = alert.showAndWait();
                        }
                        else {
                    System.out.println("User has this role, deleting role. ");
                    databaseHelper.removeRoles(user.getUsername(), roleToAdd);
                    new EditRolesPage(databaseHelper).show(primaryStage, user); // Exit
                        }
                }
            } catch (SQLException e) {
                System.out.println(
                        "Should print this after trying to add the role since I don't know the format for role");
            }
        });

		// Button to quit the application
		Button goBack = new Button("Go Back");
		goBack.setOnAction(a -> {
			new AdminHomePage(databaseHelper).show(primaryStage, user); // Exit the JavaFX application
		});

		// "Invite" button for admin to generate invitation codes

		// Center the text in the dropdown list of the comboBox
		comboBox.setCellFactory(a -> new ListCell<String>() {
			@Override
			protected void updateItem(String role, boolean flag) {
				super.updateItem(role, flag);

				if (!flag && role != null) {
					setText(role.substring(0, 1).toUpperCase() + role.substring(1));
					setAlignment(Pos.CENTER);
				}

			}
		});
		
		
		
		// Center the text in the comboBox selection
		comboBox.setButtonCell(new ListCell<String>() {
			@Override
			protected void updateItem(String role, boolean flag) {
				super.updateItem(role, flag);

				if (!flag && role != null) {
					setText(role.substring(0, 1).toUpperCase() + role.substring(1));
					setAlignment(Pos.CENTER);
				}

			}
		});

		layout.getChildren().addAll(userLable, userRoleLabel, comboBox, addOrRemove, goBack);
		Scene changeUserScene = new Scene(layout, 800, 400);

		// Set the scene to primary stage
		primaryStage.setScene(changeUserScene);
		primaryStage.setTitle("Change User Role");
	}
}
