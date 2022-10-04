package chocoteamteam.togather.service;

import chocoteamteam.togather.dto.CommentDto;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.entity.Comment;
import chocoteamteam.togather.entity.Member;
import chocoteamteam.togather.entity.Project;
import chocoteamteam.togather.exception.MemberException;
import chocoteamteam.togather.exception.ProjectException;
import chocoteamteam.togather.repository.CommentRepository;
import chocoteamteam.togather.repository.MemberRepository;
import chocoteamteam.togather.repository.ProjectRepository;
import chocoteamteam.togather.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static chocoteamteam.togather.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public CommentDto createComment(Long projectId, String content, Long memberId) {
        Member member = getMember(memberId);
        Project project = getProject(projectId);
        Comment comment = commentRepository.save(Comment.builder()
                .member(member)
                .content(content)
                .build());

        project.addComment(comment);
        return CommentDto.fromEntity(comment);
    }

    @Transactional
    public CommentDto modifyComment(Long commentId, String content, Long memberId) {
        System.out.println(commentId + " " + content + " " + memberId);

        Comment comment = getCommentByMemberId(commentId, memberId);

        comment.setContent(content);
        return CommentDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long memberId, Role role) {
        if (role.equals(Role.ROLE_ADMIN)) {
            commentRepository.delete(getComment(commentId));
        } else {
            commentRepository.delete(getCommentByMemberId(commentId, memberId));
        }
    }

    private Comment getCommentByMemberId(Long commentId, Long memberId) {
        return commentRepository.findByIdAndMemberId(commentId, memberId)
                .orElseThrow(() -> new ProjectException(NO_PERMISSION));
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_COMMENT));
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

}