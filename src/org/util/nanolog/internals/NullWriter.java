package org.util.nanolog.internals;

import java.io.IOException;
import java.io.Writer;

public final class NullWriter extends Writer {

	private static final NullWriter NULL_WRITER = new NullWriter();
	 
	private NullWriter() {
	}
	
	public static final Writer getInstance() {
		return NULL_WRITER;
	}
	
	@Override
	public final void write(char[] cbuf, int off, int len) throws IOException {
		
	}
	
	@Override
	public final void write(String str) throws IOException {
		
    }

	@Override
	public final void flush() throws IOException {
		
	}

	@Override
	public final void close() throws IOException {
		
	}

}
