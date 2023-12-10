package net.mlk.adolfserver.data.todo;

import jakarta.persistence.*;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.todo.files.UserFile;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "todo")
@Entity
public class TodoElement implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonIgnore
    private int userId;
    private String topic;
    private String description;
    @Column(name = "creation_time")
    @JsonField(key = "creation_time", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;
    @Column(name = "task_time")
    @JsonField(key = "task_time", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime taskTime;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(insertable = false, updatable = false, name = "todo_id", referencedColumnName = "id")
    private List<UserFile> files = new ArrayList<>();

    protected TodoElement() {

    }

    public TodoElement(int userId, String topic, String description, MultipartFile[] files,
                       LocalDateTime creationTime, LocalDateTime taskTime) {
        this.userId = userId;
        this.topic = topic;
        this.description = description;
        this.creationTime = creationTime;
        this.taskTime = taskTime;
        TodoService.save(this);
        if (files != null) {
            this.uploadFiles(files);
        }
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.taskTime = taskTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getCreationTime() {
        return this.creationTime;
    }

    public LocalDateTime getTaskTime() {
        return this.taskTime;
    }

    public List<Integer> uploadFiles(MultipartFile[] files) {
        List<Integer> filesId = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                UserFile newFile = new UserFile(this.id, file);
                filesId.add(newFile.getId());
            }
        }
        return filesId;
    }

    public void deleteLocalFiles() {
        this.deleteDirectory(new File(String.format(AdolfServerApplication.FILES_PATH_TEMPLATE, this.id)));
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
