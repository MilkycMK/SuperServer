package net.mlk.adolfserver.data.group.lesson;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
    Lesson findByGroupIdAndName(int grouId, String name);
    List<Lesson> findByGroupId(int grouId);
    Lesson findById(int id);
}
