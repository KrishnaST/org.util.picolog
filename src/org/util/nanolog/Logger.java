package org.util.nanolog;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.StackWalker.Option;
import java.lang.StackWalker.StackFrame;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import org.util.nanolog.internals.BufferedDailyLogger;
import org.util.nanolog.internals.BufferedLogger;
import org.util.nanolog.internals.ConsoleLogger;
import org.util.nanolog.internals.InstantDailyLogger;
import org.util.nanolog.internals.InstantLogger;

public abstract class Logger implements AutoCloseable {

	private static final StackWalker		sw			= StackWalker.getInstance(Set.of(Option.RETAIN_CLASS_REFERENCE), 2);
	private static final DateTimeFormatter	timeFormat	= DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
	private static final Writer				writer		= new FileWriter(FileDescriptor.out);
	private static final ConsoleLogger		CONSOLE		= new ConsoleLogger();

	private static final boolean meta	= LoggingConfig.logMeta;
	
	private static final int	ERROR	= Level.ERROR.value;
	private static final int	WARN	= Level.WARN.value;
	private static final int	INFO	= Level.INFO.value;
	private static final int	DEBUG	= Level.DEBUG.value;
	private static final int	TRACE	= Level.TRACE.value;

	private final int	level	= LoggingConfig.level.value+1;
	protected boolean	status	= true;
	protected boolean	cstatus	= LoggingConfig.logConsole;

	protected abstract void write(String s);

	protected static final void writeConsole(String s) {
		try {
			writer.write(s);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void error(final String s) {
		if (meta) write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
		else write(ERROR, this, s);
	}
	
	public final void error(final String t, final String v) {
		if (meta) write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
		else write(ERROR, this, t, v);
	}
	
	public final void error(final Object o) {
		if (meta) write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
		else write(ERROR, this, String.valueOf(o));
	}
	
	public final void error(final Exception e) {
		if (meta) write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
		else write(ERROR, this, stackTraceToString(e));
	}
	
	
	
	
	
	public final void warn(final String s) {
		if (meta) write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
		else write(WARN, this, s);
	}
	
	public final void warn(final String t, final String v) {
		if (meta) write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
		else write(WARN, this, t, v);
	}
	
	public final void warn(final Object o) {
		if (meta) write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
		else write(WARN, this, String.valueOf(o));
	}
	
	public final void warn(final Exception e) {
		if (meta) write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
		else write(WARN, this, stackTraceToString(e));
	}
	
	
	
	
	public final void info(final String s) {
		if (meta) write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
		else write(INFO, this, s);
	}
	
	public final void info(final String t, final String v) {
		if (meta) write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
		else write(INFO, this, t, v);
	}
	
	public final void info(final Object o) {
		if (meta) write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
		else write(INFO, this, String.valueOf(o));
	}
	
	public final void info(final Exception e) {
		if (meta) write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
		else write(INFO, this, stackTraceToString(e));
	}
	
	
	
	
	public final void debug(final String s) {
		if (meta) write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
		else write(DEBUG, this, s);
	}
	
	public final void debug(final String t, final String v) {
		if (meta) write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
		else write(DEBUG, this, t, v);
	}
	
	public final void debug(final Object o) {
		if (meta) write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
		else write(DEBUG, this, String.valueOf(o));
	}
	
	public final void debug(final Exception e) {
		if (meta) write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
		else write(DEBUG, this, stackTraceToString(e));
	}
	
	
	
	
	
	public final void trace(final String s) {
		if (meta) write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
		else write(TRACE, this, s);
	}
	
	public final void trace(final String t, final String v) {
		if (meta) write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
		else write(TRACE, this, t, v);
	}
	
	public final void trace(final Object o) {
		if (meta) write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
		else write(TRACE, this, String.valueOf(o));
	}
	
	public final void trace(final Exception e) {
		if (meta) write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
		else write(TRACE, this, stackTraceToString(e));
	}

	private static final void write(final int level, final Logger logger, final StackFrame frame, final String s) {
		if(!logger.status || level < logger.level)
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(s).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final int level, final Logger logger, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(s).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final int level, final Logger logger, final StackFrame frame, final String t, final String v) {
		if(!logger.status || level < logger.level)
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(t).append(" : ").append(v).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final int level, final Logger logger, final String t, final String v) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(t).append(" : ").append(v).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public final void activate(boolean status) {
		this.status = status;
	}

	public final void activateConsole(boolean status) {
		this.cstatus = status;
	}

	public void close() throws Exception {}

	public static final Logger getLogger(LoggerType type, LogWriter logWriter) {
		if (type == LoggerType.INSTANT && !logWriter.isIsdaily()) return new InstantLogger(logWriter);
		else if (type == LoggerType.INSTANT && logWriter.isIsdaily()) return new InstantDailyLogger(logWriter);
		else if (type == LoggerType.BUFFERED && !logWriter.isIsdaily()) return new BufferedLogger(logWriter);
		else if (type == LoggerType.BUFFERED && logWriter.isIsdaily()) return new BufferedDailyLogger(logWriter);
		else return CONSOLE;
	}

	public static final Logger getConsole() {
		return CONSOLE;
	}
	
	private static final String stackTraceToString(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception ex) {}
		return "";
	}
}
