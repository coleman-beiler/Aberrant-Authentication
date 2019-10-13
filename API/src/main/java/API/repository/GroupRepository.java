package API.repository;

import API.persistance.model.Group;
import API.persistance.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface GroupRepository extends CrudRepository<Group, Integer> {

    Group findByGroupName(String groupName);
    @Transactional
    Integer deleteById(String Id);
    @Transactional
    String deleteByGroupName(String groupName);
}

