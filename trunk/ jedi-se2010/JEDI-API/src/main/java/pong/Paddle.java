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

	private JEDI_api api;

	private Image image;
	private Timer timer;

	private String paddle = "paddle_classic.png";

	public Paddle(JEDI_api api, int x){
		this.api = api;

		this.x = x;

		ImageIcon ii = new ImageIcon(this.getClass().getResource(paddle));

		image = ii.getImage();
		width = image.getWidth(null);
		height = image.getHeight(null);

		visible = true;

		y = Pong.PONG_HEIGHT/2 - height/2;
	}

	private class ScheduleTask extends TimerTask{

		@Override
		public void run() {
			if(!Pong.isPause() && !Board.isCalibration()){
				Axis axis;

				if(api == null || (axis = api.getAcceleration()) == null)
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

		if (y >= (Pong.PONG_HEIGHT-height))
			y = Pong.PONG_HEIGHT-height;
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

	public void run() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}

	public void cancelTimer() {
		timer.cancel();
	}    
}


