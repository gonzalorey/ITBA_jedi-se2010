package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import api.JEDI_api;
import api.JEDI_api.JoystickNumbers;
import api.JEDIGame;
import client.game.Spacecraft;
import event.ButtonEvent;
import event.ConnectionEvent;

public class Pong extends JEDIGame{

	private static final long serialVersionUID = 1L;

	private Timer timer;
	private int B_WIDTH;
	private int B_HEIGHT;

	private static boolean pause = true;
	private static boolean ingame = true;
	Ball ball;

	Paddle paddle_left, paddle_right;

	public static int PONG_WIDTH = 400;
	public static int PONG_HEIGHT = 300;

	private int score1 = 0;
	private int score2 = 0;

	private boolean gameRunning;
	private int winner = 0;
	
	private final int WINNING_POINTS = 2;
	
	public Pong(JEDI_api jedi1, JEDI_api jedi2) 	{
		super(jedi1, jedi2);

		setFocusable(true);
		setBackground(Color.BLACK);
		setDoubleBuffered(true);

		setSize(PONG_WIDTH, PONG_HEIGHT);

		ball = new Ball(PONG_WIDTH/2, PONG_HEIGHT/2, .5,.5);

		score1 = 0;
		score2 = 0;
		
		paddle_left = new Paddle(jedi1, 30);
		paddle_right= new Paddle(jedi2, PONG_WIDTH - 10 - 30);
	}

	@Override
	public void start(){
		paddle_left.run();
		paddle_right.run();

		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}

	public void addNotify() {
		super.addNotify();
		B_WIDTH = getWidth();
		B_HEIGHT = getHeight();   
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

			g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(), this);

			g2d.drawImage(paddle_left.getImage(), paddle_left.getX(), paddle_left.getY(), this);
			g2d.drawImage(paddle_right.getImage(), paddle_right.getX(), paddle_right.getY(), this);

			g2d.setColor(Color.WHITE);
			g2d.drawString(score1+":"+score2, Pong.PONG_WIDTH/2-10, 20);
		}

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private boolean isGameRunning() {
		return gameRunning;
	}

	private boolean isCalibration() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class ScheduleTask extends TimerTask{
		public void run() {
			if(victoryConditionsReached()){
				winner = determineWinner();
				gameRunning= false;
				cancelTimer();
				getCallback().onFinish();
			}
			
			if(!isPause()){
				ball.move();
				paddle_left.move();
				paddle_right.move();
				checkCollisions();

			}
			repaint();
		}
	}

	private void cancelTimer() {
		paddle_left.cancelTimer();
		paddle_right.cancelTimer();
		timer.cancel();
	}
	
	public int determineWinner() {
		if(score1 == WINNING_POINTS)
			return 1;
		else
			return 2;
	}

	public boolean victoryConditionsReached() {
		if(score1== WINNING_POINTS || score2 == WINNING_POINTS)
			return true;
		
		return false;
	}

	public void checkCollisions() {

		Rectangle b = ball.getBounds();
		Rectangle p1 = paddle_left.getBounds();
		Rectangle p2 = paddle_right.getBounds();

		if(b.intersects(p1) || b.intersects(p2))
		{
			ball.inverse_horizontal_speed();
			ball.increaseVelocity();
		}

		if(ball.getX()<=0){
			ball.restorePosition();
			score2++;
		}	

		if(ball.getX()>=PONG_WIDTH-10){
			score1++;
			ball.restorePosition();
		}	
	}

	@Override
	public void buttonPressed(ButtonEvent e) {
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
	}

	private boolean jediOneConnected = false;
	private boolean jediTwoConnected = false;

	@Override
	public void connectionStarted(ConnectionEvent event) {
		if(((JEDI_api) event.getSource()).getJediNumber()==JoystickNumbers.JEDI_ONE)
			jediOneConnected = true;
		else
			jediTwoConnected = true;

		if(jediOneConnected && jediTwoConnected)
			pause = false;
	}

	@Override
	public void connectionEnded(ConnectionEvent event) {
		if(((JEDI_api) event.getSource()).getJediNumber()==JoystickNumbers.JEDI_ONE)
			jediOneConnected = false;
		else
			jediTwoConnected = false;
		pause = true;
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onEnding() {
	}

	public static void setPause(boolean pause) {
		Spacecraft.pause = pause;
	}

	public static boolean isPause() {
		return pause;
	}
}
