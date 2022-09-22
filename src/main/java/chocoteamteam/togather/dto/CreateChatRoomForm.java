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
	private Long memberId;
	private Long projectId;

	@NotBlank
	private String roomName;


}
