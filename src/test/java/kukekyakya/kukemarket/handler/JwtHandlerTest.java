package kukekyakya.kukemarket.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JwtHandlerTest {
    JwtHandler jwtHandler = new JwtHandler();

    @Test
    void createTokenTest() {
        // given, when
        String key = "myKey";
        String token = createToken(key, Map.of(), 60L);

        // then
        assertThat(token).contains("Bearer ");
    }

    @Test
    void parseTest() {
        // given
        String key = "key";
        String value = "value";
        String token = createToken(key, Map.of(key, value), 60L);

        // when
        Claims claims = jwtHandler.parse(key, token).orElseThrow(RuntimeException::new);

        // then
        assertThat(claims.get(key)).isEqualTo(value);
    }

    @Test
    void parseByInvalidKeyTest() {
        // given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = createToken(encodedKey, Map.of(), 60L);

        // when
        Optional<Claims> claims = jwtHandler.parse("invalidKey", token);

        // then
        assertThat(claims).isEmpty();
    }

    @Test
    void parseByExpiredTokenTest() {
        // given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = createToken(encodedKey, Map.of(),0L);

        // when
        Optional<Claims> claims = jwtHandler.parse("invalidKey", token);

        // then
        assertThat(claims).isEmpty();
    }

    private String createToken(String encodedKey, Map<String, Object> claims, long maxAgeSeconds) {
        return jwtHandler.createToken(
                encodedKey,
                claims,
                maxAgeSeconds);
    }
}