package farmground.layout;

import farmground.logic.NewUserFormEntity;

public class NewUserFormTO {

	private String email;
	private String username;
	private String avatar;
	private String role;
	private String playground;
	
	public NewUserFormTO() {
		
	}
	
	public NewUserFormTO(NewUserFormEntity newUserFormEntity) {
		this();
		setEmail(newUserFormEntity.getEmail());
		setUsername(newUserFormEntity.getUsername());
		setAvatar(newUserFormEntity.getAvatar());
		setRole(newUserFormEntity.getRole());
		setPlayground(newUserFormEntity.getPlayground());
	}
	
	public NewUserFormTO(String email, String username, String avatar, String role, String playground) {
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
	
	public NewUserFormEntity toEntity() {
		NewUserFormEntity newUserFormEntity = new NewUserFormEntity();
		newUserFormEntity.setEmail(email);
		newUserFormEntity.setUsername(username);
		newUserFormEntity.setAvatar(avatar);
		newUserFormEntity.setRole(role);
		newUserFormEntity.setPlayground(playground);
		
		return newUserFormEntity;
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}
}
