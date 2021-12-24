package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.comment.Comment;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.repository.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentGuard implements Guard {
    private final CommentRepository commentRepository;

    @Override
    public boolean isResourceOwner(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
        Long memberId = AuthHelper.extractMemberId();
        return comment.getMember().getId().equals(memberId);
    }

    @Override
    public boolean hasRole() {
        return hasRole(RoleType.ROLE_ADMIN);
    }
}
