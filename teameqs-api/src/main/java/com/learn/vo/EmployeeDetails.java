package com.learn.vo;

/**
 * Created by rishi.khurana on 9/12/2018.
 */
public class EmployeeDetails {

    public String empName;
    public String empNumber;
    public Integer empAge;
    public Integer empSalary;

    public EmployeeDetails(){

    }
    public EmployeeDetails(String empName, String empNumber, Integer empAge, Integer empSalary ){
        this.empName=empName;
        this.empNumber=empNumber;
        this.empAge=empAge;
        this.empSalary= empSalary;
    }
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(String empNumber) {
        this.empNumber = empNumber;
    }

    public Integer getEmpAge() {
        return empAge;
    }

    public void setEmpAge(Integer empAge) {
        this.empAge = empAge;
    }

    public Integer getEmpSalary() {
        return empSalary;
    }

    public void setEmpSalary(Integer empSalary) {
        this.empSalary = empSalary;
    }
}
