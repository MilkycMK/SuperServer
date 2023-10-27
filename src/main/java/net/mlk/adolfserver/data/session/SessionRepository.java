package net.mlk.adolfserver.data.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByUserNameAndToken(String name, String token);
    Session findByToken(String token);
}
