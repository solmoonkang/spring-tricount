package kr.co.springtricount.infra.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.springtricount.infra.exception.UnauthorizedAccessException;
import kr.co.springtricount.infra.response.ResponseStatus;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final RedisTemplate<String, String> redisTemplate;

    private final ObjectMapper objectMapper;

    public void addChatMessageToRedisList(String key, ChatMessageResDTO chatMessage) {

        try {
            String chatMessageJson = objectMapper.writeValueAsString(chatMessage);
            redisTemplate.opsForList().rightPush(key, chatMessageJson);
        } catch (JsonProcessingException e) {
            log.error("Chat message could not be serialized: {}", e.getMessage());
            throw new UnauthorizedAccessException(ResponseStatus.FAIL_UNAUTHORIZED);
        }
    }
}
