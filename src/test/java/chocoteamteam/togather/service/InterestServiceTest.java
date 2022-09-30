package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.dto.InterestDetail;
import chocoteamteam.togather.entity.Interest;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.InterestException;
import chocoteamteam.togather.repository.InterestRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.ProjectStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InterestServiceTest {

    @Mock
    InterestRepository interestRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    ProjectRepository projectRepository;
    @InjectMocks
    InterestService interestService;


    @DisplayName("관심 공고 추가 - 성공")
    @Test
    void add_success() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(
            Optional.ofNullable(Member.builder().id(1L).build()));
        given(projectRepository.findById(anyLong())).willReturn(
            Optional.ofNullable(Project.builder().id(1L).build()));

        // when
        String result = interestService.addOrRemove(1L, 1L);

        // then
        assertThat(result).isEqualTo("add");
    }

    @DisplayName("관심 공고 취소 - 성공")
    @Test
    void remove_success() {
        // given
        given(interestRepository.findByMemberIdAndProjectId(anyLong(), anyLong()))
            .willReturn(Optional.ofNullable(Interest.builder()
                .id(1L)
                .member(Member.builder().id(1L).build())
                .project(Project.builder().id(1L).build())
                .build()));

        // when
        String result = interestService.addOrRemove(1L, 1L);

        // then
        assertThat(result).isEqualTo("remove");
    }

    @DisplayName("관심 공고 추가 - 실패 회원 없음")
    @Test
    void add_failed_NOT_FOUND_MEMBER() {
        // given

        // when

        // then
        assertThatThrownBy(() -> interestService.addOrRemove(1L, 1L))
            .isInstanceOf(InterestException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getErrorMessage());
    }

    @DisplayName("관심 공고 추가 - 실패 프로젝트 없음")
    @Test
    void add_failed_NOT_FOUND_PROJECT() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(
            Optional.ofNullable(Member.builder().id(1L).build()));
        // when

        // then
        assertThatThrownBy(() -> interestService.addOrRemove(1L, 1L))
            .isInstanceOf(InterestException.class)
            .hasMessage(ErrorCode.NOT_FOUND_PROJECT.getErrorMessage());
    }

    @DisplayName("관심 공고 조회 - 성공")
    @Test
    void getDetails_success() {
        // given
        LocalDate now = LocalDate.now();
        given(interestRepository.findAllByMemberId(anyLong()))
            .willReturn(List.of(Interest.builder()
                .project(Project.builder()
                    .id(1L)
                    .build())
                .build()));
        given(projectRepository.findAllInterestProjectByIds(any()))
            .willReturn(List.of(InterestDetail.builder()
                .projectId(1L)
                .title("title")
                .writer("writer")
                .status(ProjectStatus.RECRUITING)
                .deadline(now)
                .build()));

        // when
        List<InterestDetail> details = interestService.getDetails(1L);

        // then
        assertThat(details.get(0).getProjectId()).isEqualTo(1L);
        assertThat(details.get(0).getTitle()).isEqualTo("title");
        assertThat(details.get(0).getWriter()).isEqualTo("writer");
        assertThat(details.get(0).getStatus()).isEqualTo(ProjectStatus.RECRUITING);
        assertThat(details.get(0).getDeadline()).isEqualTo(now);
    }

}