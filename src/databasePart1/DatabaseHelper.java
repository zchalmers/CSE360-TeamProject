package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import application.User;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public User currentUser;
	
	public void connectToDatabase() throws SQLException {
		try {
			
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);

			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
		//statement.execute("DROP ALL OBJECTS");
		   // System.out.println("Database cleared successfully.");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "name VARCHAR(255), "
				+ "password VARCHAR(255), "
				+ "email VARCHAR(255), "
				+ "roles VARCHAR(70))";
		statement.execute(userTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE,"
	            + "generatedDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
	    statement.execute(invitationCodesTable);
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, name, password, email, roles) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUsername());
			pstmt.setString(2, user.getName());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getEmail());
			pstmt.setString(5, rolesSerial(user.getRoles()));
			pstmt.executeUpdate();
		}
	}
	
	public void updateRoles(String username, String roles) throws SQLException {
		String insertUser = "UPDATE cse360users SET roles = ? WHERE username = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, roles);    
			pstmt.setString(2, username);
			pstmt.executeUpdate();
		}
	}
	public User getUser(String username) throws SQLException {
		String query = "SELECT * FROM cse360users AS c WHERE c.username = ?	";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String name = rs.getString("name");
				String password = rs.getString("password");
				String email = rs.getString("email");
				List<String> roles = rolesDeserial(rs.getString("roles"));
				return new User(username, name, password, email, roles);
			}
		}
		return null;
	}
	
	// TODO: update for new user class
	public boolean addRoles(String username, String newRole) throws SQLException {
		String query = "SELECT * FROM cse360users AS c WHERE c.username = ?	";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
	        ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				String password = rs.getString("password");
				List<String> roles = rolesDeserial(rs.getString("roles"));
				if (!roles.contains(newRole)) {
					roles.add(newRole);
					
					String rolesString = rolesSerial(roles); 
					try {
						updateRoles(username, rolesString); // update for roles list, update register for rolesList
						return true;
					}
					catch (SQLException e) {
						System.out.println(e.getMessage() + "\n" + "ERROR IN ADDROLES/REGISTER");
						return false;
					}
					
				}
				else {
					System.out.println("ADDROLES: User already has this role");
					return false;
				}
				
			}
			System.out.println("ADDROLES: User was not found");
			return false;
		}
	}
	
	public boolean removeRoles(String username, String newRole) throws SQLException {
		String query = "SELECT * FROM cse360users AS c WHERE c.username = ?	";
		// NOW THAT WE HAVE CURRENTROLE COULD CHANGE THIS TO ONLY IF ADMIN AND ONLY ONE ADMIN 
		// TODO: 
		if (!username.equals(currentUser.getUsername())) {
			
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
	        
				if (rs.next()) {
					String password = rs.getString("password");
					List<String> roles = rolesDeserial(rs.getString("roles"));
				
					if (roles.contains(newRole)) {
						roles.remove(roles.indexOf(newRole));  // possible to delete last role
					
						String rolesString = rolesSerial(roles);
						try {
							updateRoles(username, rolesString);
							return true;
						}
						catch (SQLException e) {
							System.out.println(e.getMessage() + "\n" + "ERROR IN REMOVEROLES/REGISTER");
							return false;
						}
					}
					else {
						System.out.println("REMOVEROLES: User does not have this role");
						return false;
					}
				}
				else {
					System.out.println("REMOVEROLES: User was not found");
				}
			}
		}
		System.out.println("REMOVEROLES: You cannot remove your own roles");
		return false;
	}
	
	public boolean deleteUser(String username)  {
		if (!username.equals(currentUser.getUsername())) {
			
			String query = "DELETE FROM cse360users AS c WHERE c.username = ?";
			try (PreparedStatement pstmt = connection.prepareStatement(query)) {
				pstmt.setString(1, username);
	        
				if (pstmt.executeUpdate() > 0) {
					System.out.println("DELETEUSER: User successfully deleted");
					return true; 
				}
				System.out.println("DELETEUSER: User was not found");
				return false;
			}
		  catch (SQLException e) {
	            System.err.println("DELETEUSER: SQL Error - " + e.getMessage());
	            return false;
	        }
		}
		System.out.println("DELETEUSER: You cannot delete yourself");
		return false;
	}
	
	public List<User> getAllUsers() throws SQLException {
		String query = "SELECT * FROM cse360users";
		List<User> users = new ArrayList<>();
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery(); 
			
			while (rs.next()) {
				String username = rs.getString("username");
				String name = rs.getString("name");
				String password = rs.getString("password");
				String email = rs.getString("email");
				List<String> roles = rolesDeserial(rs.getString("roles"));
				
				User user = new User(username, name, password, email, roles);
				System.out.println("USERS: " + user.toString());
				users.add(user);
			}
		}
		return users;
	}
	
	// got rid of needing roles to login, not sure why as usernames have to be unique
	// Validates a user's login credentials.
	public User login(String username, String password) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? ";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					currentUser = getUser(username);
					return currentUser;
				}
				return null;
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public List<String> getUserRole(String userName) {
	    String query = "SELECT roles FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            return rolesDeserial(rs.getString("roles")); // Return the role if user exists
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // If no user exists or an error occurs
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode() {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code, generatedDate) VALUES (?, CURRENT_TIMESTAMP)";
	    System.out.println(code);
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE AND generatedDate >= DATEADD('MINUTE', -15, CURRENT_TIMESTAMP)";
		// If expiration date is changed, make sure to update expiration label in InvitationPage with new time limit
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private List<String> rolesDeserial(String roles) {
		// fix bug where it turns a  blank string and still makes  alist ouot of it 
		return new ArrayList<>(Arrays.asList(roles.split(",")));
	}
	
	private String rolesSerial(List<String> roles) {
		return String.join(",", roles);
	}
	public void setUserCurrentRole(String role) {
		currentUser.setCurrentRole(role);
	}
	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}

}
