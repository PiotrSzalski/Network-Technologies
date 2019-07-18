import java.net.*;
import java.util.Hashtable;

public class Z2Receiver {
	static final int datagramSize=50;
	InetAddress localHost;
	int destinationPort;
	DatagramSocket socket;
	ReceiverThread receiver;
	
	SenderThread sender;
	DatagramPacket lastPacket = null;
	int lastId = -1;
	int timeOut = 2500;
	Hashtable<Integer, DatagramPacket> packets;

	public Z2Receiver(int myPort, int destPort) throws Exception {
		localHost=InetAddress.getByName("127.0.0.1");
		destinationPort=destPort;
		socket=new DatagramSocket(myPort);
		receiver=new ReceiverThread();    
		sender = new SenderThread();
		packets = new Hashtable<>();
    }

	class ReceiverThread extends Thread {
		public void run() {
			try {
				while(true) {
					byte[] data=new byte[datagramSize];
					DatagramPacket packet=new DatagramPacket(data, datagramSize);
					socket.receive(packet);
                    Z2Packet p=new Z2Packet(packet.getData());
					if(lastId + 1 == p.getIntAt(0)) {
						System.out.println("R:"+p.getIntAt(0)+": "+(char) p.data[4]);
						lastPacket = packet;
						lastId++;
						while (packets.containsKey(lastId + 1)) {
                            lastId++;
                            lastPacket = packets.remove(lastId);
							p = new Z2Packet(lastPacket.getData());
                            System.out.println("R:" + lastId + ": " + (char) p.data[4]);
                        }
					} else if (p.getIntAt(0) > lastId + 1 && !packets.containsKey(p.getIntAt(0))) {
						packets.put(p.getIntAt(0), packet); 
					}
				}
			} catch(Exception e) {
				System.out.println("Z2Receiver.ReceiverThread.run: "+e);
			}
		}
	}
	
	class SenderThread extends Thread {
        public void run() {
            try {
                while(true) {
                    sleep(timeOut);
                    if (lastId != -1){
						lastPacket.setPort(destinationPort);
						socket.send(lastPacket);
                    }
                }
            } catch(Exception e) { 
				System.out.println("Z2Receiver.SenderThread.run: "+e); 
			}
        }
    }

	public static void main(String[] args) throws Exception {
		Z2Receiver receiver=new Z2Receiver( Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		receiver.receiver.start();
		receiver.sender.start();
    }
}
