package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.entity.member.RoleType;

import java.util.Arrays;

public interface Guard {
    default boolean check(Long id) {
        return AuthHelper.isAuthenticated() && (hasRole() || isResourceOwner(id));
    }
    default boolean hasRole(RoleType... roleTypes) {
        return Arrays.stream(roleTypes).allMatch(roleType -> AuthHelper.extractMemberRoles().contains(roleType));
    }
    boolean hasRole();
    boolean isResourceOwner(Long id);
}
