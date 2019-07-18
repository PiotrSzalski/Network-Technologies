package lista3;

import java.util.Random;

public class sender extends Thread {
	
	private int port;
	private char mark;
	private int delay;
	private link link;
	private Random ran;
	private int times;
	private int waitingTimes;
	private int waitTime;
	private boolean sending;
	private boolean isReady;
	private boolean collision;
	private boolean wasCollision;
	private int attempt;
	
	public sender(int n, char c, int delay, link l) {
		if(n >= l.getLinkSize() - 2 || n < 0) {
			System.out.println("Incorrect position: "+n);
			System.exit(0);
		}
		this.port = n;
		this.mark = c;
		this.delay = delay;
		this.link = l;
		ran = new Random();
	}
	
	@Override
	public void run() {
		while(true) {
			if(collision) {
				if(times >= link.getLinkSize()) {
					times = 0;
					attempt++;
					link.setCellChar(port, '0');
					link.setCellTail(port, true);
					collision = false;
					wasCollision = true;
					if(attempt < 10) {
						waitTime = (int)(ran.nextDouble() * Math.pow(2, attempt) + 1) * link.getLinkSize();
					} else if(attempt < 16) {
						waitTime = (int)(ran.nextDouble() * Math.pow(2, 10) + 1) * link.getLinkSize();
					} else {
						System.err.println("Nie mo¿na przes³aæ");
						System.exit(0);
					}
				}
				times++;
			} else if(wasCollision) {
				if(times >= waitTime) {
					times = 0;
					isReady = true;
					wasCollision = false;
				}
				times++;
			} else if(sending) {
				if(link.getCellChar(port) == '#') {
					collision = true;
					sending = false;
					link.setCellChar(port, '!');
					link.setCellTail(port, true);
					times = 1;
				}
				if(times >= 2*link.getLinkSize()) {
					sending = false;
					link.setCellChar(port, '0');
					link.setCellTail(port, true);
					attempt = 0;
				}
				times++;
			} else {
				if(isReady && link.getCellChar(port) == '0' && !link.getCellTail(port) && !link.getCellTail(Math.abs(port-1)) && 
						!link.getCellTail(Math.abs(port-2)) && !link.getCellTail(port) && !link.getCellTail(port+1) && !link.getCellTail(port+2)) {
					link.setCellChar(port, mark);
					link.setCellTail(port, true);
					sending = true;
					times = 1;
					waitingTimes = 0;
					isReady = false; 
				} else {
					if(waitingTimes*link.getTimeRef() >= delay && ran.nextBoolean()) {
						isReady = true;
					}
					if(waitingTimes*link.getTimeRef() >=  delay) {
						waitingTimes = 0;
					} else {
						waitingTimes++;
					}
				}
			}
			try {
				Thread.sleep(link.getTimeRef());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
