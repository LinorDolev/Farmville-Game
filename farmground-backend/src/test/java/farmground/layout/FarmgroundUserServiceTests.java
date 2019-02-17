package farmground.layout;

import static org.assertj.core.api.Assertions.assertThat;
import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import farmground.logic.NewUserFormEntity;
import farmground.logic.UserEntity;
import farmground.logic.exceptions.UserNotFoundException;
import farmground.logic.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FarmgroundUserServiceTests {

	private static final String 
			DUMMY_EMAIL = "dummyUser@gmail.com", 
			DUMMY_USERNAME = "dummyUser", 
			DUMMY_AVATAR = "emoji", 
			DUMMY_PLAYGROUND = "dummyPlayground",
			DUMMY_ROLE = "player";
	
	
	@Autowired
	private UserService userService;

	private ObjectMapper jacksonMapper;
	private RestTemplate restTemplate;
	private String url;

	@LocalServerPort
	private int port;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		url = "http://localhost:" + port + "/playground/users";
		jacksonMapper = new ObjectMapper();
		System.err.println(url);
	}

	@Before
	public void setup() {

	}

	@After
	public void teardown() {
		userService.cleanup();
	}

	@Test
	public void testServerInitializesProperly() throws Exception {

	}
	
	@Test
	public void testRegisterUser() {
		NewUserFormTO registerForm = new NewUserFormTO(DUMMY_EMAIL, DUMMY_USERNAME, DUMMY_AVATAR, DUMMY_ROLE, DUMMY_PLAYGROUND);
		
		UserTO userTOResult = restTemplate.postForObject(url , registerForm, UserTO.class);
		
		assertThat(userTOResult)
			.isNotNull()
			.extracting("email", "username", "avatar", "role", "points")
			.containsExactly(DUMMY_EMAIL, DUMMY_USERNAME, DUMMY_AVATAR, DUMMY_ROLE, 0L);
	}
	 
	@Test(expected = Exception.class)
	public void testRegisterUserWithEmailThatAlreadyRegistered() {
		NewUserFormEntity registerForm = new NewUserFormEntity(DUMMY_EMAIL, DUMMY_USERNAME, DUMMY_AVATAR, DUMMY_ROLE, DUMMY_PLAYGROUND);
		
		userService.registerUser(registerForm);
	
		NewUserFormTO registerForm2 = new NewUserFormTO(DUMMY_EMAIL, DUMMY_USERNAME + "changed", DUMMY_AVATAR + "changed", DUMMY_ROLE + "changed", DUMMY_PLAYGROUND);
		
		restTemplate.postForObject(url , registerForm2, UserTO.class);
	}
	
	@Test
	public void testConfirmUser() {
		String code = "200";
		
		UserEntity userEntity = userService.registerUser(new NewUserFormEntity(DUMMY_EMAIL, DUMMY_USERNAME, DUMMY_AVATAR, DUMMY_ROLE, DUMMY_PLAYGROUND));

		UserTO userResult = this.restTemplate.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, DUMMY_PLAYGROUND, DUMMY_EMAIL, code);
		
		assertUserTOEqualsUserEntity(userResult, userEntity);

	}
	
	@Test(expected = Exception.class)
	public void testConfirmUserThatDoesntExist() throws UserNotFoundException{
		String code = "200";

		this.restTemplate.getForObject(this.url + "/confirm/{playground}/{email}/{code}",
				UserTO.class, DUMMY_PLAYGROUND, DUMMY_EMAIL, code);
	}
	
	@Test
	public void testLoginUser() throws UserNotFoundException, Exception {
		UserEntity userEntity = registerAndConfirmDummyUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		UserTO userTOResult = restTemplate.getForObject(url + "/login/{playground}/{email}", UserTO.class, DUMMY_PLAYGROUND, DUMMY_EMAIL);
		assertUserTOEqualsUserEntity(userTOResult, userEntity);
	}
	
	@Test(expected = Exception.class)
	public void testLoginUserThatDoesntExist() {
		restTemplate.getForObject(url + "/login/{playground}/{email}", UserTO.class, DUMMY_PLAYGROUND, DUMMY_EMAIL);
	}
	
	@Test(expected = Exception.class)
	public void testLoginUserThatAlreadyLoggedIn() throws UserNotFoundException, Exception {
		registerAndConfirmDummyUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		userService.loginUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		restTemplate.getForObject(url + "/login/{playground}/{email}", UserTO.class, DUMMY_PLAYGROUND, DUMMY_EMAIL);
	}
	
	@Test(expected = Exception.class)
	public void testLoginUserWithWrongPlayground() throws UserNotFoundException, Exception {
		String wrongPlayground = "WRONG"+ DUMMY_PLAYGROUND ;
		registerAndConfirmDummyUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		restTemplate.getForObject(url + "/login/{playground}/{email}", UserTO.class, wrongPlayground, DUMMY_EMAIL);
	}
	
	@Test
	public void testUpdateUser() throws UserNotFoundException, Exception {	
		String updatedAvatar = DUMMY_AVATAR + " UPDATED";
				
		//Given - dummyUser is registered and confirmed in the database
		UserEntity entity = registerAndConfirmDummyUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		 
		
		//When - dummyUser avatar is updated
		UserTO toForPut = new UserTO(entity);
		toForPut.setAvatar(updatedAvatar);
		
		restTemplate.put(url + "/{playground}/{email}", 
				         toForPut, 
				         toForPut.getPlayground(), 
				         toForPut.getEmail());
	 
		//Then - the avatar of dummyUser is also updated in DB
		String expectedEntityJson = jacksonMapper.writeValueAsString(toForPut);//updatedUserTOJson;
		
		UserEntity userEntityInDb = userService.getUser(entity.getEmail());
	
		String resultJSON = this.jacksonMapper.writeValueAsString(userEntityInDb);
		
		System.err.println("RESULT OF PUT: \n" + resultJSON );
		System.err.println("EXPECTED: \n" + expectedEntityJson);
		
		assertThat(userEntityInDb)
			.isNotNull()
			.isEqualToIgnoringGivenFields(entity, "avatar","isConfirmed")
			.hasFieldOrPropertyWithValue("avatar", updatedAvatar);
	}
	
	@Test (expected = Exception.class)
	public void testUpdateUserThatDoesNotExist() throws UserNotFoundException, Exception {
		restTemplate.put(url + "/{playground}/{email}", new UserTO(), DUMMY_PLAYGROUND, DUMMY_EMAIL);
	}
	
	@Test
	public void testUpdatePoints() throws UserNotFoundException, JsonProcessingException {
		long points = 15;
		
		UserEntity entity = registerAndConfirmDummyUser(DUMMY_EMAIL, DUMMY_PLAYGROUND);
		
		UserTO toForPut = new UserTO(entity);
		
		long updatedPoints = toForPut.getPoints() + points;
		
		toForPut.setPoints(updatedPoints);
		
		restTemplate.put(url + "/{playground}/{email}", 
		         toForPut, 
		         toForPut.getPlayground(), 
		         toForPut.getEmail());

		String expectedEntityJson = jacksonMapper.writeValueAsString(toForPut);//updatedUserTOJson;
		
		UserEntity userEntityInDb = userService.getUser(entity.getEmail());
	
		String resultJSON = this.jacksonMapper.writeValueAsString(userEntityInDb);
		
		System.err.println("RESULT OF PUT: \n" + resultJSON );
		System.err.println("EXPECTED: \n" + expectedEntityJson);
		
		assertThat(userEntityInDb)
			.isNotNull()
			.isEqualToIgnoringGivenFields(entity, "points", "isConfirmed")
			.hasFieldOrPropertyWithValue("points", updatedPoints);
		
	}
	
	private void assertUserTOEqualsUserEntity(UserTO userTO, UserEntity userEntity) {
			assertThat(userTO)
				.isEqualToIgnoringGivenFields(userEntity, "isConfirmed");
	}
	
	private UserEntity registerAndConfirmDummyUser(String email, String playground) throws UserNotFoundException {
	
		NewUserFormEntity registerForm = new NewUserFormEntity(email, DUMMY_USERNAME, DUMMY_AVATAR, DUMMY_ROLE, DUMMY_PLAYGROUND);
	
		UserEntity userEntity = userService.registerUser(registerForm);
		
		return userService.confirmUser(playground, userEntity.getEmail(), "200");
	}
	
	
}
