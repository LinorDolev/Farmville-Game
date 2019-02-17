package farmground.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import farmground.logic.ActivityService;
import farmground.logic.exceptions.ActivityAlreadyExistException;

@RestController
@CrossOrigin(origins="*")
public class ActivityUI {
	
	@Autowired
	private ActivityService activityService;
	
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	@RequestMapping(method = RequestMethod.POST, 
			path = "/playground/activities/{userPlayground}/{email}", 
			produces = MediaType.APPLICATION_JSON_VALUE, 
			consumes = MediaType.APPLICATION_JSON_VALUE)

	public Object createActivity(
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email, 
			@RequestBody ActivityTO activityTO) throws ActivityAlreadyExistException {

		System.err.println(activityTO);
		return this.activityService.createActivity(activityTO.toEntity());
	}
	
}
