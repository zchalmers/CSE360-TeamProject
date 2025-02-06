package application;

import databasePart1.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
	 * @param databaseHelper An instance of DatabaseHelper to handle database
	 * operations.
	 * @param primaryStage   The primary stage where the scene will be displayed.
	 */
	public void show(Stage primaryStage) {

		// Label to display the title of the page
		Label userLabel = new Label("Invite");
		userLabel.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold;");

		// Button to generate the invitation code
		Button showCodeButton = new Button("Generate Invitation Code");
		showCodeButton.setStyle(
				"-fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black, gray; -fx-border-width: 2, 1;"
						+ "-fx-border-radius: 6, 5; -fx-border-inset: 0, 4;");

		// Set showCodeButton as default so Enter can activate
		showCodeButton.setDefaultButton(true);

		// Button to return to login screen
		Button quitButton = new Button("Back to Login");
		quitButton.setStyle(
				"-fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black, gray; -fx-border-width: 2, 1;"
						+ "-fx-border-radius: 6, 5; -fx-border-inset: 0, 4;");

		// Button to return to login screen
		Button homeButton = new Button("Admin Home");
		homeButton.setStyle(
				"-fx-text-fill: black; -fx-font-weight: bold; -fx-border-color: black, gray; -fx-border-width: 2, 1;"
						+ "-fx-border-radius: 6, 5; -fx-border-inset: 0, 4;");

		// Label to display the generated invitation code
		Label inviteCodeLabel = new Label("");
		inviteCodeLabel.setAlignment(Pos.CENTER);
		
		inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic; -fx-text-fill: black; -fx-font-weight: bold;");

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

		VBox layout = new VBox(10);
		layout.setMinSize(400, 220);
		layout.setMaxSize(400, 220);
		layout.setStyle("-fx-padding: 20; -fx-background-color: gray; -fx-background-radius: 100;"
				+ "-fx-background-insets: 4; -fx-border-color: gray, gray, black;"
				+ "-fx-border-width: 2, 2, 1; -fx-border-radius: 100, 100, 100;" + "-fx-border-insets: 0, 2, 4");
		layout.setAlignment(Pos.CENTER);

		HBox hbox = new HBox(5);		
		hbox.setStyle(" -fx-padding: 20;");
		hbox.setAlignment(Pos.CENTER);

		hbox.getChildren().addAll(homeButton, quitButton);
		layout.getChildren().addAll(userLabel, showCodeButton, inviteCodeLabel, hbox);
		
		StackPane root = new StackPane(layout);
		
		Scene inviteScene = new Scene(root, 940, 400);

		// Set the scene to primary stage
		primaryStage.setScene(inviteScene);
		primaryStage.setTitle("Invite Page");

	}
}
