package application;

import java.util.List;

/**
 * The User class represents a user entity in the system. It contains the user's
 * details such as userName, password, and role.
 */
public class User {
	private String userName;
	private String name;
	private String password;
	private String email;
	private String currentRole;
	private List<String> roles;
	
	// Flag will be true if one-time password is active on user
	private boolean otp;

	// Constructor to initialize a new User object with userName, password, and
	// role.
	public User(String userName, String name, String password, String email, List<String> roles, boolean otp) {
		this.userName = userName;
		this.name = name;
		this.password = password;
		this.email = email;
		this.roles = roles;
		this.otp = otp;
	}

	public User(String username, String name, String password, String email, String currentRole, List<String> roles, boolean otp) {

	}

	public void addRole(String role) {
		roles.add(role);
	}

	public String getUsername() {
		return this.userName;
	}

	public String getPassword() {
		return this.password;
	}

	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	public String getCurrentRole() {
		return this.currentRole;
	}

	public List<String> getRoles() {
		return this.roles;
	}
	
	public boolean getOTPFlag() {
		return this.otp;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCurrentRole(String role) {
		this.currentRole = role;
	}
	
	public void setOTPFlag(boolean flag) {
		this.otp = flag;
	}

	public String toString() {
		return String.format("USER: \n Username: %s, Name: %s, Email: %s, Roles: %s", userName, name, email, roles);
	}
}
