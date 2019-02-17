package farmground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import farmground.logic.ActivityService;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.NewUserFormEntity;
import farmground.logic.UserEntity;
import farmground.logic.UserService;
import farmground.logic.exceptions.ActivityAlreadyExistException;
import farmground.logic.exceptions.ActivityNotFoundException;
import farmground.logic.exceptions.UserNotFoundException;
import farmground.logic.plugins.ElementAttributes;
import farmground.logic.plugins.ElementType;
import farmground.logic.plugins.PluginsNames;
import farmground.logic.plugins.UserMessage;
import farmground.logic.plugins.UserRole;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageBoardPluginsTests {

	private static final String PLAYGROUND = "playground", CREATOR_PLAYGROUND = "dummyUser",
			CREATOR_EMAIL = "dummyUser@gmail.com", ELEMENT_PLAYGROUND = "dummyPlayground", USER_NAME = "dummyUser",
			AVATAR = "dummyAvatar", CODE = "200";

	private ActivityService activityService;
	private UserService userService;
	private ElementService elementService;
	private ObjectMapper jacksonMapper;
	private RestTemplate restTemplate;
	private String url;

	@LocalServerPort
	private int port;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		url = "http://localhost:" + port + "/playground/activities/";
		jacksonMapper = new ObjectMapper();

		System.err.println(url);
	}

	@Before
	public void setup() {

	}

	@After
	public void teardown() {
		activityService.cleanup();
		userService.cleanup();
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}

	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	@Test
	public void testWriteMessagePluginSuccessfully() throws UserNotFoundException {
		String type = PluginsNames.WriteMessage.name();
		String role = UserRole.manager.name();

		UserEntity user = userService
				.registerUser(new NewUserFormEntity(CREATOR_EMAIL, USER_NAME, AVATAR, role, ELEMENT_PLAYGROUND));
		user = userService.confirmUser(ELEMENT_PLAYGROUND, CREATOR_EMAIL, CODE);

		ElementEntity messagesBoard = new ElementEntity();

		messagesBoard.setName("Messages Board");
		messagesBoard.setCreatorEmail(user.getEmail());
		messagesBoard.setType(ElementType.MessagesBoard.name());

		elementService.createElement(messagesBoard);

		ActivityTO thePostedActivity = createNonPluginActivity();

		thePostedActivity.setType(type);
		thePostedActivity.setPlayerEmail(CREATOR_EMAIL);
		thePostedActivity.setElementId(messagesBoard.getId());

		UserMessage sentMessage = new UserMessage("Message Title", "Write Message Number 1");
		Map<String, Object> activityAttributes = new HashMap<>();
		activityAttributes.put(ElementAttributes.MESSAGE.name(), sentMessage);
		thePostedActivity.setAttributes(activityAttributes);

		ActivityTO responseActivity = this.restTemplate.postForObject(url + "{playground}/{email}", thePostedActivity,
				ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);

		System.err.println("responseAcivity" + responseActivity);
		System.err.println("thePostedActivity" + thePostedActivity);

		assertThat(responseActivity).isNotNull().isEqualToIgnoringGivenFields(thePostedActivity, "id", "attributes")
				.hasNoNullFieldsOrPropertiesExcept("expirationDate");
	}

	@Test
	public void testReadRecentMessagePluginSuccessfully()
			throws UserNotFoundException, ActivityNotFoundException, ActivityAlreadyExistException {

		// Given - I have a mock confirmed user, and a message board he created and a
		// message he wrote
		String writeType = PluginsNames.WriteMessage.name();
		String readType = PluginsNames.ReadRecentMessages.name();
		String role = UserRole.manager.name();

		UserEntity user = userService
				.registerUser(new NewUserFormEntity(CREATOR_EMAIL, USER_NAME, AVATAR, role, ELEMENT_PLAYGROUND));
		user = userService.confirmUser(ELEMENT_PLAYGROUND, CREATOR_EMAIL, CODE);

		ElementEntity messagesBoard = new ElementEntity();

		messagesBoard.setName("Messages Board");
		messagesBoard.setCreatorEmail(user.getEmail());
		messagesBoard.setType(ElementType.MessagesBoard.name());

		elementService.createElement(messagesBoard);

		// WRITE MESSAGE
		ActivityTO thePostedActivity = createNonPluginActivity();

		thePostedActivity.setType(writeType);
		thePostedActivity.setPlayerEmail(CREATOR_EMAIL);
		thePostedActivity.setElementId(messagesBoard.getId());

		UserMessage sentMessage = new UserMessage("Message Title 1", "Write Message Number 1");
		Map<String, Object> activityAttributes = new HashMap<>();
		activityAttributes.put(ElementAttributes.MESSAGE.name(), sentMessage);
		thePostedActivity.setAttributes(activityAttributes);

        activityService.createActivity(thePostedActivity.toEntity());

		// WRITE 2nd MESSAGE
		ActivityTO thePostedActivity2 = createNonPluginActivity();

		Map<String, Object> activityAttributes2 = new HashMap<>();

		thePostedActivity2.setElementId(messagesBoard.getId());
		thePostedActivity2.setType(writeType);
		thePostedActivity2.setPlayerEmail(CREATOR_EMAIL);

		UserMessage sentMessage2 = new UserMessage("Message Title 2", "Write Message Number 2");
		activityAttributes2.put(ElementAttributes.MESSAGE.name(), sentMessage2);

		thePostedActivity2.setAttributes(activityAttributes2);

		activityService.createActivity(thePostedActivity2.toEntity());

		// READ MESSAGES
		ActivityTO readMessageActivity = createNonPluginActivity();

		readMessageActivity.setElementId(messagesBoard.getId());
		readMessageActivity.setType(readType);
		readMessageActivity.setPlayerEmail(CREATOR_EMAIL);

		ActivityTO responseReadMessageActivity = this.restTemplate.postForObject(url + "{playground}/{email}",
				readMessageActivity, ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);
		try {
			String messagesJSON = jacksonMapper.writeValueAsString(
					responseReadMessageActivity.getAttributes().get(ElementAttributes.MESSAGES.name()));
			UserMessage[] responseMessages = jacksonMapper.readValue(messagesJSON, UserMessage[].class);

			assertThat(responseMessages).isNotNull().isNotEmpty()
					.usingElementComparatorIgnoringFields("creationTime", "messageBoardId")
					.containsExactly(sentMessage2, sentMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private ActivityTO createNonPluginActivity() {
		HashMap<String, Object> attributes = new HashMap<>();
		return new ActivityTO(PLAYGROUND, null, ELEMENT_PLAYGROUND, null, null, CREATOR_PLAYGROUND, CREATOR_EMAIL,
				attributes);
	}
}
