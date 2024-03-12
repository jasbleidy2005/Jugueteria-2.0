package org.jan.model.mapper;

import org.jan.model.Customer;
import org.jan.model.Employee;
import org.jan.model.dto.CustomerDto;
import org.jan.model.dto.EmployeeDto;

public class EmployeeMapper {
    public static EmployeeDto toDto(Employee employee){
        return new EmployeeDto(employee.getName(),employee.getCertification(),employee.getPhone());
    }
    public static Employee mapFromDto(EmployeeDto employee){
        return Employee.builder()
                .name(employee.name())
                .certification(employee.certification())
                .phone(employee.phone()).build();
    }
}
