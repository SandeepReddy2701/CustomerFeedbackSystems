package dao;

import entity.Customer;
import java.util.List;

public interface CustomerDAO {
    boolean insertCustomer(Customer customer);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
}
