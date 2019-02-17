package farmground.layout;


import java.util.HashMap;
import java.util.Map;

import farmground.logic.ActivityEntity;

public class ActivityTO {

	private String playground;
	private String id;
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private Map<String, Object> attributes;

	public ActivityTO() {
		attributes = new HashMap<>();
		attributes.put("creator", "playgroundManager");
		attributes.put("color", "blue");
		attributes.put("size", "small");
		attributes.put("number", "seven");	
	
	}

	
	public ActivityTO(ActivityEntity activity)
	{
		this();
		if(activity != null)
		{ 
			setPlayground(activity.getPlayground());
			setId(activity.getId());
			setElementPlayground(activity.getElementPlayground());
			setElementId(activity.getElementId());
			setType(activity.getType());
			setPlayerPlayground(activity.getPlayerPlayground());
			setPlayerEmail(activity.getPlayerEmail());
			setAttributes(activity.getAttributes());
		}
	}
	
	public ActivityTO(String playground, String id, String elementPlayground, String elementId, String type,
			String playerPlayground, String playerEmail, Map<String, Object> attributes) {
		super();
		setPlayground(playground);
		setId(id);
		setElementPlayground(elementPlayground);
		setElementId(elementId);
		setType(type);
		setPlayerPlayground(playerPlayground);
		setPlayerEmail(playerEmail);
		setAttributes(attributes);
	}
	

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playgrond) {
		this.playground = playgrond;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getElementPlayground() {
		return elementPlayground;
	}

	public void setElementPlayground(String elementPlayground) {
		this.elementPlayground = elementPlayground;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlayerPlayground() {
		return playerPlayground;
	}

	public void setPlayerPlayground(String playerPlayground) {
		this.playerPlayground = playerPlayground;
	}

	public String getPlayerEmail() {
		return playerEmail;
	}

	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	
	public ActivityEntity toEntity() {
		ActivityEntity rv = new ActivityEntity();
		
		rv.setPlayground(this.getPlayground());
		rv.setId(this.getId());
		rv.setElementPlayground(this.getElementPlayground());
		rv.setElementId(this.getElementId());
		rv.setType(this.getType());
		rv.setPlayerPlayground(this.getPlayerPlayground());
		rv.setPlayerEmail(this.getPlayerEmail());
		rv.setAttributes(this.getAttributes());
	
		return rv;
	}


	@Override
	public String toString() {
		return "ActivityTO [playground=" + playground + ", id=" + id + ", elementPlayground=" + elementPlayground
				+ ", elementId=" + elementId + ", type=" + type + ", playerPlayground=" + playerPlayground
				+ ", playerEmail=" + playerEmail + ", attributes=" + attributes + "]";
	}
	
	
}
