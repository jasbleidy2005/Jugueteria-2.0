package org.jan.model.mapper;

import org.jan.model.Customer;
import org.jan.model.Toy;
import org.jan.model.dto.CustomerDto;
import org.jan.model.dto.ToyDto;

public class ToyMapper {
    public static ToyDto toDto(Toy toy){
        return new ToyDto(toy.getName(), toy.getType(),toy.getPrice(),toy.getCustomer(),toy.getSale(),toy.getEmployee());
    }
    public static Toy mapFromDto(ToyDto toy){
        return Toy.builder()
                .name(toy.name())
                .type(toy.type())
                .price(toy.price())
                .customer(toy.customer())
                .employee(toy.employee())
                .sale(toy.sale()).build();
    }
}
