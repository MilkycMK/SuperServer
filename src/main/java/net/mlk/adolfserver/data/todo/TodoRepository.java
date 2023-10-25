package net.mlk.adolfserver.data.todo;

import net.mlk.jmson.JsonList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TodoRepository extends JpaRepository<TodoElement, Integer> {
    TodoElement findByIdAndNameIgnoreCase(int id, String name);
    @Query(value = "SELECT task_time FROM todo WHERE name=?1", nativeQuery = true)
    JsonList findAllDatesByNameIgnoreCase(String name);
    JsonList findAllByNameIgnoreCaseAndTaskTime(String name, LocalDateTime time);
}
