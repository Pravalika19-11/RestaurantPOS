package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Homepage extends JFrame implements ActionListener {

    private JButton jbEmployee, jbAdmin;
    private JLabel lblTitle;
    private Image backgroundImage;

    
    private EmployeePage employeePage;
    private AdminPanel adminPanel;

    public Homepage() {
        setTitle("Homepage");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Load background image
        ImageIcon icon = new ImageIcon("C:\\Users\\Lenovo\\Downloads\\cafe.png");
        backgroundImage = icon.getImage();

        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        backgroundPanel.setLayout(null);

        // Title Label
        lblTitle = new JLabel("Welcome to Flavor Cafe");
        lblTitle.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 36));
        lblTitle.setForeground(Color.BLACK);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundPanel.add(lblTitle);

        // Buttons
        jbEmployee = new JButton("Employee");
        jbAdmin = new JButton("Admin");

        // Button styling
        Color buttonColor = new Color(255, 120, 60);
        jbEmployee.setBackground(buttonColor);
        jbAdmin.setBackground(buttonColor);
        jbEmployee.setForeground(Color.WHITE);
        jbAdmin.setForeground(Color.WHITE);
        jbEmployee.setFont(new Font("Tahoma", Font.BOLD, 24));
        jbAdmin.setFont(new Font("Tahoma", Font.BOLD, 24));
        jbEmployee.setFocusPainted(false);
        jbAdmin.setFocusPainted(false);
        jbEmployee.setBorderPainted(false);
        jbAdmin.setBorderPainted(false);

        backgroundPanel.add(jbEmployee);
        backgroundPanel.add(jbAdmin);

        // Center title and buttons dynamically
        backgroundPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                int panelWidth = backgroundPanel.getWidth();
                int panelHeight = backgroundPanel.getHeight();

                int titleHeight = 50;
                int buttonHeight = 60;
                int spacing = 20;

                int totalHeight = titleHeight + spacing + buttonHeight * 2 + 10;
                int startY = (panelHeight - totalHeight) / 2;

                lblTitle.setBounds(0, startY, panelWidth, titleHeight);
                jbEmployee.setBounds(panelWidth / 2 - 100, startY + titleHeight + spacing, 200, buttonHeight);
                jbAdmin.setBounds(panelWidth / 2 - 100, startY + titleHeight + spacing + buttonHeight + 10, 200, buttonHeight);
            }
        });

        add(backgroundPanel);

        // Action listeners
        jbEmployee.addActionListener(this);
        jbAdmin.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == jbEmployee) {
            if (employeePage == null || !employeePage.isDisplayable()) {
                employeePage = new EmployeePage();
                employeePage.setExtendedState(JFrame.MAXIMIZED_BOTH);
                employeePage.setVisible(true);
            } else {
                employeePage.toFront();
                employeePage.requestFocus();
            }
        } else if (ae.getSource() == jbAdmin) {
            if (adminPanel == null || !adminPanel.isDisplayable()) {
                adminPanel = new AdminPanel();
                adminPanel.setExtendedState(JFrame.MAXIMIZED_BOTH);
                adminPanel.setVisible(true);
            } else {
                adminPanel.toFront();
                adminPanel.requestFocus();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Homepage().setVisible(true));
    }
}
