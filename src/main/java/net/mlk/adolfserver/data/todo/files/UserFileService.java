package net.mlk.adolfserver.data.todo.files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserFileService")
public class UserFileService {
    private static UserFileRepository userFileRepository;

    @Autowired
    protected UserFileService(UserFileRepository userFileRepository) {
        UserFileService.userFileRepository = userFileRepository;
    }

    public static void saveUserFile(UserFile userFile) {
        userFileRepository.save(userFile);
    }

    public static UserFileRepository getUserFileRepository () {
        return userFileRepository;
    }
}
