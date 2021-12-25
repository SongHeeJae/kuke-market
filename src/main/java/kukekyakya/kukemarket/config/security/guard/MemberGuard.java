package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MemberGuard implements Guard {
    private final GuardChecker guardChecker;

    @Override
    public boolean check(Long id) {
        return guardChecker.check(
                List.of(RoleType.ROLE_ADMIN),
                () -> id.equals(AuthHelper.extractMemberId())
        );
    }
}
