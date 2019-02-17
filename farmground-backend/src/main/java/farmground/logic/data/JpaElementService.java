package farmground.logic.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import farmground.aop.logger.MyLog;
import farmground.aop.logger.PlaygroundPerformance;
import farmground.dal.ElementDao;
import farmground.dal.utils.IdGeneratorService;
import farmground.logic.ElementEntity;
import farmground.logic.ElementService;
import farmground.logic.exceptions.DistanceNegativeParamsException;
import farmground.logic.exceptions.DistanceNotFoundException;
import farmground.logic.exceptions.ElementNotFoundException;


@Service
public class JpaElementService implements ElementService {
	private ElementDao elements;
	private IdGeneratorService idGenerator;
	
	@Autowired
	public JpaElementService(ElementDao elements, IdGeneratorService idGenerator) {
		setElements(elements);
		setIdGenerator(idGenerator);
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PlaygroundPerformance
	public ElementEntity getElement(String id) throws ElementNotFoundException {
		Optional<ElementEntity> op = this.elements.findById(id);
		if(op.isPresent()){
			return op.get();
		}else {
			throw new ElementNotFoundException("no element with id: " + id);
		}
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public ElementEntity createElement(ElementEntity element) {
			element.setCreationDate(new Date());
			element.setId(idGenerator.nextId());
			return this.elements.save(element); 
	}

	@Override
	@Transactional
	public void cleanup() {
		this.elements.deleteAll();
		
	}

	@Override
	@Transactional(readOnly = true)
	@MyLog
	@PlaygroundPerformance
	public List<ElementEntity> getAllElements(int size, int page, String playground) {

		return this.elements
				.findAllElementsByPlayground(playground, PageRequest.of(page, size, Direction.DESC, "creationDate"))
				.getContent();
	}

	
	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public void updateElement(ElementEntity elementEntity) throws ElementNotFoundException {
		ElementEntity existingElement = this.getElement(elementEntity.getId());
		
		if(elementEntity.getX() != null && !elementEntity.getX().equals(existingElement.getX())) {
			existingElement.setX(elementEntity.getX());
		}
		
		if(elementEntity.getY() != null && !elementEntity.getY().equals(existingElement.getY())) {
			existingElement.setY(elementEntity.getY());
		}
		
		if(elementEntity.getName() != null && !elementEntity.getName().equals(existingElement.getName())) {
			existingElement.setName(elementEntity.getName());
		}
		
		this.elements.save(existingElement);
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public List<ElementEntity> getElementByDistance(double x, double y, double distance, String email, String playground) 
			throws DistanceNegativeParamsException, DistanceNotFoundException {
		
		if(x < 0 || y < 0 || distance < 0) {	
			throw new DistanceNegativeParamsException();
		}
		
		
		List<ElementEntity> distanceList = elements.findAllElementsByPlayground(playground).stream()
				.filter(element -> distance(x, y, element.getX(), element.getY()) < distance)
				.collect(Collectors.toList());

		if(distanceList.isEmpty())
			throw new DistanceNotFoundException();
		
		return distanceList;
	}
	
	private double distance(double x0, double y0, double x1, double y1) {
		double deltaX = x1 - x0;
		double deltaY = y1 - y0;
		
		return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
	}

	@Override
	@Transactional
	@MyLog
	@PlaygroundPerformance
	public List<ElementEntity> searchElement(String attributeName, String value, String email, String playground) throws ElementNotFoundException {
		
		List<ElementEntity> searchList = elements.findAllElementsByPlaygroundAndCreatorEmail(playground, email)
				.stream()
				.filter(element -> 
					element.getAttributes().containsKey(attributeName) 
					&& element.getAttributes().get(attributeName).equals(value))
				.collect(Collectors.toList());
		
		return searchList;
	}

	public void setIdGenerator(IdGeneratorService idGenerator) {
		this.idGenerator = idGenerator;
	}
	
	public void setElements(ElementDao elements) {
		this.elements = elements;
	}
}
