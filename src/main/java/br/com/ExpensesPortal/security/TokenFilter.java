package br.com.ExpensesPortal.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.ExpensesPortal.entities.TokenEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

@Component
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(jwtUtil.isTokenValid(authHeader)) {
            String token = authHeader.substring(7);

            Optional<TokenEntity> optional = tokenRepository.findByToken(token);

            if(optional.isPresent()) {
                TokenEntity tokenEntity = optional.get();
                    
                if(tokenEntity.getActive() && tokenEntity.isNonExpired()) {
                    UserEntity user = tokenEntity.getUser();
                    String subject = jwtUtil.extractSubject(token);
    
                    if(user.getUsername().equals(subject) && user.getActive()) {
                        SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                            )
                        );
    
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

}