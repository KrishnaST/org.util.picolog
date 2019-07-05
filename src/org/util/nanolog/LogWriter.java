package org.util.nanolog;

import java.io.Writer;

public class LogWriter {

	private final String name;
	private final boolean isdaily;
	private String writerName;
	private Writer writer;
	
	private LogWriter(String name, String writerName, Writer writer, boolean isdaily) {
		this.name = name;
		this.writerName = writerName;
		this.writer = writer;
		this.isdaily = isdaily;
	}
	
	public static final LogWriter getFileWriter(String name) {
		String writerName = LoggingUtil.getWriterName(name);
		return new LogWriter(name, writerName, LoggingUtil.getFileWriter(writerName), false);
	}
	
	public static final LogWriter getDailyFileWriter(String name) {
		String writerName = LoggingUtil.getDatedWriterName(name);
		return new LogWriter(name, writerName, LoggingUtil.getFileWriter(writerName), true);
	}
	
	public String getName() {
		return name;
	}
	
	public Writer getWriter() {
		if(isdaily) {
			final String writerName = LoggingUtil.getDatedWriterName(this.name);
			if(writerName.equals(this.writerName)) return this.writer;
			else {
				this.writerName = writerName;
				this.writer = LoggingUtil.getFileWriter(this.writerName);
				return this.writer;
			}
		}
		else return this.writer;
	}

	
	public boolean isIsdaily() {
		return isdaily;
	}
	
}
