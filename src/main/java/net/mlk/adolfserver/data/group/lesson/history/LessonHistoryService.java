package net.mlk.adolfserver.data.group.lesson.history;

import net.mlk.adolfserver.data.group.lesson.Lesson;
import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("LessonHistoryService")
public class LessonHistoryService {
    private static LessonHistoryRepository lessonHistoryRepository;

    @Autowired
    public LessonHistoryService(LessonHistoryRepository lessonHistoryRepository) {
        LessonHistoryService.lessonHistoryRepository = lessonHistoryRepository;
    }

    public static LessonHistory findById(int id) {
        return lessonHistoryRepository.findById(id);
    }

    public static List<LessonHistory> findAllByLessonId(int lessonId) {
        return lessonHistoryRepository.findAllByLessonId(lessonId);
    }

    public static List<Json> findAllJsonByLessonId(int lessonId) {
        List<LessonHistory> groups = findAllByLessonId(lessonId);
        return groups.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static void delete(LessonHistory lessonHistory) {
        lessonHistoryRepository.delete(lessonHistory);
    }

    public static void save(LessonHistory lessonHistory) {
        lessonHistoryRepository.save(lessonHistory);
        lessonHistoryRepository.flush();
    }
}
