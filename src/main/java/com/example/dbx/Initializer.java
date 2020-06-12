package com.example.dbx;

import java.util.Optional;

import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Initializer implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrgUnitRepository orgUnitRepository;

    @Autowired
    BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("INIT");
        OrgUnit orgUnit = orgUnitRepository.findByName("-");
        if (orgUnit == null) {
            orgUnit = orgUnitRepository.save(new OrgUnit("-"));
        }
        Optional<User> user = userRepository.findByUsername("admin");
        if (!user.isPresent()) {
            User newUser = new User("Admin", orgUnit, "admin", encoder.encode("12345678"));
            newUser.setIsEnabled(true);
            newUser.setRole(UserRole.ROLE_ADMIN);
            userRepository.save(newUser);
        }
        System.out.println(" -- Database has been initialized");
    }
}