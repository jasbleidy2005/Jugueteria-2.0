package org.jan.model.dto;

import org.jan.model.Customer;
import org.jan.model.Employee;
import org.jan.model.Sale;
import org.jan.model.TIpoJuguete;

public record ToyDto(String name,TIpoJuguete type,Integer price,Customer customer,Sale sale,Employee employee) {
}
