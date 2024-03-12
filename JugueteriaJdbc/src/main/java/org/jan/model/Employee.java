package org.jan.model;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Employee {
    private Long id;
    private String name;
    private String certification;
    private String phone;
}
