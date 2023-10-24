package net.mlk.adolfserver.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByNameIgnoreCase(String name);
    User findByNameIgnoreCase(String name);
}
