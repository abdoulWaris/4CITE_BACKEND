package com.akkor.hotel.service;

import com.akkor.hotel.dto.AuthenticationRequest;
import com.akkor.hotel.dto.AuthenticationResponse;
import com.akkor.hotel.dto.RegisterRequest;
import com.akkor.hotel.exception.EmailAlreadyExistsException;
import com.akkor.hotel.exception.InvalidCredentialsException;
import com.akkor.hotel.exception.UserNotFoundException;
import com.akkor.hotel.model.CustomerUserDetails;
import com.akkor.hotel.model.Role;
import com.akkor.hotel.model.User;
import com.akkor.hotel.repository.UserRepository;
import com.akkor.hotel.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Tentative d'inscription pour l'email: {}", request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Tentative d'inscription avec un email déjà existant: {}", request.getEmail());
            throw new EmailAlreadyExistsException("Un compte existe déjà avec cet email");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roles(Collections.singleton(Role.ROLE_ADMIN))
                .enabled(true)
                .build();

        user = userRepository.save(user);
        var jwtToken = jwtService.generateToken((UserDetails) user);

        log.info("Inscription réussie pour l'utilisateur: {}", user.getEmail());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Tentative d'authentification pour l'email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Échec d'authentification pour l'email: {}", request.getEmail());
            throw new InvalidCredentialsException("Email ou mot de passe incorrect");
        }

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("Utilisateur non trouvé pour l'email: {}", request.getEmail());
                    return new UserNotFoundException("Utilisateur non trouvé");
                });
        
        var jwtToken = jwtService.generateToken((UserDetails) user);
        
        log.info("Authentification réussie pour l'utilisateur: {}", user.getEmail());
        //System.out.println(httpHeaders);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(user.getRoles())
                .build();
    }

} 