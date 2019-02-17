package farmground.logic.plugins;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import farmground.dal.ActivityDao;
import farmground.logic.ActivityEntity;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;

@Component
public class ReadRecentMessagesPlugin implements Plugin{
	private static final int NUM_OF_RELEVANT_MESSAGES = 10;
	
	private ObjectMapper jackson;
	private ElementService elementService;
	private ActivityDao activityDao;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
		
	}
	
	@Autowired
	public ReadRecentMessagesPlugin(ElementService elementService, ActivityDao activityDao) {
		setElementService(elementService);
		setActivityDao(activityDao);
	}
	
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
	
	public void setActivityDao(ActivityDao activityDao) {
		this.activityDao = activityDao;
	}
	
	@Override 
	public Object execute(ActivityEntity command) throws Exception {
		ElementEntity messagesBoard = elementService.getElement(command.getElementId());
				
		if (!messagesBoard.getType().equals(ElementType.MessagesBoard.name())) { 
			throw new RuntimeException("Cannot read message - element with id: " + messagesBoard.getId() + " is not a messages board element");
		}
		
		UserMessage[] messagesArr = activityDao.findAllByTypeAndElementId(PluginsNames.WriteMessage.name(), command.getElementId(), 
				PageRequest.of(0, NUM_OF_RELEVANT_MESSAGES)).getContent()
					.stream()
					.map(this::activityToUserMessage)	//Sorting messages by creationTime descending order
					.sorted((message1, message2) -> -message1.getCreationTime().compareTo(message2.getCreationTime()))
					.toArray(UserMessage[]::new);
		
		command.getAttributes().put(ElementAttributes.MESSAGES.name(), messagesArr);
		return command;
	}
	
	private UserMessage activityToUserMessage(ActivityEntity activity) {
		try {
			Object messageObj =  activity.getAttributes().get(ElementAttributes.MESSAGE.name());
			return jackson.readValue(jackson.writeValueAsString(messageObj), UserMessage.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

}
