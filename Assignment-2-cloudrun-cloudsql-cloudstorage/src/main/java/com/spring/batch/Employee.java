package com.spring.batch;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Employee {

    @Id
    @SequenceGenerator(name = "seqGenerator", sequenceName = "EMPLOYEE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGenerator")
    private Long id;
    private String empname;
    private String emplocation;
    private String empdept;

    public Employee() {
    }

    public Employee(String empname, String emplocation, String empdept) {
        this.empname = empname;
        this.emplocation = emplocation;
        this.empdept = empdept;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmplocation() {
        return emplocation;
    }

    public void setEmplocation(String emplocation) {
        this.emplocation = emplocation;
    }

    public String getEmpdept() {
        return empdept;
    }

    public void setEmpdept(String empdept) {
        this.empdept = empdept;
    }

    @Override
    public String toString() {
        return "Employee [empname=" + getEmpname() + ", emplocation=" + getEmplocation() + ", empdept=" + getEmpdept() + "]";
    }

}
