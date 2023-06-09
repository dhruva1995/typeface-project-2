package dc.typeface.notificationsessionservice.services;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
public class SessionsManager {

    private final Map<String, Map<String, WebSocketSession>> userSessions = new ConcurrentHashMap<>();

    public boolean userHasSession(final String user) {
        return userSessions.containsKey(user);
    }

    void addSession(final WebSocketSession session) {
        final String user = SessionHandlerService.getUser(session);
        userSessions.computeIfAbsent(user, u -> new ConcurrentHashMap<>())
                .put(session.getId(), session);
    }

    void removeSession(final WebSocketSession session) {
        final String user = SessionHandlerService.getUser(session);
        var sessions = userSessions.get(user);
        if (sessions != null) {
            sessions.remove(session.getId());

            if (sessions.isEmpty()) {
                userSessions.remove(user);
            }
        }
    }

    public Collection<WebSocketSession> getAllSocketSessions(final String user) {
        var sessions = userSessions.get(user);
        if (sessions != null) {
            return sessions.values();
        }
        return Collections.emptyList();
    }
}
