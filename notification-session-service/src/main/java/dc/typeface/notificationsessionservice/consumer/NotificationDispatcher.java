package dc.typeface.notificationsessionservice.consumer;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import dc.typeface.common.models.Activity;
import dc.typeface.common.utils.CacheKeyUtils;
import dc.typeface.notificationsessionservice.services.SessionsManager;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class NotificationDispatcher implements MessageListener {

    @Autowired
    private SessionsManager sessionManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, @Nullable byte[] arg1) {
        try {
            String channelName = new String(message.getChannel(), StandardCharsets.UTF_8);
            GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
            Activity activity = serializer.deserialize(message.getBody(), Activity.class);
            String result = objectMapper.writeValueAsString(activity);
            String user = CacheKeyUtils.extractUserFromNotificationKey(channelName);
            log.trace("Received the activity {} on channel {}", result, channelName);
            for (var session : sessionManager.getAllSocketSessions(user)) {
                if (session.isOpen())
                    session.sendMessage(new TextMessage(result));
            }
        } catch (Exception e) {
            log.error("Error while notifying message", e);
        }
    }

}
