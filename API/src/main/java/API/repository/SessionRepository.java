package API.repository;

import API.persistance.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface SessionRepository extends CrudRepository<Session, Integer> {

    Session findBySessionToken(String sessionToken);

    Boolean existsBySessionToken(String sessionToken);

    @Transactional
    void deleteBySessionToken(String sessionToken);
}
