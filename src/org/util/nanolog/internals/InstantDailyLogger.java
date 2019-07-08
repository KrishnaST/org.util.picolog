package org.util.nanolog.internals;

import java.io.IOException;
import java.io.Writer;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class InstantDailyLogger extends Logger implements DailyLogger {

	protected final String	name;
	private Writer writer;
	
	public InstantDailyLogger(LogWriter logWriter) {
		this.name = logWriter.getName();
		this.writer = logWriter.getWriter();
		daily_loggers.put(name, this);
	}
	
	@Override
	public final void write(String s) {
		try {
			writer.write(s);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public final void close() {
		this.status = false;
		try {
			writer.close();
		} catch (Exception e) {}
	}
	
	@Override
	public final void changeDate() {
		
		//this.writer = LoggingUtil.getDatedWriter(this.name);
	}
}
