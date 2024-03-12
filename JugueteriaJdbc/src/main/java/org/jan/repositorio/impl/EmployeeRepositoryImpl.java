package org.jan.repositorio.impl;

import org.jan.model.Employee;
import org.jan.model.dto.EmployeeDto;
import org.jan.model.mapper.EmployeeMapper;
import org.jan.repositorio.Repository;
import org.jan.repositorio.excepciones.IdException;
import org.jan.repositorio.excepciones.SaveException;
import org.jan.repositorio.excepciones.UpdateException;
import org.jan.singleton.ConexionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepositoryImpl implements Repository<EmployeeDto> {
    private Connection getConnection() throws SQLException {
        return ConexionDataBase.getInstance();
    }

    @Override
    public List<EmployeeDto> listar() {
        List<EmployeeDto> employees = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultSet = stmt.executeQuery("SELECT * FROM employees")) {
            while (resultSet.next()) {
                Employee employee = crearEmpleado(resultSet);
                EmployeeDto employeeDto = EmployeeMapper.toDto(employee);
                employees.add(employeeDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public EmployeeDto forId(Long id) {
        Employee employee = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM employees WHERE id = ?")) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    employee = crearEmpleado(rs);
                }
            }
        } catch (IdException | SQLException e) {
            throw new IdException("Error getting toy by ID from database" + e.getMessage());
        }
        return EmployeeMapper.toDto(employee);
    }

    @Override
    public void save(EmployeeDto employee) {
        Employee employee1 = EmployeeMapper.mapFromDto(employee);
        String sql;
        if (employee1.getId() != null && employee1.getId() > 0) {
            sql = "UPDATE employees SET name=?, certificate=?, phone=? WHERE id=?";
        } else {
            sql = "INSERT INTO employees(nombre, certificate, phone) VALUES(?, ?, ?)";
        }
        try (Connection conn = getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee1.getName());
            stmt.setString(2, employee1.getCertification());
            stmt.setString(3, employee1.getPhone());

            if (employee1.getId() != null && employee1.getId() > 0){
                stmt.setLong(1, employee1.getId());
            }
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                // Si no se actualizó ninguna fila, lanzar excepción
                throw new UpdateException("Client with ID not found: " + employee1.getId() + " to actualize");
            }
        } catch (SaveException | SQLException e) {
            throw new SaveException("Error saving customer to database!");
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM employees WHERE id = ?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Employee crearEmpleado(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setCertification(rs.getString("certificate"));
        employee.setPhone(rs.getString("phone"));
        return employee;
    }
}
