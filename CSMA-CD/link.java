package lista3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.TimerTask;

public class link extends TimerTask{
	
	private cell[] link;
	private int timeRef;
	PrintWriter writer;
	
	public link(int n, int time) {
		
		link = new cell[n+2];
		try {
			writer = new PrintWriter("CSMA.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < n + 2; i++) {
			link[i] = new cell('0',false);
		}
		
		this.timeRef = time;
	}
	
	public char getCellChar(int n) {
		return link[n].value;
	}
	public boolean getCellTail(int n) {
		return link[n].tail;
	}
	public void setCellChar(int n, char v) {
		link[n].value = v;
	}
	public void setCellTail(int n, boolean t) {
		link[n].tail = t;
	}
	public int getTimeRef() {
		return timeRef;
	}
	public int getLinkSize() {
		return link.length;
	}
 	
	@Override
	public void run() {
		
		while(true) {
			print();
			//write();
			for(int i = 0; i < link.length; i++) {
				if(link[i].tail) {
					if(i > 0 && link[i-1].value != link[i].value) {
						if( link[i].value == '0') {
							link[i-1].value = '0';
						} else if( link[i].value == '!') {
							link[i-1].value = '!';
						} else if( link[i-1].value == '0') {
							link[i-1].value = link[i].value;
						} else if (link[i-1].value != link[i].value && link[i-1].value != '!') {
							link[i-1].value = '#';
						}
						link[i-1].tail = true;
					} 
					if(i < link.length - 1 && link[i+1].value != link[i].value) {
						if( link[i].value == '0') {
							link[i+1].value = '0';
						} else if( link[i].value == '!') {
							link[i+1].value = '!';
						} else if( link[i+1].value == '0') {
							link[i+1].value = link[i].value;
						} else if (link[i+1].value != link[i].value) {
							link[i+1].value = '#';
						} 
						link[i+1].tail = true;
					}
					link[i].tail = false;
					i++;
				}
			}
			try {
				Thread.sleep(timeRef);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void print() {
		for(int i=0;i<link.length-2;i++) {
			System.out.print(link[i].value);
		}
		System.out.println("");
	}
	
	public void write() {
		for(int i=0;i<link.length-2;i++) {
			writer.print(link[i].value);
		}
		writer.println("");
		writer.flush();
	}

	private class cell {
		private char value;
		private boolean tail;
	
		public cell(char v, boolean t) {
			this.tail = t;
			this.value = v;
		}
	}
}
