package net.mlk.adolfserver.data.group.lesson.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("LessonHistoryService")
public class LessonHistoryService {
    private static LessonHistoryRepository lessonHistoryRepository;

    @Autowired
    public LessonHistoryService(LessonHistoryRepository lessonHistoryRepository) {
        LessonHistoryService.lessonHistoryRepository = lessonHistoryRepository;
    }
}
