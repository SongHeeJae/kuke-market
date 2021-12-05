package kukekyakya.kukemarket.factory.dto;

import kukekyakya.kukemarket.dto.sign.SignInResponse;

public class SignInResponseFactory {
    public static SignInResponse createSignInResponse(String accessToken, String refreshToken) {
        return new SignInResponse(accessToken, refreshToken);
    }
}
