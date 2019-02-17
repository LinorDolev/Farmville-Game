package farmground.layout;

import farmground.logic.UserEntity;

public class UserTO {

	private String email;
	private String playground;
	private String username;
	private String avatar;
	private String role;
	private Long points;

	public UserTO() {
		
	}

	public UserTO(UserEntity user) {
		setEmail(user.getEmail());
		setPlayground(user.getPlayground());
		setUsername(user.getUsername());
		setAvatar(user.getAvatar());
		setRole(user.getRole());
		setPoints(user.getPoints());
	}

	public UserTO(String email, String playground, String username, String avatar, String role, Long points) {
		this();
		setEmail(email);
		setPlayground(playground);
		setUsername(username);
		setAvatar(avatar);
		setRole(role);
		setPoints(points);
	}

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

	public UserEntity toEntity() {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(getEmail());
		userEntity.setPlayground(getPlayground());
		userEntity.setUsername(getUsername());
		userEntity.setAvatar(getAvatar());
		userEntity.setRole(getRole());
		userEntity.setPoints(getPoints());
		
		return userEntity;
	}

	@Override
	public String toString() {
		return String.format("UserTO [email=%s, playground=%s, username=%s, avatar=%s, role=%s, points=%s]", email,
				playground, username, avatar, role, points);
	}
	
	
}
