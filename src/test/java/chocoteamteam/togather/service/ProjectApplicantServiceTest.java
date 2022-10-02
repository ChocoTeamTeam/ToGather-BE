package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import chocoteamteam.togather.dto.ApplicantDto;
import chocoteamteam.togather.dto.ManageApplicantForm;
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
import org.junit.jupiter.api.BeforeEach;
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

	Member owner;
	Member member;
	Project project;

	@BeforeEach
	public void init() {
		owner = Member.builder()
			.id(1L)
			.build();

		project = Project.builder()
			.id(1L)
			.member(owner)
			.build();

		member = Member.builder()
			.id(2L)
			.build();
	}

	@DisplayName("프로젝트 참여 신청 성공")
	@Test
	void applyForProject_success() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(false);

		given(projectRepository.getReferenceById(any()))
			.willReturn(project);

		given(memberRepository.getReferenceById(any()))
			.willReturn(member);

		ArgumentCaptor<Applicant> captor = ArgumentCaptor.forClass(Applicant.class);

		//when
		projectApplicantService.applyForProject(2L, 1L);

		//then
		verify(applicantRepository).save(captor.capture());

		assertThat(captor.getValue().getStatus()).isEqualTo(ApplicantStatus.WAIT);
		assertThat(captor.getValue().getProject().getId()).isEqualTo(project.getId());
		assertThat(captor.getValue().getMember().getId()).isEqualTo(member.getId());
	}

	@DisplayName("프로젝트 참여 신청 실패 - 참여 신청 기록이 존재하는 경우")
	@Test
	void applyForProject_fail() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(true);

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.applyForProject(1L, 1L))
			.isInstanceOf(ApplicantException.class)
			.hasMessage(ErrorCode.ALREADY_APPLY_PROJECT.getErrorMessage());

	}

	@DisplayName("프로젝트 신청자 목록 조회 성공")
	@Test
	void getApplicants_success() {
		//given
		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(project));

		given(applicantRepository.findAllByProjectId(anyLong(), any()))
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
	void getApplicants_fail_notMatchMemberProject() {
		//given
		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(project));

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.getApplicants(1L, 2L))
			.isInstanceOf(ProjectException.class)
			.hasMessage(ErrorCode.NOT_MATCH_MEMBER_PROJECT.getErrorMessage());
	}

	@DisplayName("프로젝트 신청자 관리 성공")
	@Test
	void manageApplicant_success() {
		//given
		Applicant applicant = Applicant.builder()
			.id(1L)
			.status(ApplicantStatus.WAIT)
			.build();

		ManageApplicantForm form = ManageApplicantForm.builder()
			.projectId(project.getId())
			.applicantMemberId(member.getId())
			.projectOwnerMemberId(owner.getId())
			.status(ApplicantStatus.ACCEPTED)
			.build();

		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(project));

		given(applicantRepository.findByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(Optional.of(applicant));

		//when
		projectApplicantService.manageApplicant(form);

		//then
		assertThat(applicant.getStatus()).isEqualTo(form.getStatus());
	}

	@DisplayName("프로젝트 신청자 관리 실패 - 프로젝트가 없는 경우")
	@Test
	void manageApplicant_fail_notFoundProject() {
		//given
		ManageApplicantForm form = ManageApplicantForm.builder()
			.projectId(project.getId())
			.applicantMemberId(member.getId())
			.projectOwnerMemberId(owner.getId())
			.status(ApplicantStatus.ACCEPTED)
			.build();

		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.manageApplicant(form))
			.isInstanceOf(ProjectException.class)
			.hasMessage(ErrorCode.NOT_FOUND_PROJECT.getErrorMessage());
	}

	@DisplayName("프로젝트 신청자 관리 실패 - 프로젝트에 대한 권한이 없는 경우")
	@Test
	void manageApplicant_fail_notMatchMemberProject() {
		//given
		ManageApplicantForm form = ManageApplicantForm.builder()
			.projectId(project.getId())
			.applicantMemberId(member.getId())
			.projectOwnerMemberId(3L)
			.status(ApplicantStatus.ACCEPTED)
			.build();

		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(project));

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.manageApplicant(form))
			.isInstanceOf(ProjectException.class)
			.hasMessage(ErrorCode.NOT_MATCH_MEMBER_PROJECT.getErrorMessage());
	}

	@DisplayName("프로젝트 신청자 관리 실패 - 신청자를 찾을 수 없는 경우")
	@Test
	void manageApplicant_fail_notFoundApplicant() {
		//given
		ManageApplicantForm form = ManageApplicantForm.builder()
			.projectId(project.getId())
			.applicantMemberId(member.getId())
			.projectOwnerMemberId(owner.getId())
			.status(ApplicantStatus.ACCEPTED)
			.build();

		given(projectRepository.findById(anyLong()))
			.willReturn(Optional.of(project));

		given(applicantRepository.findByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.manageApplicant(form))
			.isInstanceOf(ApplicantException.class)
			.hasMessage(ErrorCode.NOT_FOUND_APPLICANT.getErrorMessage());
	}

	@DisplayName("프로젝트 신청자 삭제 성공")
	@Test
	void deleteApplicant_success() {
		//given
		Applicant applicant = Applicant.builder()
			.id(1L)
			.status(ApplicantStatus.WAIT)
			.build();

		given(applicantRepository.findByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(Optional.of(applicant));

		//when
		projectApplicantService.deleteApplicant(1L,1L);

		//then
		verify(applicantRepository).delete(any());
	}

	@DisplayName("프로젝트 신청자 삭제 실패 - 신청자가 존재하지 않는 경우")
	@Test
	void deleteApplicant_fail() {
		//given
		given(applicantRepository.findByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(Optional.empty());

		//when
		//then
		assertThatThrownBy(() -> projectApplicantService.deleteApplicant(1L, 1L));

	}




}