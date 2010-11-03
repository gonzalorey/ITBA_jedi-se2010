package data;

public class Buttons{
	private boolean A;
	private boolean B;
	
	/**
	 * Class with the states of the buttons
	 * @param A Boolean with the state of the button A
	 * @param B Boolean with the state of the button B
	 */
	public Buttons(boolean A, boolean B){
		this.A = A;
		this.B = B;
	}
	
	/**
	 * Gets the state of the A button
	 * @return True if the button is pressed, false otherwise
	 */
	public boolean isPressedA(){
		return this.A;
	}
	
	/**
	 * Gets the state of the B button
	 * @return True if the button is pressed, false otherwise
	 */
	public boolean isPressedB(){
		return this.B;
	}
	
	/**
	 * Sets the state of the buttons
	 * @param buttons Object Buttons with the desired state
	 */
	public void set(Buttons buttons){
		this.A = buttons.isPressedA();
		this.B = buttons.isPressedB();
	}
	
	@Override
	public String toString() {
		return "B1:" + this.A + " B2:" + this.B;
	}
}
