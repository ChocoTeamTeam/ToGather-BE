package chocoteamteam.togather.dto;

import chocoteamteam.togather.entity.ChatRoom;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {

	private long roomId;
	private String roomName;

	public static ChatRoomDto from(ChatRoom entity) {
		return ChatRoomDto.builder()
			.roomId(entity.getId())
			.roomName(entity.getName())
			.build();
	}

	public static List<ChatRoomDto> of(List<ChatRoom> entities) {
		return entities.stream().map(ChatRoomDto::from).collect(Collectors.toList());
	}
}
