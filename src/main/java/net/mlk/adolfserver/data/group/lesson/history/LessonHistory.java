package net.mlk.adolfserver.data.group.lesson.history;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;

import java.time.LocalDate;

@Table(name = "lessons_history")
@Entity
public class LessonHistory {
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
