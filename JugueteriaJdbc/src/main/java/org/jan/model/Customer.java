package org.jan.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Customer {
    private Long id;
    private String certification;
    private String phone;
}
