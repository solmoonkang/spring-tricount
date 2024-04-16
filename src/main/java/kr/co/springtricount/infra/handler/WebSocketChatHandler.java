package kr.co.springtricount.infra.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.springtricount.service.dto.response.ChatMessageResDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * WebSocketHandler
 * 소켓 통신은 서버와 클라이언트가 1:N으로 관계를 맺는다. 따라서 한 서버에 여러 클라이언트가 접속이 가능하다.
 * 서버에는 여러 클라이언트가 발송한 메시지를 받아 처리해줄 핸들러가 필요하다.
 * TextWebSocketHandler를 상속받아 핸들러를 작성한다.
 * 클라이언트로부터 받은 메시지를 log로 출력하고, 클라이언트에게 환영 메시지를 전송해준다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: { session1, session2 }
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    // 소켓 통신 시 메시지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();
        log.info("payload {}", payload);

        // PAYLOAD를 chatMessageResDTO로 변환
        ChatMessageResDTO chatMessageResDTO = objectMapper.readValue(payload, ChatMessageResDTO.class);
        log.info("session {}", chatMessageResDTO.toString());

        Long chatRoomId = chatMessageResDTO.chatRoomId();

        // 메모리 상 채팅방에 대한 세션이 없다면 만들어준다.
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }

        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }

        sendMessageToChatRoom(chatRoomId, chatMessageResDTO);
    }

    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // ===== 채팅 관련 메서드 =====
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {

        chatRoomSession.removeIf(session -> !sessions.contains(session));
    }

    public void sendMessageToChatRoom(Long chatRoomId,
                                      ChatMessageResDTO chatMessageResDTO) {

        Set<WebSocketSession> chatRoomSessions = chatRoomSessionMap.get(chatRoomId);

        if (chatRoomSessions != null) {
            chatRoomSessions.parallelStream().forEach(session -> sendMessage(session, chatMessageResDTO));
        }
    }

    public <T> void sendMessage(WebSocketSession webSocketSession, T message) {

        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
