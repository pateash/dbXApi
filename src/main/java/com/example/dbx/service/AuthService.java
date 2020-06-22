package com.example.dbx.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.message.request.SignupForm;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthService {
    public static final String USER_REGISTERED_SUCCESSFULLY_MSG = "User registered successfully!";
    public static final String USERNAME_IS_ALREADY_TAKEN_MSG = "Fail -> Username is already taken!";

    private final UserRepository userRepository;
    private final OrgUnitRepository orgUnitRepository;
    private final PasswordEncoder encoder;

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsUserByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public List<OrgUnit> getAllOrgUnits() {
        List<OrgUnit> units = orgUnitRepository.findAll();

        units.removeIf(o -> (o.getName().equals("-")));

        return units;
    }

    public String registerUser(SignupForm signUpRequest) {
        boolean existsByUsername = existsUserByUsername(signUpRequest.getUsername());
        if (existsByUsername) {
            throw new InvalidException(USERNAME_IS_ALREADY_TAKEN_MSG);
        }

        Optional<OrgUnit> orgUnit = orgUnitRepository.findById(signUpRequest.getOrgUnit());
        if (!orgUnit.isPresent()) {
            throw new InvalidException(OrgUnitService.notExistsMsg(signUpRequest.getOrgUnit()));
        }

        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        User user = new User(null, signUpRequest.getName(), signUpRequest.getUsername(), orgUnit.get(),
                signUpRequest.getPassword(), UserRole.ROLE_USER, false);

        userRepository.save(user);

        return USER_REGISTERED_SUCCESSFULLY_MSG;
    }
}