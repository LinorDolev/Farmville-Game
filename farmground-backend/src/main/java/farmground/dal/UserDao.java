package farmground.dal;

import org.springframework.data.repository.CrudRepository;

import farmground.logic.UserEntity;

public interface UserDao extends CrudRepository<UserEntity, String>{

}
