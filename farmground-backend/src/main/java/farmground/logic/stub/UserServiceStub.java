package farmground.logic.stub;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import farmground.logic.NewUserFormEntity;
import farmground.logic.UserEntity;
import farmground.logic.UserService;
import farmground.logic.exceptions.UserNotFoundException;


//@Service
public class UserServiceStub implements UserService {
	private Map<String, UserEntity> registeredUsers;
	private Set<String> confirmedUsers;
	private Set<String> loggedInUsers;
	
	@PostConstruct
	public void init() { 
		registeredUsers = Collections.synchronizedMap(new HashMap<>());
		confirmedUsers = Collections.synchronizedSet(new HashSet<>());
		loggedInUsers = Collections.synchronizedSet(new HashSet<>());
	}
	
	@Override
	public UserEntity getUser(String email) {
		UserEntity userEntity = registeredUsers.get(email);
		return userEntity;
	}

	@Override
	public void cleanup() {
		registeredUsers.clear();
		loggedInUsers.clear();
		confirmedUsers.clear();
	}


	@Override
	public UserEntity registerUser(NewUserFormEntity userForm) {
		if(isUserRegisterd(userForm.getEmail())) {
			throw new RuntimeException("The user is already registered with that email");
		}
		
		UserEntity userEntity = new UserEntity(userForm.getEmail(), 
									null, 
									userForm.getUsername(), 
									userForm.getAvatar(), 
									userForm.getRole(), 
									0L);

		registeredUsers.put(userEntity.getEmail(), userEntity);
		
		return userEntity;
	}
	
	@Override
	public UserEntity confirmUser(String playground, String email, String code) throws UserNotFoundException {		
		UserEntity userEntity = registeredUsers.get(email);
		
		if(userEntity == null)
			throw new UserNotFoundException("No such user");
		
		if(!checkCode(userEntity, code)) {
			throw new IllegalArgumentException("Wrong confirmation code");
		}
		
		if(isUserConfirmed(userEntity)) {
			throw new RuntimeException("This user: " + userEntity + " has already been confirmed");
		}
		
		confirmedUsers.add(email);
		userEntity.setPlayground(playground);
		
		return userEntity;
	}
	
	private boolean checkCode(UserEntity user, String code) {
		//TODO Implement
		return true;
	}

	public boolean isUserConfirmed(UserEntity entity) {
		return confirmedUsers.contains(entity.getEmail());
	}
	
	public boolean isUserRegisterd(String email) {
		return registeredUsers.containsKey(email);
	}
	
	public boolean isUserLoggedIn(String email) {
		return loggedInUsers.contains(email);
	}
	
	@Override
	public UserEntity loginUser(String email, String playground) throws Exception {
		UserEntity userEntity = registeredUsers.get(email);
		
		//
		if(userEntity == null || !userEntity.getPlayground().equals(playground)) {
			throw new UserNotFoundException("No such user or wrong playground");
		}
		
		if(isUserLoggedIn(email)) {
			throw new Exception("User " + userEntity+ " already logged in");
		}
		
		if(!isUserConfirmed(userEntity)) {
			throw new Exception("User " + userEntity + " was not confirmed yet"); 
		}
		
		loggedInUsers.add(email);
				
		return userEntity;
	}

	
	@Override
	public void updateUser(String email, UserEntity updatedUser) throws Exception {
		if(!isUserRegisterd(email)) {
			throw new Exception("Cannot update - No such user exists!");
		}
		UserEntity userEntity = registeredUsers.get(email);
		
		if(!isUserConfirmed(userEntity)) { 
			throw new Exception("User " + userEntity + " was not confirmed yet");
		}
		
		registeredUsers.put(email, updatedUser);
	}

	@Override
	public Long updatePoints(String email, Long points) {
		return 8L;
		
	}

}
