package lista3;

import java.util.Timer;

public class zad2 {
	
	public static void main(String[] args) {
		
		link link = new link(50,10);
	
		Timer timer = new Timer();
		timer.schedule(link, 2);
		
		sender sn1 = new sender(0,'1',50,link);
		sender sn2 = new sender(25,'2',50,link);
		sender sn3 = new sender(49,'3',50,link);
		sn1.start();
		sn2.start();
		sn3.start();
	}
}
