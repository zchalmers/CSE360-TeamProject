package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;

/**
 * AdminPage class represents the user interface for the admin user. This page
 * displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
	 * Displays the admin page in the provided primary stage.
	 * 
	 * @param primaryStage The primary stage where the scene will be displayed.
	 */
	private final DatabaseHelper databaseHelper;

	public AdminHomePage(DatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
	}

	public void show(Stage primaryStage, User user) {
		TableView<User> table = new TableView<>();
		List<User> users = new ArrayList<>();

		try {
			users = databaseHelper.getAllUsers();
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
		// roles.setCellValueFactory(new PropertyValueFactory<>("roles"));
		
		roles.setCellValueFactory(cellData -> {
			List<String> listOfRoles = cellData.getValue().getRoles();
			
			String displayRoles;
			if (listOfRoles == null || listOfRoles.isEmpty()) {
				displayRoles = "";
			} else {					
				displayRoles = String.join(", ",  listOfRoles);
			}
			return new SimpleStringProperty(displayRoles);
		});

		// Add columns to the table
		table.getColumns().addAll(usernames, names, emails, roles);

		System.out.println("USERS:" + users);

		ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
		table.setItems(userObservableList);

		// Create backButton to return to login screen
		Button backButton = new Button("Back to login");
		backButton.setOnAction(a -> {
			new UserLoginPage(databaseHelper).show(primaryStage);
		});

		// Create inviteButton for admin to generate invitation codes
		Button inviteButton = new Button("Invite");
		inviteButton.setOnAction(a -> {
			new InvitationPage(databaseHelper).show(primaryStage);
		});

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

						if (databaseHelper.deleteUser(user.getUsername())) {
							System.out.println("User deleted: " + user.getUsername());
							getTableView().getItems().remove(user);
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

		// Add and populate Change Role column with comboBox and button to add/remove
		// roles
		TableColumn<User, Void> changeRole = new TableColumn<>("Change Role");
		changeRole.setCellFactory(tc -> new TableCell<>() {
			private final ComboBox<String> comboBox = new ComboBox<>();
			private final Button addOrRemove = new Button("Add/Remove");
			private final HBox hbox = new HBox(5, comboBox, addOrRemove);

			{
				// Populate comboBox
				comboBox.getItems().addAll("Admin", "Student", "Instructor", "Staff", "Reviewer");
				comboBox.setPromptText("Select a role");

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

				// Disable add/remove button unless a role selection is made
				comboBox.setOnAction(a -> {
					String check = comboBox.getValue();
					addOrRemove.setDisable(check == null);

				});

				// addOrRemove button will remove role if user has it or add it if they don't
				addOrRemove.setOnAction(a -> {
					try {
						User user = getTableView().getItems().get(getIndex());
						String roleToAdd = comboBox.getSelectionModel().getSelectedItem();
						if (roleToAdd == null) {
							return;
						}
						if (!user.getRoles().contains(roleToAdd)) {
							if (user.getRoles() == null || user.getRoles().isEmpty()) {
								databaseHelper.updateRoles(user.getUsername(), roleToAdd);
							}
							System.out.println(user.getRoles() + "\n" + roleToAdd);
							// System.out.println(!user.getRoles().contains(roleToAdd));
							databaseHelper.addRoles(user.getUsername(), roleToAdd);
							new AdminHomePage(databaseHelper).show(primaryStage, user);
						} else {
							if (roleToAdd.equals("Admin")) {
								Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
								alert.setTitle("STOP!!");
								alert.setHeaderText("You cannot delete the 'Admin' role");
								alert.setContentText("An admin role cannot be deleted");

								Optional<ButtonType> result = alert.showAndWait();
							} else {
								System.out.println("User has this role, deleting role. ");
								databaseHelper.removeRoles(user.getUsername(), roleToAdd);
								new AdminHomePage(databaseHelper).show(primaryStage, user);
							}
						}
					} catch (SQLException e) {
						System.out.println(
								"Should print this after trying to add the role since I don't know the format for role");
					}
				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(hbox);
				}
			}
		});

		// Add and populate Forgot Password column
		TableColumn<User, Void> tempPassword = new TableColumn<>("Forgot Password");
		tempPassword.setCellFactory(tc -> new TableCell<>() {
			private final Button button = new Button("One Time Password");

			{
				button.setOnAction(event -> {
					User user = getTableView().getItems().get(getIndex());

					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setTitle("Confirm OTP");
					alert.setHeaderText("You are about to generate a One Time Password for this user!");
					alert.setContentText("User: " + user.getUsername());

					Optional<ButtonType> result = alert.showAndWait();

					if (result.isPresent() && result.get() == ButtonType.OK) {
						String OTP = databaseHelper.generateOneTimePassword();

						Alert otpConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
						otpConfirmationAlert.setTitle("Confirm OTP");
						otpConfirmationAlert.setHeaderText("Your ONE TIME PASSWORD is " + OTP);
						otpConfirmationAlert.setContentText("Please write this down and do not share it with anyone!");

						Optional<ButtonType> otpResult = otpConfirmationAlert.showAndWait();

						if (otpResult.isPresent() && otpResult.get() == ButtonType.OK) {
							Alert finalConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
							finalConfirmationAlert.setTitle("Confirm OTP");
							finalConfirmationAlert.setHeaderText("By clicking OK you acknowledge that you have "
									+ "properly recorded and stored the OTP for this user. \nOnce completed this process cannot be undone!\n"
									+ "If the OTP is lost, another OTP must be generated to allow access to this user's account.");
							finalConfirmationAlert.setContentText("Please Confirm");

							Optional<ButtonType> finalResult = finalConfirmationAlert.showAndWait();

							if (finalResult.isPresent() && finalResult.get() == ButtonType.OK) {
								user.setPassword(OTP);
								databaseHelper.updatePassword(user.getUsername(), OTP);
								user.setOTPFlag(true);
								databaseHelper.updateOTPFlag(user.getUsername(), true);
								System.out.println(user.getPassword()); // Debug
							}
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

		table.getColumns().add(changeRole);
		table.getColumns().add(deleteColumn);
		table.getColumns().add(tempPassword);

		HBox hbox = new HBox(5, backButton, inviteButton);
		VBox vbox = new VBox(table);
		vbox.getChildren().addAll(hbox);
		Scene scene = new Scene(vbox, 940, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("JavaFX TableView with List<Users>");
		primaryStage.show();
	}
}
