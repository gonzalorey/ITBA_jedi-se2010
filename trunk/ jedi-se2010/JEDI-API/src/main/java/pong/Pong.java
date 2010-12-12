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

import javax.swing.JPanel;

import api.ButtonListenerInterface;
import api.CalibrationInterface;
import api.ConnectionListenerInterface;
import api.JEDI_api;
import api.JEDI_api.JoystickNumbers;
import client.game.Board;
import event.ButtonEvent;
import event.ConnectionEvent;

public class Pong extends JPanel implements ButtonListenerInterface, ConnectionListenerInterface, CalibrationInterface {

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
    
    public static int score1 = 0;
    public static int score2 = 0;
    
    public Pong(JEDI_api api, JEDI_api api2) {
    	
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        setSize(PONG_WIDTH, PONG_HEIGHT);

        ball = new Ball(PONG_WIDTH/2, PONG_HEIGHT/2, .5,.5);
        
        paddle_left = new Paddle(api, 30);
        paddle_right= new Paddle(api2, PONG_WIDTH - 10 - 30);
                
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

    private boolean isCalibration() {
		// TODO Auto-generated method stub
		return false;
	}

	public class ScheduleTask extends TimerTask{
		public void run() {
				if(!isPause()){
					ball.move();
			        paddle_left.move();
			        paddle_right.move();
			        checkCollisions();
			        
				}
		        repaint();
			}
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
		Board.pause = pause;
	}

	public static boolean isPause() {
		return pause;
	}
}
