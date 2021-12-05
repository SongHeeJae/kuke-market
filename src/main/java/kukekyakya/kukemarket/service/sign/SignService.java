package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.dto.sign.RefreshTokenResponse;
import kukekyakya.kukemarket.dto.sign.SignInRequest;
import kukekyakya.kukemarket.dto.sign.SignInResponse;
import kukekyakya.kukemarket.dto.sign.SignUpRequest;
import kukekyakya.kukemarket.entity.member.Member;
import kukekyakya.kukemarket.entity.member.RoleType;
import kukekyakya.kukemarket.exception.*;
import kukekyakya.kukemarket.repository.member.MemberRepository;
import kukekyakya.kukemarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpInfo(req);
        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }

    @Transactional(readOnly = true)
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        return new SignInResponse(accessToken, refreshToken);
    }

    public RefreshTokenResponse refreshToken(String rToken) {
        validateRefreshToken(rToken);
        String subject = tokenService.extractRefreshTokenSubject(rToken);
        String accessToken = tokenService.createAccessToken(subject);
        return new RefreshTokenResponse(accessToken);
    }

    private void validateRefreshToken(String rToken) {
        if(!tokenService.validateRefreshToken(rToken)) {
            throw new AuthenticationEntryPointException();
        }
    }

    private void validateSignUpInfo(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    private void validatePassword(SignInRequest req, Member member) {
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }
}
