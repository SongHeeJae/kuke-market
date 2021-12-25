package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.BooleanSupplier;

@Component
public class GuardChecker {
    public boolean check(List<RoleType> roleTypes, BooleanSupplier isResourceOwner) {
        return AuthHelper.isAuthenticated() && (hasRole(roleTypes) || isResourceOwner.getAsBoolean());
    }

    private boolean hasRole(List<RoleType> roleTypes) {
        return roleTypes.stream().allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
}
