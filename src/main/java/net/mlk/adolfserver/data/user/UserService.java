package net.mlk.adolfserver.data.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    private static UserRepository userRepository;

    @Autowired
    protected UserService(UserRepository userRepository) {
        UserService.userRepository = userRepository;
    }

    public static void saveUser(User user) {
        userRepository.save(user);
        userRepository.flush();
    }

    public static UserRepository getUserRepository() {
        return userRepository;
    }
}