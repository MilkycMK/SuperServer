package net.mlk.adolfserver.data.todo.files;

import jakarta.persistence.*;
import net.mlk.adolfserver.AdolfServerApplication;
import net.mlk.jmson.annotations.JsonIgnore;
import net.mlk.jmson.utils.JsonConvertible;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Table(name = "files")
@Entity
public class UserFile implements JsonConvertible {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "todo_id")
    @JsonIgnore
    private int todoId;
    private String name;

    protected UserFile() {

    }

    public UserFile(int todoId, MultipartFile file) {
        this.todoId = todoId;
        this.name = file.getOriginalFilename();
        this.createFile(file);
        UserFile f = UserFilesService.findByTodoIdAndName(this.todoId, this.name);
        if (f == null) {
            UserFilesService.save(this);
        }
    }

    public int getId() {
        return this.id;
    }

    public int getTodoId() {
        return this.todoId;
    }

    public String getName() {
        return this.name;
    }

    private void createFile(MultipartFile file) {
        File filePath = new File(String.format(AdolfServerApplication.FILE_PATH_TEMPLATE, this.todoId, this.name));
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        try (OutputStream outStream = new FileOutputStream(filePath)) {
            outStream.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFile() {
        File file = new File(String.format(AdolfServerApplication.FILE_PATH_TEMPLATE, this.todoId, this.name));
        if (file.exists()) {
            file.delete();
        }
        UserFilesService.delete(this);
    }
}
