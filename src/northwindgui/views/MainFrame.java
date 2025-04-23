package northwindgui.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
import northwindgui.models.Employee;
import northwindgui.models.Product;
import northwindgui.models.CategoryReport;
import northwindgui.models.Client;
import northwindgui.services.EmployeeService;
import northwindgui.services.ProductService;
import northwindgui.services.ReportService;
import northwindgui.services.ClientService;

public class MainFrame extends javax.swing.JFrame {

public MainFrame() {
        initComponents();
        initializeEmployeesTable();
        initializeProductsTable();
        initializeReportTable();
        initializeClientTables();
        loadEmployees(null);
        loadProducts();
        loadReport();
        loadAllClients();
        loadInactiveClients();
    }

private void initializeEmployeesTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Address");
        model.addColumn("Address 2");
        model.addColumn("City");
        model.addColumn("Region");
        model.addColumn("Postal Code");
        model.addColumn("Phone");
        model.addColumn("Office");
        model.addColumn("Active");
        employeesTable.setModel(model);
    }

private void initializeProductsTable() {
    // Create a new JTable
    JTable table = new JTable();
    
    // Create and configure the model
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("ID");
    model.addColumn("Name");
    model.addColumn("Supplier ID");
    model.addColumn("Category");
    model.addColumn("Qty/Unit");
    model.addColumn("Unit Price");
    model.addColumn("In Stock");
    model.addColumn("On Order");
    model.addColumn("Reorder Level");
    model.addColumn("Discontinued");
    
    // Set the model to the table
    table.setModel(model);
    
    // Set the table as the view of the scroll pane
    productsTable.setViewportView(table);
}

private void initializeReportTable() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Warehouse");
        model.addColumn("Category");
        model.addColumn("Product Count");
        reportTable.setModel(model);
    }


private void initializeClientTables() {
        // All clients table
        DefaultTableModel allClientsModel = new DefaultTableModel();
        allClientsModel.addColumn("Client ID");
        allClientsModel.addColumn("Company");
        allClientsModel.addColumn("Contact");
        allClientsModel.addColumn("Title");
        allClientsModel.addColumn("Address");
        allClientsModel.addColumn("City");
        allClientsModel.addColumn("Region");
        allClientsModel.addColumn("Postal Code");
        allClientsModel.addColumn("Country");
        allClientsModel.addColumn("Phone");
        allClientsModel.addColumn("Fax");
        allClientsModel.addColumn("Active");
        allClientsTable.setModel(allClientsModel);
        
        // Inactive clients table
        DefaultTableModel inactiveClientsModel = new DefaultTableModel();
        inactiveClientsModel.addColumn("Client ID");
        inactiveClientsModel.addColumn("Company");
        inactiveClientsModel.addColumn("Contact");
        inactiveClientsModel.addColumn("Title");
        inactiveClientsModel.addColumn("Address");
        inactiveClientsModel.addColumn("City");
        inactiveClientsModel.addColumn("Region");
        inactiveClientsModel.addColumn("Postal Code");
        inactiveClientsModel.addColumn("Country");
        inactiveClientsModel.addColumn("Phone");
        inactiveClientsModel.addColumn("Fax");
        inactiveClientsTable.setModel(inactiveClientsModel);
    }


private void loadEmployees(String searchTerm) {
    try {
        EmployeeService service = new EmployeeService();
        List<Employee> employees;
        
        if (searchTerm == null || searchTerm.isEmpty()) {
            employees = service.getAllEmployees();
        } else {
            employees = service.searchEmployees(searchTerm);
        }
        
        DefaultTableModel model = (DefaultTableModel) employeesTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        for (Employee emp : employees) {
            model.addRow(new Object[]{
                emp.getFirstName(),
                emp.getLastName(),
                emp.getAddress(),
                emp.getAddress2(),
                emp.getCity(),
                emp.getRegion(),
                emp.getPostalCode(),
                emp.getPhone(),
                emp.getOffice(),
                emp.isActive() ? "Yes" : "No"
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading employees: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void loadProducts() {
    try {
        ProductService service = new ProductService();
        List<Product> products = service.getAllProducts();
        
        // Get the table from the scroll pane
        JTable table = (JTable)productsTable.getViewport().getView();
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setRowCount(0);
        
        for (Product p : products) {
            model.addRow(new Object[]{
                p.getProductId(),
                p.getProductName(),
                p.getSupplierId(),
                p.getCategory(),
                p.getQuantityPerUnit(),
                p.getUnitPrice(),
                p.getUnitsInStock(),
                p.getUnitsOnOrder(),
                p.getReorderLevel(),
                p.isDiscontinued() ? "Yes" : "No"
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading products: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void loadReport() {
    try {
        ReportService service = new ReportService();
        List<CategoryReport> reports = service.getCategoryConsolidationReport();
        
        DefaultTableModel model = (DefaultTableModel) reportTable.getModel();
        model.setRowCount(0);
        
        for (CategoryReport r : reports) {
            model.addRow(new Object[]{
                r.getWarehouseName(),
                r.getCategoryName(),
                r.getProductCount()
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading report: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}

private void loadAllClients() {
    try {
        ClientService service = new ClientService();
        List<Client> clients;
        
        String searchTerm = clientSearchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            clients = service.getAllClients();
        }else{
            clients = service.searchClients(searchTerm);
        }
        DefaultTableModel model = (DefaultTableModel) allClientsTable.getModel();
        model.setRowCount(0);
        
        for (Client c : clients){
            model.addRow(new Object[]{
                c.getClientId(),
                c.getCompanyName(),
                c.getContactName(),
                c.getContactTitle(),
                c.getAddress(),
                c.getCity(),
                c.getRegion(),
                c.getPostalCode(),
                c.getCountry(),
                c.getPhone(),
                c.getFax(),
                c.isActive() ? "Yes" : "No"
                
            });
        }
    } catch (SQLException ex){
        JOptionPane.showMessageDialog(this,"Error loading clients: " + ex.getMessage(),"Database Error", JOptionPane.ERROR_MESSAGE);
    }
    
}

private void loadInactiveClients() {
    try {
        ClientService service = new ClientService();
        List<Client> clients = service.getInactiveClients();
        
        DefaultTableModel model = (DefaultTableModel) inactiveClientsTable.getModel();
        model.setRowCount(0);
        
        for (Client c : clients) {
            model.addRow(new Object[]{
                c.getClientId(),
                c.getCompanyName(),
                c.getContactName(),
                c.getContactTitle(),
                c.getAddress(),
                c.getCity(),
                c.getRegion(),
                c.getPostalCode(),
                c.getCountry(),
                c.getPhone(),
                c.getFax()
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading inactive clients: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
    }
}
/**
 *
 * @author jeand
 */

    
    /**
     * Creates new form MainFrame
     */

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainTabbedPane = new javax.swing.JTabbedPane();
        employeesPanel = new javax.swing.JPanel();
        employeesSearchField = new javax.swing.JTextField();
        employeesSearchButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        employeesTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        productsTable = new javax.swing.JScrollPane();
        addProductButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        notificationsTabbedPane = new javax.swing.JTabbedPane();
        allClientsPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        allClientsTable = new javax.swing.JTable();
        inactiveClientsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        inactiveClientsTable = new javax.swing.JTable();
        editClientButton = new javax.swing.JButton();
        deleteClientButton = new javax.swing.JButton();
        clientSearchField = new javax.swing.JTextField();
        clientSearchButton = new javax.swing.JButton();
        addClientButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        employeesSearchField.setText("Filter (First/Last Name/ City)");

        employeesSearchButton.setText("Search");
        employeesSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeesSearchButtonActionPerformed(evt);
            }
        });

        employeesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(employeesTable);

        javax.swing.GroupLayout employeesPanelLayout = new javax.swing.GroupLayout(employeesPanel);
        employeesPanel.setLayout(employeesPanelLayout);
        employeesPanelLayout.setHorizontalGroup(
            employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addGroup(employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(employeesSearchButton))
                    .addGroup(employeesPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(employeesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(495, 777, Short.MAX_VALUE))
        );
        employeesPanelLayout.setVerticalGroup(
            employeesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeesPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(employeesSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employeesSearchButton)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Employees", employeesPanel);

        addProductButton.setText("Add Product");
        addProductButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addProductButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productsTable)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(332, 332, 332)
                .addComponent(addProductButton)
                .addContainerGap(518, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(productsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(addProductButton)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Products", jPanel2);

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(reportTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 946, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 70, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Report", jPanel3);

        allClientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(allClientsTable);

        javax.swing.GroupLayout allClientsPanelLayout = new javax.swing.GroupLayout(allClientsPanel);
        allClientsPanel.setLayout(allClientsPanelLayout);
        allClientsPanelLayout.setHorizontalGroup(
            allClientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(allClientsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 945, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        allClientsPanelLayout.setVerticalGroup(
            allClientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
        );

        notificationsTabbedPane.addTab("All Clients", allClientsPanel);

        inactiveClientsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(inactiveClientsTable);

        javax.swing.GroupLayout inactiveClientsPanelLayout = new javax.swing.GroupLayout(inactiveClientsPanel);
        inactiveClientsPanel.setLayout(inactiveClientsPanelLayout);
        inactiveClientsPanelLayout.setHorizontalGroup(
            inactiveClientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inactiveClientsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 922, Short.MAX_VALUE)
                .addContainerGap())
        );
        inactiveClientsPanelLayout.setVerticalGroup(
            inactiveClientsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inactiveClientsPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addContainerGap())
        );

        notificationsTabbedPane.addTab("Inactive Clients", inactiveClientsPanel);

        editClientButton.setText("Edit Client");
        editClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editClientButtonActionPerformed(evt);
            }
        });

        deleteClientButton.setText("Delete Client");
        deleteClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteClientButtonActionPerformed(evt);
            }
        });

        clientSearchButton.setText("Search");
        clientSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientSearchButtonActionPerformed(evt);
            }
        });

        addClientButton.setText("Add Client");
        addClientButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClientButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(notificationsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 934, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(addClientButton)
                                .addGap(18, 18, 18)
                                .addComponent(editClientButton)
                                .addGap(18, 18, 18)
                                .addComponent(deleteClientButton)
                                .addGap(91, 91, 91)
                                .addComponent(clientSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(clientSearchButton)
                        .addGap(423, 423, 423))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(notificationsTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addClientButton)
                    .addComponent(editClientButton)
                    .addComponent(deleteClientButton)
                    .addComponent(clientSearchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clientSearchButton)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Notifications", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTabbedPane)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainTabbedPane)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void employeesSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeesSearchButtonActionPerformed
        String searchTerm = employeesSearchField.getText();
        loadEmployees(searchTerm);
    }//GEN-LAST:event_employeesSearchButtonActionPerformed

    private void addProductButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addProductButtonActionPerformed
        AddProductDialog dialog = new AddProductDialog(this, true);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        loadProducts(); // Refresh after dialog closes
    }//GEN-LAST:event_addProductButtonActionPerformed

    private void addClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClientButtonActionPerformed
        ClientDialog dialog = new ClientDialog(this, true, null, false);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    
    if (dialog.getClient() != null) {
        try {
            ClientService service = new ClientService();
            service.addClient(dialog.getClient());
            loadAllClients();
            loadInactiveClients();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding client: " + ex.getMessage(),
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_addClientButtonActionPerformed

    private void editClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClientButtonActionPerformed
        int selectedRow = allClientsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a client to edit",
                                    "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String clientId = (String) allClientsTable.getValueAt(selectedRow, 0);
    try {
        ClientService service = new ClientService();
        List<Client> clients = service.getAllClients();
        Client clientToEdit = null;
        
        for (Client c : clients) {
            if (c.getClientId().equals(clientId)) {
                clientToEdit = c;
                break;
            }
        }
        
        if (clientToEdit != null) {
            ClientDialog dialog = new ClientDialog(this, true, clientToEdit, true);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
            
            if (dialog.getClient() != null) {
                service.updateClient(dialog.getClient());
                loadAllClients();
                loadInactiveClients();
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error updating client: " + ex.getMessage(),
                                    "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_editClientButtonActionPerformed

    private void deleteClientButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteClientButtonActionPerformed
        int selectedRow = allClientsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a client to delete",
                                    "No Selection", JOptionPane.WARNING_MESSAGE);
        return;
    }
    
    String clientId = (String) allClientsTable.getValueAt(selectedRow, 0);
    String companyName = (String) allClientsTable.getValueAt(selectedRow, 1);
    
    int confirm = JOptionPane.showConfirmDialog(this, 
        "Are you sure you want to delete client: " + companyName + "?",
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            ClientService service = new ClientService();
            service.deleteClient(clientId);
            loadAllClients();
            loadInactiveClients();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting client: " + ex.getMessage(),
                                        "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_deleteClientButtonActionPerformed

    private void clientSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientSearchButtonActionPerformed
        String searchTerm = clientSearchField.getText();
        if (searchTerm == null || searchTerm.trim().isEmpty()){
            loadAllClients();
        }else {
            try{
                ClientService service = new ClientService();
                List<Client> clients = service.searchClients(searchTerm);
                
                DefaultTableModel model = (DefaultTableModel) allClientsTable.getModel();
                model.setRowCount(0);
                
                for (Client c : clients){
                    model.addRow(new Object[]{
                        c.getClientId(),
                        c.getCompanyName(),
                        c.getContactName(),
                        c.getContactTitle(),
                        c.getAddress(),
                        c.getCity(),
                        c.getRegion(),
                        c.getPostalCode(),
                        c.getCountry(),
                        c.getPhone(),
                        c.getFax(),
                        c.isActive() ? "Yes" : "No"
                    });
                }

            } catch (SQLException ex){
                JOptionPane.showMessageDialog(this, "Error searching cleints: "+ ex.getMessage(), "Database Error",JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_clientSearchButtonActionPerformed

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {                                           
    if (mainTabbedPane.getSelectedIndex() == 2) { // Report tab index
        loadReport();
    }
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClientButton;
    private javax.swing.JButton addProductButton;
    private javax.swing.JPanel allClientsPanel;
    private javax.swing.JTable allClientsTable;
    private javax.swing.JButton clientSearchButton;
    private javax.swing.JTextField clientSearchField;
    private javax.swing.JButton deleteClientButton;
    private javax.swing.JButton editClientButton;
    private javax.swing.JPanel employeesPanel;
    private javax.swing.JButton employeesSearchButton;
    private javax.swing.JTextField employeesSearchField;
    private javax.swing.JTable employeesTable;
    private javax.swing.JPanel inactiveClientsPanel;
    private javax.swing.JTable inactiveClientsTable;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JTabbedPane notificationsTabbedPane;
    private javax.swing.JScrollPane productsTable;
    private javax.swing.JTable reportTable;
    // End of variables declaration//GEN-END:variables
}

