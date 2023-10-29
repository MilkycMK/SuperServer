package net.mlk.adolfserver.data.group.lesson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LessonService")
public class LessonService {
    private static LessonRepository lessonRepository;

    @Autowired
    public LessonService(LessonRepository lessonRepository) {
        LessonService.lessonRepository = lessonRepository;
    }
}
