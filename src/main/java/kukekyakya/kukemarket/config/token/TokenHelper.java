package kukekyakya.kukemarket.config.token;

import io.jsonwebtoken.Claims;
import kukekyakya.kukemarket.handler.JwtHandler;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
public class TokenHelper {
    private final JwtHandler jwtHandler;
    private final String key;
    private final long maxAgeSeconds;

    private static final String SEP = ",";
    private static final String ROLE_TYPES = "ROLE_TYPES";
    private static final String MEMBER_ID = "MEMBER_ID";

    public String createToken(PrivateClaims privateClaims) {
        return jwtHandler.createToken(
                key,
                Map.of(MEMBER_ID, privateClaims.getMemberId(), ROLE_TYPES, privateClaims.getRoleTypes().stream().collect(joining(SEP))),
                maxAgeSeconds
        );
    }

    public Optional<PrivateClaims> parse(String token) {
        return jwtHandler.parse(key, token).map(this::convert);
    }

    private PrivateClaims convert(Claims claims) {
        return new PrivateClaims(
                claims.get(MEMBER_ID, String.class),
                Arrays.asList(claims.get(ROLE_TYPES, String.class).split(SEP))
        );
    }

    @Getter
    @AllArgsConstructor
    public static class PrivateClaims {
        private String memberId;
        private List<String> roleTypes;
    }
}
