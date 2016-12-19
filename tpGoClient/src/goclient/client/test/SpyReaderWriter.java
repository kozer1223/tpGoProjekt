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
	
	@Override
	public String read() {
		String str = resultString;
		resultString = null;
		return str;
	}

	@Override
	public boolean isReadReady() {
		return (resultString == null);
	}

}
