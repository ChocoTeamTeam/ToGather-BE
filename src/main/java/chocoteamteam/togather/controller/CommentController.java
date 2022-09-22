package chocoteamteam.togather.controller;

import chocoteamteam.togather.dto.CommentDto;
import chocoteamteam.togather.dto.LoginMember;
import chocoteamteam.togather.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @Operation(
            summary = "댓글 생성 api", description = "댓글을 생성하고 생성된 댓글을 반환합니다.",
            security = {@SecurityRequirement(name = "Authorization")})
    @PostMapping("/projects/{projectId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long projectId,
                                                    @AuthenticationPrincipal LoginMember loginMember, @RequestBody String content) {
        return ResponseEntity.ok(commentService.createComment(projectId, content, loginMember.getId()));
    }

    @Operation(
            summary = "댓글 수정 api", description = "댓글을 수정하고 수정된 댓글을 반환합니다.",
            security = {@SecurityRequirement(name = "Authorization")})
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentDto> modifyComment(@PathVariable Long commentId,
                                                    @AuthenticationPrincipal LoginMember loginMember, @RequestBody String content) {
        return ResponseEntity.ok(commentService.modifyComment(commentId, content, loginMember.getId()));
    }

    @Operation(
            summary = "댓글 삭제 api", description = "댓글을 삭제합니다.",
            security = {@SecurityRequirement(name = "Authorization")})
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @AuthenticationPrincipal LoginMember loginMember) {
        commentService.deleteComment(commentId, loginMember.getId(), loginMember.getRole());
        return ResponseEntity.ok(HttpStatus.OK);
    }

}