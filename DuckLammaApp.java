import javax.swing.*;
import java.awt.event.*;

public class DuckLammaApp extends javax.swing.JFrame {
	
	public DuckLammaApp(){
		super("Duck or Lamma?");
		super.setDefaultCloseOperation(EXIT_ON_CLOSE);
		super.setSize(400,400);

		DuckLammaPanel panel = new DuckLammaPanel();
		this.add(panel);
		this.setResizable(false);	
		this.setVisible(true);
		this.pack();
	}

	public static void main(String[] args){
		new DuckLammaApp();
	}
}