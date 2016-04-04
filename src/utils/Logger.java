package utils;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	static FileWriter writer;
	public Logger(){
		try {
			writer = new FileWriter(Strings.FileLogger);
		} catch (IOException e) {
			System.out.println("Logger failed to initialise ");
		}
	}
	public static void write(String s){
		try {
			writer.append( String.valueOf(System.currentTimeMillis()) + s);
		} catch (IOException e) {
			System.out.println("Logger failed to write " + s + "\r\n");
		}
	}
	public static void close(){
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
