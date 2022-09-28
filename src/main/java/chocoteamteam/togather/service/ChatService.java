package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.ChatMessageDto;
import chocoteamteam.togather.entity.ChatMessage;
import chocoteamteam.togather.entity.ChatRoom;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.repository.ChatMessageRepository;
import chocoteamteam.togather.repository.ChatRoomRepository;
import chocoteamteam.togather.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final TopicExchange EXCHANGE;


    public void sendMessage(ChatMessageDto chatMessageDto, Long memberId, Long chatRoomId) {

        Member member = memberRepository.getReferenceById(memberId);
        ChatRoom chatRoom = chatRoomRepository.getReferenceById(chatRoomId);

        chatMessageRepository.save(ChatMessage.builder()
            .chatRoom(chatRoom)
            .sender(member)
            .message(chatMessageDto.getMessage())
            .build());

        rabbitTemplate.convertAndSend(EXCHANGE.getName(), "room." + chatRoomId, chatMessageDto);
    }


}
