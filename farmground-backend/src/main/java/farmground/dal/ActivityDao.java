package farmground.dal;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import farmground.logic.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, String> {
	
	Page<ActivityEntity> findAllByTypeAndElementId(@Param("type") String type,@Param("elementId") String elementId, Pageable pageable);
//	public List<ActivityEntity> findAllByTypeAndMoreAttributesJsonLike(String type, String filter, Pageable pageable);
}
