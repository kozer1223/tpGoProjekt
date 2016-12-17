package goclient.client.test;

import goclient.client.ReaderWriter;

public class SpyReaderWriter implements ReaderWriter {
	
	private String resultString;

	public SpyReaderWriter() {
		
	}

	@Override
	public void write(String message) {
		resultString = message;
	}
	
	public String getString(){
		return resultString;
	}

	@Override
	public String read() {
		return null;
	}

}
