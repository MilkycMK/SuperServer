package net.mlk.adolfserver.data.group.lesson;

import net.mlk.adolfserver.data.group.Group;
import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("LessonService")
public class LessonService {
    private static LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        LessonService.lessonRepository = lessonRepository;
    }

    public static Lesson findByGroupIdAndName(int groupId, String name) {
        return lessonRepository.findByGroupIdAndName(groupId, name);
    }

    public static Lesson findById(int lessonId) {
        return lessonRepository.findById(lessonId);
    }

    public static List<Json> findByGroupId(int groupId) {
        List<Lesson> groups = lessonRepository.findByGroupId(groupId);
        return groups.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static void delete(Lesson lesson) {
        lessonRepository.delete(lesson);
    }

    public static void save(Lesson lesson) {
        lessonRepository.save(lesson);
    }
}
