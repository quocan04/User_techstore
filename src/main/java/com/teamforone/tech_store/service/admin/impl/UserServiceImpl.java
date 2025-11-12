package com.teamforone.tech_store.service.admin.impl;

import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.repository.user.UserRepository;
import com.teamforone.tech_store.service.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
