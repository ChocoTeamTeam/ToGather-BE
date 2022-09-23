package chocoteamteam.togather.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChatMessageDto {

	private String nickname;
	private String profileImage;
	private String message;
	private LocalDateTime time;

}
