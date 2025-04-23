package northwindgui.services;

import northwindgui.models.CategoryReport;
import northwindgui.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportService {
    public List<CategoryReport> getCategoryConsolidationReport() throws SQLException {
    List<CategoryReport> reports = new ArrayList<>();
    String query = "SELECT p.category, COUNT(p.id) as product_count " +
                  "FROM products p " +
                  "GROUP BY p.category " +
                  "ORDER BY p.category";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            CategoryReport report = new CategoryReport();
            report.setWarehouseName("Main Warehouse"); // Hardcoded since no warehouse table
            report.setCategoryName(rs.getString("category"));
            report.setProductCount(rs.getInt("product_count"));
            reports.add(report);
        }
    }
    return reports;
}
}