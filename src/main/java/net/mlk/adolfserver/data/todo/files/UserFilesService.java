package net.mlk.adolfserver.data.todo.files;

import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("UserFilesService")
public class UserFilesService {
    private static UserFilesRepository userFilesRepository;

    @Autowired
    public UserFilesService(UserFilesRepository userFilesRepository) {
        UserFilesService.userFilesRepository = userFilesRepository;
    }

    public static UserFile findByIdAndTodoId(int id, int todoId) {
        return userFilesRepository.findByIdAndTodoId(id, todoId);
    }

    public static UserFile findByTodoIdAndName(int taskId, String fileName) {
        return userFilesRepository.findByTodoIdAndName(taskId, fileName);
    }

    public static List<Json> findAllJsonByTodoId(int todoId) {
        List<UserFile> files = userFilesRepository.findAllByTodoId(todoId);
        return files.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static List<UserFile> findAllByTodoId(int todoId) {
        return userFilesRepository.findAllByTodoId(todoId);
    }

    public static void save(UserFile userFile) {
        userFilesRepository.save(userFile);
    }

    public static void delete(UserFile userFile) {
        userFilesRepository.delete(userFile);
    }
}
