package org.util.nanolog.internals;

import java.io.IOException;
import java.io.Writer;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class InstantLogger extends Logger {

	private final Writer writer;
	
	public InstantLogger(Writer writer) {
		this.writer = writer;
	}
	
	public InstantLogger(LogWriter logWriter) {
		this.writer = logWriter.getWriter();
	}
	
	@Override
	public final void write(String s) {
		try {
			if(status) {
				writer.write(s);
				writer.flush();
			}
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
}
