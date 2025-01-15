package com.team4.toucheese.auth.service;

import com.team4.toucheese.auth.dto.CustomUserDetails;
import com.team4.toucheese.user.entity.UserEntity;
import com.team4.toucheese.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public JpaUserDetailsManager(
            PasswordEncoder passwordEncoder,
            UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        if ( !userRepository.existsByEmail("toucheese@gmail.com") ) {
            UserEntity user1 = new UserEntity();
            user1 = user1.toBuilder()
                    .username("터치즈")
                    .password(passwordEncoder.encode("1q2w3e4r1!"))
                    .email("toucheese@gmail.com")
                    .phone("010-1234-5678")
                    .role("ROLE_USER,USER")
                    .build();
            this.userRepository.save(user1);
        }
        if ( !userRepository.existsByEmail("admin")) {
            UserEntity admin = new UserEntity();
            admin = admin.toBuilder()
                    .username("admin")
                    .password(passwordEncoder.encode("password1"))
                    .email("admin")
                    .role("ROLE_ADMIN,READ,WRITE")
                    .build();
            this.userRepository.save(admin);
        }
    }

    // 우선 개발 해볼 것
    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException(email);


        return CustomUserDetails.fromEntity(optionalUser.get());
    }

    public void createUser(String email, String password, String passwordCheck, String phone, String registration, String username) {
        if (userExists(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        if (!password.equals(passwordCheck)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");

        try{
            UserEntity newUser = UserEntity.builder()
                    .email(email)
                    .phone(phone)
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role("ROLE_USER,USER")
                    .registration(registration)
                    .build();

            userRepository.save(newUser);

        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class, e );
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void createUser(CustomUserDetails userDetails) {
        if (userExists(userDetails.getEmail())) throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");

        try{
            UserEntity newUser = UserEntity.builder()
                    .username(userDetails.getUsername())
                    .email(userDetails.getEmail())
                    .password(passwordEncoder.encode(userDetails.getPassword()))
                    .phone(userDetails.getPhone())
                    .role("ROLE_USER,USER")
                    .registration(userDetails.getRegistration())
                    .build();

            userRepository.save(newUser);

        } catch (ClassCastException e) {
            log.error("failed to cast to {}", CustomUserDetails.class, e );
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

//    // 추후 개발 해보기
//    @Override
//    public void updateUser(UserDetails user) {
//
//    }
//
//    @Override
//    public void deleteUser(String username) {
//
//    }
//
//    @Override
//    public void changePassword(String oldPassword, String newPassword) {
//
//    }

}
