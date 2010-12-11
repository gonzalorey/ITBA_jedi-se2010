package client.game.fakejedi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.JEDI_api;
import api.impl.FakeJEDI;
import data.Axis;
import event.ButtonEvent;
import event.ConnectionEvent;

public class Dashboard extends JPanel implements ActionListener, ButtonListenerInterface, ConnectionListenerInterface {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	JEDI_api api1 = null;
	JEDI_api api2 = null;
	
	public void setApi(JEDI_api api) {
		this.api1 = api;
	}

	
	@SuppressWarnings("unused")
	private boolean fakeEvents = false;
	
	private JTextArea eventTextArea;
	
	private Timer timer;
	
	private int panel_width = 400;
	private int panel_height = 300;
	
	private AccBar acc_bar_x;
	private AccBar acc_bar_y;
	private AccBar acc_bar_z;
		
	private int acc_bars_width = 20;
	private int acc_bars_separation = 10;
	
	private int acc_bars_y_coordinate = 75;
	private int acc_bar_x_coordinate_x = panel_width - acc_bars_width*3 - acc_bars_separation*5;
	private int acc_bar_y_coordinate_x = acc_bar_x_coordinate_x + acc_bars_width + acc_bars_separation;
	private int acc_bar_z_coordinate_x = acc_bar_y_coordinate_x + acc_bars_width + acc_bars_separation;
	private java.awt.Color acc_bar_color = Color.WHITE;
	
	private JPanel accZeroLine;
	
	private int acc_zero_line_thickness = 2;
	private int acc_zero_line_x = acc_bar_x_coordinate_x - acc_bars_separation;
	private int acc_zero_line_y = acc_bars_y_coordinate - acc_zero_line_thickness / 2;
	private int acc_zero_line_width = acc_bar_z_coordinate_x + acc_bars_width + acc_bars_separation - (acc_bar_x_coordinate_x - acc_bars_separation);
	private int acc_zero_line_height = acc_zero_line_thickness;
	
	private JLabel label_x;
	private JLabel label_y;
	private JLabel label_z;
	private int labels_coordinate_y = acc_bars_y_coordinate + 10;
	private int label_x_coordinate_x = acc_bar_x_coordinate_x+4;
	private int label_y_coordinate_x = acc_bar_y_coordinate_x+4;
	private int label_z_coordinate_x = acc_bar_z_coordinate_x+4;
	private java.awt.Color label_color = acc_bar_color;
	
	private Button buttonA;
	private Button buttonB;
	private int buttons_width = 30;
	private int buttons_height = 30;
	private int buttons_coordinate_y = 100;
	private int buttons_delta = 10;
	private int button_A_coordinate_x = 100;
	private int button_B_coordinate_x = button_A_coordinate_x + buttons_width + buttons_delta;
	private java.awt.Color button_color_on = Color.RED;
	private java.awt.Color button_color_off= Color.WHITE;
	
	public Dashboard(JEDI_api api1, JEDI_api api2){
		this.api1 = api1;
		if(api1 instanceof FakeJEDI)
			addKeyListener(((FakeJEDI) api1).getAdapter());
		
		this.api2 = api2;
		if(api2 instanceof FakeJEDI)
			addKeyListener(((FakeJEDI) api2).getAdapter());
		
		setLayout(null);
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        setSize(panel_width, panel_height);
        
        accZeroLine = new JPanel();
        accZeroLine.setBounds(acc_zero_line_x, acc_zero_line_y, acc_zero_line_width, acc_zero_line_height);
        accZeroLine.setBackground(acc_bar_color);
        add(accZeroLine);
        
        acc_bar_x = new AccBar(acc_bar_x_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_y = new AccBar(acc_bar_y_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_z = new AccBar(acc_bar_z_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_x.addToPanel(this);
        acc_bar_y.addToPanel(this);
        acc_bar_z.addToPanel(this);
        
        label_x = new JLabel("x\n");
        label_y = new JLabel("y\n");
        label_z = new JLabel("z\n");
        label_x.setBounds(label_x_coordinate_x, labels_coordinate_y, acc_bar_x_coordinate_x, 15);
        label_y.setBounds(label_y_coordinate_x, labels_coordinate_y, acc_bar_y_coordinate_x, 15);
        label_z.setBounds(label_z_coordinate_x, labels_coordinate_y, acc_bar_z_coordinate_x, 15);
        label_x.setHorizontalTextPosition(JLabel.CENTER);
        label_y.setHorizontalTextPosition(JLabel.CENTER);
        label_z.setHorizontalTextPosition(JLabel.CENTER);
        label_x.setForeground(label_color);
        label_y.setForeground(label_color);
        label_z.setForeground(label_color);
        add(label_x);
        add(label_y);
        add(label_z);
        
        buttonA = new Button(button_A_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "A", button_color_on, button_color_off);
        buttonB = new Button(button_B_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "B", button_color_on, button_color_off);
        add(buttonA);
        add(buttonB);
        
        JPanel textpanel = new JPanel();
        
        textpanel.setSize(100, 100);
        
        eventTextArea = new JTextArea("", 6, 8);
        JScrollPane jsc = new JScrollPane(eventTextArea);
        textpanel.add(jsc);
		add(textpanel);
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}
	
	public Dashboard(JEDI_api api1) {
		this.api1 = api1;
		if(api1 instanceof FakeJEDI)
			addKeyListener(((FakeJEDI) api1).getAdapter());
			
		setLayout(null);
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        setSize(panel_width, panel_height);
        
        accZeroLine = new JPanel();
        accZeroLine.setBounds(acc_zero_line_x, acc_zero_line_y, acc_zero_line_width, acc_zero_line_height);
        accZeroLine.setBackground(acc_bar_color);
        add(accZeroLine);
        
        acc_bar_x = new AccBar(acc_bar_x_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_y = new AccBar(acc_bar_y_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_z = new AccBar(acc_bar_z_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_x.addToPanel(this);
        acc_bar_y.addToPanel(this);
        acc_bar_z.addToPanel(this);
        
        label_x = new JLabel("x\n");
        label_y = new JLabel("y\n");
        label_z = new JLabel("z\n");
        label_x.setBounds(label_x_coordinate_x, labels_coordinate_y, acc_bar_x_coordinate_x, 15);
        label_y.setBounds(label_y_coordinate_x, labels_coordinate_y, acc_bar_y_coordinate_x, 15);
        label_z.setBounds(label_z_coordinate_x, labels_coordinate_y, acc_bar_z_coordinate_x, 15);
        label_x.setHorizontalTextPosition(JLabel.CENTER);
        label_y.setHorizontalTextPosition(JLabel.CENTER);
        label_z.setHorizontalTextPosition(JLabel.CENTER);
        label_x.setForeground(label_color);
        label_y.setForeground(label_color);
        label_z.setForeground(label_color);
        add(label_x);
        add(label_y);
        add(label_z);
        
        buttonA = new Button(button_A_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "A", button_color_on, button_color_off);
        buttonB = new Button(button_B_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "B", button_color_on, button_color_off);
        add(buttonA);
        add(buttonB);
        
        JPanel textpanel = new JPanel();
        
        textpanel.setSize(100, 100);
        
        eventTextArea = new JTextArea("", 6, 8);
        JScrollPane jsc = new JScrollPane(eventTextArea);
        textpanel.add(jsc);
		add(textpanel);
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}
	
	public Dashboard(ButtonListenerInterface bl, ConnectionListenerInterface ci){
		setLayout(null);
        setFocusable(true);
        setBackground(Color.BLACK);
        setDoubleBuffered(true);

        setSize(panel_width, panel_height);
        
        accZeroLine = new JPanel();
        accZeroLine.setBounds(acc_zero_line_x, acc_zero_line_y, acc_zero_line_width, acc_zero_line_height);
        accZeroLine.setBackground(acc_bar_color);
        add(accZeroLine);
        
        acc_bar_x = new AccBar(acc_bar_x_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_y = new AccBar(acc_bar_y_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_z = new AccBar(acc_bar_z_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_x.addToPanel(this);
        acc_bar_y.addToPanel(this);
        acc_bar_z.addToPanel(this);
        
        label_x = new JLabel("x\n");
        label_y = new JLabel("y\n");
        label_z = new JLabel("z\n");
        label_x.setBounds(label_x_coordinate_x, labels_coordinate_y, acc_bar_x_coordinate_x, 15);
        label_y.setBounds(label_y_coordinate_x, labels_coordinate_y, acc_bar_y_coordinate_x, 15);
        label_z.setBounds(label_z_coordinate_x, labels_coordinate_y, acc_bar_z_coordinate_x, 15);
        label_x.setHorizontalTextPosition(JLabel.CENTER);
        label_y.setHorizontalTextPosition(JLabel.CENTER);
        label_z.setHorizontalTextPosition(JLabel.CENTER);
        label_x.setForeground(label_color);
        label_y.setForeground(label_color);
        label_z.setForeground(label_color);
        add(label_x);
        add(label_y);
        add(label_z);
        
        buttonA = new Button(button_A_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "A", button_color_on, button_color_off);
        buttonB = new Button(button_B_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "B", button_color_on, button_color_off);
        add(buttonA);
        add(buttonB);
        
        JPanel textpanel = new JPanel();
        
        textpanel.setSize(100, 100);
        
        eventTextArea = new JTextArea("", 6, 8);
        JScrollPane jsc = new JScrollPane(eventTextArea);
        textpanel.add(jsc);
		add(textpanel);
        
        timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(), 5, 5);
	}
	
	public void paint(){
	}
	
	private class ScheduleTask extends TimerTask{

		@Override
		public void run() {
			if(api1 != null){
				
				Axis acc = api1.getAcceleration();
				if(acc == null)
					return;
				
				int x = (int) acc.getX();
				int y = (int) acc.getY();
				int z = (int) acc.getZ();

				acc_bar_x.resizeHeight(x);
				acc_bar_y.resizeHeight(y);
				acc_bar_z.resizeHeight(z);
			}
		}
    }
	


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	private void setFakeEvents(boolean fakeEvents) {
		this.fakeEvents = fakeEvents;
	}
	
	public void enableEvents(){
		setFakeEvents(true);
	}
	
	public void disableEvents(){
		setFakeEvents(false);
	}

	@Override
	public void connectionStarted(ConnectionEvent event) {
		eventTextArea.append("C\n");
	}

	@Override
	public void connectionEnded(ConnectionEvent event) {
		eventTextArea.append("D\n");
	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		if(e.getPressedButton() == ButtonEvent.JEDI_A){
			eventTextArea.append(((JEDI_api) e.getSource()).getJediID() + "P_A\n");
			buttonA.setPressed();
		}
		else if(e.getPressedButton() == ButtonEvent.JEDI_B){
			eventTextArea.append("P_B\n");
			buttonB.setPressed();	
		}
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		if(e.getPressedButton() == ButtonEvent.JEDI_A){
			eventTextArea.append(((JEDI_api) e.getSource()).getJediID() + "R_A\n");
			buttonA.setReleased();
		}
		else if(e.getPressedButton() == ButtonEvent.JEDI_B){
			eventTextArea.append("R_B\n");
			buttonB.setReleased();
		}
	}
}

