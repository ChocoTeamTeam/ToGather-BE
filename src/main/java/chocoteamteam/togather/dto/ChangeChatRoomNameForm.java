package chocoteamteam.togather.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChangeChatRoomNameForm {
	private long projectId;
	private long memberId;
	private long roomId;

	@NotBlank
	private String roomName;
}
