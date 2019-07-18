package lista3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.CRC32;

public class zad1b {

	public static void main(String[] args) throws IOException {
		
		FileInputStream reader = new FileInputStream(new File("W.txt"));
		PrintWriter writer = new PrintWriter("O.txt");
		boolean reading = false;
		String message = "";
		int bit, n = 0;
		
		while ((bit = reader.read()) != -1) {
			if (bit == '1') {
				if(reading) {
					message += "1";
				}
				n++;
			} else if( bit == '0') {
				if(n<5) {
					n = 0;
					if(reading) {
						message += "0";
					}
				} else if (n == 5) {
					n=0;
				} else if (n == 6) {
					if(reading) {
						reading = false;
						n = 0;
						message = message.substring(0, message.length()-7);
						if(checkCRC(message)) {
							writer.append(message.substring(0,message.length() - 32));
						} else {
							System.out.println("Error");
						}
						message = "";
					} else {
						reading = true;
						n = 0;
					}
				} else {
					System.out.println("Error");
					System.exit(0);
				}
			}
		}
		if(reading) {
			System.out.println("Error");
		}
		writer.close();
		reader.close();
	}
	
	public static boolean checkCRC(String s) {
		
		if(s.length() < 32) {
			return false;
		}
		String message = s.substring(0, s.length() - 32);
		String crccode = s.substring(s.length()-32);
		byte[] messagearr = new byte[message.length()];
		for(int i = 0; i < message.length(); i++) {
			messagearr[i] = (byte) (message.charAt(i) - 48);
		}
		CRC32 crc = new CRC32();
		crc.update(messagearr);
		String expected = Long.toBinaryString(crc.getValue());
		while(crccode.charAt(0) == '0') {
			crccode = crccode.substring(1);
		}
		if(crccode.equals(expected)) {
			return true;
		} else {
			return false;
		}
	}
}
