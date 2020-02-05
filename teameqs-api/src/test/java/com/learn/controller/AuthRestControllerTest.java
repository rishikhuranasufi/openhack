package com.learn.controller;

import com.learn.repository.UserRepository;
import com.learn.security.JwtTokenProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

/**
 * Created by rishi.khurana on 12/28/2018.
 */
/*@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc*/
@RunWith(MockitoJUnitRunner.class)
public class AuthRestControllerTest {

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    JwtTokenProvider tokenProvider;

    @InjectMocks
    AuthRestController authRestController;

    @Test
    public void testAuthenticateUser() throws Exception {
        /*Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("rishikhu","rishikhu"));
        String token = tokenProvider.generateToken(authentication);*/
        /*mvc.perform(MockMvcRequestBuilders.get("/v1/api/auth/signin").header("Authorization", "Bearer "+token).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Login loginRequest = new Login();
        loginRequest.setUsername("rishikhu");
        loginRequest.setPassword("rishikhu");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post("/v1/api/auth/signin",loginRequest).accept(MediaType.ALL_VALUE))
                .andReturn();
        System.out.println(mvcResult.toString());
        System.out.println(mvcResult.getRequest().getPathInfo());*/


    }

    @Test
    public void testRegisterUser() throws Exception {

        /*Authentication authentication = mock(Authentication.class);
        User result = mock(User.class);
        SignUp signUp = mock(SignUp.class);


        SignUp signUpRequest = new SignUp();
        signUpRequest.setPassword("rishikhutest");
        signUpRequest.setUsername("rishikhu");
        signUpRequest.setRole("ADMIN");
        signUpRequest.setEmail("rishikhu@gmail.com");
        signUpRequest.setEnabled(true);
        signUpRequest.setName("Rishi Khurana");

        User user = new User();
        user.setPassword("rishikhutest");
        user.setUsername("rishikhu");
        user.setRoleid("ADMIN");
        user.setEmail("rishikhu@gmail.com");
        user.setName("Rishi Khurana");
        user.setId(null);

        when(userRepository.findByEmail(signUp.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(signUp.getUsername())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(result);
        when(passwordEncoder.encode(signUpRequest.getPassword())).thenReturn("rishikhutest");


        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                result.getUsername(),
                signUp.getPassword()
        ))).thenReturn(authentication);

        when(tokenProvider.generateToken(authentication)).thenReturn("jwtToken");*/


        //authenticationManager
    }

    @Test
    public void testCheckUsernameAvailability() throws Exception {

    }

    @Test
    public void testCheckEmailAvailability() throws Exception {

    }
}