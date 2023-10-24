package net.mlk.adolfserver.data.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SessionService")
public class SessionService {
    private static SessionRepository sessionRepository;

    @Autowired
    protected SessionService(SessionRepository sessionRepository) {
        SessionService.sessionRepository = sessionRepository;
    }

    public static void saveSession(Session session) {
        sessionRepository.save(session);
    }

    public static SessionRepository getSessionRepository() {
        return sessionRepository;
    }
}
