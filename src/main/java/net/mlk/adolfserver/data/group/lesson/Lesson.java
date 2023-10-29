package net.mlk.adolfserver.data.group.lesson;

import jakarta.persistence.*;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;

@Table(name = "lessons")
@Entity
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "group_id")
    @JsonIgnore
    private int groupId;
    @Column(name = "name")
    @JsonField(key = "name")
    private String name;
    private int hours;
    @Column(name = "passed_hours")
    @JsonField(key = "passed_hours")
    private int passedHours;

    public int getGroupId() {
        return this.groupId;
    }

    public String getName() {
        return this.name;
    }

    public int getHours() {
        return this.hours;
    }

    public int getPassedHours() {
        return this.passedHours;
    }

    public int getId() {
        return this.id;
    }
}
