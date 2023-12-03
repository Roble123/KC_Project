package KC_Energy;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerInfo extends JFrame {

	Connection connection = null;
	 
	private JTable customerTable;
	private DefaultTableModel customerTableModel;
	private JButton addButton, updateButton, deleteButton, searchButton;
	private JTextField nameField, phoneField, addressField, searchField;
	private JComboBox tariffComboBox, meterTypeComboBox;

	public CustomerInfo() {
        try {
            // Set up database connection
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerinfo", "root", "Abdirahman123$");

            // Set up the main window
            setTitle("Customer Management System");
            setSize(800,500);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Set up the customer table
            customerTableModel = new DefaultTableModel();
            customerTableModel.addColumn("ID");
            customerTableModel.addColumn("Name");
            customerTableModel.addColumn("Phone");
            customerTableModel.addColumn("Address");
            customerTableModel.addColumn("Energy Tariff");
            customerTableModel.addColumn("Meter Type");

            // Retrieve the data from the database and add it to the customer table model
            String sql = "SELECT * FROM kc_customers";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
        

            while (resultSet.next()) {
                Object[] row = new Object[6];
                row[0] = resultSet.getInt("ID");
                row[1] = resultSet.getString("Name");
                row[2] = resultSet.getString("Phone");
                row[3] = resultSet.getString("Address");
                row[4] = resultSet.getString("Energy_Tariff");
                row[5] = resultSet.getString("Meter_Type");
                customerTableModel.addRow(row);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        customerTable = new JTable(customerTableModel);
        JScrollPane scrollPane = new JScrollPane(customerTable);
     // Set the background color of the table
        customerTable.setBackground(Color.lightGray);

        add(scrollPane, BorderLayout.CENTER);

        // Set up the buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        searchButton = new JButton("Search");
         
        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(searchButton);
         
        
        add(buttonsPanel, BorderLayout.SOUTH);
        
      

        // Set up the form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2));
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel phoneLabel = new JLabel("Phone:");
        phoneField = new JTextField();
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField();
        JLabel tariffLabel = new JLabel("Energy Tariff:");
        String[] tariffs = {"Standard", "Off-peak", "Time-of-use"};
        tariffComboBox = new JComboBox(tariffs);
        JLabel meterTypeLabel = new JLabel("Meter Type:");
        String[] meterTypes = {"Gas", "Electricty"};
        meterTypeComboBox = new JComboBox(meterTypes);
        JLabel searchLabel = new JLabel("Search by Name:");
        searchField = new JTextField();
        
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(addressLabel);
        formPanel.add(addressField);
        formPanel.add(tariffLabel);
        formPanel.add(tariffComboBox);
        formPanel.add(meterTypeLabel);
        formPanel.add(meterTypeComboBox);
        formPanel.add(searchLabel);
        formPanel.add(searchField);
       //ormPanel.add(searchButton);
        add(formPanel, BorderLayout.NORTH);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the search term from the search field
                String searchTerm = searchField.getText();

                // Query the database for customers with names that match the search term
                try {
                    String sql = "SELECT * FROM kc_customers WHERE Name LIKE ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, "%" + searchTerm + "%");
                    ResultSet resultSet = statement.executeQuery();

                    // Clear the customer table model
                    customerTableModel.setRowCount(0);

                    // Add the results to the customer table model
                    while (resultSet.next()) {
                        Object[] row = new Object[6];
                        row[0] = resultSet.getInt("ID");
                        row[1] = resultSet.getString("Name");
                        row[2] = resultSet.getString("Phone");
                        row[3] = resultSet.getString("Address");
                        row[4] = resultSet.getString("Energy_Tariff");
                        row[5] = resultSet.getString("Meter_Type");
                        customerTableModel.addRow(row);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });


        // Add listeners to the buttons
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get input values from text fields and combo boxes
                String name = nameField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String tariff = (String) tariffComboBox.getSelectedItem();
                String meterType = (String) meterTypeComboBox.getSelectedItem();

                // Create a new row with input values
                Object[] row = new Object[6];
                row[0] = customerTableModel.getRowCount() + 1; // ID
                row[1] = name;
                row[2] = phone;
                row[3] = address;
                row[4] = tariff;
                row[5] = meterType;

                // Add the new row to the customer table model
                customerTableModel.addRow(row);

                // Update the customer table to display the new row
                customerTable.setModel(customerTableModel);
             // Insert the new customer to the database
                try {
                    String st = "INSERT INTO kc_customers  (Name, Phone, Address, Energy_Tariff, Meter_Type) " +
                                 "VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(st);
                    stmt.setString(1, name);
                    stmt.setString(2, phone);
                    stmt.setString(3, address);
                    stmt.setString(4, tariff);
                    stmt.setString(5, meterType);
                    stmt.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                // Clear the input fields
                nameField.setText("");
                phoneField.setText("");
                addressField.setText("");
                tariffComboBox.setSelectedIndex(0);
                meterTypeComboBox.setSelectedIndex(0);
            }
        });


        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRowIndex = customerTable.getSelectedRow();

                if (selectedRowIndex != -1) {
                    // Get the selected row data
                    String id = customerTableModel.getValueAt(selectedRowIndex, 0).toString();
                    String name = customerTableModel.getValueAt(selectedRowIndex, 1).toString();
                    String phone = customerTableModel.getValueAt(selectedRowIndex, 2).toString();
                    String address = customerTableModel.getValueAt(selectedRowIndex, 3).toString();
                    String tariff = customerTableModel.getValueAt(selectedRowIndex, 4).toString();
                    String meterType = customerTableModel.getValueAt(selectedRowIndex, 5).toString();

                    // Populate the form fields with the selected row data
                    nameField.setText(name);
                    phoneField.setText(phone);
                    addressField.setText(address);
                    tariffComboBox.setSelectedItem(tariff);
                    meterTypeComboBox.setSelectedItem(meterType);

                    // Add a listener to the update button to update the row
                    updateButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // Retrieve the updated data from the form fields
                            String updatedName = nameField.getText();
                            String updatedPhone = phoneField.getText();
                            String updatedAddress = addressField.getText();
                            String updatedTariff = (String) tariffComboBox.getSelectedItem();
                            String updatedMeterType = (String) meterTypeComboBox.getSelectedItem();

                            // Update the row in the customerTableModel
                            customerTableModel.setValueAt(updatedName, selectedRowIndex, 1);
                            customerTableModel.setValueAt(updatedPhone, selectedRowIndex, 2);
                            customerTableModel.setValueAt(updatedAddress, selectedRowIndex, 3);
                            customerTableModel.setValueAt(updatedTariff, selectedRowIndex, 4);
                            customerTableModel.setValueAt(updatedMeterType, selectedRowIndex, 5);

                            // Update the database with the new data
                            try {
                                String st = "UPDATE kc_customers SET Name = ?, Phone = ?, Address = ?, Energy_Tariff = ?, Meter_Type = ? WHERE ID = ?";
                                PreparedStatement stmt = connection.prepareStatement(st);
                                stmt.setString(1, updatedName);
                                stmt.setString(2, updatedPhone);
                                stmt.setString(3, updatedAddress);
                                stmt.setString(4, updatedTariff);
                                stmt.setString(5, updatedMeterType);
                                stmt.setString(6, id);
                                stmt.executeUpdate();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                            // Clear the input fields
                            nameField.setText("");
                            phoneField.setText("");
                            addressField.setText("");
                            tariffComboBox.setSelectedIndex(0);
                            meterTypeComboBox.setSelectedIndex(0);
                        }
                    });
                } else {
                    JOptionPane.showMessageDialog(CustomerInfo.this, "Please select a row to update.");
                }
            }
        });






        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row(s) index
                int[] selectedRows = customerTable.getSelectedRows();
                
                // Remove the row(s) from the customer table model and database
                for (int i = selectedRows.length - 1; i >= 0; i--) {
                    int selectedRow = selectedRows[i];
                    int customerID = (int) customerTableModel.getValueAt(selectedRow, 0);
                    customerTableModel.removeRow(selectedRow);
                    try {
                        String sql = "DELETE FROM kc_customers WHERE ID=?";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setInt(1, customerID);
                        statement.executeUpdate();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        
        
        setVisible(true);
    
   }
	public static void main(String[] args) {
		new CustomerInfo();
	}
	
}


