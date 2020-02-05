package com.learn.controller;

import com.learn.model.User;
import com.learn.payload.UserDetails;
import com.learn.repository.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by rishi.khurana on 12/31/2018.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserRestControllerMockTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserRestController userRestController;

    @Test
    public void testGetAll() throws Exception {
        when(repository.findAll()).thenReturn(getUsers());
        List<UserDetails> allReturnedUsers = userRestController.getAll();
        Assert.assertNotNull(allReturnedUsers.get(0));
        Assert.assertEquals("portal.admin@eqs.com",allReturnedUsers.get(0).getEmail());
        Assert.assertEquals("Portal Admin",allReturnedUsers.get(0).getName());
    }

    private List<User> getUsers() {
        User user = new User("portal.admin@eqs.com","test","Portal Admin","portaladmin","ADMIN");
        User user1 = new User("test.admin@eqs.com","test","Test","test","ADMIN");
        List<User> allUsers = new ArrayList<User>();
        allUsers.add(user);
        allUsers.add(user1);
        return allUsers;
    }
}