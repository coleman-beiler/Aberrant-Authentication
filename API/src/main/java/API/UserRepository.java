package API;

import org.springframework.data.repository.CrudRepository;
import API.User;

public interface UserRepository extends CrudRepository<User, Integer> {

}

