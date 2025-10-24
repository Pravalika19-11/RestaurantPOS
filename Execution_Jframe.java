package gui;

import javax.swing.JFrame;
public class Execution_Jframe {

	public static void main(String[] args) {
			
			//LoginPage lp = new LoginPage();
			//RegPage rp = new RegPage();
			Homepage hp=new Homepage();
			hp.setTitle(" HomePage");
			hp.setSize(800,500);
			hp.setVisible(true);
			hp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

	}