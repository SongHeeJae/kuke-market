package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findWithRolesById(Long.valueOf(userId))
                .orElseGet(() -> new Member(null, null, null, null, List.of()));
        return new CustomUserDetails(
                String.valueOf(member.getId()),
                member.getRoles().stream().map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
