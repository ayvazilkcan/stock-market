package com.inghubs.stock_market.security;

import com.inghubs.stock_market.domain.User;
import com.inghubs.stock_market.dto.LoginUserRequestDTO;
import com.inghubs.stock_market.dto.RegisterUserDTO;
import com.inghubs.stock_market.repository.UserRepository;
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
    private final AuthenticationManager authenticationManager;


    public void signUp(RegisterUserDTO registerUserDTO) {
        User user = User.builder()
                .fullName(registerUserDTO.getFullName())
                .email(registerUserDTO.getEmail())
                .password(passwordEncoder.encode(registerUserDTO.getPassword()))
                .build();

        userRepository.save(user);
    }

    public User authenticate(LoginUserRequestDTO loginUserRequestDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginUserRequestDTO.getEmail(), loginUserRequestDTO.getPassword());

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return userRepository.findByEmail(loginUserRequestDTO.getEmail()).orElseThrow();
    }
}
