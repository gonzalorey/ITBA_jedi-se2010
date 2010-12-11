package pong;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import api.JEDI_api;
import client.game.Board;
import data.Axis;

public class Paddle {
	public int x, y;
	public int dy;
	
    private int width;
    private int height;
	
	private boolean visible;
	
	@SuppressWarnings("unused")
	private final int BOARD_WIDTH = 390;
	private final int BOARD_HEIGHT = 290;
	
    private JEDI_api api;
	
    private Image image;
    private Timer timer;
    
    private String paddle = "paddle.png";
    
	public Paddle(JEDI_api api, int x){
		this.api = api;
		
		this.x = x;
		
		ImageIcon ii = new ImageIcon(this.getClass().getResource(paddle));
		
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        
        visible = true;
        
        y = BOARD_HEIGHT/2 - height/2;
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}
	
	  private class ScheduleTask extends TimerTask{

			@Override
			public void run() {
				if(!Pong.isPause() && !Board.isCalibration()){
					Axis axis = api.getAcceleration();
					if(axis == null)
						return;
					dy = (int) (axis.getZ()/2);
				}
			}
	    	
	    }
	
	public void move() {
        y += dy;

        if (y < 0) {
            y = 0;
        }
        
        if (y > (BOARD_HEIGHT-height))
        	y = BOARD_HEIGHT-height;
    }
	
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    
}


