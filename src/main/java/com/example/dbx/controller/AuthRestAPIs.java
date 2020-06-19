package com.example.dbx.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.message.request.LoginForm;
import com.example.dbx.message.request.SignupForm;
import com.example.dbx.message.response.JwtResponse;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;
import com.example.dbx.security.jwt.JwtProvider;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthRestAPIs {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrgUnitRepository orgUnitRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());

        UserRole userRole = UserRole.ROLE_USER;
        if (u.isPresent()) {
            userRole = u.get().getRole();
        }

        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt, userRole));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignupForm signUpRequest) {
        boolean existsByUsername = userRepository.existsByUsername(signUpRequest.getUsername());
        if (existsByUsername) {
            return new ResponseEntity<>("Fail -> Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        Optional<OrgUnit> orgUnit = orgUnitRepository.findById(signUpRequest.getOrgUnit());
        if (!orgUnit.isPresent()) {
            throw new InvalidException("Org-Unit -> " + signUpRequest.getOrgUnit() + " does not Exist");
        }

        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        User user = new User(signUpRequest.getName(), orgUnit.get(), signUpRequest.getUsername(),
                signUpRequest.getPassword());

        userRepository.save(user);

        return ResponseEntity.ok().body("User registered successfully!");
    }

    @GetMapping("/orgUnit")
    public List<OrgUnit> getAllOrgUnits() {
        List<OrgUnit> units = orgUnitRepository.findAll();

        units.removeIf(o -> (o.getName().equals("-")));

        return units;
    }
}