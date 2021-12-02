package kukekyakya.kukemarket.service.sign;

import kukekyakya.kukemarket.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @InjectMocks TokenService tokenService;
    @Mock JwtHandler jwtHandler;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(tokenService, "accessTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "accessKey", "accessKey");
        ReflectionTestUtils.setField(tokenService, "refreshKey", "refreshKey");
    }

    @Test
    void createAccessTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");

        // when
        String token = tokenService.createAccessToken("subject");

        // then
        assertThat(token).isEqualTo("access");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void createRefreshTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

        // when
        String token = tokenService.createRefreshToken("subject");

        // then
        assertThat(token).isEqualTo("refresh");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void validateAccessTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(true);

        // when, then
        assertThat(tokenService.validateAccessToken("token")).isTrue();
    }

    @Test
    void invalidateAccessTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when, then
        assertThat(tokenService.validateAccessToken("token")).isFalse();
    }

    @Test
    void validateRefreshTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(true);

        // when, then
        assertThat(tokenService.validateRefreshToken("token")).isTrue();
    }

    @Test
    void invalidateRefreshTokenTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when, then
        assertThat(tokenService.validateRefreshToken("token")).isFalse();
    }

    @Test
    void extractAccessTokenSubjectTest() {
        // given
        String subject = "subject";
        given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

        // when
        String result = tokenService.extractAccessTokenSubject("token");

        // then
        assertThat(subject).isEqualTo(result);
    }

    @Test
    void extractRefreshTokenSubjectTest() {
        // given
        String subject = "subject";
        given(jwtHandler.extractSubject(anyString(), anyString())).willReturn(subject);

        // when
        String result = tokenService.extractRefreshTokenSubject("token");

        // then
        assertThat(subject).isEqualTo(result);
    }
}