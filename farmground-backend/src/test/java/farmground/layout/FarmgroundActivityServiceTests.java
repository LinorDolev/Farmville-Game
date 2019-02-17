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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import farmground.logic.ActivityService;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.NewUserFormEntity;
import farmground.logic.UserEntity;
import farmground.logic.UserService;
import farmground.logic.exceptions.ElementNotFoundException;
import farmground.logic.exceptions.UserNotFoundException;
import farmground.logic.plugins.ElementAttributes;
import farmground.logic.plugins.PluginsNames;
import farmground.logic.plugins.PlantPlugin;
import farmground.logic.plugins.UserRole;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FarmgroundActivityServiceTests {

	private static final String 
					CREATOR_PLAYGROUND = "dummyUser", 
					CREATOR_EMAIL = "dummyUser@gmail.com",
					ELEMENT_PLAYGROUND = "dummyPlayground";
	
	private ElementService elementService;
	private ActivityService activityService;
	private UserService userService;
	
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
		elementService.cleanup();
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
	public void testCreateActivitySuccessfully() throws Exception {
		ActivityTO thePostedActivity = createNonPluginActivity();
		 
		ActivityTO responseActivity = this.restTemplate.postForObject(this.url + "{playground}/{email}",
									  thePostedActivity, ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);

		// then
		assertThat(responseActivity)
		.isNotNull()
		.isEqualToIgnoringGivenFields(thePostedActivity, "id", "elementId")
		.hasNoNullFieldsOrPropertiesExcept("type", "elementId");  
	}
	 
	@Test
	public void testPlantPluginSuccessfully() throws UserNotFoundException, ElementNotFoundException {  
		
		String type = PluginsNames.Plant.name(); 
		String role = UserRole.player.name(); 
		String color = "Purple", name = "Eggplant";
		
		userService.registerUser(new NewUserFormEntity(CREATOR_EMAIL, "dummyUser", "dummyAvatar", role, ELEMENT_PLAYGROUND));
		userService.confirmUser(ELEMENT_PLAYGROUND, CREATOR_EMAIL, "123"); 
		
		Map<String, Object> plantAttributes = new HashMap<>();
		
		ActivityTO thePostedActivity = createNonPluginActivity();
		plantAttributes.put(ElementAttributes.COLOR.name(), color);
		plantAttributes.put(ElementAttributes.NAME.name(), name);			
		
		thePostedActivity.setAttributes(plantAttributes);
		thePostedActivity.setType(type);
		thePostedActivity.setPlayerEmail(CREATOR_EMAIL);
		
		ActivityTO responseActivity = this.restTemplate.postForObject(this.url + "{playground}/{email}",
									  thePostedActivity, ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);
		
		System.err.println("responseActivity" + responseActivity);
		System.err.println("thePostedActivity" + thePostedActivity);
		
		assertThat(responseActivity) 
		.isNotNull() 
		.isEqualToIgnoringGivenFields(thePostedActivity, "id", "elementId", "attributes")
		.hasNoNullFieldsOrPropertiesExcept("number");
		
		UserEntity userWithUpdatedPoints = userService.getUser(CREATOR_EMAIL);
		
		assertThat(userWithUpdatedPoints)
			.hasFieldOrPropertyWithValue("points", PlantPlugin.POINTS_FOR_PLANTING);
		
		ElementEntity element = elementService.getElement(responseActivity.getElementId());
		
		Map<String, Object> elementExpectedAttributes = new HashMap<>();
		
		elementExpectedAttributes.put(ElementAttributes.COLOR.name(), color);
		elementExpectedAttributes.put(ElementAttributes.HAS_HARVESTED.name(), Boolean.toString(false));

	
		try {
			assertThat(element)
				.isNotNull()
				.extracting(
						"id", 
						"creatorEmail", 
						"type",
						"name",
						"moreAttributesJson")
				.containsExactly(
						responseActivity.getElementId(), 
						CREATOR_EMAIL, 
						PluginsNames.Plant.name(),
						name,
						jacksonMapper.writeValueAsString(elementExpectedAttributes));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test(expected = Exception.class)
	public void testPlantPluginWithUserThatIsNotExist() throws UserNotFoundException, ElementNotFoundException, Exception {  
		
		String type = PluginsNames.Plant.name(); 
		String color = "Purple", name = "Eggplant"; 
		
		Map<String, Object> plantAttributes = new HashMap<>();
		
		ActivityTO thePostedActivity = createNonPluginActivity();
		plantAttributes.put(ElementAttributes.COLOR.name(), color);
		plantAttributes.put(ElementAttributes.NAME.name(), name);			
		
		thePostedActivity.setAttributes(plantAttributes);
		thePostedActivity.setType(type);
		thePostedActivity.setPlayerEmail(CREATOR_EMAIL);
		
		ActivityTO responseActivity = this.restTemplate.postForObject(this.url + "{playground}/{email}",
									  thePostedActivity, ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);
		
		System.err.println("responseActivity" + responseActivity);
		System.err.println("thePostedActivity" + thePostedActivity);
		
		assertThat(responseActivity) 
		.isNotNull() 
		.isEqualToIgnoringGivenFields(thePostedActivity, "id", "elementId", "attributes")
		.hasNoNullFieldsOrPropertiesExcept("number");
		
		UserEntity userWithUpdatedPoints = userService.getUser(CREATOR_EMAIL);
		
		assertThat(userWithUpdatedPoints)
			.hasFieldOrPropertyWithValue("points", PlantPlugin.POINTS_FOR_PLANTING);
		
		ElementEntity element = elementService.getElement(responseActivity.getElementId());
		
		Map<String, Object> elementExpectedAttributes = new HashMap<>();
		
		elementExpectedAttributes.put(ElementAttributes.COLOR.name(), color);
		elementExpectedAttributes.put(ElementAttributes.HAS_HARVESTED.name(), Boolean.toString(false));

	
		try {
			assertThat(element)
				.isNotNull()
				.extracting(
						"id", 
						"creatorEmail", 
						"type",
						"name",
						"moreAttributesJson")
				.containsExactly(
						responseActivity.getElementId(), 
						CREATOR_EMAIL, 
						PluginsNames.Plant.name(),
						name,
						jacksonMapper.writeValueAsString(elementExpectedAttributes));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test(expected = Exception.class)
	public void testPlantPluginWithWrongElementType() throws UserNotFoundException, ElementNotFoundException, Exception {  
		
		String type = PluginsNames.Plant.name(); 
		String role = UserRole.player.name(); 
		String color = "Purple", name = "Eggplant"; 
		
		userService.registerUser(new NewUserFormEntity(CREATOR_EMAIL, "dummyUser", "dummyAvatar", role, ELEMENT_PLAYGROUND));
		userService.confirmUser(ELEMENT_PLAYGROUND, CREATOR_EMAIL, "123"); 
		
		Map<String, Object> plantAttributes = new HashMap<>();
		
		ActivityTO thePostedActivity = createNonPluginActivity();
		plantAttributes.put(ElementAttributes.COLOR.name(), color);
		plantAttributes.put(ElementAttributes.NAME.name(), name);			
		
		thePostedActivity.setAttributes(plantAttributes);
		thePostedActivity.setType(type);
		thePostedActivity.setPlayerEmail(CREATOR_EMAIL);
		
		ActivityTO responseActivity = this.restTemplate.postForObject(this.url + "{playground}/{email}",
									  thePostedActivity, ActivityTO.class, CREATOR_PLAYGROUND, CREATOR_EMAIL);
		
		System.err.println("responseActivity" + responseActivity);
		System.err.println("thePostedActivity" + thePostedActivity);
		
		assertThat(responseActivity) 
		.isNotNull() 
		.isEqualToIgnoringGivenFields(thePostedActivity, "id", "elementId", "attributes")
		.hasNoNullFieldsOrPropertiesExcept("number");
		
		UserEntity userWithUpdatedPoints = userService.getUser(CREATOR_EMAIL);
		
		assertThat(userWithUpdatedPoints)
			.hasFieldOrPropertyWithValue("points", PlantPlugin.POINTS_FOR_PLANTING);
		
		ElementEntity element = elementService.getElement(responseActivity.getElementId());
		
		Map<String, Object> elementExpectedAttributes = new HashMap<>();
		
		elementExpectedAttributes.put(ElementAttributes.COLOR.name(), color);
		elementExpectedAttributes.put(ElementAttributes.HAS_HARVESTED.name(), Boolean.toString(false));

	
		try {
			assertThat(element)
				.isNotNull()
				.extracting(
						"id", 
						"creatorEmail", 
						"type",
						"name",
						"moreAttributesJson")
				.containsExactly(
						responseActivity.getElementId(), 
						CREATOR_EMAIL, 
						PluginsNames.Plant.name(),
						name,
						jacksonMapper.writeValueAsString(elementExpectedAttributes));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private ActivityTO createNonPluginActivity() { 
		Map<String, Object> attributes = new HashMap<>();

		attributes.put("attr1", "1");
		return new ActivityTO(CREATOR_PLAYGROUND, 
							  null, 
							  ELEMENT_PLAYGROUND, 
							  null, 
							  null, 
							  CREATOR_PLAYGROUND,
							  CREATOR_EMAIL,
							  attributes);
	} 
	
	
	

}
