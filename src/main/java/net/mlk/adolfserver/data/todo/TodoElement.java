package net.mlk.adolfserver.data.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "todo")
public class TodoElement {
    @Id
    private int id;
    private String name;
    private String header;
    private String description;
    private String files;
    private LocalDateTime creation_time;
    private LocalDateTime task_time;

    protected TodoElement() {

    }

    public TodoElement(String name, String header, LocalDateTime creation_time) {
        this(name, header, null, creation_time);
    }

    public TodoElement(String name, String header, String description, LocalDateTime creation_time) {
        this(name, header, description, null, creation_time);
    }

    public TodoElement(String name, String header, String description, List<String> files, LocalDateTime creation_time) {
        this(name, header, description, files, creation_time, null);
    }

    public TodoElement(String name, String header, String description, List<String> files, LocalDateTime creation_time, LocalDateTime task_time) {
        this.setName(name);
        this.setHeader(header);
        this.setDescription(description);
        this.setFiles(files);
        this.setCreationTime(creation_time);
        this.setTaskTime(task_time);
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

    public void setFiles(List<String> files) {
        this.files = files.toString();
    }

    public String getFiles() {
        return this.files;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creation_time = creationTime;
    }

    public LocalDateTime getCreationTime() {
        return this.creation_time;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.task_time = taskTime;
    }

    public LocalDateTime getTaskTime() {
        return this.task_time;
    }
}
