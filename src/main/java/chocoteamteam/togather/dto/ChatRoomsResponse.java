package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomsResponse {

    private Long projectId;
    private List<ChatRoomDto> chatRoomDtos;

}
