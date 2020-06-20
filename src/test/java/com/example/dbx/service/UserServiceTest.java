package com.example.dbx.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.OrgUnit;
import com.example.dbx.model.User;
import com.example.dbx.model.UserFilter;
import com.example.dbx.model.UserRole;
import com.example.dbx.model.UsersResult;
import com.example.dbx.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllUser() {
        List<User> users = new ArrayList<>();

        users.add(new User(1l, "name", "username", new OrgUnit(), "password", UserRole.ROLE_USER, true));

        when(userRepository.findAllByRole(any(UserRole.class), any(Pageable.class))).thenReturn(new PageImpl<>(users));
        when(userRepository.findByIsEnabledAndRole(any(Boolean.class), any(UserRole.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(users));

        UsersResult res = userService.getAllUsers(1, 1, UserRole.ROLE_USER, new UserFilter());

        assertNotNull(res);
        assertNotNull(res.getUsers());
        assertEquals((1), res.getTotalElements());

        res = userService.getAllUsers(1, 1, UserRole.ROLE_USER, new UserFilter(true));

        assertNotNull(res);
        assertNotNull(res.getUsers());
        assertEquals((1), res.getTotalElements());

        res = userService.getAllUsers(1, 1, UserRole.ROLE_USER, null);

        assertNotNull(res);
        assertNotNull(res.getUsers());
        assertEquals((1), res.getTotalElements());
    }

    @Test
    public void testGetUserById() {
        when(userRepository.findById(any(Long.class))).thenReturn(dummyUser(true));

        User res = userService.getUserById(1l);

        assertNotNull(res);
        assertEquals(1l, res.getId());
    }

    @Test
    public void testGetUserByIdNull() {
        Long id = 1l;

        when(userRepository.findById(any(Long.class))).thenReturn(dummyUser(null));

        InvalidException exception = assertThrows(InvalidException.class, () -> {
            userService.getUserById(1l);
        });

        String expectedMessage = UserService.notExistsMsg(id);
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateUser() {
        User saveUser = User.builder().name("test").isEnabled(true).build();

        when(userRepository.save(any(User.class))).thenReturn(saveUser);
        when(userRepository.findById(any(Long.class))).thenReturn(dummyUser(true));

        User res = userService.updateUser(1l, saveUser);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals("test", res.getName());
        assertEquals(true, res.getIsEnabled());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(any(Long.class))).thenReturn(dummyUser(null));

        User res = userService.updateUser(1l, new User());

        assertNotNull(res);
        assertEquals(null, res.getId());
    }

    @Test
    public void testUpdateUserAdmin() {
        when(userRepository.findById(any(Long.class))).thenReturn(dummyUser(true, UserRole.ROLE_ADMIN));

        User res = userService.updateUser(1l, new User());

        assertNotNull(res);
        assertNotNull(res.getId());
        assertNotEquals("test", res.getName());
        assertNotEquals(true, res.getIsEnabled());
    }

    Optional<User> dummyUser(Boolean isNull, UserRole role) {
        User user = User.builder().id(1l).role(role).name("name").build();
        Optional<User> res = isNull == null ? Optional.empty() : Optional.of(user);
        return res;
    }

    Optional<User> dummyUser(Boolean isNull) {
        return dummyUser(isNull, UserRole.ROLE_USER);
    }
}