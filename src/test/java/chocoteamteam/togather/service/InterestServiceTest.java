package chocoteamteam.togather.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.InterestException;
import chocoteamteam.togather.repository.InterestRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
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
        interestService.add(1L, 1L, 1L);

        // then
    }

    @DisplayName("관심 공고 추가 - 실패 회원 없음")
    @Test
    void add_failed_NOT_FOUND_MEMBER(){
        // given

        // when

        // then
        assertThatThrownBy(() -> interestService.add(1L, 1L, 1L))
            .isInstanceOf(InterestException.class)
            .hasMessage(ErrorCode.NOT_FOUND_MEMBER.getErrorMessage());
    }

    @DisplayName("관심 공고 추가 - 실패 프로젝트 없음")
    @Test
    void add_failed_NOT_FOUND_PROJECT(){
        // given
        given(memberRepository.findById(anyLong())).willReturn(
            Optional.ofNullable(Member.builder().id(1L).build()));
        // when

        // then
        assertThatThrownBy(() -> interestService.add(1L, 1L, 1L))
            .isInstanceOf(InterestException.class)
            .hasMessage(ErrorCode.NOT_FOUND_PROJECT.getErrorMessage());
    }

    @DisplayName("관심 공고 추가 - 실패 로그인한 회원과 요청한 회원 다름")
    @Test
    void add_failed_no_permission(){
        // given

        // when

        // then
        assertThatThrownBy(() -> interestService.add(1L, 1L, 2L))
            .isInstanceOf(InterestException.class)
            .hasMessage(ErrorCode.NO_PERMISSION.getErrorMessage());
    }



}