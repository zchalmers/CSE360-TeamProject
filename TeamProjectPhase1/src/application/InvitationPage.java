package application;

import databasePart1.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * InvitePage class represents the page where an admin can generate an
 * invitation code. The invitation code is displayed upon clicking a button.
 */
public class InvitationPage {
	
	private final DatabaseHelper databaseHelper;

	public InvitationPage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}
	/**
	 * Displays the Invite Page in the provided primary stage.
	 * 
	 * @param databaseHelper An instance of DatabaseHelper to handle database
	 *                       operations.
	 * @param primaryStage   The primary stage where the scene will be displayed.
	 */
	public void show(Stage primaryStage) {
		VBox layout = new VBox(10);
		HBox hbox = new HBox(5);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
		hbox.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Label to display the title of the page
		Label userLabel = new Label("Invite ");
		userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		// Button to generate the invitation code
		Button showCodeButton = new Button("Generate Invitation Code");

		// Set showCodeButton as default so Enter can activate
		showCodeButton.setDefaultButton(true);

		// Button to return to login screen
		Button quitButton = new Button("Back to Login");
		
		// Button to return to login screen
		Button homeButton = new Button("Home");

		// Label to display the generated invitation code
		Label inviteCodeLabel = new Label("");
		;
		inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

		showCodeButton.setOnAction(a -> {
			// Generate the invitation code using the databaseHelper and set it to the label
			String invitationCode = databaseHelper.generateInvitationCode();
			inviteCodeLabel.setText(invitationCode + " - Code will expire in 15 minutes.");
		});

		quitButton.setOnAction(a -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});
		
		homeButton.setOnAction(a -> {
			new AdminHomePage(databaseHelper).show(primaryStage, databaseHelper.currentUser);
		});
		
		hbox.getChildren().addAll(homeButton, quitButton);
		layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel, hbox);
		Scene inviteScene = new Scene(layout, 940, 400);

		// Set the scene to primary stage
		primaryStage.setScene(inviteScene);
		primaryStage.setTitle("Invite Page");

	}
}
