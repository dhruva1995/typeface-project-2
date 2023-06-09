package dc.typeface.common.utils;

public class CacheKeyUtils {

    public static String feedCacheKeyFor(String user) {
        return "user-feed-" + user;
    }

}
