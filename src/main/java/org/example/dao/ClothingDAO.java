package org.example.dao;

import org.example.DBUtil;
import org.example.model.Clothing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.example.model.Clothing.create;

public class ClothingDAO {
    private static final String GET_ALL_SQL = "SELECT category, prodName, quantity, unitCost, margin, volume FROM clothing";
    private static final String INSERT_ELECTRONICS_SQL = "insert into clothing(category, prodName, quantity, unitCost, margin, volume) values (?, ?, ?, ?, ?, ?)";

    public List<Clothing> batchInsertClothing(List<Clothing> clothingList) throws SQLException {
        try(Connection conn = DBUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(INSERT_ELECTRONICS_SQL)){
            conn.setAutoCommit(false);

            for (Clothing c : clothingList) {
                ps.setString(1, c.getCategory());
                ps.setString(2, c.getProdName());
                ps.setInt(3, c.getQuantity());
                ps.setDouble(4, c.getUnitCost());
                ps.setInt(5, c.getMargin());
                ps.setDouble(6, c.getVolume());
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            throw e;
        }
        return clothingList;
    }

    public List<Clothing> getAllClothing() {
        List<Clothing> list = new ArrayList<>();
        try (
                Connection conn = DBUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(GET_ALL_SQL);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Optional<Clothing> clothOpt = create(
                        rs.getString("category"),
                        rs.getString("prodName"),
                        rs.getInt("quantity"),
                        rs.getDouble("unitCost"),
                        rs.getInt("margin"),
                        rs.getDouble("volume"));
                clothOpt.ifPresent(list::add);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
