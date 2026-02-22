package org.example.dao;

import org.example.DBUtil;
import org.example.model.Electronics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.model.Electronics.create;

public class ElectronicsDAO {
    private static final String GET_ALL_SQL = "SELECT category, prodName, quantity, unitCost, margin, weight FROM electronics";
    private static final String INSERT_ELECTRONICS_SQL = "insert into electronics(category, prodName, quantity, unitCost, margin, weight) values (?, ?, ?, ?, ?, ?)";

    public List<Electronics> batchInsertElectronics(List<Electronics> electronicsList) throws SQLException {
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_ELECTRONICS_SQL)){
            conn.setAutoCommit(false);

            for (Electronics e : electronicsList) {
                ps.setString(1, e.getCategory());
                ps.setString(2, e.getProdName());
                ps.setInt(3, e.getQuantity());
                ps.setDouble(4, e.getUnitCost());
                ps.setInt(5, e.getMargin());
                ps.setDouble(6, e.getWeight());
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
        return electronicsList;
    }

    public List<Electronics> getAllElectronics() {
        List<Electronics> list = new ArrayList<>();
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_ALL_SQL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Optional<Electronics> electroOpt = create(
                        rs.getString("category"),
                        rs.getString("prodName"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitCost"),
                        rs.getInt("margin"),
                        rs.getDouble("weight"));
                electroOpt.ifPresent(list::add);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
