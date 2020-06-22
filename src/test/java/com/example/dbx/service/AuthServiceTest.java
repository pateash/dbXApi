package com.example.dbx.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.message.request.SignupForm;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserRole;
import com.example.dbx.repository.OrgUnitRepository;
import com.example.dbx.repository.UserRepository;

public class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrgUnitRepository orgUnitRepository;

    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindUserByUsername() {
        when(userRepository.findByUsername(anyString())).thenReturn(dummyUser("test"));

        Optional<User> res = authService.findUserByUsername("test");

        assertNotNull(res);
        assertNotNull(res.get());
        assertNotNull(res.get().getId());
        assertEquals("test", res.get().getUsername());
    }

    @Test
    public void testExistsUserByUsername() {
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        Boolean res = authService.existsUserByUsername("test");

        assertNotNull(res);
    }

    @Test
    public void testGetAllOrgUnits() {
        List<OrgUnit> orgUnits = new ArrayList<>();
        orgUnits.add(new OrgUnit("-"));

        when(orgUnitRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(orgUnits));

        List<OrgUnit> res = authService.getAllOrgUnits();

        assertNotNull(res);
        assertEquals(0, res.size());
    }

    @Test
    public void registerUser() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(orgUnitRepository.findById(anyLong())).thenReturn(Optional.of(new OrgUnit()));
        when(encoder.encode(anyString())).thenReturn("HASHED");

        String res = authService
                .registerUser(SignupForm.builder().username("teest").password("password").orgUnit(1l).build());

        assertNotNull(res);
    }

    @Test
    public void registerUserUsernameNotAvailable() {
        SignupForm req = SignupForm.builder().username("teest").password("password").orgUnit(1l).build();

        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            authService.registerUser(req);
        });

        String expectedMessage = AuthService.USERNAME_IS_ALREADY_TAKEN_MSG;
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void registerUserOrgUnitNotpresent() {
        SignupForm req = SignupForm.builder().username("teest").password("password").orgUnit(1l).build();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(orgUnitRepository.findById(anyLong())).thenReturn(Optional.empty());

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            authService.registerUser(req);
        });

        String expectedMessage = OrgUnitService.notExistsMsg(1l);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    Optional<User> dummyUser(Boolean isNull, UserRole role, String username) {
        User user = User.builder().id(1l).role(role).username(username).build();
        Optional<User> res = isNull == null ? Optional.empty() : Optional.of(user);
        return res;
    }

    Optional<User> dummyUser(Boolean isNull, UserRole role) {
        return dummyUser(isNull, role, "username");
    }

    Optional<User> dummyUser(Boolean isNull) {
        return dummyUser(isNull, UserRole.ROLE_USER);
    }

    Optional<User> dummyUser(String username) {
        return dummyUser(true, UserRole.ROLE_USER, username);
    }
}