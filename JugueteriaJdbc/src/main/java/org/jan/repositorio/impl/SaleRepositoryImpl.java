package org.jan.repositorio.impl;

import org.jan.model.*;
import org.jan.model.dto.SaleDto;
import org.jan.model.mapper.SaleMapper;
import org.jan.repositorio.Repository;
import org.jan.repositorio.excepciones.IdException;
import org.jan.repositorio.excepciones.SaveException;
import org.jan.repositorio.excepciones.UpdateException;
import org.jan.singleton.ConexionDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryImpl implements Repository<SaleDto> {
    private Connection getConnection() throws SQLException {
        return ConexionDataBase.getInstance();
    }
    @Override
    public List<SaleDto> listar() {
        List<SaleDto> sales = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet resultado = stmt.executeQuery("SELECT * FROM sales")){
            while (resultado.next()) {
                Sale sale = crearVenta(resultado);
                SaleDto saleDto = SaleMapper.toDto(sale);
                sales.add(saleDto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }
    @Override
    public SaleDto forId(Long id) {
        Sale sale = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT *FROM sales WHERE id = ?")){
            stmt.setLong(1,id);
            try (ResultSet resultSet = stmt.executeQuery()){
                if (resultSet.next()){
                    sale = crearVenta(resultSet);
                }
            }
        } catch (IdException | SQLException e) {
            throw new IdException("Error getting toy by ID from database" + e.getMessage());       }
        return SaleMapper.toDto(sale);
    }
    @Override
    public void save(SaleDto sale) {
        Sale sale1 = SaleMapper.mapFromDto(sale);
        String sql;
        if (sale1.getId() != null && sale1.getId() > 0) {
            sql = "UPDATE sales SET quantity=? WHERE id=?";
        } else {
            sql = "INSERT INTO sales(quantity,registration_date) VALUES (?,?,?,?,?)";
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sale1.getQuantity());

            if (sale1.getId() != null && sale1.getId() > 0) {
                stmt.setLong(2, sale1.getId());
            } else {
                stmt.setDate(2, new Date(sale1.getRegistration_date().getTime()));

            }
            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas == 0) {
                // Si no se actualizó ninguna fila, lanzar excepción
                throw new UpdateException("Client with ID not found: " + sale1.getId() + " to actualize");
            }
        } catch (SaveException | SQLException e) {
            throw new SaveException("Error saving customer to database!");
        }
    }
    @Override
    public void delete(Long id) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM sales WHERE id = ?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static Sale crearVenta(ResultSet resultado) throws SQLException {
       Sale sale = new Sale();
       sale.setId(resultado.getLong("id"));
       sale.setQuantity(resultado.getInt("quantity"));
       sale.setRegistration_date(resultado.getDate("registration_date"));
       return sale;
    }
}
