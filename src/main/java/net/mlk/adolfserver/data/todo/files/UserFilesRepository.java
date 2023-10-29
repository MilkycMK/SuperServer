package net.mlk.adolfserver.data.todo.files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFilesRepository extends JpaRepository<UserFile, Integer> {
    List<UserFile> findAllByTodoId(int todoId);
    UserFile findByIdAndTodoId(int id, int todoId);
    UserFile findByTodoIdAndName(int id, String name);
}
