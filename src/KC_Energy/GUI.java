package KC_Energy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener {
	private JFrame currentWindow;
    private JMenuBar menuBar;
    private JMenu homeMenu, customerMenu, billingMenu;
    private JPanel homePanel, customerPanel, billingPanel;

    public GUI() {
        setTitle("KC Energy System");

        // Initialize menu bar and menus
        menuBar = new JMenuBar();

        homeMenu = new JMenu("Home");
        customerMenu = new JMenu("Manage Customers");
        billingMenu = new JMenu("Billing");

        // Add menus to menu bar
        menuBar.add(homeMenu);
        menuBar.add(customerMenu);
        menuBar.add(billingMenu);

        // Initialize panels
        homePanel = new JPanel();
        customerPanel = new JPanel();
        billingPanel = new JPanel();
        
        

        // Add components to home panel
        JLabel homeLabel = new JLabel("Welcome to KC Energy System!");
        homePanel.add(homeLabel);

        // Add components to customer panel
        JLabel customerLabel = new JLabel("Customer Management Panel");
        customerPanel.add(customerLabel);

        // Add components to billing panel
        JLabel billingLabel = new JLabel("Billing Panel");
        billingPanel.add(billingLabel);

        // Add action listeners to menu items
        JMenuItem homeItem = new JMenuItem("Home");
        homeItem.addActionListener(this);

        JMenuItem addCustomerItem = new JMenuItem("Add Customer");
        addCustomerItem.addActionListener(this);

        //JMenuItem displayDashboardItem = new JMenuItem("Display Dashboard");
        //displayDashboardItem.addActionListener(this);

        JMenuItem generateInvoiceItem = new JMenuItem("Generate Invoice");
        generateInvoiceItem.addActionListener(this);

        //JMenuItem recordPaymentItem = new JMenuItem("Record Payment");
       // recordPaymentItem.addActionListener(this);

        // Add menu items to menus
        homeMenu.add(homeItem);

        customerMenu.add(addCustomerItem);
        //customerMenu.add(displayDashboardItem);

        billingMenu.add(generateInvoiceItem);
        //billingMenu.add(recordPaymentItem);

        // Set default panel to home panel
        add(homePanel);

        // Set frame properties
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setJMenuBar(menuBar);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        // Switch panels based on menu item selection
        if (e.getActionCommand().equals("Home")) {
            //setContentPane(homePanel);
        } else if (e.getActionCommand().equals("Add Customer")) {
            //setContentPane(customerPanel);
        	//currentWindow.setVisible(false);
        	CustomerInfo CustomerInfo = new CustomerInfo();
        } else if (e.getActionCommand().equals("Display Dashboard")) {
            //setContentPane(customerPanel);  
        } else if (e.getActionCommand().equals("Generate Invoice")) {
            //setContentPane(billingPanel); 
        	Invoice Invoice = new Invoice();
        } else if (e.getActionCommand().equals("Record Payment")) {
            //setContentPane(billingPanel);  
        }

       
    }

    public static void main(String[] args) {
        new GUI();
    }
}
