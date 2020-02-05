package com.learn.controller;

import com.learn.model.User;
import com.learn.payload.*;
import com.learn.repository.UserRepository;
import com.learn.security.JwtTokenProvider;
import com.learn.security.UserPrincipal;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by rishi.khurana on 10/10/2018.

@RestController */
@RequestMapping("/v1/api/auth")
@Api("This ")
public class AuthRestController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid Login loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();


        return ResponseEntity.ok(new AuthResponse(jwt,loginRequest.getUsername(),userPrincipal.getName(),userPrincipal.getEmail(),userPrincipal.getAuthorities().stream().findFirst().get().getAuthority(), userPrincipal.getId()));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUp signUpRequest) {
        if(userRepository.findByUsername(signUpRequest.getUsername()) != null) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.findByEmail(signUpRequest.getEmail()) != null) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setName(signUpRequest.getName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setUsername(signUpRequest.getUsername());
        user.setRoleid(signUpRequest.getRole());
        User result = userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        result.getUsername(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(new AuthResponse(jwt,signUpRequest.getUsername(),userPrincipal.getName(),userPrincipal.getEmail(),userPrincipal.getAuthorities().stream().findFirst().get().getAuthority(),userPrincipal.getId()));
    }

    @GetMapping("/checkUsernameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "username") String username) {
        Boolean isAvailable = false;
        isAvailable = userRepository.findByUsername(username) == null;

        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = false;
        isAvailable = userRepository.findByEmail(email) == null;
        return new UserIdentityAvailability(isAvailable);
    }
}
