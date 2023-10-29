package net.mlk.adolfserver.api.groups;

import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.group.GroupService;
import net.mlk.adolfserver.data.group.lesson.Lesson;
import net.mlk.adolfserver.data.group.lesson.LessonService;
import net.mlk.adolfserver.data.group.lesson.history.LessonHistory;
import net.mlk.adolfserver.data.group.lesson.history.LessonHistoryService;
import net.mlk.adolfserver.data.user.session.Session;
import net.mlk.adolfserver.errors.ResponseError;
import net.mlk.adolfserver.utils.AdolfUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class LessonController {

    @PostMapping(path = {"/groups/{gId}/lessons", "/groups/{gId}/lessons/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createLesson(@PathVariable String gId,
                                          @RequestParam String name,
                                          @RequestParam int hours,
                                          @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);

        ResponseEntity<ResponseError> validate;
        if ((validate = validateLessonValues(name, groupId, hours)) != null) {
            return validate;
        }

        Lesson lesson = new Lesson(groupId, name, hours);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/groups/" + groupId + "/lessons/" + lesson.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping(path = {"/groups/{gId}/lessons/{lId}/history", "/groups/{gId}/lessons/{lId}/history/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> createLessonHistory(@PathVariable String gId,
                                                      @PathVariable String lId,
                                                      @RequestParam String date,
                                                      @RequestParam(required = false) String topic,
                                                      @RequestAttribute Session session) {
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(lId);
        Lesson lesson;

        ResponseEntity<ResponseError> validate = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if ((lesson = LessonService.findById(lessonId)) == null ||
                (validate = validateLessonHistoryValues(groupId, date, topic)) != null) {
            return validate;
        } else if (lesson.getHours() == lesson.getPassedHours()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        LessonHistory lessonHistory =
                new LessonHistory(lessonId, LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT), topic);
        lesson.addLessonHours();
        LessonService.save(lesson);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/groups/" + groupId + "/lessons/" + lessonId + "/history/" + lessonHistory.getId());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @GetMapping(path = {"/groups/{gId}/lessons", "/groups/{gId}/lessons/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLessons(@PathVariable String gId,
                                                    @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);

        if (GroupService.findById(groupId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(LessonService.findByGroupId(groupId).toString(), HttpStatus.OK);
    }

    @GetMapping(path = {"/groups/{gId}/lessons/{aId}/history", "/groups/{gId}/lessons/{aId}/history/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLessonHistory(@PathVariable String gId,
                                                   @PathVariable String aId,
                                                   @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(aId);

        if (GroupService.findById(groupId) == null || LessonService.findById(lessonId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(LessonHistoryService.findAllJsonByLessonId(lessonId).toString(), HttpStatus.OK);
    }


    @PatchMapping(path = {"/groups/{gId}/lessons/{lId}/history/{aId}", "/groups/{gId}/lessons/{lId}/{aId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/groups/{gId}/lessons/{lId}/history/{aId}", "/groups/{gId}/lessons/{lId}/history/{aId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> updateLessonArchive(@PathVariable String gId,
                                                             @PathVariable String lId,
                                                             @PathVariable String aId,
                                                             @RequestParam String date,
                                                             @RequestParam String topic,
                                                             @RequestAttribute Session session) {
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(lId);
        int historyId = AdolfUtils.tryParseInteger(aId);
        LessonHistory lessonHistory;

        ResponseEntity<ResponseError> validate = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (LessonService.findById(lessonId) == null ||
                (lessonHistory = LessonHistoryService.findById(historyId)) == null ||
                (validate = validateLessonHistoryValues(groupId, date, topic)) != null) {
            return validate;
        }
        lessonHistory.setDate(LocalDate.parse(date, AdolfServerApplication.DATE_FORMAT));
        lessonHistory.setTopic(topic);
        LessonHistoryService.save(lessonHistory);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = {"/groups/{gId}/lessons/{lId}", "/groups/{gId}/lessons/{lId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(path = {"/groups/{gId}/lessons/{lId}", "/groups/{gId}/lessons/{lId}/"},
            method = RequestMethod.POST,
            headers = {"X-HTTP-Method-Override=PATCH"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> updateLesson(@PathVariable String gId,
                                                      @PathVariable String lId,
                                                      @RequestParam String name,
                                                      @RequestParam int hours,
                                                      @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(lId);
        Lesson lesson;

        ResponseEntity<ResponseError> validate = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if ((lesson = LessonService.findById(lessonId)) == null ||
                (validate = validateLessonValues(name, groupId, hours)) != null) {
            return validate;
        }

        lesson.setName(name);
        lesson.setHours(hours);
        LessonService.save(lesson);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/groups/{gId}/lessons/{lId}/history/{aId}", "/groups/{gId}/lessons/{lId}/history/{aId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteHistoryLesson(@PathVariable String gId,
                                                             @PathVariable String lId,
                                                             @PathVariable String aId,
                                                             @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(lId);
        int historyLessonId = AdolfUtils.tryParseInteger(aId);
        LessonHistory lessonHistory;
        Lesson lesson;

        if (GroupService.findById(groupId) == null || (lesson = LessonService.findById(lessonId)) == null
                || (lessonHistory = LessonHistoryService.findById(historyLessonId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        lesson.removeLessonHours();
        LessonService.save(lesson);
        LessonHistoryService.delete(lessonHistory);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = {"/groups/{gId}/lessons/{lId}", "/groups/{gId}/lessons/{lId}/"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResponseError> deleteLesson(@PathVariable String gId,
                                                      @PathVariable String lId,
                                                      @RequestAttribute Session session) {
        int userId = session.getUserId();
        int groupId = AdolfUtils.tryParseInteger(gId);
        int lessonId = AdolfUtils.tryParseInteger(lId);
        Lesson lesson;

        if (GroupService.findById(groupId) == null || (lesson = LessonService.findById(lessonId)) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LessonService.delete(lesson);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private static ResponseEntity<ResponseError> validateLessonValues(String name, int groupId, int hours) {
        if (GroupService.findById(groupId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (name.isBlank()) {
            return new ResponseEntity<>(new ResponseError("Lesson name can't be empty."), HttpStatus.BAD_REQUEST);
        } else if (name.length() > 64) {
            return new ResponseEntity<>(new ResponseError("Lesson name length can't be > 64."), HttpStatus.BAD_REQUEST);
        } else if (LessonService.findByGroupIdAndName(groupId, name) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else if (hours < 2 || hours % 2 != 0) {
            return new ResponseEntity<>(new ResponseError("Hours can't be < 2 and odd."), HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private static ResponseEntity<ResponseError> validateLessonHistoryValues(int groupId, String date, String topic) {
        if (GroupService.findById(groupId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (!AdolfUtils.compareDateFormat(date)) {
            return new ResponseEntity<>(new ResponseError("Wrong date format."), HttpStatus.BAD_REQUEST);
        } else if (topic != null && topic.length() > 255) {
            return new ResponseEntity<>(new ResponseError("topic length can't be > 255"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}