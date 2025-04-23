package northwindgui.services;

import northwindgui.models.Employee;
import northwindgui.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jeand
 */
public class EmployeeService {
    
    public List<Employee> getAllEmployees() throws SQLException {
    List<Employee> employees = new ArrayList<>();
    String query = "SELECT e.id, e.last_name, e.first_name, e.job_title, e.address, " +
                  "e.city, e.state_province, e.zip_postal_code, e.country_region, e.business_phone " +
                  "FROM employees e";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            Employee employee = new Employee(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("address"),
                "", // address_line2 doesn't exist
                rs.getString("city"),
                rs.getString("state_province"),
                rs.getString("zip_postal_code"),
                rs.getString("business_phone"),
                "", // office doesn't exist
                true // active status
            );
            employees.add(employee);
        }
    }
    return employees;
}
    
    public List<Employee> searchEmployees(String searchTerm) throws SQLException {
    List<Employee> employees = new ArrayList<>();
    String query = "SELECT e.id, e.last_name, e.first_name, e.job_title, e.address, " +
                  "e.city, e.state_province, e.zip_postal_code, e.country_region, e.home_phone, " +
                  "e.notes " +
                  "FROM employees e " +
                  "WHERE e.first_name LIKE ? OR e.last_name LIKE ? OR e.city LIKE ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        pstmt.setString(1, "%" + searchTerm + "%");
        pstmt.setString(2, "%" + searchTerm + "%");
        pstmt.setString(3, "%" + searchTerm + "%");
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("address"),
                    "", // address_line2
                    rs.getString("city"),
                    rs.getString("state_province"),
                    rs.getString("zip_postal_code"),
                    rs.getString("home_phone"),
                    "", // office
                    true // active
                );
                employees.add(employee);
            }
        }
    }
    return employees;
}
    
}
