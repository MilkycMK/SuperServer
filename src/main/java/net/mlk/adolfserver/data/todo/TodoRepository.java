package net.mlk.adolfserver.data.todo;

import net.mlk.jmson.JsonList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoElement, Integer> {
    TodoElement findByIdAndUserId(int id, int userId);
    @Query(value = "SELECT DATE(task_time) FROM todo WHERE user_id=?1", nativeQuery = true)
    JsonList findAllDatesByUserId(int id);
    @Query(value = "SELECT * FROM todo WHERE user_id=?1 AND DATE(task_time)=?2", nativeQuery = true)
    List<TodoElement> findAllByUserIdAndTaskTime(int userId, LocalDate time);
}
