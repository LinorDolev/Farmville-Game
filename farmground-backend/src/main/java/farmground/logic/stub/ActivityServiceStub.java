package farmground.logic.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;


import farmground.logic.ActivityEntity;
import farmground.logic.ActivityService;
import farmground.logic.exceptions.ActivityAlreadyExistException;
import farmground.logic.exceptions.ActivityNotFoundException;

//@Service
public class ActivityServiceStub implements ActivityService {
	private Map<String, ActivityEntity> activities;
	
	@PostConstruct
	public void init() {
		activities = new HashMap<>();
	}

	@Override
	public ActivityEntity createActivity(ActivityEntity activity) throws ActivityAlreadyExistException {
		if(!activities.containsValue(activity)) 
			return activities.put(activity.getId(), activity);

		throw new ActivityAlreadyExistException("element already exist with with id"+activities.get(activity).getId());
			
	}

	@Override
	public ActivityEntity getActivity(String id) throws ActivityNotFoundException {
		ActivityEntity op = activities.get(id);
		if(op != null)
			return op;
		
		throw new ActivityNotFoundException();
	}


	@Override
	public void cleanup() {
		activities.clear();
	}


	@Override
	public List<ActivityEntity> getAllActivities(int size, int page) {
		return activities.values().stream().skip(page*size).limit(size).collect(Collectors.toList());

	}

	@Override
	public void updateActivity(String id, ActivityEntity entity) throws ActivityNotFoundException {
		ActivityEntity existingActivity = this.getActivity(id);
		
		if(entity.getType() != null && !entity.getType().equals(existingActivity.getType())) {
			existingActivity.setType(entity.getType());
		}
		
		if(entity.getElementPlayground() != null && !entity.getElementPlayground().equals(existingActivity.getElementPlayground())) {
			existingActivity.setElementPlayground(entity.getElementPlayground());
		}

		this.activities.put(id, existingActivity);
		
	}
	 
}
