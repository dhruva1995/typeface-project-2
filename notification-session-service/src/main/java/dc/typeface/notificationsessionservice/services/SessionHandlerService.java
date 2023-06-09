package dc.typeface.notificationsessionservice.services;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import dc.typeface.common.utils.CacheKeyUtils;
import dc.typeface.notificationsessionservice.consumer.NotificationDispatcher;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SessionHandlerService extends TextWebSocketHandler {

    @Autowired
    private SessionsManager sessionsManager;

    @Autowired
    private RedisMessageListenerContainer listnerContainer;

    @Autowired
    private NotificationDispatcher notificationDispatcher;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        final String user = getUser(session);
        log.trace("Creating session for user {} with session id {}",
                user, session.getId());
        if (!sessionsManager.userHasSession(user)) {
            listnerContainer.addMessageListener(notificationDispatcher,
                    new ChannelTopic(CacheKeyUtils.notificationKeyFor(user)));
        }
        sessionsManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        final String user = getUser(session);
        log.trace("Deleting session for user {} with session id {}",
                user, session.getId());
        sessionsManager.removeSession(session);
        if (!sessionsManager.userHasSession(user)) {
            listnerContainer.removeMessageListener(notificationDispatcher,
                    new ChannelTopic(CacheKeyUtils.notificationKeyFor(user)));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("Error encountered for session with id {}", session.getId(), exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    static String getUser(WebSocketSession session) {
        URI uriObj = session.getUri();
        if (uriObj == null) {
            log.error("URI not found in the session with id {}", session.getId());
            throw new RuntimeException("URI is null unable to detect the user");
        }
        String uri = uriObj.toString();
        String[] tokens = uri.split("/");
        return tokens[tokens.length - 1];
    }

}
