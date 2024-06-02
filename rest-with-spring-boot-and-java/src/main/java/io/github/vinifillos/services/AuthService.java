package io.github.vinifillos.services;

import io.github.vinifillos.model.dto.security.AccountCredentialsDto;
import io.github.vinifillos.model.dto.security.TokenDto;
import io.github.vinifillos.repositories.UserRepository;
import io.github.vinifillos.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    public final AuthenticationManager authenticationManager;
    public final JwtTokenProvider tokenProvider;
    public final UserRepository userRepository;

    @SuppressWarnings("rawtypes")
    public ResponseEntity signin(AccountCredentialsDto data) {
        try {
            var username = data.getUsername();
            var password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            var user = userRepository.findByUsername(username);

            var tokenResponse = new TokenDto();
            if (user != null) {
                tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
            } else throw new UsernameNotFoundException("Username " + username + " not found!");
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid username/password supplied!");
        }
    }
}
