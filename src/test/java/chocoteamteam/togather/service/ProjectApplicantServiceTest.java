package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.entity.Applicant;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ApplicantException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.ApplicantRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectApplicantServiceTest {

	@Mock
	ProjectRepository projectRepository;

	@Mock
	ApplicantRepository applicantRepository;

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	ProjectApplicantService projectApplicantService;

	@DisplayName("프로젝트 참여 신청 성공")
	@Test
	void addApplcant_success() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(false);

		given(projectRepository.getReferenceById(any()))
			.willReturn(Project.builder().id(1L).build());

		given(memberRepository.getReferenceById(any()))
			.willReturn(Member.builder().id(1L).build());

		ArgumentCaptor<Applicant> captor = ArgumentCaptor.forClass(Applicant.class);

		//when
		projectApplicantService.addApplicant(1L, 1L);

		//then
		verify(applicantRepository).save(captor.capture());

		assertThat(captor.getValue().getStatus()).isEqualTo(ApplicantStatus.WAIT);
		assertThat(captor.getValue().getProject().getId()).isEqualTo(1L);
		assertThat(captor.getValue().getMember().getId()).isEqualTo(1L);
	}

	@DisplayName("프로젝트 참여 신청 실패 - 참여 신청 기록이 존재하는 경우")
	@Test
	void addApplicant_fail() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(true);

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.addApplicant(1L, 1L))
			.isInstanceOf(ApplicantException.class)
			.hasMessage(ErrorCode.ALREADY_APPLY_PROJECT.getErrorMessage());

	}

	@DisplayName("프로젝트 신청자 목록 조회 성공")
	@Test
	void getApplicants_success(){
	    //given
		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(Project.builder()
					.member(Member.builder()
						.id(1L)
						.build())
				.build()));

		given(applicantRepository.findAllByProjectId(anyLong(),any()))
			.willReturn(Arrays.asList(ApplicantDto.builder()
				.applicantId(1L)
				.memberId(1L)
				.build()
			));

	    //when
		List<ApplicantDto> applicants = projectApplicantService.getApplicants(1L, 1L);

		//then
		assertThat(applicants.get(0).getApplicantId()).isEqualTo(1L);
	}

	@DisplayName("프로젝트 신청자 목록 조회 실패 - 프로젝트 관리자가 아닌 경우")
	@Test
	void getApplicants_fail_notMatchMemberProject(){
	    //given
		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(Project.builder()
					.member(Member.builder()
						.id(1L)
						.build())
				.build()));

	    //when
		//then
		assertThatThrownBy(() -> projectApplicantService.getApplicants(1L, 2L))
			.isInstanceOf(ProjectException.class)
			.hasMessage(ErrorCode.NOT_MATCH_MEMBER_PROJECT.getErrorMessage());
	}




}