package farmground.logic.data;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import farmground.aop.logger.MyLog;
import farmground.aop.logger.PlaygroundPerformance;
import farmground.dal.ActivityDao;
import farmground.dal.utils.IdGeneratorService;
import farmground.logic.ActivityEntity;
import farmground.logic.ActivityService;
import farmground.logic.exceptions.ActivityAlreadyExistException;
import farmground.logic.exceptions.ActivityNotFoundException;
import farmground.logic.plugins.Plugin;

@Service
public class JpaActivityService implements ActivityService {
	private ActivityDao activities;
	private IdGeneratorService idGenerator;
	
	private ConfigurableApplicationContext spring;
	private ObjectMapper jackson;
	
	@Autowired
	public JpaActivityService(ActivityDao activities, IdGeneratorService idGenerator, ConfigurableApplicationContext spring) {
		this.activities = activities;
		this.idGenerator = idGenerator;
		this.spring = spring;
		this.jackson = new ObjectMapper();
	}
 
	@Override
	@Transactional(readOnly = true)
	@MyLog 
	@PlaygroundPerformance
	public ActivityEntity getActivity(String id) throws ActivityNotFoundException {
		Optional<ActivityEntity> op = this.activities.findById(id);
		if(op.isPresent())
			return op.get();
		
		throw new ActivityNotFoundException();
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public Object createActivity(ActivityEntity activity) throws ActivityAlreadyExistException{

		activity.setId(idGenerator.nextId());

		try {
			if(activity.getType() != null) {
				String type = activity.getType(); 
				Plugin plugin = (Plugin) spring.getBean(Class.forName("farmground.logic.plugins." + type + "Plugin"));
				Object content = plugin.execute(activity);
				
				//write the response on moreAttributes          
				activity = jackson.readValue(jackson.writeValueAsString(content), ActivityEntity.class);
			}
		} catch (Exception e) { 
			throw new RuntimeException(e);
		}
		
		return this.activities.save(activity);
	}

	@Override
	@Transactional
	public void cleanup() {
		activities.deleteAll(); 
	}

	@Override
	@Transactional
	@MyLog 
	@PlaygroundPerformance
	public List<ActivityEntity> getAllActivities(int size, int page) {
		return this.activities
				.findAll(PageRequest.of(page, size))
				.getContent();
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public void updateActivity(String id, ActivityEntity entity) throws ActivityNotFoundException {
		ActivityEntity existingActivity = this.getActivity(id);
		
		if(entity.getType() != null && !entity.getType().equals(existingActivity.getType())) {
			existingActivity.setType(entity.getType());
		}
		
		if(entity.getElementPlayground() != null && !entity.getElementPlayground().equals(existingActivity.getElementPlayground())) {
			existingActivity.setElementPlayground(entity.getElementPlayground());
		}

		this.activities.save(existingActivity);
	}
}
