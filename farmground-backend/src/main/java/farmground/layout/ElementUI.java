package farmground.layout;


import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.exceptions.ElementAlreadyExistsException;


@RestController
@CrossOrigin(origins="*")
public class ElementUI {

	@Autowired
	private ElementService elementService;
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	
	@RequestMapping(method = RequestMethod.POST, 
			path = "/playground/elements/{userPlayground}/{email}", 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public ElementTO createElement(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, 
			@RequestBody ElementTO elementTo) throws ElementAlreadyExistsException {

		ElementEntity rv = this.elementService.createElement(elementTo.toEntity());
		return new ElementTO(rv);
	}

	@RequestMapping(
			method = RequestMethod.PUT, 
			path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}", 
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateElement(
			@PathVariable("userPlayground") String userPlayground, 
			@PathVariable("email") String email,
			@PathVariable("playground") String playground, 
			@PathVariable("id") String id,
			@RequestBody ElementTO newElement) throws Exception {

		this.elementService.updateElement(newElement.toEntity());
	}

	@RequestMapping(
			method = RequestMethod.GET, 
			path = "/playground/elements/{userPlayground}/{email}/{playground}/{id}", 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementTO getElement(@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, @PathVariable("playground") String playground,
			@PathVariable("id") String id) throws Exception {

		ElementEntity elementEntity = this.elementService.getElement(id);
		return new ElementTO(elementEntity);
	}

	@RequestMapping(
			method = RequestMethod.GET, 
			path = "/playground/elements/{userPlayground}/{email}/all", 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementTO[] getAllElements(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email,
			@RequestParam(name="size", required=false, defaultValue="10") int size, 
			@RequestParam(name="page", required=false, defaultValue="0") int page) {

		return this.elementService.getAllElements(size, page, userPlayground)
				.stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);
	}

	@RequestMapping(method = RequestMethod.GET, 
			path = "/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}", 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementTO[] getElementsByDistance(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, 
			@PathVariable("x") double x, 
			@PathVariable("y") double y,
			@PathVariable("distance") double distance) throws Exception {

		
		return elementService.getElementByDistance(x, y, distance, email, userPlayground).
				stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);

	}

	@RequestMapping(
			method = RequestMethod.GET, 
			path = "/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}", 
			produces = MediaType.APPLICATION_JSON_VALUE)

	public ElementTO[] searchElements(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, 
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value) throws Exception {

		return elementService
				.searchElement(attributeName, value, email, userPlayground)
				.stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);

	}
}
