package com.example.dbx;

import java.util.Optional;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class Initializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final OrgUnitRepository orgUnitRepository;
    private final BCryptPasswordEncoder encoder;
    Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Override
    public void run(String... args) throws Exception {
        OrgUnit orgUnit = orgUnitRepository.findByName("-");
        if (orgUnit == null) {
            orgUnit = orgUnitRepository.save(new OrgUnit("-"));
        }
        Optional<User> user = userRepository.findByUsername("admin");
        if (!user.isPresent()) {
            User newUser = new User(null, "Admin", "admin", orgUnit, encoder.encode("12345678"), UserRole.ROLE_ADMIN, true);
            userRepository.save(newUser);
        }

        logger.info("DB Initialized");
    }
}