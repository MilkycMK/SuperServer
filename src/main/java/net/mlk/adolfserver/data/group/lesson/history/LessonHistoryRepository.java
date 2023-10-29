package net.mlk.adolfserver.data.group.lesson.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonHistoryRepository extends JpaRepository<LessonHistory, Integer> {
}
