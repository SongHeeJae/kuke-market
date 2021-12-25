package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentGuard implements Guard {
    private final GuardChecker guardChecker;
    private final CommentRepository commentRepository;

    @Override
    public boolean check(Long id) {
        return guardChecker.check(
                List.of(RoleType.ROLE_ADMIN),
                () -> {
                    Comment comment = commentRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
                    Long memberId = AuthHelper.extractMemberId();
                    return comment.getMember().getId().equals(memberId);
                }
        );
    }
}
