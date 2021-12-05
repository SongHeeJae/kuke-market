package kukekyakya.kukemarket.config.security;

import kukekyakya.kukemarket.config.token.TokenHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if(validateToken(token)) {
            setAuthentication(token);
        }
        chain.doFilter(request, response);
    }

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest)request).getHeader("Authorization");
    }

    private boolean validateToken(String token) {
        return token != null && accessTokenHelper.validate(token);
    }

    private void setAuthentication(String token) {
        String userId = accessTokenHelper.extractSubject(token);
        CustomUserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

}
