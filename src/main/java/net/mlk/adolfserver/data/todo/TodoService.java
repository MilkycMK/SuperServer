package net.mlk.adolfserver.data.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("TodoService")
public class TodoService {
    private static TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        TodoService.todoRepository = todoRepository;
    }

    public static void saveTodo(TodoElement element) {
        todoRepository.save(element);
        todoRepository.flush();
    }

    public static TodoRepository getTodoRepository() {
        return todoRepository;
    }
}
