package chocoteamteam.togather.dto;

import chocoteamteam.togather.type.ApplicantStatus;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ManageApplicantForm {

	private Long projectId;
	private Long applicantMemberId;
	private Long projectOwnerMemberId;
	private ApplicantStatus status;

}
