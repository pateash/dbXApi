package com.example.dbx.service;

import java.util.Optional;

import com.example.dbx.exception.InvalidException;
import com.example.dbx.model.User;
import com.example.dbx.model.UserFilter;
import com.example.dbx.model.UserRole;
import com.example.dbx.model.UsersResult;
import com.example.dbx.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UsersResult getAllUsers(int page, int pageSize, UserRole userRole, UserFilter filter) {
        Pageable pageRequest = PageRequest.of(page, pageSize);
        Page<User> pageResult;

        if (filter == null || (filter.getIsEnabled() == null)) {
            pageResult = userRepository.findAllByRole(userRole, pageRequest);
        } else {
            pageResult = userRepository.findByIsEnabledAndRole(filter.getIsEnabled(), userRole, pageRequest);
        }

        return new UsersResult(pageResult.getContent(), pageResult.getTotalElements());
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new InvalidException("User -> " + id + " does not Exist");
        }

        return user.get();
    }

    public User updateUser(Long id, User user) {
        Optional<User> res = userRepository.findById(id);
        if (!res.isPresent()) {
            return new User();
        }

        User updatedUser = res.get();
        if (updatedUser.getRole() != UserRole.ROLE_ADMIN) {
            updatedUser.setName(user.getName());
            updatedUser.setIsEnabled(user.getIsEnabled());
            userRepository.save(updatedUser);
        }

        return updatedUser;
    }
}