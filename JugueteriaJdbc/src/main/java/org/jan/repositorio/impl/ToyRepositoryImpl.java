package org.jan.repositorio.impl;

import org.jan.model.*;
import org.jan.model.dto.ToyDto;
import org.jan.model.mapper.ToyMapper;
import org.jan.repositorio.Repository;
import org.jan.repositorio.excepciones.IdException;
import org.jan.repositorio.excepciones.SaveException;
import org.jan.repositorio.excepciones.UpdateException;
import org.jan.singleton.ConexionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToyRepositoryImpl implements Repository<ToyDto> {
    private Connection getConnection() throws SQLException {
        return ConexionDataBase.getInstance();
    }
    @Override
    public List<ToyDto> listar() {
        List<ToyDto> toys = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT " +
                     "j.id AS juguete_id, j.name, j.type, j.price, " +
                     "v.id AS venta_id, v.registration_date AS venta_fecha, v.quantity AS venta_cantidad, " +
                     "c.id AS cliente_id, c.certification AS cliente_cedula, c.phone AS cliente_telefono, " +
                     "e.id AS empleado_id, e.name AS empleado_nombre, e.certificate AS empleado_cedula, e.phone AS empleado_telefono " +
                     "FROM toys AS j " +
                     "JOIN costumers AS c ON (j.custumer_id = c.id) " +
                     "JOIN employees AS e ON (j.employee_id = e.id) " +
                     "JOIN sales AS v ON (j.sale_id = v.id)")) {
            while (resultSet.next()) {
                Toy toy = crearJuguete(resultSet);
                ToyDto toyDto = ToyMapper.toDto(toy);
                toys.add(toyDto);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toys;
    }

    @Override
    public ToyDto forId(Long id) {
        Toy toy = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT " +
                     "j.id AS juguete_id, j.name, j.type, j.price, " +
                     "v.id AS venta_id, v.registration_date AS venta_fecha, v.quantity AS venta_cantidad, " +
                     "c.id AS cliente_id, c.certification AS cliente_cedula, c.phone AS cliente_telefono, " +
                     "e.id AS empleado_id, e.name AS empleado_nombre, e.certificate AS empleado_cedula, e.phone AS empleado_telefono " +
                     "FROM toys AS j " +
                     "JOIN costumers AS c ON (j.custumer_id = c.id) " +
                     "JOIN employees AS e ON (j.employee_id = e.id) " +
                     "JOIN sales AS v ON (j.sale_id = v.id)WHERE j.id = ?")){
            stmt.setLong(1,id);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    toy = crearJuguete(resultSet);
                }
            }
        } catch (IdException | SQLException e) {
            throw new IdException("Error getting toy by ID from database" + e.getMessage());
        }
        return ToyMapper.toDto(toy);
    }

    @Override
    public void save(ToyDto toy) {
        Toy toy1 = ToyMapper.mapFromDto(toy);
        String sql;
        if (toy1.getId() != null && toy1.getId() > 0) {
            sql = "UPDATE toys SET name=?, type=?, price=?,custumer_id=?,employee_id=?,sale_id WHERE id=?";
        } else {
            sql = "INSERT INTO toys(name, type, price, custumer_id,employee_id,sale_id) VALUES(?, ?, ?, ?,?,?)";
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, toy1.getName());
            stmt.setString(2, toy1.getType().toString());
            stmt.setInt(3, toy1.getPrice());
            stmt.setLong(4, toy1.getCustomer().getId());
            stmt.setLong(5, toy1.getEmployee().getId());
            stmt.setLong(6, toy1.getSale().getId());

            if (toy1.getId() != null && toy1.getId() > 0) {
                stmt.setLong(7, toy1.getId());
            }
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                // Si no se actualizó ninguna fila, lanzar excepción
                throw new UpdateException("Client with ID not found: " + toy1.getId() + " to actualize");
            }
        } catch (SaveException | SQLException e) {
            throw new SaveException("Error saving customer to database!");
        }
    }

        @Override
    public void delete(Long id) {
            try (Connection conn = getConnection();
                 PreparedStatement stmt = conn.prepareStatement("DELETE FROM toys WHERE id = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    private static Toy crearJuguete(ResultSet resultado) throws SQLException {
        Toy toy = new Toy();
        toy.setId(resultado.getLong("juguete_id"));
        toy.setName(resultado.getString("name"));
        toy.setType(TIpoJuguete.valueOf(resultado.getString("type")));
        toy.setPrice(resultado.getInt("price"));
        Sale sale = new Sale();
        sale.setId(resultado.getLong("venta_id"));
        sale.setRegistration_date(resultado.getDate("venta_fecha"));
        sale.setQuantity(resultado.getInt("venta_cantidad"));
        toy.setSale(sale);
        Customer customer = new Customer();
        customer.setId(resultado.getLong("cliente_id"));
        customer.setCertification(resultado.getString("cliente_cedula"));
        customer.setPhone(resultado.getString("cliente_telefono"));
        toy.setCustomer(customer);
        Employee employee = new Employee();
        employee.setId(resultado.getLong("empleado_id"));
        employee.setName(resultado.getString("empleado_nombre"));
        employee.setCertification(resultado.getString("empleado_cedula"));
        employee.setPhone(resultado.getString("empleado_telefono"));
        toy.setEmployee(employee);

        return toy;
    }
}
