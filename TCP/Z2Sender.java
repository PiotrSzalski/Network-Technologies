import java.net.*;
import java.util.ArrayList;


class Z2Sender {
	static final int datagramSize=50;
	static final int sleepTime= 2500;
	static final int maxPacket=50;
	InetAddress localHost;
	int destinationPort;
	DatagramSocket socket;
	SenderThread sender;
	ReceiverThread receiver;
	
	ArrayList<DatagramPacket> packets;
	int last = -1;
	InputReader reader;

	public Z2Sender(int myPort, int destPort) throws Exception {
		localHost=InetAddress.getByName("127.0.0.1");
		destinationPort=destPort;
		socket=new DatagramSocket(myPort);
		sender=new SenderThread();
		receiver=new ReceiverThread();
		packets = new ArrayList<>();
		reader = new InputReader();
    }

	class SenderThread extends Thread {
		public void run() {
			int i;
			try {
				while(true) {
					int lastId = last;
					for(i = lastId + 1; i <= lastId + 10 ; i++) {
						if(i < packets.size()) {
							socket.send(packets.get(i));
						}
					}
					sleep(sleepTime);
				}
 				
			} catch(Exception e) {
				System.out.println("Z2Sender.SenderThread.run: "+e);
			}
		}
	}
	
	class ReceiverThread extends Thread {
		public void run() {
			try {
				while(true) {
					byte[] data = new byte[datagramSize];
					DatagramPacket packet = new DatagramPacket(data, datagramSize);
					socket.receive(packet);
					Z2Packet p=new Z2Packet(packet.getData());
					System.out.println("S:"+p.getIntAt(0)+": "+(char) p.data[4]);
					if(p.getIntAt(0) > last) {
						last = p.getIntAt(0);
					}
				}
			} catch(Exception e) {
				System.out.println("Z2Sender.ReceiverThread.run: "+e);
			}
		}
	}
	
	class InputReader extends Thread{
        public void run(){
            int x;
            try {
                for(int i = 0; (x = System.in.read()) >= 0 ; i++) {
                    Z2Packet p=new Z2Packet(4+1);
                    p.setIntAt(i,0);
                    p.data[4]= (byte) x;
                    DatagramPacket packet = new DatagramPacket(p.data, p.data.length, localHost, destinationPort);
                    packets.add(packet);
                }
            } catch(Exception e) { 
				System.out.println("Z2Sender.InputReaderThread.run: "+e); 
			}
        }
    }

	public static void main(String[] args) throws Exception {
		Z2Sender sender=new Z2Sender(Integer.parseInt(args[0]),Integer.parseInt(args[1]));
		sender.sender.start();
		sender.receiver.start();
		sender.reader.start();
	}
}
