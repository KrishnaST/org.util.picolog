package org.util.nanolog.internals;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class BufferedLogger extends Logger {

	private final Writer writer;
	
	private final List<String> list = new ArrayList<>();
	
	public BufferedLogger(Writer writer) {
		this.writer = writer;
	}
	
	public BufferedLogger(LogWriter logWriter) {
		this.writer = logWriter.getWriter();
	}
	
	@Override
	public final void write(String s) {
		if(super.status) list.add(s);
	}
	
	@Override
	public final void close() {
		super.status = false;
		final StringBuilder sb = new StringBuilder(2000);
		list.forEach(s-> sb.append(s));
		final String s = sb.toString();
		try{
			synchronized (writer) {
				writer.write(s);
				writer.flush();
			}
		} catch (Exception e) {e.printStackTrace();}
		list.clear();
	}
	
}
