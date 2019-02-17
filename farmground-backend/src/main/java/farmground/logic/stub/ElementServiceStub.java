package farmground.logic.stub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.exceptions.ElementNotFoundException;



//@Service
public class ElementServiceStub implements ElementService {
	private Map<String, ElementEntity> elements;

	@PostConstruct
	private void init() {
		elements = new HashMap<>();

	}

	@Override
	public ElementEntity getElement(String id) throws ElementNotFoundException {
		ElementEntity rv = this.elements.get(id);
		if(rv != null) {
			return rv;			
		}else {
			throw new ElementNotFoundException("no element found for id: " + id);
		}
	}
	
	@Override
	public ElementEntity createElement(ElementEntity element) {
		
		elements.put(element.getId(), element);
		return elements.get(element.getId());

	}

	@Override
	public void cleanup() {
		this.elements.clear();
	}

	@Override
	public List<ElementEntity> getAllElements(int size, int page, String email, String playground) {
		return this.elements
				.values()
				.stream()
				.skip(page * size)
				.limit(size)
				.collect(Collectors.toList());

	}
	
	@Override
	public void updateElement(ElementEntity elementEntity) throws ElementNotFoundException {
		ElementEntity existingElement = this.elements.get(elementEntity.getId());
		if(existingElement == null) {
			throw new ElementNotFoundException("nu element for id: " + elementEntity.getId());
		}
		
		boolean dirty = false;
		if(elementEntity.getX() != null && !elementEntity.getX().equals(existingElement.getX())) {
			existingElement.setX(elementEntity.getX());
			dirty = true;
		}
		
		if(elementEntity.getY() != null && !elementEntity.getY().equals(existingElement.getY())) {
			existingElement.setY(elementEntity.getY());
			dirty = true;
		}
		
		if(elementEntity.getName() != null && !elementEntity.getName().equals(existingElement.getName())) {
			existingElement.setName(elementEntity.getName());
			dirty = true;
		}
		
		
		if(dirty) {
			this.elements.put(elementEntity.getId(), existingElement);
		}
	}


	@Override
	public List<ElementEntity> getElementByDistance(double x, double y, double distance, String email, String playground) {
		return this.elements.values().stream()
				.filter(e->Math.abs(e.getX() - x) < distance && Math.abs(e.getY() - y) < distance)
				.collect(Collectors.toList());
		
	}

	@Override
	public List<ElementEntity> searchElement(String attributeName, String value, String email, String playground) {
		return this.elements.values().stream().
				filter(e->e.getName().equals(attributeName) && e.getType().equals(value)).collect(Collectors.toList());
	}

}
