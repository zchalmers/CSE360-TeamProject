package application;

import java.util.List;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String name;
    private String password;
    private String role;
    private String email;
    private List<String> roles;
    
    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
    
    public User( String userName, String password, List<String> roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
    // Sets the role of the user.
    public void setRole(String role) {
    	this.role=role;
    }
    
    public void addRole(String role) {
    	roles.add(role);
    }

    public String getUsername() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email;}
    public String getName() { return name; }
    public List<String> getRoles() { return roles;}
}
