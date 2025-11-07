package dao.impl;

import dao.ProductDAO;
import database.JdbcConnection;
import entity.Product;
import util.LogUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductDAOImpl implements ProductDAO {
    private static final Logger logger = LogUtil.getInstance().getLogger();

    @Override
    public boolean insertProduct(Product product) {
        String sql = "INSERT INTO Product (product_id, product_name) VALUES (?, ?)";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, product.getProductId());
            ps.setString(2, product.getProductName());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("✅ Product inserted: " + product.getProductName());
                return true;
            }

        } catch (SQLException e) {
            logger.severe("Error inserting product: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Product getProductById(int productId) {
        String sql = "SELECT * FROM Product WHERE product_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Product(rs.getInt("product_id"), rs.getString("product_name"));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching product ID " + productId + ": " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Product";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Product(rs.getInt("product_id"), rs.getString("product_name")));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching all products: " + e.getMessage());
        }

        return list;
    }

    // ✅ NEW METHOD — Fetch only product name by ID
    public String getProductNameById(int productId) {
        String sql = "SELECT product_name FROM Product WHERE product_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("product_name");
                logger.info("✅ Found product: " + productName + " (ID: " + productId + ")");
                return productName;
            } else {
                logger.warning("⚠️ No product found for ID: " + productId);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching product name for ID " + productId + ": " + e.getMessage());
        }

        return null; // Product not found
    }
}
