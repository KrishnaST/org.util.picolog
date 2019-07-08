package org.util.nanolog.internals;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.util.nanolog.LogWriter;
import org.util.nanolog.Logger;

public final class BufferedDailyLogger extends Logger {
	
	protected final String	name;
	private Writer writer;
	
	private final List<String> list = new ArrayList<>();
	
	public BufferedDailyLogger(LogWriter logWriter) {
		this.name = logWriter.getName();
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
