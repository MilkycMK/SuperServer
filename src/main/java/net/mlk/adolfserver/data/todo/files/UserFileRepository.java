package net.mlk.adolfserver.data.todo.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Integer> {
}
