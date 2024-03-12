package org.jan.model;

import java.util.Date;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Sale {
    private Long id;
    private Date registration_date;
    private Integer quantity;

}
