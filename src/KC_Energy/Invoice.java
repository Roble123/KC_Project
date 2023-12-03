package KC_Energy;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Invoice extends JFrame {
	Connection connection = null;
	
	// Set up database connection
    
    private JTextField customerNameTextField;
    private JComboBox<String> energyTariffComboBox, meterTypeComboBox;
    private JTable invoiceTable;

    
    public Invoice() {
    	try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/customerinfo", "root", "Abdirahman123$");
		
			setTitle("Invoice");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(700, 350);
	        
	        
	        
    	} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameTextField = new JTextField();
        energyTariffComboBox = new JComboBox<>(new String[] {"Standard Energy", "Off-Peak", "Time-of-Use"});
        meterTypeComboBox = new JComboBox<>(new String[] {"Electricity", "TV", "Gas"});
        
        inputPanel.add(customerNameLabel);
        inputPanel.add(customerNameTextField);
        inputPanel.add(new JLabel("Energy Tariff:"));
        inputPanel.add(energyTariffComboBox);
        inputPanel.add(new JLabel("Meter Type:"));
        inputPanel.add(meterTypeComboBox);
        add(inputPanel, BorderLayout.NORTH);

        invoiceTable = new JTable(new DefaultTableModel(new Object[]{"Invoice ID", "Customer Name", "Meter Type", "Amount"}, 0));
        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        add(scrollPane, BorderLayout.CENTER);

     // Create a panel to hold the buttons
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2));
                
        // Create the buttons
        JButton generateInvoiceButton = new JButton("Generate Invoice");
        JButton deleteInvoiceButton = new JButton("Delete Invoice");

        // Add the buttons to the panel
        buttonsPanel.add(generateInvoiceButton);
        buttonsPanel.add(deleteInvoiceButton);

        // Add the panel to the south region of the JFrame
        add(buttonsPanel, BorderLayout.SOUTH);
        
        
        
        // Fetch data from the database and populate the JTable
        try {
            String query = "SELECT * FROM customer_invoice";
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();

            while (rs.next()) {
                int invoiceID = rs.getInt("InvoiceID");
                String customerName = rs.getString("customer_name");
                String meterType = rs.getString("meter_type");
                String energyTariff = rs.getString("energy_tariff");
                double amount = rs.getDouble("amount");

                model.addRow(new Object[] { invoiceID, customerName, meterType, amount });
            }

        } catch (SQLException e) {
            System.out.println("Error fetching data from the database: " + e.getMessage());
        }

        
     // Add an ActionListener to the deleteInvoiceButton
        deleteInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRowIndex = invoiceTable.getSelectedRow();
                
                // Check if a row is selected
                if (selectedRowIndex != -1) {
                    // Get the invoice ID from the selected row
                    int invoiceID = (int) invoiceTable.getValueAt(selectedRowIndex, 0);
                    
                    try {
                        // Create a PreparedStatement to delete the invoice from the database
                        String query = "DELETE FROM customer_invoice WHERE InvoiceID = ?";
                        PreparedStatement stmt = connection.prepareStatement(query);
                        
                        // Set the parameter in the PreparedStatement
                        stmt.setInt(1, invoiceID);
                        
                        // Execute the PreparedStatement
                        int rowsDeleted = stmt.executeUpdate();
                        if (rowsDeleted > 0) {
                            System.out.println("Invoice deleted successfully!");
                            
                            // Remove the selected row from the JTable
                            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
                            model.removeRow(selectedRowIndex);
                        }
                    } catch (SQLException e1) {
                        System.out.println("Error deleting invoice: " + e1.getMessage());
                    }
                }
            }
        });
        
        generateInvoiceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String energyTariff = energyTariffComboBox.getSelectedItem().toString();
                double energyTariffRate = 0.0;
                if (energyTariff.equals("Standard Energy")) {
                    energyTariffRate = 0.7;
                } else if (energyTariff.equals("Off-Peak")) {
                    energyTariffRate = 1.2;
                } else if (energyTariff.equals("Time-of-Use")) {
                    energyTariffRate = 0.9;
                }

                String meterType = meterTypeComboBox.getSelectedItem().toString();
                double meterTypeRate = 0.0;
                if (meterType.equals("Electricity")) {
                    meterTypeRate = 0.6;
                } else if (meterType.equals("Gas")) {
                    meterTypeRate = 0.9;
                } else if (meterType.equals("TV")) {
                    meterTypeRate = 0.12;
                }

                double invoiceAmount = energyTariffRate * meterTypeRate;

                DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
                int invoiceID = model.getRowCount() + 1;
                String customerName = customerNameTextField.getText();
                model.addRow(new Object[] { invoiceID, customerName, meterType, invoiceAmount });
                try {
                    // Create a PreparedStatement to insert the invoice data into the database
                    String query = "INSERT INTO customer_invoice (customer_name, meter_type, energy_tariff, amount) VALUES (?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    
                    // Set the parameters in the PreparedStatement
                    stmt.setString(1, customerName);
                    stmt.setString(2, meterType);
                    stmt.setString(3, energyTariff);
                    stmt.setDouble(4, invoiceAmount);
                    
                    // Execute the PreparedStatement
                    int rowsInserted = stmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Invoice inserted successfully!");
                    }
                } catch (SQLException e1) {
                    System.out.println("Error inserting invoice: " + e1.getMessage());
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        Invoice invoice = new Invoice();
    }
}
