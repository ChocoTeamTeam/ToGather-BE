package chocoteamteam.togather.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApplicantDto {

	private Long applicantId;
	private Long projectId;
	private Long memberId;
	private String nickname;
	private String profileImage;
	private List<TechStackDto> techStacks;

}
