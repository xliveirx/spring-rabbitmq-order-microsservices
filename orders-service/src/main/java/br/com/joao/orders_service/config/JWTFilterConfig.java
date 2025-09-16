package br.com.joao.orders_service.config;

import br.com.joao.orders_service.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
public class JWTFilterConfig extends OncePerRequestFilter {
    private final TokenService tokenService;

    public JWTFilterConfig(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = recoverToken(request);

        if(token != null){
            var decodedJWT = tokenService.validateToken(token);
            var email = decodedJWT.getSubject();
            var role = decodedJWT.getClaim("role").asString();

            var auth = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);

    }

    private String recoverToken(HttpServletRequest request) {

        var authenticationHeader = request.getHeader("Authorization");

        if(authenticationHeader != null){
            return authenticationHeader.replace("Bearer ", "");
        }

        return null;
    }
}
