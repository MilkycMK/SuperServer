package net.mlk.adolfserver.data.group.lesson.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonHistoryRepository extends JpaRepository<LessonHistory, Integer> {
    List<LessonHistory> findAllByLessonId(int lessonId);
    LessonHistory findById(int id);
}
