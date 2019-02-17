package farmground.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "ELEMENTS")
public class ElementEntity {
	private String playground;
	private String id;
	private Double x;
	private Double y;
	private String name;
	private Date creationDate;
	private Date expirationDate;
	private String type;
	private Map<String, Object> attributes;
	private String creatorPlayground;
	private String creatorEmail;


	public ElementEntity() {
		this.x = 0.0;
		this.y = 0.0;
//		this.creationDate = new Date();
		this.attributes = new HashMap<>();
	}
	
	public ElementEntity(String playground, String id, Double x, Double y, String name, Date creationDate,
			Date expirationDate, String type, Map<String, Object> attributes, String creatorPlayground,
			String creatorEmail) {
		super();
		setPlayground(playground);
		setId(id);
		setX(x);
		setY(y);
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

	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Transient
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
	
	@Lob
	public String getMoreAttributesJson () {
		try {
			return new ObjectMapper().writeValueAsString(this.attributes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setMoreAttributesJson (String json) {
		try {
			this.attributes = new ObjectMapper().readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	@Override
	public String toString() {
		return "ElementEntity [playground=" + playground + ", id=" + id + ", x=" + x + ", y=" + y + ", name=" + name
				+ ", creationDate=" + creationDate + ", expirationDate=" + expirationDate + ", type=" + type
				+ ", attributes=" + attributes + ", creatorPlayground=" + creatorPlayground + ", creatorEmail="
				+ creatorEmail + "]";
	}

}

