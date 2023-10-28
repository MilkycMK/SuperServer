package net.mlk.adolfserver.data.todo.files;

import jakarta.persistence.*;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.jmson.annotations.JsonField;
import net.mlk.jmson.utils.JsonConvertible;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Entity
@Table(name = "files")
public class UserFile implements JsonConvertible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "user_id")
    @JsonField(key = "user_id")
    private int userId;
    @Column(name = "file_name")
    @JsonField(key = "file_name")
    private String fileName;
    @Column(name = "task_id")
    @JsonField(key = "task_id")
    private int taskId;

    protected UserFile() {

    }

    public UserFile(int userId, MultipartFile file, int taskId) {
        this.userId = userId;
        this.fileName = file.getOriginalFilename();
        this.taskId = taskId;
        this.createFile(file);
        UserFile f = UserFileService.getUserFileRepository().findByUserIdAndTaskIdAndFileName(userId, taskId, fileName);
        if (f == null) {
            UserFileService.saveUserFile(this);
        }
    }

    public int getId() {
        return this.id;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getTaskId() {
        return this.taskId;
    }

    private String createFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        File filePath = new File(String.format(AdolfServerApplication.FILE_PATH_TEMPLATE, this.userId, this.taskId, file.getOriginalFilename()));
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        try (OutputStream outStream = new FileOutputStream(filePath)) {
            outStream.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileName;
    }

}
