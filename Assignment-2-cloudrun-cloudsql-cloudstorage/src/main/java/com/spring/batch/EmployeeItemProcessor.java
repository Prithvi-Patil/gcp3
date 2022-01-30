package com.spring.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class EmployeeItemProcessor implements ItemProcessor<Employee, Employee> {

   // private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeItemProcessor.class);

    @Override
    public Employee process(final Employee employee) throws Exception {
        String empname = employee.getEmpname().toUpperCase();
        String emplocation = employee.getEmplocation().toUpperCase();
        String chracteristics = employee.getEmpdept().toUpperCase();

        Employee transformedEmployee = new Employee(empname, emplocation, chracteristics);
       // LOGGER.info("Converting ( {} ) into ( {} )", employee, transformedEmployee);

        return transformedEmployee;
    }

}
