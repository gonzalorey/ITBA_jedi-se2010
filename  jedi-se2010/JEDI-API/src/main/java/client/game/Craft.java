package client.game;

import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import api.JEDI_api;
import data.Axis;
import event.ButtonEvent;

public class Craft {

    private String craft = "craft.png";

    private int dx;
    private int dy;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean visible;
    private Image image;
    private ArrayList<Missile> missiles;

    private JEDI_api api;
    private Timer timer;
    
    public Craft(JEDI_api api) {
    	
    	this.api = api;
    	
        ImageIcon ii = new ImageIcon(this.getClass().getResource(craft));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
        missiles = new ArrayList<Missile>();
        visible = true;
        x = 40;
        y = 60;
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
    }

    private class ScheduleTask extends TimerTask{

		@Override
		public void run() {
			if(!Board.isPause() && !Board.isCalibration()){
				Axis axis = api.getAcceleration();
//				Axis axis2 = api.getPosition();
//				Axis axis2 = api.getVelocity();
//				if(axis2 != null){
//					System.out.println(axis2.getX());
//				}
	//			System.out.println(axis);
				if(axis == null)
					return;
				dx = (int) (axis.getX()/2);
				dy = (int) (axis.getZ()/2);
			}
		}
    	
    }

    public void move() {

        x += dx;
        y += dy;

        if (x < 1) {
            x = 1;
        }

        if (y < 1) {
            y = 1;
        }
        
        if (y > (300-40))
        	y = 300-40;
        
        if (x > (400-40))
        	x = 400-40;
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

    public ArrayList<Missile> getMissiles() {
        return missiles;
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

    public void keyPressed(ButtonEvent e) {

        int key = e.getPressedButton();

        if (key == ButtonEvent.JEDI_A) {
            fire();
        } else {
//        	api.getVelocity();
        }
    }

    public void fire() {
        missiles.add(new Missile(x + width, y + height/2));
    }

    public void keyReleased(ButtonEvent e) {
    	if(e.getPressedButton() == ButtonEvent.JEDI_A){
    		//DO SOMETHING
    	} else {
    		//DO SOMETHING
    	}
    }
}
