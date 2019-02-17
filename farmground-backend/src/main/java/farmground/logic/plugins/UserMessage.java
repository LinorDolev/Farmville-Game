package farmground.logic.plugins;

import java.util.Date;

public class UserMessage {
	private String messageBoardId;
	private String content;
	private String title;
	private Date creationTime;
	
	public UserMessage() {
	}
	
	public UserMessage(String messageBoardId, String message, String title, Date publishDate) {
		setMessageBoardId(messageBoardId);
		setContent(message);
		setTitle(title);
		setCreationTime(publishDate);
	}
	
	public UserMessage(String title, String message) {		
		setContent(message);
		setTitle(title);
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessageBoardId() {
		return messageBoardId;
	}

	public void setMessageBoardId(String messageBoardId) {
		this.messageBoardId = messageBoardId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	
	
}
