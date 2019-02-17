package farmground.logic;

import java.util.List;

import farmground.logic.exceptions.DistanceNegativeParamsException;
import farmground.logic.exceptions.DistanceNotFoundException;
import farmground.logic.exceptions.ElementNotFoundException;



public interface ElementService {
	public ElementEntity getElement(String id) throws ElementNotFoundException;
	
	public ElementEntity createElement(ElementEntity element);
	
	public void cleanup();
	
	public List<ElementEntity> getAllElements(int size, int page, String playground);

	public void updateElement(ElementEntity elementEntity) throws ElementNotFoundException;
	
	public List<ElementEntity> getElementByDistance(double x, double y, double distance, String email, String playground) throws DistanceNegativeParamsException, DistanceNotFoundException;
	
	public List<ElementEntity> searchElement(String attributeName, String value, String email, String playground) throws ElementNotFoundException;
}
