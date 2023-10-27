package net.mlk.adolfserver.data.todo;

import net.mlk.jmson.JsonList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<TodoElement, Integer> {
    TodoElement findByIdAndUserName(int id, String name);
    @Query(value = "SELECT CONVERT(task_time, DATE) FROM todo WHERE user_name=?1", nativeQuery = true)
    JsonList findAllDatesByName(String name);
    @Query(value = "SELECT * FROM todo WHERE user_name=?1 AND DATE(task_time)=?2", nativeQuery = true)
    List<TodoElement> findAllByUserNameAndTaskTime(String name, LocalDate time);
}
