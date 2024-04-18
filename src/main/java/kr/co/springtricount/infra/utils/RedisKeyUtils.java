package kr.co.springtricount.infra.utils;

public class RedisKeyUtils {

    private static final String CHAT_ROOM = "chatRoom";

    public static String chatRoomKey(Long chatRoomId) {

        return CHAT_ROOM + ":" + chatRoomId;
    }
}
