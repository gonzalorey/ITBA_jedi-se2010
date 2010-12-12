package client.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import pong.Pong;
import api.JEDIGame;
import api.JEDI_api;
import event.ButtonEvent;
import event.ConnectionEvent;

public class Menu extends JEDIGame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static int MENU_WIDTH = 400;
	public static int MENU_HEIGHT = 300;

	JEDIGame selectedGame;

	public Menu(JEDI_api jedi1, JEDI_api jedi2) {
		super(jedi1, jedi2);

		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		setSize(MENU_HEIGHT, MENU_WIDTH);
	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D)g;

		g2d.setColor(Color.WHITE);
		g2d.drawString("MENU", Menu.MENU_WIDTH/2-10, 20);


		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	@Override
	public void connectionStarted(ConnectionEvent event) {
		// TODO Auto-generated method stub
	}

	@Override
	public void connectionEnded(ConnectionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		selectedGame = new Pong(jedi1, jedi2);
		getCallback().onFinish();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEnding() {
		// TODO Auto-generated method stub

	}

	public JEDIGame getSelectedGame() {
		return selectedGame;
	}

	@Override
	protected void start() {
		System.out.println("menu run");
	}
}
