package net.mlk.adolfserver.data.todo;

import jakarta.persistence.*;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.adolfserver.data.todo.files.UserFile;
import net.mlk.adolfserver.data.todo.files.UserFileService;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.utils.JsonConvertible;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "todo")
public class TodoElement implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_name")
    @JsonField(key = "user_name")
    private String userName;
    private String header;
    private String description;
    @Column(name = "creation_time")
    @JsonField(key = "creation_time")
    private LocalDateTime creationTime;
    @Column(name = "task_time")
    @JsonField(key = "task_time")
    private LocalDateTime taskTime;
    @OneToMany
    @JoinColumns({
            @JoinColumn(updatable=false,insertable=false, name="task_id", referencedColumnName="id"),
            @JoinColumn(updatable=false,insertable=false, name="user_name", referencedColumnName="user_name"),
        })
    @JsonField(key = "user_files")
    public List<UserFile> userFiles = new ArrayList<>();

    protected TodoElement() {

    }

    public TodoElement(String userName, String header, LocalDateTime creationTime) {
        this(userName, header, null, creationTime);
    }

    public TodoElement(String userName, String header, String description, LocalDateTime creationTime) {
        this(userName, header, description, null, creationTime);
    }

    public TodoElement(String userName, String header, String description, MultipartFile[] files, LocalDateTime creationTime) {
        this(userName, header, description, files, creationTime, null);
    }

    public TodoElement(String userName, String header, String description, MultipartFile[] files, LocalDateTime creationTime, LocalDateTime taskTime) {
        this.userName = userName;
        this.header = header;
        this.description = description;
        this.creationTime = creationTime;
        this.taskTime = taskTime;
        TodoService.saveTodo(this);
        if (files != null) {
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() != null && !file.getOriginalFilename().isEmpty()) {
                    this.userFiles.add(new UserFile(this.userName, file, this.id));
                }
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getHeader() {
        return this.header;
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

    public void deleteFiles() {
        for (UserFile file : this.userFiles) {
            UserFileService.getUserFileRepository().delete(file);
        }
        this.deleteDirectory(new File(String.format(AdolfServerApplication.FILES_PATH_TEMPLATE, this.userName, this.id)));
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
