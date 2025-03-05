package org.example.progetto_gestionale_cv_server.WEBSOCKET.objects;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private TipoMessaggio messageType;
}
