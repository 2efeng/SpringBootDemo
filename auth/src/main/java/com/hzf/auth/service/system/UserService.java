package com.hzf.auth.service.system;

import com.hzf.auth.models.system.User;
import com.hzf.auth.repository.system.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User selectByName(String name) {
        return userRepository.findByUsername(name);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
    public User findById(long id) {
        return userRepository.findById(id);
    }

}
