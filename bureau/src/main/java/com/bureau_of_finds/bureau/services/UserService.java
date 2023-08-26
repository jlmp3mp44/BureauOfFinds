package com.bureau_of_finds.bureau.services;

import com.bureau_of_finds.bureau.models.User;
import com.bureau_of_finds.bureau.models.enums.Role;
import com.bureau_of_finds.bureau.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public boolean createUser(User user){
        String name = user.getUsername();
        if(userRepository.findByUsername(name) == null) {
            user.getRoles().add(Role.ROLE_USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            log.info("Saving new user with : {}", name);
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
