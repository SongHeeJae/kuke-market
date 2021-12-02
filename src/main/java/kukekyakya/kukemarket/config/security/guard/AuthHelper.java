package kukekyakya.kukemarket.config.security.guard;

import kukekyakya.kukemarket.config.security.CustomAuthenticationToken;
import kukekyakya.kukemarket.config.security.CustomUserDetails;
import kukekyakya.kukemarket.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AuthHelper {

    public boolean isAuthenticated() {
        return getAuthentication() instanceof CustomAuthenticationToken &&
                getAuthentication().isAuthenticated();
    }

    public Long extractMemberId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public Set<RoleType> extractMemberRoles() {
        return getUserDetails().getAuthorities()
                .stream()
                .map(authority -> authority.getAuthority())
                .map(strAuth -> RoleType.valueOf(strAuth))
                .collect(Collectors.toSet());
    }

    public boolean isAccessTokenType() {
        return "access".equals(((CustomAuthenticationToken) getAuthentication()).getType());
    }

    public boolean isRefreshTokenType() {
        return "refresh".equals(((CustomAuthenticationToken) getAuthentication()).getType());
    }

    private CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
