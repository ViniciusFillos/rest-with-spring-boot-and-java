package io.github.vinifillos.services;

import io.github.vinifillos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository personRepository;
    private final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (logger.isLoggable(Level.INFO)) logger.info("Finding one user by name " + username + "!");
        var user = personRepository.findByUsername(username);
        if(user != null) return user;
        else throw new UsernameNotFoundException("Username "+ username+" not found!");
    }
}