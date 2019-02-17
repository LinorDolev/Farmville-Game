package farmground.logic.plugins;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import farmground.aop.Manager;
import farmground.logic.ActivityEntity;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;

@Component
public class WriteMessagePlugin implements Plugin {	
	private ObjectMapper jackson;
	
	private ElementService elementService;
	
	@PostConstruct
	public void init() {
		this.jackson = new ObjectMapper();
		
	}
	
	@Autowired
	public WriteMessagePlugin(ElementService elementService) {
		setElementService(elementService); 
	}
	

	@Manager
	@Override 
	public Object execute(ActivityEntity command) throws Exception {
		ElementEntity messagesBoard = elementService.getElement(command.getElementId());
					
		if (!messagesBoard.getType().equals(ElementType.MessagesBoard.name().toString())) { 
			throw new RuntimeException("Cannot write message - element with id: " + messagesBoard.getId() + " is not a messages Baord element");
		}
		
		String messageFromActivity = jackson.writeValueAsString(command.getAttributes().get(ElementAttributes.MESSAGE.name()));
		
		//create message 
		UserMessage message = jackson.readValue(messageFromActivity, UserMessage.class);
		message.setMessageBoardId(messagesBoard.getId());
		message.setCreationTime(new Date());
		command.getAttributes().put(ElementAttributes.MESSAGE.name(), message);
		
		return command;
	}
	
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}
}
