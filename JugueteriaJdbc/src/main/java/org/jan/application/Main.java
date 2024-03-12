package org.jan.application;

import org.jan.model.Customer;
import org.jan.model.dto.CustomerDto;
import org.jan.model.dto.EmployeeDto;
import org.jan.model.dto.SaleDto;
import org.jan.model.dto.ToyDto;
import org.jan.repositorio.Repository;
import org.jan.repositorio.impl.CustomerRepositoryImpl;
import org.jan.repositorio.impl.EmployeeRepositoryImpl;
import org.jan.repositorio.impl.SaleRepositoryImpl;
import org.jan.repositorio.impl.ToyRepositoryImpl;


public class Main {
    public static void main(String[] args) {
        Repository<CustomerDto> repository = new CustomerRepositoryImpl();
        System.out.println("========= List of Customers =========");
        repository.listar().forEach(System.out::println);

        Repository<ToyDto> repository1 = new ToyRepositoryImpl();
        System.out.println("========= List of Toy =========");
        repository1.listar().forEach(System.out::println);

        Repository<SaleDto> repository2 = new SaleRepositoryImpl();
        System.out.println("========= List of Sales =========");
        repository2.listar().forEach(System.out::println);

        Repository<EmployeeDto> repository3 = new EmployeeRepositoryImpl();
        System.out.println("========= List of Employees =========");
        repository3.listar().forEach(System.out::println);


    }
}
