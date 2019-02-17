package farmground.logic.data;


import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import farmground.dal.UserDao;
import farmground.logic.NewUserFormEntity;
import farmground.logic.UserEntity;
import farmground.logic.UserService;
import farmground.logic.exceptions.UserNotFoundException;
import farmground.mail.MailService;

@Service
public class JpaUserService implements UserService {

	private UserDao userDao;
	private Set<String> loggedInUsers;
	
	private MailService mailService;
	
	@Value("${server.port}")	
	private int port;
	
	@Value("${server.address}")
	private String ip;
	
	@Autowired
	public JpaUserService(UserDao userDao, MailService mailService) {
		loggedInUsers = Collections.synchronizedSet(new HashSet<>());
		setUserDao(userDao);
		setMailService(mailService);
	}
	
	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
		
	@Transactional
	@Override
	public UserEntity registerUser(NewUserFormEntity userForm) {

		if(isUserRegistered(userForm.getEmail())) {
			throw new RuntimeException("Cannot register: The user is already registered with that email");
		}		
		
		//create new user in DB
		UserEntity userEntity = new UserEntity(userForm.getEmail(), 
									userForm.getPlayground(), 
									userForm.getUsername(), 
									userForm.getAvatar(), 
									userForm.getRole(), 
									0L);
		
		
		

		Random random = new Random();
		String code = random.nextLong() + "";
		userEntity.setConfirmationCode(code);
		
		Executors.newSingleThreadExecutor().execute(() -> sendConfirmationMail(userEntity, code));
		
		return userDao.save(userEntity);
	}
	
	
	private void sendConfirmationMail(UserEntity userEntity, String code) {
		try {	
			String link = "http://"+ip +":"+port+ "/playground/users/confirm/"+ userEntity.getPlayground()+ "/" + userEntity.getEmail()+ "/" + code;	
			
			String emailBody = "<h1>Hello " + userEntity.getUsername()+ ", Welcome to Farmground!</h1> "
					+ "<br/> "
					+ "<p>Click " 
					+ "<a href='"+ link+"'>here</a>"+" to activate your account on farmground:"
					+ "</p>"
					+ "<a href='"+ link+"'>Confirm thats your email</a>";
			mailService.send("Farmground Email Confirmation", emailBody, userEntity.getEmail());
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Transactional
	@Override
	public UserEntity confirmUser(String playground, String email, String code) throws UserNotFoundException {
		
		Optional<UserEntity> userOptional = userDao.findById(email);
		
		if(!userOptional.isPresent()) {
			throw new RuntimeException("Cannot confirm user: user with that email was never registered:" +  email);
		}
		
		UserEntity user = userOptional.get();
		
		if(user.isConfirmed()) {
			throw new RuntimeException("Cannot confirm user: user was already confirmed: " + email);
		}
		
		if(!user.getPlayground().equals(playground)) {
			throw new RuntimeException("Cannot confirm user: wrong playground: " + playground);

		}
		
		if(!checkCode(user, code)) {
			throw new RuntimeException("Cannot confirm user: wrong confirmation code:" +  code);
		}
		
		user.setConfirmed(true);

		userDao.save(user);
		
		return user;
	}

	@Transactional
	@Override
	public UserEntity loginUser(String email, String playground) throws UserNotFoundException, Exception {
			
		Optional<UserEntity> optionalUser = userDao.findById(email);
		
		if(!optionalUser.isPresent()) {
			throw new RuntimeException("Cannot log in: No user with that email: " + email);
		}
		
		UserEntity user = optionalUser.get();
		
		if(!user.isConfirmed()) {
			throw new RuntimeException("Cannot log in: email was not confirmed yet: " + email);
		}

		
		loggedInUsers.add(email);
		
		return user;
	}

	@Transactional
	@Override
	public void updateUser(String email, UserEntity updatedUser) throws Exception {	
		
		Optional<UserEntity> userOP = userDao.findById(email);
		if(!userOP.isPresent())
			throw new RuntimeException("Cannot update user: user was not exist " + email);
		
		UserEntity user = userOP.get();
		
		if(!user.isConfirmed()) { 
			System.err.println("IN NOT CONFIRMED: "+ user.toString());
			throw new RuntimeException("Cannot update user: user was not confirmed yet: " + email);			
		} 
		
		if(user.getRole() != null && !user.getRole().equals(updatedUser.getRole())) {
			user.setRole(updatedUser.getRole());
			System.err.println("IN EQUAL ROLE: "+ user.toString());
		}
		
		if(user.getAvatar() != null && !user.getAvatar().equals(updatedUser.getAvatar())) {
			user.setAvatar(updatedUser.getAvatar());
		}
		
		if(user.getPoints() != null && !user.getPoints().equals(updatedUser.getPoints())) {
			user.setPoints(updatedUser.getPoints());
		}
		
		if(user.getUsername()!= null && !user.getUsername().equals(updatedUser.getUsername())) {
			user.setUsername(updatedUser.getUsername());
		}
		
		userDao.save(user);
	}

	@Transactional(readOnly = true)
	@Override
	public UserEntity getUser(String email) {
		
		Optional<UserEntity> userEntityOptional = userDao.findById(email);
		if(userEntityOptional.isPresent())
			return userEntityOptional.get();
		
		throw new RuntimeException("There is no user with that email: " + email);
	}
	
	private boolean checkCode(UserEntity userEntity, String code) {
		
		return userEntity.getConfirmationCode().equals(code);
	}

	private boolean isUserRegistered(String email) {
		return userDao.findById(email).isPresent();
	}
	
	@Transactional
	@Override
	public void cleanup() {
		userDao.deleteAll();
		loggedInUsers.clear();
	}

	@Override
	public Long updatePoints(String email, Long points) {
		UserEntity user = getUser(email);
		
		user.setPoints(user.getPoints() + points);
		
		return userDao.save(user).getPoints();
	}



}
