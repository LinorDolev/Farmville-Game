package farmground.logic.plugins;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import farmground.aop.Player;
import farmground.logic.ActivityEntity;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.UserService;

@Component
public class PlantPlugin implements Plugin {
	public static final Long POINTS_FOR_PLANTING = 5L;

	private UserService userService;
	
	private ElementService elementService;
	
	@Autowired
	public PlantPlugin(ElementService elementService, UserService userService) {
		setElementService(elementService); 
		setUserService(userService); 
	}
	
	@Player
	@Override
	public Object execute(ActivityEntity command) throws Exception {
		ElementEntity elementEntity = new ElementEntity();
		
		if (!command.getType().equals(PluginsNames.Plant.name())) { 
			throw new RuntimeException("Cannot plant - activity with id: " + command.getId() + " is not a plant activity");
		}
		
		elementEntity.setName(command.getAttributes().get(ElementAttributes.NAME.name()).toString());
		elementEntity.setCreatorEmail(command.getPlayerEmail());
		elementEntity.setCreatorPlayground(command.getElementPlayground());
		elementEntity.setType(PluginsNames.Plant.name());
		elementEntity.setX(Double.parseDouble(command.getAttributes().get(ElementAttributes.X.name()).toString()));
		elementEntity.setY(Double.parseDouble(command.getAttributes().get(ElementAttributes.Y.name()).toString()));
		elementEntity.setPlayground(command.getPlayground());
		
		Map<String, Object> attributes = new HashMap<>();
		attributes.put(ElementAttributes.COLOR.name(), command.getAttributes().get(ElementAttributes.COLOR.name()));
		attributes.put(ElementAttributes.HAS_HARVESTED.name(), Boolean.toString(false));
		elementEntity.setAttributes(attributes);

		String userEmail = command.getPlayerEmail();
		Long points = userService.updatePoints(userEmail, POINTS_FOR_PLANTING);
		
		elementEntity = elementService.createElement(elementEntity);
		
		command.setElementId(elementEntity.getId());
		command.getAttributes().put("points", points.intValue());
		
		return command;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

}
