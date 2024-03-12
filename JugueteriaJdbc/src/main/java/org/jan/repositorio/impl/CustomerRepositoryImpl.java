package org.jan.repositorio.impl;

import org.jan.model.Customer;
import org.jan.model.dto.CustomerDto;
import org.jan.model.mapper.CustomerMapper;
import org.jan.repositorio.Repository;
import org.jan.repositorio.excepciones.*;
import org.jan.singleton.ConexionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepositoryImpl implements Repository<CustomerDto> {
    private Connection getConnection() throws SQLException {
        return ConexionDataBase.getInstance();
    }
    @Override
    public List<CustomerDto> listar() {
        List<CustomerDto> customers = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM costumers")){
            while (resultSet.next()){
                Customer customer = crearCliente(resultSet);
                CustomerDto customerDto = CustomerMapper.toDto(customer);
                customers.add(customerDto);
            }
        } catch (ToListException | SQLException e) {
            throw new ToListException("Error al listar juguetes desde la base de datos!" + e.getMessage());
        }
        return customers;
    }

    @Override
    public CustomerDto forId(Long id) {
        Customer customer = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM costumers WHERE id = ?")){
            stmt.setLong(1,id);
            try (ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    customer = crearCliente(rs);
                }
            }
        } catch (IdException | SQLException e) {
            throw new IdException("Error getting toy by ID from database" + e.getMessage());
        }
        return CustomerMapper.toDto(customer);
    }

    @Override
    public void save(CustomerDto customer) {
        Customer customer1 = CustomerMapper.mapFromDto(customer);
        String sql;
        if (customer1.getId() != null && customer1.getId() > 0){
            sql = "UPDATE costumers SET certification=?, phone=? WHERE id=?";
        }else {
            sql = "INSERT INTO costumers(certification, phone) VALUES(?, ?)";
        }
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, customer1.getCertification());
            stmt.setString(2, customer1.getPhone());
            if (customer1.getId() != null && customer1.getId() > 0){
                stmt.setLong(3, customer1.getId());
            }
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                // Si no se actualizó ninguna fila, lanzar excepción
                throw new UpdateException("Client with ID not found: " + customer1.getId() + " to actualize");
            }
        } catch (SaveException | SQLException e) {
throw new SaveException("Error saving customer to database!");
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM costumers WHERE id = ?")){
            stmt.setLong(1,id);
            int filasEliminadas = stmt.executeUpdate();

            if (filasEliminadas == 0) {
                // Si no se eliminó ninguna fila, lanzar excepción
                throw new DeleteException("No se encontró el cliente con ID: " + id + " para eliminar");
            }

        } catch (DeleteException | SQLException e) {
            // Capturas la excepción de SQL y la lanzas como tu excepción personalizada
            throw new DeleteException("Error al eliminar el juguete de la base de datos" + e.getMessage());
        }
        }

    private static Customer crearCliente(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setCertification(rs.getString("certification"));
        customer.setPhone(rs.getString("phone"));
        return customer;
    }
}
