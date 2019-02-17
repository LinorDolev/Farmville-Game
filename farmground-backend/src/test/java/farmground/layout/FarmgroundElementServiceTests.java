package farmground.layout;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

import farmground.logic.ElementEntity;
import farmground.logic.ElementService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class FarmgroundElementServiceTests {

	private static final String 
			CREATOR_PLAYGROUND = "playground", 
			CREATOR_MAIL = "dummyUser@gmail.com",   
		    ELEMENT_NAME = "dummyElement", 
		    ELEMENT_TYPE = "elementType", 
		    PLAYGROUND = "Farmground";
 
	private Date expirationDate = null;

	@Autowired
	private ElementService elementService;

	private ObjectMapper jacksonMapper;

	private RestTemplate restTemplate;
	
	private String url;

	@LocalServerPort
	private int port;

	@PostConstruct
	public void init() {
		restTemplate = new RestTemplate();
		url = "http://localhost:" + port + "/playground/elements";

		System.err.println(url);
		this.jacksonMapper = new ObjectMapper();
	}

	@Before
	public void setup() {
	}

	@After
	public void teardown() {
		elementService.cleanup();
	}

	@Test
	public void testServerInitializesProperly() throws Exception {

	}

	/// playground/elements/{userPlayground }/{email}
	@Test
	public void testCreateElementSuccessfully() throws Exception {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		// given - nothing

		// when

		ElementEntity elementEntity = new ElementEntity(PLAYGROUND, null, 0.0, 0.0, ELEMENT_NAME, null, null, ELEMENT_TYPE, new HashMap<>(), CREATOR_PLAYGROUND, CREATOR_MAIL);
		
		ElementTO thePostedElement = new ElementTO(elementEntity);
		
		ElementTO responseElement = this.restTemplate.postForObject(this.url + "/{playground}/{email}",
				thePostedElement, ElementTO.class, CREATOR_PLAYGROUND, CREATOR_MAIL);

		ElementEntity expectedElement = elementEntity;

		ElementEntity actualElementInDb = this.elementService.getElement(responseElement.getId());

		// than
		assertThat(actualElementInDb)
		.isNotNull()
		.isEqualToIgnoringGivenFields(expectedElement, "id", "creationDate")
		.hasNoNullFieldsOrPropertiesExcept("expirationDate");
	}

	

	@Test
	public void testUpdateElementSuccessful() throws Exception {
		// given
		ElementEntity elementEntity = new ElementEntity(PLAYGROUND,"e1", 0.0, 0.0, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, new HashMap<>(), CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntityWithId = this.elementService.createElement(elementEntity);

		// when name is updated
		ElementTO toForPut = new ElementTO(elementEntityWithId);
		String updatedName = "elementUpdatedName";
		toForPut.setName(updatedName);
		
		this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", toForPut,
				elementEntity.getCreatorPlayground(), elementEntity.getCreatorEmail(), elementEntity.getPlayground(),
				elementEntityWithId.getId());
		
		//Then the element in db has the updated creation date
		ElementEntity actualEntityInDb = this.elementService.getElement(elementEntityWithId.getId());

		String expectedJson = this.jacksonMapper.writeValueAsString(toForPut.toEntity());
		String resultFromDBJson = this.jacksonMapper.writeValueAsString(actualEntityInDb);
		assertThat(resultFromDBJson)
			.isEqualTo(expectedJson);
	}

	@Test(expected = Exception.class)
	public void testUpdateNonExistingElement() throws Exception {
		// given - nothing

		// when
		String toJson = "{\"id\":\"NonExistingId\", \"playground\":\"farmground\", \"name\":\"dummyElement-UPDATED\", \"type\":\"elementType\", \"location\":{\"x\":0.0, \"y\":0.0}, \"attributes\":{\"attr1\":1}, \"creatorPlayground\":\"playground\", \"creatorEmail\":\"dummyUser@gmail.com\"}";
		ElementTO toForPut = this.jacksonMapper.readValue(toJson, ElementTO.class);

		this.restTemplate.put(this.url + "/{userPlayground}/{email}/{playground}/{id}", toForPut,
				toForPut.getCreatorPlayground(), toForPut.getCreatorEmail(), toForPut.getPlayground(),
				toForPut.getId());
	}

	@Test
	public void testGetSpecificElementByIdSuccessfully() throws Exception {

		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		ElementEntity elementEntity = 
				new ElementEntity(PLAYGROUND,"e1", 0.0, 0.0, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		ElementEntity elementFromDB = this.elementService.createElement(elementEntity);

		// when
		ElementTO actualElement = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class, CREATOR_PLAYGROUND,
				CREATOR_MAIL, PLAYGROUND, elementFromDB.getId());

		// then
		assertThat(actualElement.toEntity())
		.isNotNull()
		.isEqualToComparingFieldByField(elementFromDB);
	}

	@Test(expected = Exception.class)
	public void testGetSpecificElementByInvalidId() throws Exception {
		this.restTemplate.getForObject(this.url + "/{userPlayground}/{email}/{playground}/{id}", ElementTO.class,
				CREATOR_PLAYGROUND, CREATOR_MAIL, PLAYGROUND, "null");
	}

	@Test
	public void testShowElementsUsingPaginationSuccessfully() throws Exception {
		String attributeName = "attr1";
		String attributeValue = "1";
		Double x1 = 3.0, y1 = 2.7, x2 = 1.5, y2 = 4.2, x3= 1.0, y3=100.0;

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(attributeName, attributeValue);
		
		ElementEntity elementEntity1 = new ElementEntity(PLAYGROUND,"e1", x1, y1, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity2 = new ElementEntity(PLAYGROUND,"e2", x2, y2, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity3 = new ElementEntity(PLAYGROUND,"e3", x3, y3, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		this.elementService.createElement(elementEntity2);
		this.elementService.createElement(elementEntity3);
	
		ElementTO[] actualElements = this.restTemplate.getForObject(
									 this.url + "/{userPlayground}/{email}/all" + "?size={size}&page={page}",
									 ElementTO[].class, PLAYGROUND, CREATOR_MAIL, 5, 0);

		List<String> expectedElements = Stream.of(new ElementEntity[]{elementEntity1, elementEntity2, elementEntity3})
			.map(ElementTO::new)
			.map(this::toJson)
			.collect(Collectors.toList());
		
		List<String> returnedElements = Stream.of(actualElements)
			.map(this::toJson)
			.collect(Collectors.toList());
		
		assertThat(returnedElements)
			.isNotNull()
			.isNotEmpty()
			.hasSize(3)
			.containsExactlyInAnyOrderElementsOf(expectedElements);
	}
	
	private String toJson(ElementTO element) {
		try {
			return jacksonMapper.writeValueAsString(element);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Test
	public void testGetElementsByDistance() throws Throwable {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		Double x1 = 3.0, y1 = 2.7, x2 = 1.5, y2 = 4.2, x3= 1.0, y3=100.0;
		Double distance = 50.0;
		
		
		ElementEntity elementEntity1 = new ElementEntity(PLAYGROUND,"e1", x1, y1, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity2 = new ElementEntity(PLAYGROUND,"e2", x2, y2, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity3 = new ElementEntity(PLAYGROUND,"e3", x3, y3, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		this.elementService.createElement(elementEntity2);
		this.elementService.createElement(elementEntity3);
	
		// when
		ElementTO[] actualElement = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
				ElementTO[].class, PLAYGROUND, CREATOR_MAIL,
				x1,y1,distance);

		// then
		assertThat(actualElement)
			.hasSize(2);
	}
	
	@Test(expected = Exception.class)
	public void testGetElementsByDistanceWithIlegalX() throws Throwable {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		Double  badX = -2D;
		Double x = 3.0, y = 2.7;
		Double distance = 50D;
		ElementEntity elementEntity1 = new ElementEntity(PLAYGROUND,"e1", x, y, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		
        this.restTemplate.getForObject(
		this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
		ElementTO[].class, CREATOR_PLAYGROUND, CREATOR_MAIL,
		badX,y,distance);
	}
	
	@Test(expected = Exception.class)
	public void testGetElementsByDistanceWithIlegalY() throws Throwable {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		Double  badY = -2D;
		Double x = 3.0, y = 2.7;
		Double distance = 50D;
		ElementEntity elementEntity1 = new ElementEntity(PLAYGROUND,"e1", x, y, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		
        this.restTemplate.getForObject(
		this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
		ElementTO[].class, CREATOR_PLAYGROUND, CREATOR_MAIL,
		x,badY,distance);
	}
	
	@Test(expected = Exception.class)
	public void testGetElementsByDistanceWithIlegalDistance() throws Throwable {
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		Double  badDistance = -2D;
		Double x = 3.0, y = 2.7;
		
		
		ElementEntity elementEntity1 = 
				new ElementEntity(PLAYGROUND,"e1", x, y, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		
		this.restTemplate.getForObject(
		this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
		ElementTO[].class, CREATOR_PLAYGROUND, CREATOR_MAIL,
		x,y,badDistance);
	}
	
	@Test(expected = Exception.class)
	public void testNotFoundDistance() throws Throwable{
		Map<String, Object> attributes = new HashMap<>();
		attributes.put("attr1", "1");
		Double x = 1D, y = 1D, checkX = 100D, checkY = 100D;
		Double checkDistance = 1D;
		ElementEntity elementEntity1 = 
				new ElementEntity(PLAYGROUND,"e1", x, y, ELEMENT_NAME, new Date(), null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);

		this.elementService.createElement(elementEntity1);
		
		this.restTemplate.getForObject(
		this.url + "/{userPlayground}/{email}/near/{x}/{y}/{distance}",
		ElementTO[].class, CREATOR_PLAYGROUND, CREATOR_MAIL,
		checkX, checkY ,checkDistance);
	}
	
	@Test
	public void testSearchElements() throws Throwable {
		String attributeName = "dummyAttribute", value = "dummyValue";
		Double x1 = 3.0, y1 = 2.7, x2 = 1.5, y2 = 4.2, x3= 1.0, y3=100.0;

		Map<String, Object> attributes = new HashMap<>();
		attributes.put(attributeName, value);
		
		Map<String, Object> attributes2 = new HashMap<>();
		attributes2.put(attributeName, "Other Value");
		
		Map<String, Object> attributes3 = new HashMap<>();
		attributes3.put("Another Attribute", value);
		
		ElementEntity elementEntity1 = new ElementEntity(PLAYGROUND,"e1", x1, y1, ELEMENT_NAME, new Date(),
				null, ELEMENT_TYPE, attributes, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity2 = new ElementEntity(PLAYGROUND,"e2", x2, y2, ELEMENT_NAME, new Date(),
				null, ELEMENT_TYPE, attributes2, CREATOR_PLAYGROUND, CREATOR_MAIL);
		ElementEntity elementEntity3 = new ElementEntity(PLAYGROUND,"e3", x3, y3, ELEMENT_NAME, new Date(),
				null, ELEMENT_TYPE, attributes3, CREATOR_PLAYGROUND, CREATOR_MAIL);

		elementEntity1 = this.elementService.createElement(elementEntity1);
		elementEntity2 = this.elementService.createElement(elementEntity2);
		elementEntity3 = this.elementService.createElement(elementEntity3);

		ElementTO[] actualElement = this.restTemplate.getForObject(
				this.url + "/{userPlayground}/{email}/search/{attributeName}/{value}",
				ElementTO[].class, PLAYGROUND,CREATOR_MAIL, attributeName, value);
		
		assertThat(actualElement)
			.hasSize(1);
	}
}
