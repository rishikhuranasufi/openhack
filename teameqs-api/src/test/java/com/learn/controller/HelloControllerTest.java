package com.learn.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.learn.vo.EmployeeDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc mvc;


    @Before
    public void setUp() {
        /*mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();*/
    }
    @Test
    public void getHello() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Welcome to EQS training sample application !!")));
        System.out.print(content().toString());
    }

    @Test
    public void getEmployeeDetails() throws Exception {
        ArrayList<EmployeeDetails> employeeDetails = new ArrayList<EmployeeDetails>();
        employeeDetails.add(new EmployeeDetails("Employee1","E123",23,1000000));
        employeeDetails.add(new EmployeeDetails("Employee2","E124",29,2000000));
        employeeDetails.add(new EmployeeDetails("Employee3","E125",32,3000000));
        employeeDetails.add(new EmployeeDetails("Employee4","E126",36,4000000));
        employeeDetails.add(new EmployeeDetails("Employee5","E127",39,5000000));
        mvc.perform(MockMvcRequestBuilders.get("/employee-details.html").accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk());
    }
}