package org.jiahuan.common.config;

import org.jiahuan.common.filter.CustomWebSocketInterceptorFilter;
import org.jiahuan.websocket.CustomWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class CustomWebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customWebSocketHandler(), "/webSocketBySpring/customWebSocketHandler").addInterceptors(new CustomWebSocketInterceptorFilter()).setAllowedOrigins("*");
        registry.addHandler(customWebSocketHandler(), "/sockjs/webSocketBySpring/customWebSocketHandler").addInterceptors(new CustomWebSocketInterceptorFilter()).setAllowedOrigins("*").withSockJS();
    }

    @Bean
    public CustomWebSocketHandler customWebSocketHandler() {
        return new CustomWebSocketHandler();
    }
}
