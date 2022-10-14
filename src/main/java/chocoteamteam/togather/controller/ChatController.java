package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.ChatMessageDto;
import chocoteamteam.togather.service.ChatService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("chat.{chatRoomId}.message")
    public void send(Principal principal, ChatMessageDto chatMessageDto,
        @DestinationVariable Long chatRoomId) {
        chatService.sendMessage(chatMessageDto, Long.valueOf(principal.getName()), chatRoomId);
    }

}
