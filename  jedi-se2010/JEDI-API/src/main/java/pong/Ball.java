package pong;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Ball {
	public double x, y;
	public int width, height;
	public double speed_x, speed_y;
	
	boolean visible;
		
    private String ball = "ball_classic.png";
    private Image image;
	
	public Ball(double x, double y, double xp, double yp){
		this.x = x;
		this.y = y;
		this.speed_x = xp;
		this.speed_y = yp;
		visible = true;
		
        ImageIcon ii = new ImageIcon(this.getClass().getResource(ball));
        image = ii.getImage();
        width = image.getWidth(null);
        height = image.getHeight(null);
	}
	
    public int getX() {
        return (int)x;
    }

    public int getY() {
        return (int)y;
    }
    
    public void inverse_horizontal_speed(){
    	speed_x = -speed_x;
    }
    
    public void inverse_vertial_speed(){
    	speed_y = -speed_y;
    }
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void move(){
    	x += speed_x;
    	y += speed_y;
    	
    	/* bouncing */
    	if(y<0){
    		y = -y;
    		inverse_vertial_speed();
    	}	
    	
    	if(y>Pong.PONG_HEIGHT-height){
    		y = Pong.PONG_HEIGHT-height-Math.abs(y-(Pong.PONG_HEIGHT-height));
    		inverse_vertial_speed();
    	}
    	
        if (x > Pong.PONG_WIDTH || x < 0)
            visible = false;
    }

	public Image getImage() {
		return image;
	}

	public void increaseVelocity() {
		if(speed_x>0)
			speed_x+=.1;
		else
			speed_x-=.1;
	}

	public void restorePosition() {
		this.x = Pong.PONG_WIDTH/2;
		this.y = Pong.PONG_HEIGHT/2;
		
		this.speed_x = .5;
		this.speed_y = .5;
	}
	
	
}
