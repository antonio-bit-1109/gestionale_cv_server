package org.example.progetto_gestionale_cv_server.WEBSOCKET;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.progetto_gestionale_cv_server.WEBSOCKET.objects.ChatMessage;
import org.example.progetto_gestionale_cv_server.WEBSOCKET.objects.TipoMessaggio;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
// questa classe dovrà notificare gli eventi di disconnessione a tutti gli utenti collegati alla chat.
public class WebSocketEvent_listener {

    private final SimpMessageSendingOperations messagingTemplate;

    // questo metodo dovrà notificare gli utenti collegati alla chat
    // che un utente si è disconnesso
    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("User disconnected: {}", username);
            var chatMessage = ChatMessage.builder()
                    .messageType(TipoMessaggio.LEAVE)
                    .sender(username)
                    .build();

            // sto notificando lla queue /topic/public che un utente si è disconnesso
            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
