package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostGuard implements Guard {
    private final PostRepository postRepository;

    @Override
    public boolean isResourceOwner(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
        Long memberId = AuthHelper.extractMemberId();
        return post.getMember().getId().equals(memberId);
    }

    @Override
    public boolean hasRole() {
        return hasRole(RoleType.ROLE_ADMIN);
    }
}
