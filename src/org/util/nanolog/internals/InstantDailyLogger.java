package org.util.nanolog.internals;

import java.io.IOException;
import java.io.Writer;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class InstantDailyLogger extends Logger implements DailyLogger {

	private Writer          writer;
	private final LogWriter logWriter;

	public InstantDailyLogger(LogWriter logWriter) {
		this.logWriter = logWriter;
		this.writer    = logWriter.getWriter();
		daily_loggers.put(logWriter.getName(), this);
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
		System.out.println("changing log date");
		this.writer = logWriter.changeDate();
		System.out.println("changing log date changed.");
	}
}
