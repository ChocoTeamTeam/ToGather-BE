package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import chocoteamteam.togather.entity.Applicant;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ApplicantException;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.repository.ApplicantRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ApplicantStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProjectApplyServiceTest {

	@Mock
	ProjectRepository projectRepository;

	@Mock
	ApplicantRepository applicantRepository;

	@Mock
	MemberRepository memberRepository;

	@InjectMocks
	ProjectApplyService projectApplyService;

	@DisplayName("프로젝트 참여 신청 성공")
	@Test
	void applyProject_success() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(false);

		given(projectRepository.getReferenceById(any()))
			.willReturn(Project.builder().id(1L).build());

		given(memberRepository.getReferenceById(any()))
			.willReturn(Member.builder().id(1L).build());

		ArgumentCaptor<Applicant> captor = ArgumentCaptor.forClass(Applicant.class);

		//when
		projectApplyService.applyProject(1L, 1L);

		//then
		verify(applicantRepository).save(captor.capture());

		assertThat(captor.getValue().getStatus()).isEqualTo(ApplicantStatus.WAIT);
		assertThat(captor.getValue().getProject().getId()).isEqualTo(1L);
		assertThat(captor.getValue().getMember().getId()).isEqualTo(1L);
	}

	@DisplayName("프로젝트 참여 신청 실패 - 참여 신청 기록이 존재하는 경우")
	@Test
	void applyProject_fail() {
		//given
		given(applicantRepository.existsByProjectIdAndMemberId(anyLong(), anyLong()))
			.willReturn(true);

		//when
		//then
		assertThatThrownBy(() -> projectApplyService.applyProject(1L, 1L))
			.isInstanceOf(ApplicantException.class)
			.hasMessage(ErrorCode.ALREADY_APPLY_PROJECT.getErrorMessage());

	}


}