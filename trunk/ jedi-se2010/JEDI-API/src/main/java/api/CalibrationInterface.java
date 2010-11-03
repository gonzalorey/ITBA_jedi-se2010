package api;

public interface CalibrationInterface {
	
	/**
	 * Function to be called before the calibration starts
	 */
	public void onStart();
	
	/**
	 * Function to be called when the calibration ends
	 */
	public void onEnding();
}
