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
public class CreateChatRoomForm {
	private long memberId;
	private long projectId;

	@NotBlank
	private String roomName;


}
