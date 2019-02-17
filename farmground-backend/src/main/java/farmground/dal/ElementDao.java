package farmground.dal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import farmground.logic.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String> {
	Page<ElementEntity> findAllByMoreAttributesJsonLike(@Param("value") String value, Pageable pageable);
	
	Page<ElementEntity> findAllElementsByPlayground(
			@Param("playground") String playground, Pageable pageable);
	

	List<ElementEntity> findAllElementsByPlayground(@Param("playground") String playground);
	
	List<ElementEntity> findAllElementsByPlaygroundAndCreatorEmail(
			@Param("playground") String playground, @Param("creatorEmail") String creatorEmail);
	
	Optional<ElementEntity> findOptionalByType(@Param("type") String type);
}
