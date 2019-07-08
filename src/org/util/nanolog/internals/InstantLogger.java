package org.util.nanolog.internals;

import java.io.IOException;
import java.io.Writer;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class InstantLogger extends Logger {

	private final Writer writer;
	
	public InstantLogger(LogWriter logWriter) {
		this.writer = logWriter.getWriter();
	}
	
	@Override
	public final void write(final String s) {
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
