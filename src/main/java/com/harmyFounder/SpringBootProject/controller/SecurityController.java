package com.harmyFounder.SpringBootProject.controller;

import com.harmyFounder.SpringBootProject.config.JwtCore;
import com.harmyFounder.SpringBootProject.dto.SigninRequest;
import com.harmyFounder.SpringBootProject.dto.SignupRequest;
import com.harmyFounder.SpringBootProject.model.User;
import com.harmyFounder.SpringBootProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtCore jwtCore;



    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody SigninRequest request){

        Authentication authentication = null;

        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch(BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);


        return ResponseEntity.ok(jwt);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Choose different name");
        }

        if(userRepository.existsByEmail(request.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is already in use");
        }

        String hashed = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(hashed);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("You are successfully signed up!");
    }
}
