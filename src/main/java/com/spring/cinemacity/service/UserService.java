package com.spring.cinemacity.service;

import com.spring.cinemacity.DTO.RegisterDTO;
import com.spring.cinemacity.model.Role;
import com.spring.cinemacity.model.RoleType;
import com.spring.cinemacity.model.User;
import com.spring.cinemacity.repository.RoleRepository;
import com.spring.cinemacity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }


    public User register(RegisterDTO newUser) {
        Optional<User> foundUserOptional = userRepository.findUserByUsername(newUser.getUsername());
        if (foundUserOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CREATED, "already exist");
        }
        User user = new User();
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        Role foundRole = roleRepository.findByRoleType(RoleType.ROLE_CLIENT);
        user.getRoleList().add(foundRole);
        foundRole.getUserList().add(user);
        return userRepository.save(user);
    }
}

