package chocoteamteam.togather.repository;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.type.ApplicantStatus;
import java.util.List;

public interface QuerydslApplicantRepository {

	List<ApplicantDto> findAllByProjectId(Long projectId, ApplicantStatus status);

}
