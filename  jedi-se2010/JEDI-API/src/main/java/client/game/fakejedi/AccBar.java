package client.game.fakejedi;

import javax.swing.JPanel;

public class AccBar extends JPanel {
	/**
	 * Default version ID
	 */
	private static final long serialVersionUID = -7536178304166306866L;
	private int x, y, width, height;
	
	public AccBar(int x, int y, int width, int height, java.awt.Color color){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.setBounds(x, y, width, height);
		this.setBackground(color);
	}
	
	public void addToPanel(JPanel panel){
		panel.add(this);
	}
	
	public void resizeHeight(int newHeight){
		this.height = newHeight;
		this.setBounds(x, y+(height>0?0:height), width, height>0?height:-height);
	}
}
