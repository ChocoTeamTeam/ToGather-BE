package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantRepository extends JpaRepository<Applicant, Long>,QuerydslApplicantRepository  {

	boolean existsByProjectIdAndMemberId(Long projectId, Long applicantId);

}
