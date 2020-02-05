package com.learn.controller;

import com.learn.model.User;
import com.learn.payload.ApiResponse;
import com.learn.payload.UserDetails;
import com.learn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


/*@RestController*/
public class UserRestController {

    @Autowired
    private UserRepository repository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/v1/users", method = RequestMethod.GET)
    public List<UserDetails> getAll() {
        List<UserDetails> listOfUsersWithRole = new ArrayList<>();
        ((Collection<User>) repository.findAll()).stream().forEach(user ->{
            listOfUsersWithRole.add(new UserDetails(user));
        });
        return listOfUsersWithRole;
    }

    @RequestMapping(value = "/v1/userPasswordUpdate/{username}/{oldPassword}/{newPassword}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePassword(@PathVariable String username,
                                            @PathVariable String oldPassword, @PathVariable String newPassword) {
        User update = repository.findByUsername(username);
        if (!passwordEncoder.matches(oldPassword, update.getPassword())){
            return new ResponseEntity<ApiResponse>(new ApiResponse(false, "Please check your Old password"),
                    HttpStatus.UNAUTHORIZED);
        }
        update.setPassword(passwordEncoder.encode(newPassword));
        repository.save(update);
        return ResponseEntity.ok(new ApiResponse(true,"Password Update Success !"));
    }

    @RequestMapping(value = "/v1/user/{id}", method = RequestMethod.GET)
    public User get(@PathVariable String id) {
        return repository.findById(UUID.fromString(id)).get();
    }

    @RequestMapping(value = "/v1/user", method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return repository.save(user);
    }

    @RequestMapping(value = "/v1/user/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String id) {
        repository.deleteById(UUID.fromString(id));
    }
    @RequestMapping(value = "/v1/user/role/{id}", method = RequestMethod.PUT)
    public User updateRole(@PathVariable String id, @RequestBody User user) {
        User update = repository.findById(UUID.fromString(id)).get();
        update.setRoleid(user.getRoleid());
        return repository.save(update);
    }
}
