package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfWriter;

public class EmployeePage extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;

    public EmployeePage() {
        setTitle("Employee Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Background Image
        ImageIcon bgIcon = new ImageIcon("C:\\Users\\Lenovo\\Downloads\\employee.png");
        java.awt.Image bgImage = bgIcon.getImage().getScaledInstance(
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height,
                java.awt.Image.SCALE_SMOOTH
        );
        JLabel background = new JLabel(new ImageIcon(bgImage));
        background.setBounds(0, 0,
                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
        add(background);

        // Login Panel
        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(255, 255, 255, 230));
        panel.setSize(400, 250);
        int centerX = (Toolkit.getDefaultToolkit().getScreenSize().width - panel.getWidth()) / 2;
        int centerY = (Toolkit.getDefaultToolkit().getScreenSize().height - panel.getHeight()) / 2;
        panel.setLocation(centerX, centerY);
        background.add(panel);

        JLabel lblTitle = new JLabel("Employee Login", SwingConstants.CENTER);
        lblTitle.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 24));
        lblTitle.setBounds(0, 10, 400, 40);
        panel.add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 70, 100, 30);
        panel.add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(160, 70, 180, 30);
        panel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 120, 100, 30);
        panel.add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(160, 120, 180, 30);
        panel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 180, 200, 35);
        btnLogin.setBackground(new Color(0, 153, 76));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        panel.add(btnLogin);

        btnLogin.addActionListener(e -> login());

        // ====== BACK BUTTON AT BOTTOM (slightly above taskbar) ======
        JButton btnBack = new JButton("Back");
        btnBack.setBounds(
                (Toolkit.getDefaultToolkit().getScreenSize().width - 200) / 2, // center horizontally
                Toolkit.getDefaultToolkit().getScreenSize().height - 120, // move up 120 px from bottom
                200, 40
        );
        btnBack.setBackground(new Color(153, 0, 0));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 16));
        background.add(btnBack);

        btnBack.addActionListener(e -> {
            dispose(); // close Employee login
            new AdminPanel(); // go back to admin panel
        });

        setVisible(true);
    }

    private void login() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.equals("employee") && password.equals("1234")) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            dispose();
            new OrderFrame().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeePage::new);
    }
}

// ====================== ORDER FRAME ======================
class OrderFrame extends JFrame {

    private JTable menuTable, orderTable;
    private JTextField txtSearch, txtTotal, txtTax, txtGrandTotal;
    private DefaultTableModel menuModel, orderModel;

    public OrderFrame() {
        setTitle("Employee Dashboard - Order System");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // LEFT PANEL - MENU
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(700, getHeight()));
        leftPanel.setBackground(Color.WHITE);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = new JLabel("Search Product:");
        txtSearch = new JTextField(20);
        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Menu Table - use shared model from MenuData
        menuModel = MenuData.menuModel;  // <-- Your 16 items
        menuTable = new JTable(menuModel);
        JScrollPane menuScroll = new JScrollPane(menuTable);
        leftPanel.add(menuScroll, BorderLayout.CENTER);

        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String search = txtSearch.getText().toLowerCase();
                filterMenu(search);
            }
        });

        // RIGHT PANEL - ORDER
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(245, 245, 245));

        orderModel = new DefaultTableModel(new Object[]{"Item", "Qty", "Price", "Subtotal"}, 0);
        orderTable = new JTable(orderModel);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        rightPanel.add(orderScroll, BorderLayout.CENTER);

        // BUTTON PANEL
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Add Item");
        JButton btnClear = new JButton("Clear Order");
        JButton btnBill = new JButton("Generate Bill (PDF & DB)");
        JButton btnLogout = new JButton("Logout");

        btnAdd.setBackground(new Color(0, 153, 76));
        btnAdd.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(255, 153, 0));
        btnClear.setForeground(Color.WHITE);
        btnBill.setBackground(new Color(0, 102, 204));
        btnBill.setForeground(Color.WHITE);
        btnLogout.setBackground(new Color(102, 0, 204));
        btnLogout.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnBill);
        buttonPanel.add(btnLogout);
        rightPanel.add(buttonPanel, BorderLayout.NORTH);

        // TOTALS PANEL
        JPanel totalsPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        totalsPanel.setBorder(BorderFactory.createTitledBorder("Totals"));
        totalsPanel.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        totalsPanel.add(txtTotal);
        totalsPanel.add(new JLabel("Tax (5%):"));
        txtTax = new JTextField();
        totalsPanel.add(txtTax);
        totalsPanel.add(new JLabel("Grand Total:"));
        txtGrandTotal = new JTextField();
        totalsPanel.add(txtGrandTotal);
        txtTotal.setEditable(false);
        txtTax.setEditable(false);
        txtGrandTotal.setEditable(false);
        rightPanel.add(totalsPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);

        // BUTTON ACTIONS
        btnAdd.addActionListener(e -> addItemToOrder());
        btnClear.addActionListener(e -> clearOrder());
        btnBill.addActionListener(e -> generatePDFAndStoreDB());
        btnLogout.addActionListener(e -> logout());

        setVisible(true);
    }

    private void filterMenu(String search) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(menuModel);
        menuTable.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search));
    }

    private void addItemToOrder() {
        int selected = menuTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from menu.");
            return;
        }

        String itemName = menuModel.getValueAt(selected, 0).toString();
        double price = Double.parseDouble(menuModel.getValueAt(selected, 2).toString());

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:", "1");
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity, using 1");
            qty = 1;
        }

        double subtotal = price * qty;
        orderModel.addRow(new Object[]{itemName, qty, price, subtotal});
        updateTotals();
    }

    private void clearOrder() {
        orderModel.setRowCount(0);
        updateTotals();
    }

    private void updateTotals() {
        double total = 0;
        for (int i = 0; i < orderModel.getRowCount(); i++)
            total += Double.parseDouble(orderModel.getValueAt(i, 3).toString());
        double tax = total * 0.05;
        double grand = total + tax;
        txtTotal.setText(String.format("%.2f", total));
        txtTax.setText(String.format("%.2f", tax));
        txtGrandTotal.setText(String.format("%.2f", grand));
    }

    private void generatePDFAndStoreDB() {
        if (orderModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No items to generate bill!");
            return;
        }

        Connection conn = null;
        PreparedStatement psBill = null;
        PreparedStatement psItem = null;

        try {
            String customerName = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            if (customerName == null || customerName.trim().isEmpty()) customerName = "Guest";

            String filePath = "C:\\Users\\Lenovo\\Documents\\OrderBill.pdf";
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            Font mono = new Font(Font.FontFamily.COURIER, 12, Font.NORMAL);
            Font monoBold = new Font(Font.FontFamily.COURIER, 12, Font.BOLD);

            Paragraph title = new Paragraph("Flavor Cafe", new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD));
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String billNo = "FV" + String.format("%03d", new Random().nextInt(999) + 1);
            Paragraph info = new Paragraph("Customer: " + customerName + "      Date: " + sdf.format(new Date()) + "      Bill No: " + billNo, mono);
            info.setAlignment(Element.ALIGN_CENTER);
            doc.add(info);

            doc.add(new Paragraph("------------------------------------------------------------------------", mono));
            Paragraph headers = new Paragraph(String.format("%-20s %5s %8s %10s", "Item Name", "Qty", "Price", "Amount"), monoBold);
            doc.add(headers);
            doc.add(new Paragraph("------------------------------------------------------------------------", mono));

            for (int i = 0; i < orderModel.getRowCount(); i++) {
                String name = orderModel.getValueAt(i, 0).toString();
                int qty = Integer.parseInt(orderModel.getValueAt(i, 1).toString());
                double price = Double.parseDouble(orderModel.getValueAt(i, 2).toString());
                double amount = Double.parseDouble(orderModel.getValueAt(i, 3).toString());

                String line = String.format("%-20s %5d %8.2f %10.2f", name, qty, price, amount);
                doc.add(new Paragraph(line, mono));
            }

            doc.add(new Paragraph("------------------------------------------------------------------------", mono));

            double total = Double.parseDouble(txtTotal.getText());
            double tax = Double.parseDouble(txtTax.getText());
            double grand = Double.parseDouble(txtGrandTotal.getText());

            Paragraph totals = new Paragraph(String.format("Total: Rs. %.2f\nGST (5%%): Rs. %.2f\nGrand Total: Rs. %.2f", total, tax, grand), monoBold);
            totals.setAlignment(Element.ALIGN_CENTER);
            doc.add(totals);

            doc.add(new Paragraph("------------------------------------------------------------------------", mono));
            Paragraph thanks = new Paragraph("Thank you for visiting! Visit again.", mono);
            thanks.setAlignment(Element.ALIGN_CENTER);
            doc.add(thanks);

            doc.close();
            if (java.awt.Desktop.isDesktopSupported())
                java.awt.Desktop.getDesktop().open(new java.io.File(filePath));

            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "SYSTEM", "root");
            conn.setAutoCommit(false);

            psBill = conn.prepareStatement(
                    "INSERT INTO bills (bill_id, customer_name, bill_date, total, tax, grand_total) VALUES (?, ?, ?, ?, ?, ?)"
            );
            psBill.setString(1, billNo);
            psBill.setString(2, customerName);
            psBill.setDate(3, new java.sql.Date(new Date().getTime()));
            psBill.setDouble(4, total);
            psBill.setDouble(5, tax);
            psBill.setDouble(6, grand);
            psBill.executeUpdate();

            psItem = conn.prepareStatement(
                    "INSERT INTO bill_items (bill_id, item_name, qty, price, amount) VALUES (?, ?, ?, ?, ?)"
            );
            for (int i = 0; i < orderModel.getRowCount(); i++) {
                psItem.setString(1, billNo);
                psItem.setString(2, orderModel.getValueAt(i, 0).toString());
                psItem.setInt(3, Integer.parseInt(orderModel.getValueAt(i, 1).toString()));
                psItem.setDouble(4, Double.parseDouble(orderModel.getValueAt(i, 2).toString()));
                psItem.setDouble(5, Double.parseDouble(orderModel.getValueAt(i, 3).toString()));
                psItem.executeUpdate();
            }

            conn.commit();
            clearOrder();

            JOptionPane.showMessageDialog(this, "Bill generated and stored in DB!\nSaved at: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            JOptionPane.showMessageDialog(this, "Error:\n" + sw.toString());
        } finally {
            try { if (psBill != null) psBill.close(); } catch (Exception ex) {}
            try { if (psItem != null) psItem.close(); } catch (Exception ex) {}
            try { if (conn != null) conn.close(); } catch (Exception ex) {}
        }
    }

    private void logout() {
        dispose();
        new EmployeePage().setVisible(true);
    }
}
