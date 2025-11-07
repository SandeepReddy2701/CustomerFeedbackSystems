package dao.impl;

import dao.FeedbackDAO;
import dao.impl.CustomerDAOImpl;
import dao.impl.ProductDAOImpl;
import database.JdbcConnection;
import entity.Feedback;
import util.LogUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FeedbackDAOImpl implements FeedbackDAO {
    private static final Logger logger = LogUtil.getInstance().getLogger();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();
    private final CustomerDAOImpl customerDAO = new CustomerDAOImpl();

    // ✅ Helper method — check if Customer exists
    private boolean isCustomerExists(Connection conn, int customerId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Customer WHERE customer_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    // ✅ Helper method — check if Product exists
    private boolean isProductExists(Connection conn, int productId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Product WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        }
        return false;
    }

    @Override
    public boolean insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback (feedback_id, customer_id, product_id, feedback_date, comments, rating) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = JdbcConnection.getInstance().getConnection()) {

            // ✅ Strict 6-digit numeric validation for IDs
            if (!String.valueOf(feedback.getFeedbackId()).matches("^[0-9]{6}$")) {
                System.out.println("❌ Feedback ID must contain exactly 6 digits (numbers only).");
                logger.warning("❌ Invalid Feedback ID: " + feedback.getFeedbackId());
                return false;
            }
            if (!String.valueOf(feedback.getCustomerId()).matches("^[0-9]{6}$")) {
                System.out.println("❌ Customer ID must contain exactly 6 digits (numbers only).");
                logger.warning("❌ Invalid Customer ID: " + feedback.getCustomerId());
                return false;
            }
            if (!String.valueOf(feedback.getProductId()).matches("^[0-9]{6}$")) {
                System.out.println("❌ Product ID must contain exactly 6 digits (numbers only).");
                logger.warning("❌ Invalid Product ID: " + feedback.getProductId());
                return false;
            }

            // ✅ Check if Customer & Product exist
            boolean customerExists = isCustomerExists(conn, feedback.getCustomerId());
            boolean productExists = isProductExists(conn, feedback.getProductId());

            if (!customerExists) {
                System.out.println("❌ Customer does not exist in the database!");
                logger.warning("❌ Customer ID not found: " + feedback.getCustomerId());
                return false;
            }
            if (!productExists) {
                System.out.println("❌ Product does not exist in the database!");
                logger.warning("❌ Product ID not found: " + feedback.getProductId());
                return false;
            }

            // ✅ Display Product & Customer names before inserting
            String productName = productDAO.getProductNameById(feedback.getProductId());
            String customerName = customerDAO.getCustomerById(feedback.getCustomerId()) != null
                    ? customerDAO.getCustomerById(feedback.getCustomerId()).getCustomerName()
                    : "Unknown";

            System.out.println("📦 Product: " + productName);
            System.out.println("👤 Customer: " + customerName);

            // ✅ Proceed to insert feedback
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, feedback.getFeedbackId());
                ps.setInt(2, feedback.getCustomerId());
                ps.setInt(3, feedback.getProductId());
                ps.setDate(4, Date.valueOf(feedback.getFeedbackDate()));
                ps.setString(5, feedback.getComments());
                ps.setInt(6, feedback.getRating());

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    System.out.println("✅ Feedback added successfully!");
                    logger.info("✅ Feedback inserted successfully (ID: " + feedback.getFeedbackId() + ")");
                    return true;
                } else {
                    System.out.println("⚠️ Feedback insertion failed — no rows affected.");
                    return false;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Duplicate Feedback ID. Please try a new one.");
            logger.warning("❌ Duplicate Feedback ID: " + feedback.getFeedbackId());
        } catch (SQLException e) {
            System.out.println("❌ Database error: " + e.getMessage());
            logger.severe("Database error while inserting feedback: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean updateFeedback(int feedbackId, String comments, Integer rating) {
        StringBuilder sql = new StringBuilder("UPDATE Feedback SET ");
        List<Object> params = new ArrayList<>();

        if (comments != null) {
            sql.append("comments = ?");
            params.add(comments);
        }
        if (rating != null) {
            if (!params.isEmpty()) sql.append(", ");
            sql.append("rating = ?");
            params.add(rating);
        }
        sql.append(" WHERE feedback_id = ?");
        params.add(feedbackId);

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("✅ Feedback updated successfully!");
                logger.info("✅ Feedback updated successfully (ID: " + feedbackId + ")");
                return true;
            } else {
                System.out.println("⚠️ Feedback ID not found in database!");
                logger.warning("⚠️ Feedback ID not found for update: " + feedbackId);
            }

        } catch (SQLException e) {
            logger.severe("Error updating feedback (ID: " + feedbackId + "): " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean deleteFeedback(int feedbackId) {
        String sql = "DELETE FROM Feedback WHERE feedback_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, feedbackId);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("🗑️ Feedback deleted successfully!");
                logger.info("🗑️ Feedback deleted successfully (ID: " + feedbackId + ")");
                return true;
            } else {
                System.out.println("⚠️ Feedback ID not found in database!");
                logger.warning("⚠️ Feedback ID not found for deletion: " + feedbackId);
            }

        } catch (SQLException e) {
            logger.severe("Error deleting feedback ID " + feedbackId + ": " + e.getMessage());
        }

        return false;
    }

    @Override
    public List<Feedback> getFeedbackByProductId(int productId) {
        List<Feedback> list = new ArrayList<>();
        String sql = "SELECT * FROM Feedback WHERE product_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Feedback f = new Feedback(
                        rs.getInt("feedback_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("product_id"),
                        rs.getDate("feedback_date").toLocalDate(),
                        rs.getString("comments"),
                        rs.getInt("rating")
                );
                list.add(f);
            }

            String productName = productDAO.getProductNameById(productId);
            System.out.println("📦 Product: " + (productName != null ? productName : "Unknown"));
            System.out.println("Total Feedback Entries: " + list.size());

        } catch (SQLException e) {
            logger.severe("Error fetching feedback for product ID " + productId + ": " + e.getMessage());
        }

        return list;
    }

    @Override
    public double getAverageRatingByProductId(int productId) {
        String sql = "SELECT AVG(rating) FROM Feedback WHERE product_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            logger.severe("Error calculating average rating for product ID " + productId + ": " + e.getMessage());
        }

        return 0.0;
    }
}
