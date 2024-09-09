package com.example.project_aggregator.Controller;

import com.example.project_aggregator.Dto.LoginDto;
import com.example.project_aggregator.Entity.Role;
import com.example.project_aggregator.Entity.UserEntity;
import com.example.project_aggregator.Repository.RoleRepository;
import com.example.project_aggregator.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public ResponseEntity<String> register(){
        if(userRepository.existsByUsername("admin")){
            return new ResponseEntity<>("Error", HttpStatus.FORBIDDEN);
        }
        UserEntity user = new UserEntity();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("Joni98yPri01"));

        Role roles = roleRepository.findByName("ADMIN").get();
        user.setRoles(Collections.singletonList(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed in Success", HttpStatus.OK);
    }
}
