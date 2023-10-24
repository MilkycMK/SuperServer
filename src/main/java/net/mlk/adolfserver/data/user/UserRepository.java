package net.mlk.adolfserver.data.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByNameIgnoreCase(String name);
}
