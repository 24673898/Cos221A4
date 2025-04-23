package northwindgui.services;

import northwindgui.models.Client;
import northwindgui.utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//the servicde file
public class ClientService {
    public List<Client> getAllClients() throws SQLException {
    List<Client> clients = new ArrayList<>();
    String query = "SELECT c.id, c.company, c.last_name, c.first_name, " +
                  "c.email_address, c.job_title, c.business_phone, " +
                  "c.home_phone, c.mobile_phone, c.fax_number, c.address, " +
                  "c.city, c.state_province, c.zip_postal_code, c.country_region " +
                  "FROM customers c";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            Client client = new Client();
            client.setClientId(rs.getString("id"));
            client.setCompanyName(rs.getString("company"));
            client.setContactName(rs.getString("first_name") + " " + rs.getString("last_name"));
            client.setContactTitle(rs.getString("job_title"));
            client.setAddress(rs.getString("address"));
            client.setCity(rs.getString("city"));
            client.setRegion(rs.getString("state_province"));
            client.setPostalCode(rs.getString("zip_postal_code"));
            client.setCountry(rs.getString("country_region"));
            client.setPhone(rs.getString("business_phone"));
            client.setFax(rs.getString("fax_number"));
            client.setActive(true); // You'll need to determine this based on your business logic
            clients.add(client);
        }
    }
    return clients;
}
    
    public List<Client> getInactiveClients() throws SQLException {
    List<Client> clients = new ArrayList<>();
    String query = "SELECT c.id, c.company, c.last_name, c.first_name, " +
                  "c.job_title, c.business_phone, c.fax_number, c.address, " +
                  "c.city, c.state_province, c.zip_postal_code, c.country_region " +
                  "FROM customers c " +
                  "WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.customer_id = c.id " +
                  "AND o.order_date >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR))";
    
    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        
        while (rs.next()) {
            Client client = new Client();
            client.setClientId(rs.getString("id"));
            client.setCompanyName(rs.getString("company"));
            client.setContactName(rs.getString("first_name") + " " + rs.getString("last_name"));
            client.setContactTitle(rs.getString("job_title"));
            client.setAddress(rs.getString("address"));
            client.setCity(rs.getString("city"));
            client.setRegion(rs.getString("state_province"));
            client.setPostalCode(rs.getString("zip_postal_code"));
            client.setCountry(rs.getString("country_region"));
            client.setPhone(rs.getString("business_phone"));
            client.setFax(rs.getString("fax_number"));
            client.setActive(false);
            clients.add(client);
        }
    }
    return clients;
}
    
    private boolean isClientActive(String clientId) throws SQLException {
        String query = "SELECT COUNT(*) FROM orders WHERE customer_id = ? " +
                      "AND order_date >= DATE_SUB(CURDATE(), INTERVAL 1 YEAR)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public void addClient(Client client) throws SQLException {
    String query = "INSERT INTO customers (id, company, last_name, first_name, " +
                 "job_title, address, city, state_province, zip_postal_code, country_region, " +
                 "business_phone, fax_number) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        // Split contact name into first and last name
        String[] names = client.getContactName().split(" ");
        String firstName = names.length > 0 ? names[0] : "";
        String lastName = names.length > 1 ? names[1] : "";
        
        pstmt.setString(1, client.getClientId());
        pstmt.setString(2, client.getCompanyName());
        pstmt.setString(3, lastName);
        pstmt.setString(4, firstName);
        pstmt.setString(5, client.getContactTitle());
        pstmt.setString(6, client.getAddress());
        pstmt.setString(7, client.getCity());
        pstmt.setString(8, client.getRegion());
        pstmt.setString(9, client.getPostalCode());
        pstmt.setString(10, client.getCountry());
        pstmt.setString(11, client.getPhone());
        pstmt.setString(12, client.getFax());
        
        pstmt.executeUpdate();
    }
}
    
    public void updateClient(Client client) throws SQLException {
        String query = "UPDATE customers SET company = ?, first_name = ?, " +
                      "job_title = ?, address = ?, city = ?, state_province = ?, " +
                      "zip_postal_code = ?, country_region = ?, mobile_phone = ?, fax_number = ? " +
                      "WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, client.getCompanyName());
            pstmt.setString(2, client.getContactName());
            pstmt.setString(3, client.getContactTitle());
            pstmt.setString(4, client.getAddress());
            pstmt.setString(5, client.getCity());
            pstmt.setString(6, client.getRegion());
            pstmt.setString(7, client.getPostalCode());
            pstmt.setString(8, client.getCountry());
            pstmt.setString(9, client.getPhone());
            pstmt.setString(10, client.getFax());
            pstmt.setString(11, client.getClientId());
            
            pstmt.executeUpdate();
        }
    }
    
    public void deleteClient(String clientId) throws SQLException {
    // First delete all orders for this client
    String deleteOrdersQuery = "DELETE FROM orders WHERE customer_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(deleteOrdersQuery)) {
        pstmt.setString(1, clientId);
        pstmt.executeUpdate();
    }
    
    // Then delete the client
    String deleteClientQuery = "DELETE FROM customers WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(deleteClientQuery)) {
        pstmt.setString(1, clientId);
        pstmt.executeUpdate();
    }
}
    public List<Client> searchClients(String searchTerm) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String query = "SELECT c.id, c.company, c.last_name, c.first_name,  c.job_title, c.business_phone, c.fax_number, c.address, c.city, c.state_province, c.zip_postal_code, c.country_region FROM customers c WHERE c.first_name LIKE ? OR c.last_name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query))
            
        {
            pstmt.setString(1, "%" + searchTerm + "%");
            pstmt.setString(2, "%" + searchTerm + "%");
            
            try (ResultSet rs = pstmt.executeQuery()){
                while (rs.next()){
                    Client client = new Client();
                    client.setClientId(rs.getString("id"));
                    client.setCompanyName(rs.getString("company"));
                    client.setContactName(rs.getString("first_name")+" "+ rs.getString("last_name"));
                    client.setContactTitle(rs.getString("job_title"));
                    client.setAddress(rs.getString("address"));
                    client.setCity(rs.getString("city"));
                    client.setRegion(rs.getString("state_province"));
                    client.setPostalCode(rs.getString("zip_postal_code"));
                    client.setCountry(rs.getString("country_region"));
                    client.setPhone(rs.getString("business_phone"));
                    client.setFax(rs.getString("fax_number"));
                    client.setActive(true);
                    clients.add(client);
                    
                }
            }
        }
        return clients;
    }
    
}
