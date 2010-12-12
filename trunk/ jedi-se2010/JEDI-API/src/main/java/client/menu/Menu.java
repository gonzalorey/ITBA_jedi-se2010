package client.menu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import pong.Pong;

import client.game.Spacecraft;
import data.Axis;

import api.JEDIGame;
import api.JEDI_api;
import api.impl.FakeJEDI;
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
	int selectedGameIndex;
	int gameCount = 2;

	private String arrow = "arrow.png";
	private Image image;
	private int arrowX;
	private int arrowY;

	private final int GAME_TITLE_X = 100;
	private final int GAME_TITLE_Y = 60;
	private final int GAME_TITLE_SEPARATION = 20;

	private final int GAME_ARROW_X = GAME_TITLE_X - 20;
	private final int GAME_ARROW_Y = GAME_TITLE_Y - 10;

	private Timer timer;

	public Menu(JEDI_api jedi1, JEDI_api jedi2) {
		super(jedi1, jedi2);

		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		setSize(MENU_HEIGHT, MENU_WIDTH);

		ImageIcon ii = new ImageIcon(this.getClass().getResource(arrow));
		image = ii.getImage();

		arrowX = GAME_ARROW_X;
		arrowY = GAME_ARROW_Y;

		selectedGameIndex = 0;
	}

	private class ScheduleTask extends TimerTask{
		public void run() {
			repaint();	

			Axis axis = jedi1.getAcceleration();

			if(axis == null)
				return;

			double shock = Math.abs(axis.getX())+Math.abs(axis.getY())+Math.abs(axis.getZ());
			
			if(jedi1 instanceof FakeJEDI && shock >= 30)
				shock = 100;

			if(shock>=100){
				switch(selectedGameIndex){
					case 0:
						selectedGame = new Spacecraft(jedi1);
						break;
					case 1:
						selectedGame = new Pong(jedi1, jedi2);
						break;
				}
				timer.cancel();
				getCallback().onFinish();
			}


		}

	}

	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D)g;

		g2d.setColor(Color.WHITE);
		g2d.drawString("MENU", Menu.MENU_WIDTH/2-10, 20);

		g2d.drawString("SpaceCraft", GAME_TITLE_X, GAME_TITLE_Y);
		g2d.drawString("Pong", GAME_TITLE_X, GAME_TITLE_Y + GAME_TITLE_SEPARATION);

		g2d.drawImage(getImage(), getArrowX(), getArrowY(), this);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	public int getArrowX() {
		return arrowX;
	}

	public int getArrowY() {
		return arrowY;
	}

	public Image getImage() {
		return image;
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

	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		if(e.getPressedButton()==ButtonEvent.JEDI_B)
			selectedGameIndex = (selectedGameIndex+1)%gameCount;
		else
			selectedGameIndex = (selectedGameIndex-1)<0?gameCount-1:selectedGameIndex-1;

		arrowY = GAME_ARROW_Y+GAME_TITLE_SEPARATION*selectedGameIndex;
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
		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}
}
