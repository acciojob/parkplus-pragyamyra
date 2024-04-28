package com.driver.services.impl;

import com.driver.model.User;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository4;
    @Override
    public void deleteUser(Integer userId) {
        Optional<User> userOptional = userRepository4.findById(userId);
        User user=null;
        if(userOptional.isPresent()){
            user= userOptional.get();
            userRepository4.deleteById(userId);
        }


    }

    @Override
    public User updatePassword(Integer userId, String password) {
        Optional<User> userOptional = userRepository4.findById(userId);
        User user=null;
        if(userOptional.isPresent()){
            user= userOptional.get();
            user.setPassword(password);
            userRepository4.save(user);
        }
        return user;

    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        userRepository4.save(user);
    }
}
