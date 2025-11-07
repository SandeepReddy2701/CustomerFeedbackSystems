package dao.impl;

import dao.CustomerDAO;
import database.JdbcConnection;
import entity.Customer;
import util.LogUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomerDAOImpl implements CustomerDAO {
    private static final Logger logger = LogUtil.getInstance().getLogger();

    @Override
    public boolean insertCustomer(Customer customer) {
        String sql = "INSERT INTO Customer (customer_id, customer_name) VALUES (?, ?)";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // ✅ Validate that customer_id is exactly 6 digits
            if (String.valueOf(customer.getCustomerId()).length() != 6) {
                System.out.println("❌ Customer ID must be exactly 6 digits.");
                logger.warning("❌ Invalid Customer ID length: " + customer.getCustomerId());
                return false;
            }

            ps.setInt(1, customer.getCustomerId());
            ps.setString(2, customer.getCustomerName());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                logger.info("✅ Customer inserted: " + customer.getCustomerName());
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("❌ Duplicate Customer ID. Please use a unique ID.");
            logger.warning("❌ Duplicate Customer ID: " + customer.getCustomerId());
        } catch (SQLException e) {
            logger.severe("Error inserting customer: " + e.getMessage());
        }

        return false;
    }

    @Override
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE customer_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(rs.getInt("customer_id"), rs.getString("customer_name"));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching customer ID " + customerId + ": " + e.getMessage());
        }

        return null;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_name")));
            }

        } catch (SQLException e) {
            logger.severe("Error fetching all customers: " + e.getMessage());
        }

        return list;
    }

    // ✅ NEW METHOD — Fetch only customer name by ID
    public String getCustomerNameById(int customerId) {
        String sql = "SELECT customer_name FROM Customer WHERE customer_id = ?";

        try (Connection conn = JdbcConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("customer_name");
                logger.info("✅ Found customer: " + name + " (ID: " + customerId + ")");
                return name;
            } else {
                logger.warning("⚠️ No customer found for ID: " + customerId);
            }

        } catch (SQLException e) {
            logger.severe("Error fetching customer name for ID " + customerId + ": " + e.getMessage());
        }

        return null; // Customer not found
    }
}
