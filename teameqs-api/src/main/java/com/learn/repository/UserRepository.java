package com.learn.repository;

/**
 * Created by rishi.khurana on 10/10/2018.
 */
import com.learn.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

@EnableScan()
public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);
    User findByEmail(String email);
}
