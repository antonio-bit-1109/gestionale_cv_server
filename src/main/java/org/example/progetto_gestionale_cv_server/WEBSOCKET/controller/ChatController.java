package org.example.progetto_gestionale_cv_server.WEBSOCKET.controller;

import org.example.progetto_gestionale_cv_server.WEBSOCKET.objects.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Metodo per inviare un messaggio all'interno dell chat
    // annotation messageMapping serve per mappare il messaggio in arrivo
    // e chiamare uno specifico metodo.
    // Simile al request mapping in un controller REST classico
    @MessageMapping("/chat.sendMessage")

    // sendto specifica a quale queue deve essere inviato
    // il messaggio ritornato dal metodo che sta venendo chiamato,
    // metodo che in questo caso è sendMessage()
    // mentre la queue è topic,
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        return message;

    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    // metodo per aggiungere un nuovo utente alla chat.
    public ChatMessage addUser(
            @Payload ChatMessage message,
            SimpMessageHeaderAccessor headerAccessor) {

        // aggiunge un nome utente (stringa) nella sessione del web socket
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return message;
    }
}
