package kr.co.springtricount.infra.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP(Simple Text Oriented Message Protocol)
 * 기존 WebSocket 통신 방식을 좀 더 효율적으로 조금 더 쉽게 다룰 수 있게 해주는 프로토콜이다.
 * 해당 프로토콜은 publish, subscribe 개념으로 동작한다.
 *  - 클라이언트가 서버로 메시지를 보내는 것을 publish(pub),
 *  - 클라이언트가 서버로부터 메시지를 받는 것을 메시지를 구독한다는 개념으로 subscribe(sub)이 사용된다.
 */
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableSimpleBroker("/sub");                    // 해당 주소를 구독하고 있는 클라이언트에게 메시지 전달
        registry.setApplicationDestinationPrefixes("/pub");     // 클라이언트에서 보낸 메시지를 받을 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws-stomp")   // SockJS 연결 주소
                .withSockJS();              // 버전이 낮은 브라우저에서도 적용이 가능

        // 주소: "ws://localhost:8080/ws-stomp"
    }
}
