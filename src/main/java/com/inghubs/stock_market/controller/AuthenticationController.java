package com.inghubs.stock_market.controller;

import com.inghubs.stock_market.domain.User;
import com.inghubs.stock_market.dto.*;
import com.inghubs.stock_market.security.JwtService;
import com.inghubs.stock_market.security.AuthenticationService;
import com.inghubs.stock_market.util.Constants;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterUserDTO registerUserDto) {
        authenticationService.signUp(registerUserDto);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginUserResponseDTO>> authenticate(@Valid @RequestBody LoginUserRequestDTO loginUserRequestDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserRequestDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginUserResponseDTO loginUserResponseDTO = LoginUserResponseDTO.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();

        ApiResponse<LoginUserResponseDTO> apiResponse = ApiResponse.<LoginUserResponseDTO>builder()
                .success(true)
                .message(Constants.SUCCESS_RESPONSE_MESSAGE)
                .data(loginUserResponseDTO)
                .build();

        return ResponseEntity.ok().body(apiResponse);
    }
}
