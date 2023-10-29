package net.mlk.adolfserver.data.group.lesson.history;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDate;

@Table(name = "lessons_history")
@Entity
public class LessonHistory implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lesson_id")
    @JsonIgnore
    private int lessonId;
    @Column(name = "number")
    @JsonField(key = "number")
    private int number;
    @Column(name = "date")
    @JsonField(key = "date")
    private LocalDate date;
    private String topic;

    protected LessonHistory() {

    }

    public LessonHistory(int lessonId, LocalDate date, String topic) {
        this.lessonId = lessonId;
        this.date = date;
        this.topic = topic;
        this.number = LessonHistoryService.findAllByLessonId(lessonId).size() + 1;
        LessonHistoryService.save(this);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getId() {
        return this.id;
    }

    public int getLessonId() {
        return this.lessonId;
    }

    public int getNumber() {
        return this.number;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public String getTopic() {
        return this.topic;
    }
}
