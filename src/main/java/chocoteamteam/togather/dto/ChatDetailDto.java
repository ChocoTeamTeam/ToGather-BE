package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatDetailDto {
	private long roomId;
	private List<ChatMessageDto> messages;
}
