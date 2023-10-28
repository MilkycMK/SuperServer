package net.mlk.adolfserver.data.user.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Session findByToken(String token);
}
