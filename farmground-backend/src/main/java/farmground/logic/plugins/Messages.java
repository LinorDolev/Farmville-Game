package farmground.logic.plugins;

import java.util.ArrayList;
import java.util.List;

public class Messages {
	private List<UserMessage> messages;

	public Messages() {
		messages = new ArrayList<>();
	}
	

	public List<UserMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<UserMessage> messages) {
		this.messages = messages;
	}

	public void addMessage(UserMessage message) {
		messages.add(message);
	}
	
	
}
