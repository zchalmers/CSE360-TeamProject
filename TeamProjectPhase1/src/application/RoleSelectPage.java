package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import javafx.geometry.Pos;

/**
 * RoleSelectPage class represents the user interface for the role-selection
 * page. This page displays only if a user has multiple roles and allows the
 * user to select which role they which to play.
 */
public class RoleSelectPage {
	/**
	 * Displays the role-selection page.
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
	private final DatabaseHelper databaseHelper;
	String selectedRole;
	User user;

	public RoleSelectPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
		user = databaseHelper.currentUser;
	}

	public void show(Stage primaryStage) {

		// Create Next and Back buttons
		Button nextButton = new Button("Next");
		Button quitButton = new Button("Back");

		// Set nextButton to default to allow Enter button to activate
		nextButton.setDefaultButton(true);

		// Create layout for buttons and combobox
		VBox layoutV = new VBox();
		layoutV.setStyle("-fx-alignment: center; -fx-padding: 20;");
		HBox layoutH = new HBox(10);
		layoutH.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Create combobox for selecting roles
		ComboBox<String> comboBox = new ComboBox<>();

		// Add roles text to comboBox
		comboBox.getItems().addAll(user.getRoles());

		// Set default comboBox text
		comboBox.setPromptText("Please select a role");

		// ComboBox allows selection of roles. Deactivates Next button until selection
		// is made
		comboBox.setOnAction(a -> {
			selectedRole = comboBox.getValue();
			if (selectedRole != null) {
				nextButton.setDisable(false);
			} else {
				nextButton.setDisable(true);
			}
		});

		// Next button to proceed with selection. Is inactive unless selection is made
		// in ComboBox. Based on role selected, go to that role's home page.
		nextButton.setOnAction(a -> {
			// Set currentRole in databaseHelper
			databaseHelper.currentUser.setCurrentRole(selectedRole);

			// Direct user to role's home page
			switch (selectedRole) {
			case "Admin":
				new AdminHomePage(databaseHelper).show(primaryStage, user);
				break;
			case "Student":
				new StudentHomePage(databaseHelper).show(primaryStage);
				break;
			case "Instructor":
				new InstructorHomePage(databaseHelper).show(primaryStage);
				break;
			case "Staff":
				new StaffHomePage(databaseHelper).show(primaryStage);
				break;
			case "Reviewer":
				new ReviewerHomePage(databaseHelper).show(primaryStage);
				break;
			}
		});

		// Quit button to return to User Login Screen
		quitButton.setOnAction(a -> {
			new UserLoginPage(databaseHelper).show(primaryStage);

		});

		// Attach nextButton and quitButton to the same container
		layoutH.getChildren().addAll(nextButton, quitButton);

		// Attach buttons and combobox to the same container
		layoutV.getChildren().addAll(comboBox, layoutH);

		Scene roleSelectScene = new Scene(layoutV, 940, 400);

		// Set the scene to primary stage
		primaryStage.setScene(roleSelectScene);
		primaryStage.setTitle("Role-Selection Page");
		
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
	}
}
