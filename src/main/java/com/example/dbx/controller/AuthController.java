package com.example.dbx.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.example.dbx.message.request.LoginForm;
import com.example.dbx.message.request.SignupForm;
import com.example.dbx.message.response.JwtResponse;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.security.jwt.JwtProvider;
import com.example.dbx.service.AuthService;

@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @PostMapping("/signin")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<User> u = authService.findUserByUsername(loginRequest.getUsername());
        UserRole userRole = UserRole.ROLE_USER;

        if (u.isPresent()) {
            userRole = u.get().getRole();
        }

        return new JwtResponse(jwtProvider.generateJwtToken(authentication), userRole);
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @RequestBody SignupForm signUpRequest) {
        return authService.registerUser(signUpRequest);
    }

    @GetMapping("/orgUnit")
    public List<OrgUnit> getAllOrgUnits() {
        return authService.getAllOrgUnits();
    }
}