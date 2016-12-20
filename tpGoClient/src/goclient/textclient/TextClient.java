package goclient.textclient;

import java.util.Scanner;

import goclient.client.Communication;
import goclient.client.ReaderWriter;

public class TextClient {
	
	private static ReaderWriter communication;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		communication = new Communication();
		WriterThread t = new WriterThread(communication);
		t.start();
		
		while(true){
			String line = communication.read();
			if(line != null){
				System.out.println(line);
			}
				
		}
	}

}
