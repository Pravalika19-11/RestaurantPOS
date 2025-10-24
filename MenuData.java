package gui;

import javax.swing.table.DefaultTableModel;

public class MenuData {
    // Shared menu table model
    public static DefaultTableModel menuModel = new DefaultTableModel(
            new Object[]{"Item Name", "Category", "Price"}, 0
    );

    // Add default items
    static {
        String[][] items = {
                {"Fried Rice", "Chinese", "120"},
                {"Noodles", "Chinese", "100"},
                {"Arabian Burger", "Arabic", "200"},
                {"Shawarma", "Arabic", "180"},
                {"Paneer Butter Masala", "Indian Veg", "180"},
                {"Dal Makhani", "Indian Veg", "150"},
                {"Butter Chicken", "Indian Non-Veg", "220"},
                {"Tandoori Chicken", "Indian Non-Veg", "250"},
                {"Pasta Alfredo", "Italian", "220"},
                {"Margherita Pizza", "Italian", "300"},
                {"Grilled Chicken", "Sea Food", "250"},
                {"Fish Curry", "Sea Food", "280"},
                {"Veg Burger", "Fast Food", "120"},
                {"French Fries", "Fast Food", "80"},
                {"Coke", "Beverage", "40"},
                {"Coffee", "Beverage", "50"}
        };
        for (String[] item : items) menuModel.addRow(item);
    }

	public static void loadMenu() {
		
		
	}

	
	}

