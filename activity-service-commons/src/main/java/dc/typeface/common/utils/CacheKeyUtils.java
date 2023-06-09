package dc.typeface.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheKeyUtils {

    private static final String NOTIFICATION_PREFIX = "notification-";

    public static String feedCacheKeyFor(String user) {
        return "user-feed-" + user;
    }

    public static String notificationKeyFor(String user) {
        return NOTIFICATION_PREFIX + user;
    }

    public static String extractUserFromNotificationKey(String notificationKey) {
        return notificationKey.substring(NOTIFICATION_PREFIX.length());
    }

}
