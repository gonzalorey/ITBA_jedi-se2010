package api;

import data.Buttons;
import data.Axis;

public interface JEDI_api {
	
	public static enum JoystickNumbers{JEDI_ONE, JEDI_TWO};
	
	JoystickNumbers getJediNumber();
	
	Integer getJediID();
	
	/** 
	 * Get the acceleration component of each axis
	 * @return An object Axis with the JEDI accelerationsw
	 */
	Axis getAcceleration();
	
	/** 
	 * Get the velocity component of each axis
	 * @return An object Axis with the JEDI velocity
	 */
	Axis getVelocity();
	
	/**
	 * Get the position component of each axis
	 * @return An object Axis with the JEDI position
	 */
	Axis getPosition();

	/**
	 * Get the direction component of each axis
	 * @return An object Axis with the JEDI directions
	 */
	Axis getDirection();
	
	/**
	 * Get the rotation component of each axis
	 * @return An object containing the states of the JEDI rotation 
	 */
	Axis getRotation();
	
	/**
	 * Get the buttons
	 * @return An object containing the states of the JEDI buttons
	 */
	Buttons getPressedButtons();
	
	/**
	 * Calibrate the JEDI
	 * @param milliseconds Lapse where the calibration will take place
	 * @param calibrationCallback Callback function to be called at the beginning and at the end 
	 * of the calibration process
	 */
	void calibrate(int milliseconds, CalibrationInterface calibrationCallback);
}
