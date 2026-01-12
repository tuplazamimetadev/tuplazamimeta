package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.config.JwtService;
import com.tuplazamimeta.policiaapi.dto.request.AuthenticationRequest;
import com.tuplazamimeta.policiaapi.dto.request.RegisterRequest;
import com.tuplazamimeta.policiaapi.dto.response.AuthenticationResponse;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        // ¡ENCRIPTAMOS LA CONTRASEÑA ANTES DE GUARDAR!
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("STUDENT");

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Esto lanza una excepción si el usuario o pass son incorrectos
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // Si llegamos aquí, es que el login es correcto
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .name(user.getName())
                .build();
    }
}