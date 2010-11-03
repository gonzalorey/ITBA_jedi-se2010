package data.packets;


public class DiscoverRespPacket extends Packet {
	
	//type of the id
	private static int TYPE_ID = 12;
	
	private int joystickNum;
	
	public static enum acc_method{ACC_METHOD_MODE, ACC_METHOD_MEDIAN, ACC_METHOD_MEAN};
	
	private acc_method acc_method_selector;
	
	public DiscoverRespPacket(int orig, int dest, int joystickNum, acc_method acc_method_selector, byte checksum) {
		
		//transport data
		this.length = 7;
		this.orig = orig;
		this.dest = dest;
		this.acc_method_selector = acc_method_selector;
		this.checksum = checksum;
		
		//discover response data
		this.type = TYPE_ID;
		this.joystickNum = joystickNum;
		
	}
	
	public static DiscoverRespPacket parseInform(byte[] readBuffer, int serverID) {
		
		if(readBuffer[2] != TYPE_ID || (readBuffer[1] != 0 && readBuffer[1] != serverID))
			return null;
		
		int acc_method_aux = readBuffer[4];
		acc_method acc_method_selector_aux;
		
		// set the method to calculate the acceleration
		switch(acc_method_aux)
		{
			case 0:
				acc_method_selector_aux = acc_method.ACC_METHOD_MODE;
				break;
			case 1:
				acc_method_selector_aux = acc_method.ACC_METHOD_MEDIAN;
				break;
			case 2:
				acc_method_selector_aux = acc_method.ACC_METHOD_MEAN;
				break;
			default:
				acc_method_selector_aux = acc_method.ACC_METHOD_MEDIAN;
		}
		
		return new DiscoverRespPacket(readBuffer[0], readBuffer[1], readBuffer[3], acc_method_selector_aux, readBuffer[5]);
	}
	
	@Override
	public byte[] serialize() {
		byte[] ans = new byte[11];
		
		for(int i = 0; i < this.sync.length; i++)
			ans[i] = this.sync[i];
		
		ans[4] = (byte) this.length;
		ans[5] = (byte) this.orig;
		ans[6] = (byte) this.dest;
		ans[7] = (byte) this.type;
		ans[8] = (byte) this.joystickNum;

		// set the method to calculate the acceleration
		switch(this.acc_method_selector)
		{
			case ACC_METHOD_MODE:
				ans[9] = 0;
				break;
			case ACC_METHOD_MEDIAN:
				ans[9] = 1;
				break;
			case ACC_METHOD_MEAN:
				ans[9] = 2;
				break;
			default:
				ans[9] = 1;
		}
		
		ans[10] = (byte) this.checksum;
		
		return ans;
	}
	
	@Override
	public String toString() {
		String ans = "";
		
		ans += "Length = " + this.length + "\n";
		ans += "Orig = " + this.orig + "\n";
		ans += "Dest = " + this.dest + "\n";
		ans += "Type = DISCOVERY_RESPONSE\n";
		ans += "JoystickNum = " + this.joystickNum + "\n";
		ans += "Checksum = " + this.checksum + "\n";
		
		return ans;
	}
}
