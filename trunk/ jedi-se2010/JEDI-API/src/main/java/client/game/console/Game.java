package client.game.console;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import api.ButtonListenerInterface;

import event.ButtonEvent;


public class Game extends JPanel implements ButtonListenerInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Image image;
	Timer timer;
	int x, y;

	
	public Game(){
		setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        ImageIcon ii = new ImageIcon(this.getClass().getResource("star.png"));
        image = ii.getImage();

        
        setSize(400, 300);
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 100, 10);

	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		System.out.println("ingamepressed");
		if(e.getPressedButton() == ButtonEvent.JEDI_A)
		{
			x += 10;
		}
		if(e.getPressedButton() == ButtonEvent.JEDI_B)
		{
			y += 10;
		}
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		System.out.println("ingamereleased");
		
	}
	
	public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(image, x, y, this);
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }

	
	class ScheduleTask extends TimerTask {

        public void run() {
            x += 1;
            y += 1;

            if (y > 240) {
                y = -45;
                x = -45;
            }
            repaint();
        }
    }

}
