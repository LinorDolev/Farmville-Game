package farmground.logic;

public class NewUserFormEntity {

	private String email;
	private String username;
	private String avatar;
	private String role;
	private String playground;
	
	public NewUserFormEntity() {
		
	}
	
	public NewUserFormEntity(String email, String username, String avatar, String role, String playground) {
		this();
		setEmail(email);
		setUsername(username);
		setAvatar(avatar);
		setRole(role);
		setPlayground(playground);
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "NewUserFormEntity [email=" + email + ", username=" + username + ", avatar=" + avatar + ", role=" + role
				+ "]";
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}
	
	
}
