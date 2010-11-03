package data.packets;

public abstract class Packet {
	
	//packet syncronization data
	protected byte[] sync = {0,0,0,0};
	
	//packet length
	protected int length;
	
	//packet origin
	protected int orig;
	
	//packet destiny
	protected int dest;
	
	//packet type
	protected int type;
	
	//packet checksum
	protected byte checksum;
	
	//serialize the packet
	public abstract byte[] serialize();

	public int getLength() {
		return length;
	}

	public int getOrig() {
		return orig;
	}

	public int getDest() {
		return dest;
	}

	public int getType() {
		return type;
	}

	public byte getChecksum() {
		return checksum;
	}
}
