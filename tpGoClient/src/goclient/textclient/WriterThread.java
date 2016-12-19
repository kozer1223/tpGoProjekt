package goclient.textclient;

import java.util.Scanner;

import goclient.client.ReaderWriter;

public class WriterThread extends Thread implements Runnable {

	public ReaderWriter communication;
	Scanner in;
	
	public WriterThread(ReaderWriter communication) {
		this.communication = communication;
		in = new Scanner(System.in);
	}

	public void run(){
		//if (in.hasNextLine()){
		while(true){
			String line = in.nextLine();
			if(line != ""){
				System.out.println("> "+line);
				communication.write(line);
			}
		}
	}

}
