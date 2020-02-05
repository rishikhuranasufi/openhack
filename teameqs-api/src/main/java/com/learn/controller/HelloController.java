package com.learn.controller;

/**
 * Created by rishi.khurana on 9/12/2018.
 */
import com.learn.vo.EmployeeDetails;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Welcome to EQS training sample application !!";

    }


    @GetMapping(path = "/employee-details.html", produces = MediaType.TEXT_HTML_VALUE)
    public String getEmployeeDetails() {
        ArrayList<EmployeeDetails> employeeDetails = new ArrayList<EmployeeDetails>();
        employeeDetails.add(new EmployeeDetails("Employee1","E123",23,1000000));
        employeeDetails.add(new EmployeeDetails("Employee2","E124",29,2000000));
        employeeDetails.add(new EmployeeDetails("Employee3","E125",32,3000000));
        employeeDetails.add(new EmployeeDetails("Employee4","E126",36,4000000));
        employeeDetails.add(new EmployeeDetails("Employee5","E127",39,5000000));
        employeeDetails.add(new EmployeeDetails("Employee6","E128",40,6000000));

        StringBuilder startHTML = new StringBuilder("<html><body><table border=\"1\"><colgroup><col /><col /><col /><col /></colgroup><tbody><tr><th>Employee Name</th><th>Employee Id</th><th>Employee Age</th><th>Employee Salary</th></tr>");

        for(EmployeeDetails employeeDetails1 : employeeDetails){
            startHTML.append("<tr><td>").append(employeeDetails1.getEmpName()).append("</td>");
            startHTML.append("<td>").append(employeeDetails1.getEmpNumber()).append("</td>");
            startHTML.append("<td>").append(employeeDetails1.getEmpAge()).append("</td>");
            startHTML.append("<td>").append(employeeDetails1.getEmpSalary()).append("</td></tr>");
        }
        startHTML.append("</body></html>");
        return startHTML.toString();

    }
}