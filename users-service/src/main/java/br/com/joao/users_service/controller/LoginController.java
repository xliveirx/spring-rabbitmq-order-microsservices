package br.com.joao.users_service.controller;

import br.com.joao.users_service.domain.User;
import br.com.joao.users_service.dto.LoginRequest;
import br.com.joao.users_service.dto.LoginResponse;
import br.com.joao.users_service.service.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest req) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(req.email(), req.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        String token = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
