package chocoteamteam.togather.repository;

import chocoteamteam.togather.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Optional<Comment> findById(Long commentId);

    Optional<Comment> findByIdAndMemberId(Long id, Long member_id);
}