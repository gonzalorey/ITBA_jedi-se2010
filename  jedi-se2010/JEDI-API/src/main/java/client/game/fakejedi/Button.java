package client.game.fakejedi;

import javax.swing.JPanel;

public class Button extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, width, height;
	java.awt.Color colorOn, colorOff;
	String label;
	
	public Button(int x, int y, int width, int height, String label, java.awt.Color colorOn, java.awt.Color colorOff)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.label = label;
		this.colorOn = colorOn;
		this.colorOff = colorOff;
		this.setBounds(x, y, width, height);
		this.setBackground(colorOff);
	}
	
	public void setPressed(){
		this.setBackground(colorOn);
	}
	
	public void setReleased(){
		this.setBackground(colorOff);
	}
}
