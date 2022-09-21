package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CommentDto;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.entity.Comment;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.ErrorCode;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.CommentRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성")
    void create_Comment() {
        //given
        Member member = Member.builder()
                .id(9L)
                .nickname("두개더")
                .profileImage("img_url")
                .build();
        given(memberRepository.findById(anyLong()))
                .willReturn(Optional.of(member));
        given(projectRepository.findById(anyLong()))
                .willReturn(Optional.of(Project.builder()
                        .id(99L)
                        .build()));
        given(commentRepository.save(any()))
                .willReturn(Comment.builder()
                        .id(123L)
                        .content("댓글 내용")
                        .member(member)
                        .build());
        //when
        CommentDto commentDto = commentService.createComment(123L, "댓글 내용", 9L);
        //then
        assertEquals(123L, commentDto.getId());
        assertEquals("댓글 내용", commentDto.getContent());
        assertEquals(member.getId(), commentDto.getMember().getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void modify_Comment() {
        //given
        Member member = Member.builder()
                .id(9L)
                .nickname("두개더")
                .profileImage("img_url")
                .build();
        given(commentRepository.findByIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.ofNullable(Comment.builder()
                        .id(123L)
                        .content("댓글 내용")
                        .member(member)
                        .build()));
        //when
        CommentDto commentDto = commentService.modifyComment(123L, "수정 내용", 9L);

        //then
        assertEquals(123L, commentDto.getId());
        assertEquals("수정 내용", commentDto.getContent());
        assertEquals(member.getId(), commentDto.getMember().getId());
    }

    @Test
    @DisplayName("댓글 수정 실패")
    void modify_Comment_fail() {
        //given
        given(commentRepository.findByIdAndMemberId(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> commentService.modifyComment(123L, "수정 내용",99L));

        //then
        assertEquals(ErrorCode.NO_PERMISSION, exception.getErrorCode());
    }

    @Test
    @DisplayName("댓글 삭제 실패")
    void delete_Comment() {
        //given
        Member member = Member.builder()
                .id(9L)
                .build();
        given(commentRepository.findByIdAndMemberId(anyLong(),anyLong()))
                .willReturn(Optional.empty());
        //when
        ProjectException exception = assertThrows(ProjectException.class,
                () -> commentService.deleteComment(123L, 1L, Role.ROLE_USER));

        //then
        assertEquals(ErrorCode.NO_PERMISSION, exception.getErrorCode());
    }

}