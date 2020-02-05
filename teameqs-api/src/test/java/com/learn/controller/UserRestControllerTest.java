package com.learn.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.learn.model.User;
import com.learn.payload.UserDetails;
import com.learn.repository.UserRepository;
import com.learn.security.JwtTokenProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rishi.khurana on 12/28/2018.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserRestController userRestController;


    @Test
    public void testGetAllUsingMockBean() throws Exception {

        Mockito.when(userRepository.findAll()).thenReturn(getUsers());
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

    @Ignore
    public void testGetAll() throws Exception {

        String token = getToken();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/v1/users").header("Authorization", "Bearer "+token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        JSONArray allUsers = (JSONArray) JSONParser.parseJSON(mvcResult.getResponse().getContentAsString());

        Assert.assertEquals(allUsers.length(),3);
        User user = getUserFromJSONArray(allUsers,0);
        Assert.assertNotNull(user);
        Assert.assertEquals("portal.admin@eqs.com",user.getEmail());
        Assert.assertEquals("Portal Admin",user.getName());
    }

    private String getToken() {
        Authentication authentication = authenticationManager
               .authenticate(new UsernamePasswordAuthenticationToken("rishikhu","rishikhu"));
        return tokenProvider.generateToken(authentication);
    }

    private User getUserFromJSONArray(JSONArray allUsers,int arrayIndex) throws JSONException, java.io.IOException {
        JsonNode jsonNode = convertJsonFormat(allUsers.getJSONObject(arrayIndex));
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new TreeTraversingParser(jsonNode), User.class);
    }


    static JsonNode convertJsonFormat(JSONObject json) {
        ObjectNode ret = JsonNodeFactory.instance.objectNode();

        @SuppressWarnings("unchecked")
        Iterator<String> iterator = json.keys();
        for (; iterator.hasNext();) {
            String key = iterator.next();
            Object value;
            try {
                value = json.get(key);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (json.isNull(key))
                ret.putNull(key);
            else if (value instanceof String)
                ret.put(key, (String) value);
            else if (value instanceof Integer)
                ret.put(key, (Integer) value);
            else if (value instanceof Long)
                ret.put(key, (Long) value);
            else if (value instanceof Double)
                ret.put(key, (Double) value);
            else if (value instanceof Boolean)
                ret.put(key, (Boolean) value);
            else if (value instanceof JSONObject)
                ret.put(key, convertJsonFormat((JSONObject) value));
            else if (value instanceof JSONArray)
                ret.put(key, convertJsonFormat((JSONArray) value));
            else
                throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
        }
        return ret;
    }

    static JsonNode convertJsonFormat(JSONArray json) {
        ArrayNode ret = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < json.length(); i++) {
            Object value;
            try {
                value = json.get(i);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if (json.isNull(i))
                ret.addNull();
            else if (value instanceof String)
                ret.add((String) value);
            else if (value instanceof Integer)
                ret.add((Integer) value);
            else if (value instanceof Long)
                ret.add((Long) value);
            else if (value instanceof Double)
                ret.add((Double) value);
            else if (value instanceof Boolean)
                ret.add((Boolean) value);
            else if (value instanceof JSONObject)
                ret.add(convertJsonFormat((JSONObject) value));
            else if (value instanceof JSONArray)
                ret.add(convertJsonFormat((JSONArray) value));
            else
                throw new RuntimeException("not prepared for converting instance of class " + value.getClass());
        }
        return ret;
    }
    @Test
    public void testUpdatePassword() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testUpdateRole() throws Exception {

    }
}