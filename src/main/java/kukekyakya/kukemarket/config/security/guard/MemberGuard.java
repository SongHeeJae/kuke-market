package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MemberGuard implements Guard {
    @Override
    public boolean isResourceOwner(Long id) {
        return id.equals(AuthHelper.extractMemberId());
    }

    @Override
    public boolean hasRole() {
        return hasRole(RoleType.ROLE_ADMIN);
    }
}
