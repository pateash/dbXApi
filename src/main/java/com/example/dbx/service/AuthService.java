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
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class AuthService {
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
        boolean existsByUsername = userRepository.existsByUsername(signUpRequest.getUsername());
        if (existsByUsername) {
            throw new InvalidException("Fail -> Username is already taken!");
        }

        Optional<OrgUnit> orgUnit = orgUnitRepository.findById(signUpRequest.getOrgUnit());
        if (!orgUnit.isPresent()) {
            throw new InvalidException("Org-Unit -> " + signUpRequest.getOrgUnit() + " does not Exist");
        }

        signUpRequest.setPassword(encoder.encode(signUpRequest.getPassword()));

        User user = new User(signUpRequest.getName(), orgUnit.get(), signUpRequest.getUsername(),
                signUpRequest.getPassword());

        userRepository.save(user);

        return "User registered successfully!";
    }
}