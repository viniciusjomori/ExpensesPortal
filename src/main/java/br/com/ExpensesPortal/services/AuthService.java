package br.com.ExpensesPortal.services;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ExpensesPortal.DTOs.requests.LoginRequestDTO;
import br.com.ExpensesPortal.DTOs.responses.TokenResponseDTO;
import br.com.ExpensesPortal.entities.TokenEntity;
import br.com.ExpensesPortal.entities.UserEntity;
import br.com.ExpensesPortal.repositories.TokenRepository;
import br.com.ExpensesPortal.repositories.UserRepository;
import br.com.ExpensesPortal.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authManager;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    public TokenResponseDTO authenticate(LoginRequestDTO login) {
        
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                login.email(), login.password()
            )
        );

        UserEntity user = userRepository.findByEmail(login.email())
            .orElseThrow();

        if(user.getActive())
            return createTokenResponse(user);

        throw new ResponseStatusException(
            HttpStatus.UNAUTHORIZED
        );
    }

    public TokenResponseDTO createTokenResponse(UserEntity user) {
        String token = jwtUtil.generateToken(user);
        saveUserToken(user, token);
        return new TokenResponseDTO(token);
    }

    public void saveUserToken(UserEntity user, String token) {
        revokeAllTokens(user);
        TokenEntity tokenEntity = TokenEntity.builder()
            .user(user)
            .token(token)
            .expirationDate(LocalDateTime.now().plusNanos(
                jwtExpiration * 1000000
            ))
            .build();
        tokenRepository.save(tokenEntity);
    }

    public void revokeAllTokens(UserEntity user) {
        Collection<TokenEntity> tokens = tokenRepository.findActiveByUser(user);
        tokens.forEach(token -> {
            token.setActive(false);
        });
        tokenRepository.saveAll(tokens);

    }

}
