package org.jan.model.mapper;

import org.jan.model.Customer;
import org.jan.model.dto.CustomerDto;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer){
        return new CustomerDto(customer.getCertification(),customer.getPhone());
    }
    public static Customer mapFromDto(CustomerDto customer){
        return Customer.builder()
                .certification(customer.certification())
                .phone(customer.phone()).build();
    }
}
