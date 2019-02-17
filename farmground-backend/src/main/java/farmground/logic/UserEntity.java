package farmground.logic;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERS")
public class UserEntity {
	
	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private Long points;
	
	private boolean isConfirmed;
	private String confirmationCode;
	
	public UserEntity() {

	}


	public UserEntity(String email, String playground, String username, String avatar, String role, Long points) {
		this();
		setEmail(email);
		setPlayground(playground);
		setUsername(username);
		setAvatar(avatar);
		setRole(role);
		setPoints(points);
	}
	
	@Id
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPlayground() {
		return playground;
	}
	public void setPlayground(String playground) {
		this.playground = playground;
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
	public Long getPoints() {
		return points;
	}
	public void setPoints(Long points) {
		this.points = points;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	
	public String getConfirmationCode() {
		return confirmationCode;
	}


	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", playground=" + playground + ", username=" + username + ", avatar="
				+ avatar + ", role=" + role + ", points=" + points + "]";
	}
	
	
}
