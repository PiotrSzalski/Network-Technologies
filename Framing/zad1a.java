package lista3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.zip.CRC32;

public class zad1a {

	static String TRANSMISSION_MARKER = "01111110";
	
	public static void main(String[] args) throws IOException {
		
		FileInputStream reader = new FileInputStream(new File("Z.txt"));
		PrintWriter writer = new PrintWriter("W.txt");
		String message = "";
		int bit;
		
		while( (bit = reader.read()) != -1 ) {
			if(bit != 48 && bit != 49) {
				continue;
			}
			message += bit - 48;
			if(message.length() == 200) {
				message = addCRC(message);
				message = stretchBits(message);
				writer.append(message);
				message = "";
			} 
		}
		message = addCRC(message);
		message = stretchBits(message);
		writer.append(message);
		writer.close();
		reader.close();
	}
	
	public static String addCRC(String message) {
		
		byte messagearr[] = new byte[message.length()];	
		
		for(int i = 0; i < message.length(); i++) {
			messagearr[i] =  (byte) (message.charAt(i) - 48);
		}
		CRC32 crc = new CRC32();
		crc.update(messagearr);
		String crccode = Long.toBinaryString(crc.getValue());
		while (crccode.length() != 32) {
			crccode = "0" + crccode;
		}
		message += crccode;
		
		return message;
	}
	
	public static String stretchBits (String s) {
		
		int counter = 0;
		String frame = "";
		
		frame += TRANSMISSION_MARKER;
		for(int i = 0; i < s.length(); i++) {
			if(counter == 5) {
				frame += "0";
				counter = 0;
			}
			if(s.charAt(i) == '1' ) {
				frame += "1";
				counter++;
			} else {
				frame += "0";
				counter = 0;
			}
		}
		frame += TRANSMISSION_MARKER;
		
		return frame;
	}
}
