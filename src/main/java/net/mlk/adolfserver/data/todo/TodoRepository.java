package net.mlk.adolfserver.data.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoElement, Integer> {
    TodoElement findByIdAndUserId(int id, int userId);
    List<TodoElement> findAllByUserIdOrderByCreationTime(int userId);
}
