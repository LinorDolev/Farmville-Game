package farmground.logic;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;


@Entity
@Table(name = "ACTIVITY")
public class ActivityEntity {

	private String playground;
	private String id;
	private String elementPlayground;
	private String elementId;
	private String type;
	private String playerPlayground;
	private String playerEmail;
	private Map<String, Object> attributes;
	
	public ActivityEntity() {
	}

	
	public ActivityEntity(String playground, String id, String elementPlayground, String elementId, String type,
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

	@Id
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

	@Transient
	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	
	@Lob
	public String getAttributesJson () {
		try {
			return new ObjectMapper().writeValueAsString(this.attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setAttributesJson (String json) {
		try {
			this.attributes = new ObjectMapper().readValue(json, Map.class); 
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public String toString() {
		return String.format(
				"ActivityEntity {playground=%s, id=%s, elementPlayground=%s, elementId=%s, type=%s, playerPlayground=%s, playerEmail=%s, attributes=%s}",
				playground, id, elementPlayground, elementId, type, playerPlayground, playerEmail, attributes);
	}
	
	

}
