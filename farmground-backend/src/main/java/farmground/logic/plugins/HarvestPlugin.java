package farmground.logic.plugins;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import farmground.aop.Player;
import farmground.dal.ElementDao;
import farmground.logic.ActivityEntity;
import farmground.logic.ElementEntity;
import farmground.logic.UserService;

@Component
public class HarvestPlugin implements Plugin {
	public static final Long MINUTES_BETWEEN_PLANT_AND_HARVEST = 3L, POINTS_FOR_HARVESTING = 10L;
	
	private ElementDao elementDao;
	
	private UserService userService;
	
	
	@Autowired
	public HarvestPlugin(ElementDao elementDao, UserService userService) {
		setElementDao(elementDao);
		setUserService(userService);
	}


	private void validateHarvest(ElementEntity element) {
		if (!element.getType().equals(PluginsNames.Harvest.name())) {
			throw new RuntimeException("Cannot harvest - element with id: " + element.getId() + " is not a plant");
		}
		
		String wasHarvestedStr = element.getAttributes().get(ElementAttributes.HAS_HARVESTED.name()).toString();
		if(Boolean.parseBoolean(wasHarvestedStr)) {
			throw new RuntimeException("Cannot harvest plant with id: " + element.getId() + ", it was already harvested");
		}
		
		Date currentDate = new Date();
		
		float timeFromPlanting = ChronoUnit.MINUTES.between(element.getCreationDate().toInstant(), currentDate.toInstant());
		
		if(timeFromPlanting < MINUTES_BETWEEN_PLANT_AND_HARVEST) {
			throw new RuntimeException("Cannot harvest - element with id: " + element.getId() + " was planted less than " + 
					MINUTES_BETWEEN_PLANT_AND_HARVEST + " you need to wait " +
					(MINUTES_BETWEEN_PLANT_AND_HARVEST - timeFromPlanting) +" more minutes");
		}
	}

	@Player
	@Override
	public Object execute(ActivityEntity command) throws Exception {
		
		Optional<ElementEntity> optionalElement = elementDao.findById(command.getElementId());
		
		if(!optionalElement.isPresent()) {
			throw new RuntimeException("Element with id: " + command.getElementId() + " does not exist");
		}
		
		ElementEntity element = optionalElement.get();
		
		validateHarvest(element);
		
		element.getAttributes().put(ElementAttributes.HAS_HARVESTED.name(), Boolean.toString(true));
		
		String userEmail = element.getCreatorEmail();
						
		userService.updatePoints(userEmail, POINTS_FOR_HARVESTING);
				
		return elementDao.save(element);
	}
	

	public void setElementDao(ElementDao elementDao) {
		this.elementDao = elementDao;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
