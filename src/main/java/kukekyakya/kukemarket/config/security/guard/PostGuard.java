package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.entity.post.Post;
import kukekyakya.kukemarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostGuard implements Guard {
    private final GuardChecker guardChecker;
    private final PostRepository postRepository;

    @Override
    public boolean check(Long id) {
        return guardChecker.check(
                List.of(RoleType.ROLE_ADMIN),
                () -> {
                    Post post = postRepository.findById(id).orElseThrow(() -> { throw new AccessDeniedException(""); });
                    Long memberId = AuthHelper.extractMemberId();
                    return post.getMember().getId().equals(memberId);
                }
        );
    }
}
