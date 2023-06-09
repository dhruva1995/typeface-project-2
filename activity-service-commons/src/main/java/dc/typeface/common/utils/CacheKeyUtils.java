package dc.typeface.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CacheKeyUtils {

    public static String feedCacheKeyFor(String user) {
        return "user-feed-" + user;
    }

    public static String notificationKeyFor(String user) {
        return "notification-" + user;
    }

}
