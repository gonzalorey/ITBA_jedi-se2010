package pong;

import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

public class Ball {
	public int x, y;
	public int width, height;
	public int speed_x, speed_y;
	
	boolean visible;
	
	private final int BOARD_WIDTH = 390;
	private final int BOARD_HEIGHT = 290;
	
    private String ball = "ball.png";
    private Image image;
	
	public Ball(int x, int y, int xp, int yp){
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
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void inverse_horizontal_speed(){
    	speed_x = -speed_x;
    }
    
    public void inverse_vertial_speed(){
    	speed_y = -speed_y;
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
    
    public void move(){
    	x += speed_x;
    	y += speed_y;
    	
    	/* bouncing */
    	if(y<0){
    		y = -y;
    		inverse_vertial_speed();
    	}	
    	
    	if(y>BOARD_HEIGHT-height){
    		y = BOARD_HEIGHT-height-Math.abs(y-(BOARD_HEIGHT-height));
    		inverse_vertial_speed();
    	}
    	
        if (x > BOARD_WIDTH || x < 0)
            visible = false;
    }

	public Image getImage() {
		return image;
	}
}
