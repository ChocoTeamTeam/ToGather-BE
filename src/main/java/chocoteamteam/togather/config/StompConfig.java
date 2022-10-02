package chocoteamteam.togather.config;

import chocoteamteam.togather.component.stomp.ChatErrorHandler;
import chocoteamteam.togather.component.stomp.StompJwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class StompConfig implements WebSocketMessageBrokerConfigurer {

    private final StompJwtHandler stompJwtHandler;
    private final ChatErrorHandler chatErrorHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp/chat")
            .setAllowedOriginPatterns("*");
        registry.setErrorHandler(chatErrorHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher("."));
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
            .setRelayHost("localhost")
            .setVirtualHost("/")
            .setRelayPort(61613)
            .setClientLogin("admin")
            .setClientPasscode("admin")
            .setSystemLogin("admin")
            .setSystemPasscode("admin");

    }
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompJwtHandler);
    }
}
