package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomsResponse {

    private Long projectId;
    private List<ChatRoomDto> chatRoomDtos;

}
