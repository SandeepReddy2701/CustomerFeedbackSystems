package dao;

import entity.Product;
import java.util.List;

public interface ProductDAO {
    boolean insertProduct(Product product);
    Product getProductById(int productId);
    List<Product> getAllProducts();
}
