package farmground.logic;

import farmground.logic.exceptions.UserNotFoundException;

public interface UserService {
	
	UserEntity getUser(String email);
	
	UserEntity registerUser(NewUserFormEntity userForm);
	
	UserEntity loginUser(String email, String playground) throws UserNotFoundException, Exception;
	
	UserEntity confirmUser(String playground, String email, String code) throws UserNotFoundException;
	
	void updateUser(String email, UserEntity updatedUser) throws Exception;
	
	Long updatePoints(String email, Long points);
	
	void cleanup();
}
