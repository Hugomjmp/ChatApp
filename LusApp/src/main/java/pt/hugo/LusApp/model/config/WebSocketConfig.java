package pt.hugo.LusApp.model.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pt.hugo.LusApp.model.component.MessageWebSocketHandler;
import pt.hugo.LusApp.model.controllers.ChatController;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageWebSocketHandler messageWebSocketHandler;

    public WebSocketConfig(MessageWebSocketHandler messageWebSocketHandler) {
        this.messageWebSocketHandler = messageWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(messageWebSocketHandler, "/ws/messages")
                /*.setAllowedOrigins("http://localhost:3000");*/
                .setAllowedOrigins("*");
    }
}
