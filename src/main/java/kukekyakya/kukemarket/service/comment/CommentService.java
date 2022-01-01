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
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher publisher;

    public List<CommentDto> readAll(CommentReadCondition cond) {
        return CommentDto.toDtoList(
                commentRepository.findAllWithMemberAndParentByPostIdOrderByParentIdAscNullsFirstCommentIdAsc(cond.getPostId())
        );
    }

    @Transactional
    public void create(CommentCreateRequest req) {
        Comment comment = commentRepository.save(CommentCreateRequest.toEntity(req, memberRepository, postRepository, commentRepository));
        comment.publishCreatedEvent(publisher);
        log.info("CommentService.create");
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findWithParentById(id).orElseThrow(CommentNotFoundException::new);
        comment.findDeletableComment().ifPresentOrElse(commentRepository::delete, comment::delete);
    }
}
