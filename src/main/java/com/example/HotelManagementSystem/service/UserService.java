package com.example.HotelManagementSystem.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.HotelManagementSystem.entity.User;
import com.example.HotelManagementSystem.repository.UserRepository;

@Service
public class UserService {

	// Repository Injection

    private final UserRepository userRepository;

   
    // BCrypt Password Encoder
  

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    
    // CREATE USER
  

    public User createUser(User user) {

        // Check Username Already Exists
        userRepository.findByUsername(user.getUsername())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("Username already exists");
                });

        // Check Email Already Exists
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new RuntimeException("Email already exists");
                });

        // Encrypt Password
        user.setPassword(
                passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

 
    // GET ALL USERS
 

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }


    // GET USER BY ID
    
    public User getUserById(Long id) {

        return userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

  
    // UPDATE USER
 

    public User updateUser(Long id, User user) {

        User existingUser = getUserById(id);

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());
        existingUser.setRole(user.getRole());

        // Update Password Only If Provided
        if (user.getPassword() != null &&
                !user.getPassword().isBlank()) {

            existingUser.setPassword(
                    passwordEncoder.encode(user.getPassword()));
        }

        return userRepository.save(existingUser);
    }

  
    // DELETE USER


    public void deleteUser(Long id) {

        User user = getUserById(id);

        userRepository.delete(user);
    }

   
    // LOGIN USER
  

    public User loginUser(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username"));

        // Compare Encrypted Password
        boolean isPasswordMatch =
                passwordEncoder.matches(password,
                        user.getPassword());

        if (!isPasswordMatch) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}