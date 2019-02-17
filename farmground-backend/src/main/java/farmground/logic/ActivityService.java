package farmground.logic;

import java.util.List;

import farmground.logic.exceptions.ActivityAlreadyExistException;
import farmground.logic.exceptions.ActivityNotFoundException;
public interface ActivityService {
	
	
	public ActivityEntity getActivity(String id) throws ActivityNotFoundException;
	
	public Object createActivity(ActivityEntity activity) throws ActivityAlreadyExistException;
	
	public void cleanup();
	
	public List<ActivityEntity> getAllActivities(int size, int page);

	public void updateActivity(String id, ActivityEntity entity) throws ActivityNotFoundException;

	
	
}
