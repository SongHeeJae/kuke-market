package kukekyakya.kukemarket.service.comment;

import kukekyakya.kukemarket.dto.comment.CommentCreateRequest;
import kukekyakya.kukemarket.dto.comment.CommentDto;
import kukekyakya.kukemarket.dto.comment.CommentReadCondition;
import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.exception.CommentNotFoundException;
import kukekyakya.kukemarket.repository.comment.CommentRepository;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public List<CommentDto> readAll(CommentReadCondition cond) {
        return CommentDto.toDtoList(
                commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(cond.getPostId())
        );
    }

    @Transactional
    public void create(CommentCreateRequest req) {
        commentRepository.save(CommentCreateRequest.toEntity(req, memberRepository, postRepository, commentRepository));
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }
}
