package org.jan.model;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Toy {
    private Long id;
    private String name;
    private TIpoJuguete type;
    private Integer price;
    private Customer customer;
    private Sale sale;
    private Employee employee;
    @Override
    public String toString() {
        return id + " | " +
                sale.getRegistration_date() + " | " +
                customer.getCertification() + " | " +
                customer.getPhone() + " | " +
                name + " | " +
                type + " | " +
                ("$" + price* sale.getQuantity()) + " | " +
                employee.getName() + " | " +
                employee.getCertification() + " | " +
                employee.getPhone() + " | ";
    }

}
