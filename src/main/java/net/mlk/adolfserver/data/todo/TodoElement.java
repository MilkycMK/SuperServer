package net.mlk.adolfserver.data.todo;

import jakarta.persistence.*;
import net.mlk.adolfserver.data.converters.ListConverter;
import net.mlk.jmson.JsonList;
import net.mlk.jmson.utils.JsonConvertible;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
public class TodoElement implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String header;
    private String description;
    @Convert(converter = ListConverter.class)
    private JsonList files;
    @Column(name = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "task_time")
    private LocalDateTime taskTime;

    protected TodoElement() {

    }

    public TodoElement(String name, String header, LocalDateTime creationTime) {
        this(name, header, null, creationTime);
    }

    public TodoElement(String name, String header, String description, LocalDateTime creationTime) {
        this(name, header, description, null, creationTime);
    }

    public TodoElement(String name, String header, String description, JsonList files, LocalDateTime creationTime) {
        this(name, header, description, files, creationTime, null);
    }

    public TodoElement(String name, String header, String description, JsonList files, LocalDateTime creationTime, LocalDateTime taskTime) {
        this.setName(name);
        this.setHeader(header);
        this.setDescription(description);
        this.setFiles(files);
        this.setCreationTime(creationTime);
        this.setTaskTime(taskTime);
        TodoService.saveTodo(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return this.header;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFiles(JsonList files) {
        this.files = files;
    }

    public JsonList getFiles() {
        return this.files;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.taskTime = taskTime;
    }

    public LocalDateTime getTaskTime() {
        return this.taskTime;
    }

    public int getId() {
        return this.id;
    }
}
