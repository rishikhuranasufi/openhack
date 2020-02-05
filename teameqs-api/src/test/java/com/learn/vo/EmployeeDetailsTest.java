package com.learn.vo;

import com.learn.controller.DtoTest;

/**
 * Created by rishi.khurana on 9/14/2018.
 */
public class EmployeeDetailsTest extends DtoTest<EmployeeDetails>{
    @Override
    protected EmployeeDetails getInstance() {
        return new EmployeeDetails();
    }
}