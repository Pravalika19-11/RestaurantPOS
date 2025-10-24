package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminPanel extends JFrame {

    Connection conn;

    JPanel loginPanel, dashboardPanel, sidebarPanel, contentPanel;
    JTextField txtUsername;
    JPasswordField txtPassword;
    JButton btnLogin;
    JButton btnEmployees, btnMenu, btnSales, btnLogout;
    Image bgImage;
    JPanel employeePanel, menuPanel, salesPanel;

    public AdminPanel() {
        setTitle("Admin Panel");
        setSize(1550, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        connectDB();
        initLoginPanel();
        initDashboardPanel();

        add(loginPanel);
        add(dashboardPanel);

        loginPanel.setVisible(true);
        dashboardPanel.setVisible(false);

        setVisible(true);
    }

    void connectDB() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@localhost:1521:XE", "system", "root"
            );
            System.out.println("Oracle Database Connected!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "DB Connection Failed");
            System.exit(0);
        }
    }

    void initLoginPanel() {
        loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImage != null) {
                    g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        loginPanel.setBounds(0, 0, 1550, 850);
        loginPanel.setLayout(null);

        try {
            bgImage = new ImageIcon("C:\\Users\\Lenovo\\Downloads\\food.png").getImage();
        } catch (Exception e) {
            System.out.println("Background image not found");
        }

        JPanel loginBox = new JPanel();
        loginBox.setLayout(null);
        loginBox.setBounds(1550 / 2 - 200, 850 / 2 - 150, 400, 300);
        loginBox.setBackground(new Color(255, 255, 255, 180));
        loginPanel.add(loginBox);

        JLabel lblTitle = new JLabel("Admin Login", SwingConstants.CENTER);
        lblTitle.setBounds(0, 20, 400, 60);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(Color.BLACK);
        loginBox.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 100, 100, 30);
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBox.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 100, 190, 30);
        loginBox.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 150, 100, 30);
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBox.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 150, 190, 30);
        loginBox.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 210, 200, 35);
        btnLogin.setBackground(new Color(0, 102, 204));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBox.add(btnLogin);

        btnLogin.addActionListener(e -> login());
    }

    void login() {
        String sql = "SELECT * FROM ADMIN WHERE username=? AND password=?";
        try {
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, txtUsername.getText());
            pst.setString(2, new String(txtPassword.getPassword()));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                loginPanel.setVisible(false);
                dashboardPanel.setVisible(true);
                MenuData.loadMenu(); // Load menu into EmployeePage shared model
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void initDashboardPanel() {
        dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBounds(0, 0, 1550, 850);

        sidebarPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        sidebarPanel.setPreferredSize(new Dimension(250, 650));
        sidebarPanel.setBackground(new Color(0, 0, 128));

        JLabel lblMenu = new JLabel("Dashboard", SwingConstants.CENTER);
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setFont(new Font("SansSerif", Font.BOLD, 20));
        sidebarPanel.add(lblMenu);

        btnEmployees = new JButton("Manage Employees");
        btnMenu = new JButton("Manage Menu");
        btnSales = new JButton("Sales Report");
        btnLogout = new JButton("Logout");

        for (JButton b : new JButton[]{btnEmployees, btnMenu, btnSales, btnLogout}) {
            if (b != btnLogout) styleButton(b);
            else {
                b.setBackground(new Color(204, 0, 0));
                b.setForeground(Color.WHITE);
                b.setFont(new Font("SansSerif", Font.BOLD, 16));
                b.setFocusPainted(false);
            }
            sidebarPanel.add(b);
        }

        contentPanel = new JPanel(new CardLayout());

        createEmployeePanel();
        createMenuPanel();
        createSalesPanel();

        contentPanel.add(employeePanel, "Employees");
        contentPanel.add(menuPanel, "Menu");
        contentPanel.add(salesPanel, "Sales");

        dashboardPanel.add(sidebarPanel, BorderLayout.WEST);
        dashboardPanel.add(contentPanel, BorderLayout.CENTER);

        btnEmployees.addActionListener(e -> switchPanel("Employees"));
        btnMenu.addActionListener(e -> switchPanel("Menu"));
        btnSales.addActionListener(e -> switchPanel("Sales"));
        btnLogout.addActionListener(e -> {
            loginPanel.setVisible(true);
            dashboardPanel.setVisible(false);
        });
    }

    void switchPanel(String name) {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        cl.show(contentPanel, name);
    }

    void styleButton(JButton b) {
        b.setBackground(new Color(0, 102, 204));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setFocusPainted(false);
    }

    // ================== EMPLOYEE PANEL ==================
    void createEmployeePanel() {
        employeePanel = new JPanel(null);
        employeePanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Employees", SwingConstants.CENTER);
        lblTitle.setBounds(0, 10, 940, 60);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 102));
        employeePanel.add(lblTitle);

        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 70, 1250, 500);
        employeePanel.add(scroll);

        int buttonY = 600, buttonHeight = 40, buttonWidth = 180, buttonSpacing = 30, startX = 20;

        JButton btnLoad = new JButton("Load Employees");
        btnLoad.setBounds(startX, buttonY, buttonWidth, buttonHeight);
        styleButton(btnLoad);
        employeePanel.add(btnLoad);

        JButton btnAdd = new JButton("Add Employee");
        btnAdd.setBounds(startX + (buttonWidth + buttonSpacing), buttonY, buttonWidth, buttonHeight);
        styleButton(btnAdd);
        employeePanel.add(btnAdd);

        JButton btnEdit = new JButton("Edit Employee");
        btnEdit.setBounds(startX + 2*(buttonWidth + buttonSpacing), buttonY, buttonWidth, buttonHeight);
        styleButton(btnEdit);
        employeePanel.add(btnEdit);

        JButton btnDelete = new JButton("Delete Employee");
        btnDelete.setBounds(startX + 3*(buttonWidth + buttonSpacing), buttonY, buttonWidth, buttonHeight);
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDelete.setFocusPainted(false);
        employeePanel.add(btnDelete);

        btnLoad.addActionListener(e -> loadEmployees(table));
        btnAdd.addActionListener(e -> addEmployee(table));
        btnEdit.addActionListener(e -> editEmployee(table));
        btnDelete.addActionListener(e -> deleteEmployee(table));
    }

    void loadEmployees(JTable table){
        try {
            String sql = "SELECT * FROM EMPLOYEES ORDER BY ID";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"ID","Username","Password","DOB","Aadhaar","Address","Phone","Email","Gender"},0
            );
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("ID"), rs.getString("USERNAME"), rs.getString("PASSWORD"),
                        rs.getDate("DOB"), rs.getString("AADHAAR"), rs.getString("ADDRESS"),
                        rs.getString("PHONE"), rs.getString("EMAIL"), rs.getString("GENDER")
                });
            }
            table.setModel(model);
        } catch(Exception e){ e.printStackTrace(); }
    }

    void addEmployee(JTable table){
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JTextField dob = new JTextField();
        JTextField aadhaar = new JTextField();
        JTextField address = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"});

        Object[] fields = {"Username:", username,"Password:", password,"DOB(YYYY-MM-DD):",dob,
                "Aadhaar:",aadhaar,"Address:",address,"Phone:",phone,"Email:",email,"Gender:",gender};

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Employee", JOptionPane.OK_CANCEL_OPTION);
        if(option==JOptionPane.OK_OPTION){
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO EMPLOYEES(ID, USERNAME, PASSWORD, DOB, AADHAAR, ADDRESS, PHONE, EMAIL, GENDER) " +
                                "VALUES(EMP_SEQ.NEXTVAL, ?, ?, TO_DATE(?,'YYYY-MM-DD'), ?, ?, ?, ?, ?)"
                );
                ps.setString(1, username.getText());
                ps.setString(2, new String(password.getPassword()));
                ps.setString(3, dob.getText());
                ps.setString(4, aadhaar.getText());
                ps.setString(5, address.getText());
                ps.setString(6, phone.getText());
                ps.setString(7, email.getText());
                ps.setString(8, (String)gender.getSelectedItem());
                ps.executeUpdate();
                loadEmployees(table);
            } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error adding employee!"); }
        }
    }

    void editEmployee(JTable table){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Select an employee!"); return; }

        int id = (int)table.getValueAt(row,0);
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM EMPLOYEES WHERE ID=?");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                JTextField username = new JTextField(rs.getString("USERNAME"));
                JPasswordField password = new JPasswordField(rs.getString("PASSWORD"));
                JTextField dob = new JTextField(rs.getDate("DOB").toString());
                JTextField aadhaar = new JTextField(rs.getString("AADHAAR"));
                JTextField address = new JTextField(rs.getString("ADDRESS"));
                JTextField phone = new JTextField(rs.getString("PHONE"));
                JTextField email = new JTextField(rs.getString("EMAIL"));
                JComboBox<String> gender = new JComboBox<>(new String[]{"Male","Female","Other"});
                gender.setSelectedItem(rs.getString("GENDER"));

                Object[] fields = {"Username:", username,"Password:", password,"DOB:",dob,
                        "Aadhaar:",aadhaar,"Address:",address,"Phone:",phone,"Email:",email,"Gender:",gender};

                int option = JOptionPane.showConfirmDialog(this,fields,"Edit Employee",JOptionPane.OK_CANCEL_OPTION);
                if(option==JOptionPane.OK_OPTION){
                    PreparedStatement ps2 = conn.prepareStatement(
                            "UPDATE EMPLOYEES SET USERNAME=?, PASSWORD=?, DOB=TO_DATE(?,'YYYY-MM-DD'), AADHAAR=?, ADDRESS=?, PHONE=?, EMAIL=?, GENDER=? WHERE ID=?"
                    );
                    ps2.setString(1, username.getText());
                    ps2.setString(2, new String(password.getPassword()));
                    ps2.setString(3, dob.getText());
                    ps2.setString(4, aadhaar.getText());
                    ps2.setString(5, address.getText());
                    ps2.setString(6, phone.getText());
                    ps2.setString(7, email.getText());
                    ps2.setString(8, (String)gender.getSelectedItem());
                    ps2.setInt(9,id);
                    ps2.executeUpdate();
                    loadEmployees(table);
                }
            }
        } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error editing employee!"); }
    }

    void deleteEmployee(JTable table){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Select an employee!"); return; }

        int id = (int)table.getValueAt(row,0);
        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure to delete Employee ID "+id+"?");
        if(confirm == JOptionPane.YES_OPTION){
            try{
                PreparedStatement ps = conn.prepareStatement("DELETE FROM EMPLOYEES WHERE ID=?");
                ps.setInt(1,id);
                ps.executeUpdate();
                loadEmployees(table);
            } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error deleting employee!"); }
        }
    }

    // ================== MENU PANEL ==================
    void createMenuPanel() {
        menuPanel = new JPanel(null);
        menuPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Menu Items", SwingConstants.CENTER);
        lblTitle.setBounds(0, 10, 740, 40);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 102));
        menuPanel.add(lblTitle);

        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 70, 700, 400);
        menuPanel.add(scroll);

        JButton btnLoad = new JButton("Load Menu");
        btnLoad.setBounds(20, 480, 150, 35);
        styleButton(btnLoad);
        menuPanel.add(btnLoad);

        JButton btnAdd = new JButton("Add Menu");
        btnAdd.setBounds(200, 480, 150, 35);
        styleButton(btnAdd);
        menuPanel.add(btnAdd);

        JButton btnEdit = new JButton("Edit Menu");
        btnEdit.setBounds(380, 480, 150, 35);
        styleButton(btnEdit);
        menuPanel.add(btnEdit);

        JButton btnDelete = new JButton("Delete Menu");
        btnDelete.setBounds(560, 480, 160, 35);
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnDelete.setFocusPainted(false);
        menuPanel.add(btnDelete);

        btnLoad.addActionListener(e -> loadMenu(table));
        btnAdd.addActionListener(e -> addMenu(table));
        btnEdit.addActionListener(e -> editMenu(table));
        btnDelete.addActionListener(e -> deleteMenu(table));
    }

    void loadMenu(JTable table){
        try {
            String sql = "SELECT * FROM MENU ORDER BY ID";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(new String[]{"ID","Name","Price"},0);
            while(rs.next()){
                model.addRow(new Object[]{
                        rs.getInt("ID"), rs.getString("ITEM_NAME"), rs.getDouble("PRICE")
                });
            }
            table.setModel(model);
            MenuData.loadMenu(); // sync with EmployeePage
        } catch(Exception e){ e.printStackTrace(); }
    }

    void addMenu(JTable table){
        JTextField name = new JTextField();
        JTextField price = new JTextField();
        Object[] fields = {"Name:",name,"Price:",price};
        int option = JOptionPane.showConfirmDialog(this,fields,"Add Menu",JOptionPane.OK_CANCEL_OPTION);
        if(option==JOptionPane.OK_OPTION){
            try {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO MENU(ID,ITEM_NAME,PRICE) VALUES(MENU_SEQ.NEXTVAL,?,?)"
                );
                ps.setString(1,name.getText());
                ps.setDouble(2,Double.parseDouble(price.getText()));
                ps.executeUpdate();
                loadMenu(table);
            } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error adding menu!"); }
        }
    }

    void editMenu(JTable table){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Select a menu item!"); return; }

        int id = (int)table.getValueAt(row,0);
        try{
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM MENU WHERE ID=?");
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                JTextField name = new JTextField(rs.getString("ITEM_NAME"));
                JTextField price = new JTextField(String.valueOf(rs.getDouble("PRICE")));
                Object[] fields = {"Name:",name,"Price:",price};
                int option = JOptionPane.showConfirmDialog(this,fields,"Edit Menu",JOptionPane.OK_CANCEL_OPTION);
                if(option==JOptionPane.OK_OPTION){
                    PreparedStatement ps2 = conn.prepareStatement("UPDATE MENU SET ITEM_NAME=?, PRICE=? WHERE ID=?");
                    ps2.setString(1,name.getText());
                    ps2.setDouble(2,Double.parseDouble(price.getText()));
                    ps2.setInt(3,id);
                    ps2.executeUpdate();
                    loadMenu(table);
                }
            }
        } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error editing menu!"); }
    }

    void deleteMenu(JTable table){
        int row = table.getSelectedRow();
        if(row == -1){ JOptionPane.showMessageDialog(this,"Select a menu item!"); return; }

        int id = (int)table.getValueAt(row,0);
        int confirm = JOptionPane.showConfirmDialog(this,"Are you sure to delete menu ID "+id+"?");
        if(confirm == JOptionPane.YES_OPTION){
            try{
                PreparedStatement ps = conn.prepareStatement("DELETE FROM MENU WHERE ID=?");
                ps.setInt(1,id);
                ps.executeUpdate();
                loadMenu(table);
            } catch(Exception e){ e.printStackTrace(); JOptionPane.showMessageDialog(this,"Error deleting menu!"); }
        }
    }

 // ================== SALES PANEL ==================
 // ================== SALES PANEL ==================
    void createSalesPanel() {
        salesPanel = new JPanel(null);
        salesPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Sales Report", SwingConstants.CENTER);
        lblTitle.setBounds(0, 10, 940, 50);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 102));
        salesPanel.add(lblTitle);

        JTable table = new JTable();
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 70, 900, 500);
        salesPanel.add(scroll);

        JButton btnLoad = new JButton("Load Sales");
        btnLoad.setBounds(20, 600, 180, 40);
        styleButton(btnLoad);
        salesPanel.add(btnLoad);

        JButton btnReport = new JButton("Generate PDF Report");
        btnReport.setBounds(220, 600, 250, 40);
        styleButton(btnReport);
        salesPanel.add(btnReport);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(500, 600, 180, 40);
        styleButton(btnBack);
        salesPanel.add(btnBack);

        btnLoad.addActionListener(e -> loadSales(table));
        btnReport.addActionListener(e -> generateSalesReport(table));
        btnBack.addActionListener(e -> switchPanel("Menu")); // return to dashboard
    }

    void loadSales(JTable table){
        try {
            String sql = "SELECT bill_id, customer_name, bill_date, total, tax, grand_total FROM bills ORDER BY bill_date DESC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Bill ID", "Customer Name", "Bill Date", "Total", "Tax", "Grand Total"}, 0
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("bill_id"),
                    rs.getString("customer_name"),
                    rs.getDate("bill_date"),
                    rs.getDouble("total"),
                    rs.getDouble("tax"),
                    rs.getDouble("grand_total")
                });
            }

            table.setModel(model);
            rs.close();
            pst.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading sales: " + e.getMessage());
        }
    }

    void generateSalesReport(JTable table){
        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No sales data to generate report!");
            return;
        }

        try {
            String filePath = "C:\\Users\\Lenovo\\Documents\\SalesReport.pdf";
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
            com.itextpdf.text.pdf.PdfWriter.getInstance(doc, new java.io.FileOutputStream(filePath));
            doc.open();

            com.itextpdf.text.Font fontTitle = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 20, com.itextpdf.text.Font.BOLD);
            com.itextpdf.text.Font fontNormal = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.NORMAL);

            doc.add(new com.itextpdf.text.Paragraph("Flavor Cafe - Sales Report", fontTitle));
            doc.add(new com.itextpdf.text.Paragraph("Date: " + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), fontNormal));
            doc.add(new com.itextpdf.text.Paragraph("\n"));

            com.itextpdf.text.pdf.PdfPTable pdfTable = new com.itextpdf.text.pdf.PdfPTable(6);
            pdfTable.setWidthPercentage(100);
            pdfTable.addCell("Bill ID");
            pdfTable.addCell("Customer");
            pdfTable.addCell("Date");
            pdfTable.addCell("Total");
            pdfTable.addCell("Tax");
            pdfTable.addCell("Grand Total");

            double grandTotalSum = 0;
            int totalBills = 0;

            for (int i = 0; i < table.getRowCount(); i++) {
                pdfTable.addCell(table.getValueAt(i, 0).toString());
                pdfTable.addCell(table.getValueAt(i, 1).toString());
                pdfTable.addCell(table.getValueAt(i, 2).toString());
                pdfTable.addCell(table.getValueAt(i, 3).toString());
                pdfTable.addCell(table.getValueAt(i, 4).toString());
                pdfTable.addCell(table.getValueAt(i, 5).toString());

                grandTotalSum += Double.parseDouble(table.getValueAt(i, 5).toString());
                totalBills++;
            }

            doc.add(pdfTable);
            doc.add(new com.itextpdf.text.Paragraph("\nTotal Bills: " + totalBills, fontNormal));
            doc.add(new com.itextpdf.text.Paragraph("Overall Grand Total: Rs. " + grandTotalSum, fontNormal));
            doc.add(new com.itextpdf.text.Paragraph("\nThank you for reviewing sales!", fontNormal));
            doc.close();

            JOptionPane.showMessageDialog(this, "Sales report generated successfully!\nFile: " + filePath);

            if (java.awt.Desktop.isDesktopSupported())
                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new AdminPanel()); 
    }
    
    }