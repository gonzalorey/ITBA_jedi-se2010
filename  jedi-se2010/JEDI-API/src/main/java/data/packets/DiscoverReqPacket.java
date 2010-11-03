package data.packets;



public class DiscoverReqPacket extends Packet {
	
	//type of the id
	private static int TYPE_ID = 11;
	
	public DiscoverReqPacket(int orig, int dest, byte checksum) {
		
		//transport data
		this.length = 5;
		this.orig = orig;
		this.dest = dest;
		this.checksum = checksum;
		
		//discover request data
		this.type = TYPE_ID;
	}
	
	public static DiscoverReqPacket parseInform(byte[] readBuffer) {
		
		if(readBuffer[2] != TYPE_ID || readBuffer[1] != 0)
			return null;
		
		return new DiscoverReqPacket(readBuffer[0], readBuffer[1], readBuffer[3]);
	}
	
	@Override
	public byte[] serialize() {
		byte[] ans = new byte[9];
		
		for(int i = 0; i < this.sync.length; i++)
			ans[i] = this.sync[i];
		
		ans[4] = (byte) this.length;
		ans[5] = (byte) this.orig;
		ans[6] = (byte) this.dest;
		ans[7] = (byte) this.type;
		ans[8] = (byte) this.checksum;
		
		return ans;
	}
	
	@Override
	public String toString() {
		String ans = "";
		
		ans += "Length = " + this.length + "\n";
		ans += "Orig = " + this.orig + "\n";
		ans += "Dest = " + this.dest + "\n";
		ans += "Type = DISCOVERY_REQUEST\n";
		ans += "Checksum = " + this.checksum + "\n";
		
		return ans;
	}
}
