package net.mlk.adolfserver.data.todo;

import net.mlk.jmson.Json;
import net.mlk.jmson.utils.JsonConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("TodoService")
public class TodoService {
    private static TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        TodoService.todoRepository = todoRepository;
    }

    public static TodoElement findByIdAndUserId(int id, int userId) {
        return todoRepository.findByIdAndUserId(id, userId);
    }

    public static List<Json> findAllJsonsByUserId(int userId) {
        List<TodoElement> todo = todoRepository.findAllByUserIdOrderByCreationTime(userId);
        return todo.stream()
                .map(JsonConverter::convertToJson)
                .collect(Collectors.toList());
    }

    public static void delete(TodoElement todoElement) {
        todoRepository.delete(todoElement);
    }

    public static void save(TodoElement todoElement) {
        todoRepository.save(todoElement);
        todoRepository.flush();
    }
}
