package client.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import api.JEDIGame;
import api.JEDI_api;
import event.ButtonEvent;
import event.ConnectionEvent;

public class Spacecraft extends JEDIGame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Timer timer;
	private Craft craft;
	private ArrayList<Alien> aliens;
	private boolean ingame;
	private int B_WIDTH;
	private int B_HEIGHT;

	public static boolean pause = true;
	private static boolean calibration = false;

	private int[][] pos = { 
			//        {2380, 29}, {2500, 59}, {1380, 89},
			//        {780, 109}, {580, 139}, {680, 239}, 
			//        {790, 259}, {760, 50}, {790, 150},
			//        {980, 209}, {560, 45}, {510, 70},
			//        {930, 159}, {590, 80}, {530, 60},
			//        {940, 59}, {990, 30}, {920, 200},
			//        {900, 259}, {660, 50}, {540, 90},
			//        {810, 220}, {860, 20}, {740, 180},
			{820, 128}, {490, 170}, {700, 30}
	};

	private JEDI_api api;

	public Spacecraft(JEDI_api jedi) {
		super(jedi);

		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);
		ingame = true;

		setSize(400, 300);

		craft = new Craft(jedi);
		this.api = jedi; 

		initAliens();
	}

	@Override
	protected void start() {
		craft.start();

		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}

	public void addNotify() {
		super.addNotify();
		B_WIDTH = getWidth();
		B_HEIGHT = getHeight();   
	}

	public void initAliens() {
		aliens = new ArrayList<Alien>();

		for (int i=0; i<pos.length; i++ ) {
			aliens.add(new Alien(pos[i][0], pos[i][1]));
		}
	}


	public void paint(Graphics g) {
		super.paint(g);

		if (ingame) {

			if(isPause()){
				String msg = "PAUSE";
				Font small = new Font("Helvetica", Font.BOLD, 18);
				FontMetrics metr = this.getFontMetrics(small);
				g.setColor(Color.WHITE);
				g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2,
						B_HEIGHT / 2);
			}

			if(isCalibration()){
				String msg1 = "CALIBRATING";
				String msg2 = "Please, leave the JEDI still for a few seconds...";
				Font small = new Font("Helvetica", Font.BOLD, 12);
				FontMetrics metr = this.getFontMetrics(small);
				g.setColor(Color.WHITE);
				g.drawString(msg1, (B_WIDTH - metr.stringWidth(msg1)) / 2,
						B_HEIGHT / 2 - (metr.getHeight() / 2));
				g.drawString(msg2, (B_WIDTH - metr.stringWidth(msg2)) / 2,
						B_HEIGHT / 2 + (metr.getHeight() / 2));
			}

			Graphics2D g2d = (Graphics2D)g;

			if (craft.isVisible())
				g2d.drawImage(craft.getImage(), craft.getX(), craft.getY(),
						this);

			@SuppressWarnings("rawtypes")
			ArrayList ms = craft.getMissiles();

			for (int i = 0; i < ms.size(); i++) {
				Missile m = (Missile)ms.get(i);
				g2d.drawImage(m.getImage(), m.getX(), m.getY(), this);
			}

			for (int i = 0; i < aliens.size(); i++) {
				Alien a = aliens.get(i);
				if (a.isVisible())
					g2d.drawImage(a.getImage(), a.getX(), a.getY(), this);
			}

			g2d.setColor(Color.WHITE);
			g2d.drawString("Aliens left: " + aliens.size(), 5, 15);


		} else {
			String msg = "Game Over";
			Font small = new Font("Helvetica", Font.BOLD, 14);
			FontMetrics metr = this.getFontMetrics(small);

			g.setColor(Color.white);
			g.setFont(small);
			g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2,
					B_HEIGHT / 2);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}



	public class ScheduleTask extends TimerTask{

		private int count=0;
		@Override
		public void run() {
			count++;
			if(!isPause() && !isCalibration()){
				if (aliens.size()==0) {
					endGame();
				}

				@SuppressWarnings("rawtypes")
				ArrayList ms = craft.getMissiles();

				for (int i = 0; i < ms.size(); i++) {
					Missile m = (Missile) ms.get(i);
					if (m.isVisible()) 
						m.move();
					else ms.remove(i);
				}

				if(count%7==0)
				{
					for (int i = 0; i < aliens.size(); i++) {
						Alien a = aliens.get(i);
						if (a.isVisible()) 
							a.move();
						else aliens.remove(i);
					}	
				}

				craft.move();
				checkCollisions();
			}

			repaint();

		}

	}

	private void endGame() {
		craft.cancelTimer();
		timer.cancel();
		getCallback().onFinish();
	}

	public void checkCollisions() {

		Rectangle r3 = craft.getBounds();

		for (int j = 0; j<aliens.size(); j++) {
			Alien a = aliens.get(j);
			Rectangle r2 = a.getBounds();

			if (r3.intersects(r2)) {
				craft.setVisible(false);
				a.setVisible(false);
				endGame();
			}
		}

		@SuppressWarnings("rawtypes")
		ArrayList ms = craft.getMissiles();

		for (int i = 0; i < ms.size(); i++) {
			Missile m = (Missile) ms.get(i);

			Rectangle r1 = m.getBounds();

			for (int j = 0; j<aliens.size(); j++) {
				Alien a = aliens.get(j);
				Rectangle r2 = a.getBounds();

				if (r1.intersects(r2)) {
					m.setVisible(false);
					a.setVisible(false);
				}
			}
		}
	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		//		System.out.println("ingamepressed");
		craft.keyPressed(e);
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		//		System.out.println("ingamereleased");
		craft.keyReleased(e);
		if(e.getPressedButton() == ButtonEvent.JEDI_B){
			api.calibrate(3000, this);
			//			System.out.println(api.getVelocity());
		}
	}

	@Override
	public void connectionStarted(ConnectionEvent event) {
		Spacecraft.setPause(false);
		System.out.println("JEDI connected");
	}

	@Override
	public void connectionEnded(ConnectionEvent event) {
		Spacecraft.setPause(true);
		System.out.println("JEDI disconnected");
	}

	@Override
	public void onStart() {
		setCalibration(true);
	}

	@Override
	public void onEnding() {
		setCalibration(false);
	}

	public static void setPause(boolean pause) {
		Spacecraft.pause = pause;
	}

	public static boolean isPause() {
		return pause;
	}

	public static void setCalibration(boolean calibrating) {
		Spacecraft.calibration = calibrating;
	}

	public static boolean isCalibration() {
		return calibration;
	}
}
