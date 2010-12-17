package client.game.fakejedi;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import api.ButtonListenerInterface;
import api.ConnectionListenerInterface;
import api.JEDIGame;
import api.JEDI_api;
import api.JEDI_api.JoystickNumbers;
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
	
	private int panel_width = 400;
	private int panel_height = 300;
		
	private int acc_bars_width = 20;
	private int acc_bars_separation = 10;
	
	private int acc_bars_y_coordinate = 75;
	private int acc_bar_x_coordinate_x = panel_width - acc_bars_width*3 - acc_bars_separation*5;
	private int acc_bar_y_coordinate_x = acc_bar_x_coordinate_x + acc_bars_width + acc_bars_separation;
	private int acc_bar_z_coordinate_x = acc_bar_y_coordinate_x + acc_bars_width + acc_bars_separation;
	private java.awt.Color acc_bar_color = Color.WHITE;
	
	private int acc_zero_line_thickness = 2;
	private int acc_zero_line_x = acc_bar_x_coordinate_x - acc_bars_separation;
	private int acc_zero_line_y = acc_bars_y_coordinate - acc_zero_line_thickness / 2;
	private int acc_zero_line_width = acc_bar_z_coordinate_x + acc_bars_width + acc_bars_separation - (acc_bar_x_coordinate_x - acc_bars_separation);
	private int acc_zero_line_height = acc_zero_line_thickness;
	
	private int labels_coordinate_y = acc_bars_y_coordinate + 10;
	private int label_x_coordinate_x = acc_bar_x_coordinate_x+4;
	private int label_y_coordinate_x = acc_bar_y_coordinate_x+4;
	private int label_z_coordinate_x = acc_bar_z_coordinate_x+4;
	private java.awt.Color label_color = acc_bar_color;
	
	private int buttons_width = 30;
	private int buttons_height = 30;
	private int buttons_coordinate_y = 100;
	private int buttons_delta = 10;
	private int button_A_coordinate_x = 100;
	private int button_B_coordinate_x = button_A_coordinate_x + buttons_width + buttons_delta;
	private java.awt.Color button_color_on = Color.RED;
	private java.awt.Color button_color_off= Color.WHITE;
	
	private JEDIPanel jediPanel1;
	private JEDIPanel jediPanel2;
	
	public JEDIPanel buildJEDIPanel(JEDI_api api){
		Button button_a;
		Button button_b;
		
		JLabel label_x;
		JLabel label_y;
		JLabel label_z;
		
		JPanel accZeroLine;
		
		AccBar acc_bar_x;
		AccBar acc_bar_y;
		AccBar acc_bar_z;
		
		JTextArea eventTextArea;
		
		JEDIPanel jediPanel = new JEDIPanel();
		
		jediPanel.getJPanel().setLayout(null);
		jediPanel.getJPanel().setFocusable(true);
		jediPanel.getJPanel().setBackground(Color.BLACK);
		
		jediPanel.getJPanel().setSize(panel_width, panel_height);
        
		accZeroLine = new JPanel();
        accZeroLine.setBounds(acc_zero_line_x, acc_zero_line_y, acc_zero_line_width, acc_zero_line_height);
        accZeroLine.setBackground(acc_bar_color);
        jediPanel.addComponent("accZeroLine", accZeroLine);
        
        acc_bar_x = new AccBar(acc_bar_x_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_y = new AccBar(acc_bar_y_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        acc_bar_z = new AccBar(acc_bar_z_coordinate_x, acc_bars_y_coordinate, acc_bars_width, 0, acc_bar_color);
        jediPanel.addComponent("acc_bar_x", acc_bar_x);
        jediPanel.addComponent("acc_bar_y", acc_bar_y);
        jediPanel.addComponent("acc_bar_z", acc_bar_z);
                
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
        jediPanel.addComponent("label_x", label_x);
        jediPanel.addComponent("label_y", label_y);
        jediPanel.addComponent("label_z", label_z);
        
        button_a = new Button(button_A_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "A", button_color_on, button_color_off);
        button_b = new Button(button_B_coordinate_x, buttons_coordinate_y, buttons_width, buttons_height, "B", button_color_on, button_color_off);
        jediPanel.addComponent("button_a", button_a);
        jediPanel.addComponent("button_b", button_b);
        
        JPanel textPanel = new JPanel();
        
        textPanel.setSize(100, 100);
        
        eventTextArea = new JTextArea("", 6, 8);
        JScrollPane jsc = new JScrollPane(eventTextArea);
        textPanel.add(jsc);
        jediPanel.addComponent("textPanel", textPanel);
        jediPanel.addComponentJ2M("eventTextArea", eventTextArea);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ScheduleTask(api, jediPanel), 5, 5);
		
        return jediPanel;
	}
	
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
		
		jediPanel1 = buildJEDIPanel(api1);
		jediPanel2 = buildJEDIPanel(api2);
		
		jediPanel1.getJPanel().setBounds(0, 300, 400, 300);
		jediPanel2.getJPanel().setBounds(400, 300, 400, 300);
		
//		jediPanel2.getJPanel().setBounds(jediPanel1.getJPanel().getX(), jediPanel1.getJPanel().getHeight(), 
//				jediPanel1.getJPanel().getWidth(), jediPanel1.getJPanel().getHeight());
				
		this.add(jediPanel1.getJPanel());
		this.add(jediPanel2.getJPanel());
	}
	
	private class ScheduleTask extends TimerTask{

		private JEDI_api api;
		private JEDIPanel jediPanel;
		
		public ScheduleTask(JEDI_api api, JEDIPanel jediPanel) {
			this.api = api;
			this.jediPanel = jediPanel;
		}
		
		@Override
		public void run() {
			if(api != null){
				
				Axis acc = api.getAcceleration();
				if(acc == null)
					return;
				
				int x = (int) acc.getX();
				int y = (int) acc.getY();
				int z = (int) acc.getZ();

				((AccBar) jediPanel.getComponent("acc_bar_x")).resizeHeight(x);
				((AccBar) jediPanel.getComponent("acc_bar_y")).resizeHeight(y);
				((AccBar) jediPanel.getComponent("acc_bar_z")).resizeHeight(z);
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
		if(((JEDI_api) event.getSource()).getJediNumber() == JoystickNumbers.JEDI_ONE)
			((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) event.getSource()).getJediNumber() + ": C\n");
		else
			((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) event.getSource()).getJediNumber() + ": C\n");
	}		

	@Override
	public void connectionEnded(ConnectionEvent event) {
		if(((JEDI_api) event.getSource()).getJediNumber() == JoystickNumbers.JEDI_ONE)
			((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) event.getSource()).getJediNumber() + ": D\n");
		else
			((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) event.getSource()).getJediNumber() + ": D\n");
	}

	@Override
	public void buttonPressed(ButtonEvent e) {
		if(((JEDI_api) e.getSource()).getJediNumber() == JoystickNumbers.JEDI_ONE){
			if(e.getPressedButton() == ButtonEvent.JEDI_A){
				((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": P_A\n");
				((Button) jediPanel1.getComponent("button_a")).setPressed();
			}
			else if(e.getPressedButton() == ButtonEvent.JEDI_B){
				((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": P_B\n");
				((Button) jediPanel1.getComponent("button_b")).setPressed();
			}
		} else {
			if(e.getPressedButton() == ButtonEvent.JEDI_A){
				((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": P_A\n");
				((Button) jediPanel2.getComponent("button_a")).setPressed();
			}
			else if(e.getPressedButton() == ButtonEvent.JEDI_B){
				((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": P_B\n");
				((Button) jediPanel2.getComponent("button_b")).setPressed();
			}
		}
	}

	@Override
	public void buttonReleased(ButtonEvent e) {
		if(((JEDI_api) e.getSource()).getJediNumber() == JoystickNumbers.JEDI_ONE){
			if(e.getPressedButton() == ButtonEvent.JEDI_A){
				((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": R_A\n");
				((Button) jediPanel1.getComponent("button_a")).setReleased();
			}
			else if(e.getPressedButton() == ButtonEvent.JEDI_B){
				((JTextArea) jediPanel1.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": R_B\n");
				((Button) jediPanel1.getComponent("button_b")).setReleased();
			}
		} else {
			if(e.getPressedButton() == ButtonEvent.JEDI_A){
				((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": R_A\n");
				((Button) jediPanel2.getComponent("button_a")).setReleased();
			}
			else if(e.getPressedButton() == ButtonEvent.JEDI_B){
				((JTextArea) jediPanel2.getComponent("eventTextArea")).append(((JEDI_api) e.getSource()).getJediNumber() + ": R_B\n");
				((Button) jediPanel2.getComponent("button_b")).setReleased();
			}
		}
	}
	private class JEDIPanel{
		private ConcurrentHashMap<String, JComponent> componentMap;
		private JPanel jpanel;
		
		public JEDIPanel() {
			componentMap = new ConcurrentHashMap<String, JComponent>();
			jpanel = new JPanel();
		}
		
		public JPanel getJPanel(){
			return jpanel;
		}
		
		public void addComponent(String name, JComponent component){
			componentMap.put(name, component);
			jpanel.add(component);
		}
		
		public void addComponentJ2M(String name, JComponent component){
			componentMap.put(name, component);
		}
		
		public JComponent getComponent(String name){
			return componentMap.get(name);
		}
	}

	public void addGame(JEDIGame game) {
		game.setBounds(200, 0, 400, 300);
		this.add(game);
	} 
}

