package org.jan.model.mapper;

import org.jan.model.Customer;
import org.jan.model.Sale;
import org.jan.model.dto.CustomerDto;
import org.jan.model.dto.SaleDto;

public class SaleMapper {
    public static SaleDto toDto(Sale sale){
        return new SaleDto(sale.getRegistration_date(), sale.getQuantity());
    }
    public static Sale mapFromDto(SaleDto sale){
        return Sale.builder()
                .registration_date(sale.registration_date())
                .quantity(sale.quantity()).build();
    }
}
