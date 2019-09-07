package API;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface UserRepository extends CrudRepository<User, Integer> {
    List<User> findByUsername(String username);
    List<User> findById(int id);
    @Transactional
    Integer deleteByUsername(String username);
}

