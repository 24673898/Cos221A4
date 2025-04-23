package northwindgui.services;

import northwindgui.models.Product;
import northwindgui.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeand
 */
public class ProductService {
    public List<Product> getAllProducts() throws SQLException {
    List<Product> products = new ArrayList<>();
    String query = "SELECT p.id, p.product_name, p.supplier_ids, p.category, " +
                 "p.quantity_per_unit, p.list_price, p.reorder_level, p.discontinued, " +
                 "(SELECT SUM(it.quantity) FROM inventory_transactions it WHERE it.product_id = p.id AND it.transaction_type = 1) as in_stock, " +
                 "(SELECT SUM(it.quantity) FROM inventory_transactions it WHERE it.product_id = p.id AND it.transaction_type = 2) as on_order " +
                 "FROM products p";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            Product product = new Product();
            product.setProductId(rs.getInt("id"));
            product.setProductName(rs.getString("product_name"));
            
            // Extract first supplier ID from supplier_ids string
            String supplierIds = rs.getString("supplier_ids");
            int supplierId = 0;
            if (supplierIds != null && !supplierIds.isEmpty()) {
                try {
                    supplierId = Integer.parseInt(supplierIds.split(";")[0]);
                } catch (NumberFormatException e) {
                    supplierId = 0;
                }
            }
            product.setSupplierId(supplierId);
            
            product.setCategory(rs.getString("category")); // Changed to category name
            product.setQuantityPerUnit(rs.getString("quantity_per_unit"));
            product.setUnitPrice(rs.getDouble("list_price"));
            product.setUnitsInStock(rs.getInt("in_stock")); // Now using actual inventory data
            product.setUnitsOnOrder(rs.getInt("on_order")); // Now using actual order data
            product.setReorderLevel(rs.getInt("reorder_level"));
            product.setDiscontinued(rs.getBoolean("discontinued"));
            products.add(product);
        }
    }
    return products;
}
    
    public void addProduct(Product product) throws SQLException {
    String query = "INSERT INTO products (product_name, supplier_ids, category, " +
                 "quantity_per_unit, list_price, reorder_level, discontinued) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, product.getProductName());
        pstmt.setString(2, String.valueOf(product.getSupplierId())); // Convert supplier ID to string
        pstmt.setString(3, product.getCategory());
        pstmt.setString(4, product.getQuantityPerUnit());
        pstmt.setDouble(5, product.getUnitPrice());
        pstmt.setInt(6, product.getReorderLevel());
        pstmt.setBoolean(7, product.isDiscontinued());
        
        pstmt.executeUpdate();
    }
}
    
    public List<String> getAllSuppliers() throws SQLException {
        List<String> suppliers = new ArrayList<>();
        String query = "SELECT id, company FROM suppliers ORDER BY company";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                suppliers.add(rs.getInt("id") + " - " + rs.getString("company"));
            }
        }
        return suppliers;
    }
    
    public List<String> getAllCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT id, category FROM products ORDER BY category";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                categories.add(rs.getInt("id") + " - " + rs.getString("category"));
            }
        }
        return categories;
    }
    
}
