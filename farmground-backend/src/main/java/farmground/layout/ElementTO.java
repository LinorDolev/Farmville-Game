package farmground.layout;

import java.util.Date;
import java.util.Map;

import farmground.logic.ElementEntity;

public class ElementTO {

	private String playground;
	private String id;
	private Location location;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;

	public ElementTO() {

	}
	
	public ElementTO(ElementEntity element) {
		setPlayground(element.getPlayground());
		setId(element.getId());
		setLocation(new Location(element.getX(), element.getY()));
		setName(element.getName());
		setCreationDate(element.getCreationDate());
		setExpirationDate(element.getExpirationDate());
		setType(element.getType());
		setAttributes(element.getAttributes());
		setCreatorPlayground(element.getCreatorPlayground());
		setCreatorEmail(element.getCreatorEmail());
	}


	public ElementTO(String playground, String id, Location location, 
			String name, Date creationDate,
			Date expirationDate, String type,
			Map<String, Object> attributes, 
			String creatorPlayground,
			String creatorEmail) {
		setPlayground(playground);
		setId(id);
		setLocation(location);
		setName(name);
		setCreationDate(creationDate);
		setExpirationDate(expirationDate);
		setType(type);
		setAttributes(attributes);
		setCreatorPlayground(creatorPlayground);
		setCreatorEmail(creatorEmail);
	}

	public String getPlayground() {
		return playground;
	}

	public void setPlayground(String playground) {
		this.playground = playground;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public String getCreatorPlayground() {
		return creatorPlayground;
	}

	public void setCreatorPlayground(String creatorPlayground) {
		this.creatorPlayground = creatorPlayground;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}
	
	public ElementEntity toEntity() {
		ElementEntity elementEntity = new ElementEntity();
		elementEntity.setPlayground(this.getPlayground());
		elementEntity.setId(this.getId());
		elementEntity.setX(this.getLocation().getX());
		elementEntity.setY(this.getLocation().getY());
		elementEntity.setName(this.getName());
		elementEntity.setCreationDate(this.getCreationDate());
		elementEntity.setExpirationDate(this.getExpirationDate());
		elementEntity.setType(this.getType());
		elementEntity.setAttributes(this.getAttributes());
		elementEntity.setCreatorPlayground(this.getCreatorPlayground());
		elementEntity.setCreatorEmail(this.getCreatorEmail());
		
		return elementEntity;
	}

	@Override
	public String toString() {
		return "ElementTO [playground=" + playground + ", id=" + id + ", location=" + location + ", name=" + name
				+ ", creationDate=" + creationDate + ", expirationDate=" + expirationDate + ", type=" + type
				+ ", attributes=" + attributes + ", creatorPlayground=" + creatorPlayground + ", creatorEmail="
				+ creatorEmail + "]";
	}

	
}
