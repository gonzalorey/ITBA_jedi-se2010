package event;

public class ButtonEvent extends java.util.EventObject {
	
	private static final long serialVersionUID = 1L;
	
	//buttons state
	public static final int JEDI_A = 1;
	public static final int JEDI_B = 2;
	
	//pressed button
	int pressedButton;
	
    public ButtonEvent(Object source, int pressedButton) {
    	super(source);
    	this.pressedButton = pressedButton;
    }
    
    public int getPressedButton(){
    	return pressedButton;
    }
}

