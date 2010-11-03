package data;

import java.util.Calendar;

public class Axis {

	// axis type
	public static enum TypeOfData{ACCELERATION, VELOCITY, POSITION};
	
	// axis values
	private double x;
	private double y;
	private double z;
	
	// time value
	private long time;
	
	// type of axis
	private TypeOfData type;
	
	private static Axis zero = new Axis(0,0,0);
	
	
	public Axis(double x, double y, double z, TypeOfData type, long time){
		this(x,y,z);
		this.type = type;
		this.time = time;
	}
	
	public Axis(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = TypeOfData.ACCELERATION;
		this.time = Calendar.getInstance().getTimeInMillis();
		
//		if(zero != null && this.type == TypeOfData.ACCELERATION)
//			System.out.println("(" + round(x - zero.x, 2) + "," + round(y - zero.y, 2) + 
//					"," + round(z - zero.z, 2) + ")");
//		
	}
	
//	private double round(double d, int decimalPlace){
//		// see the Javadoc about why we use a String in the constructor
//		// http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
//		BigDecimal bd = new BigDecimal(Double.toString(d));
//		bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
//		return bd.doubleValue();
//	}
	
	public double getX() {
		if(type == TypeOfData.ACCELERATION){
			return x - zero.x;
		}
		return x;
	}

	public double getY() {
		if(type == TypeOfData.ACCELERATION)
			return y - zero.y;
		return y;
	}
	
	public double getZ() {
		if(type == TypeOfData.ACCELERATION)
			return z - zero.z;
		return y;
	}
	
	public double getOriginalX() {
		return x;
	}

	public double getOriginalY() {
		return y;
	}
	
	public double getOriginalZ() {
		return z;
	}
	
	public long getTime(){
		return time;
	}
	
	public static void setZero(double newX, double newY, double newZ){
		zero = new Axis(newX, newY, newZ);
	}
	
	public static Axis getZero(){
		return zero;
	}

	public TypeOfData getType() {
		return type;
	}

	@Override
	public String toString() {
		return "(" + this.x + "," + this.y + "," + this.z + ")"; 
	}
}
