package data.packets;

import data.Axis;
import data.Buttons;

public class InformPacket extends Packet {
	
	//type of the id
	private static int TYPE_ID = 20;
	
	//axis
	private int X, Y, Z;
	
	//buttons
	private int B1, B2;
	
	public InformPacket(int orig, int dest, int X, 
			int Y, int Z, int B1, int B2, byte checksum) {
		
		//transport data
		this.length = 10;
		this.orig = orig;
		this.dest = dest;
		this.checksum = checksum;
		
		//inform data
		this.type = TYPE_ID;
		this.X = X;
		this.Y = Y;
		this.Z = Z;
		this.B1 = B1;
		this.B2 = B2;
		
		//reajust the data
		if(this.X >= 0)
			this.X -= 127;
		else
			this.X += 128;
		
		if(this.Y >= 0)
			this.Y -= 127;
		else
			this.Y += 128;
		
		if(this.Z >= 0)
			this.Z -= 127;
		else
			this.Z += 128;
			
	}
	
	public static InformPacket parseInform(byte[] readBuffer, int serverID) {
		
		if(readBuffer[2] != TYPE_ID || (readBuffer[1] != 0 && readBuffer[1] != serverID))
			return null;
		
		return new InformPacket(readBuffer[0], readBuffer[1], 
				readBuffer[3], readBuffer[4], readBuffer[5], 
				readBuffer[6], readBuffer[7], readBuffer[8]);
	}
	
	public Axis getAxis(){
		return new Axis(X, Y, Z);
	}
	
	public Buttons getButtons(){
		return new Buttons(B1 == 1, B2 == 1);
	}
	
	@Override
	public byte[] serialize() {
		byte[] ans = new byte[14];
		
		for(int i = 0; i < this.sync.length; i++)
			ans[i] = this.sync[i];
		
		ans[4] = (byte) this.length;
		ans[5] = (byte) this.orig;
		ans[6] = (byte) this.dest;
		ans[7] = (byte) this.type;
		ans[8] = (byte) this.X;
		ans[9] = (byte) this.Y;
		ans[10] = (byte) this.Z;
		ans[11] = (byte) this.B1;
		ans[12] = (byte) this.B2;
		ans[13] = (byte) this.checksum;
		
		return ans;
	}
	
	@Override
	public String toString() {
		String ans = "";
		
		ans += "Length = " + this.length + "\n";
		ans += "Orig = " + this.orig + "\n";
		ans += "Dest = " + this.dest + "\n";
		ans += "Type = INFORM\n";
		ans += "X = " + this.X + "\n";
		ans += "Y = " + this.Y + "\n";
		ans += "Z = " + this.Z + "\n";
		ans += "B1 = " + this.B1 + "\n";
		ans += "B2 = " + this.B2+ "\n";
		ans += "Checksum = " + this.checksum + "\n";
		
		return ans;
	}
}
