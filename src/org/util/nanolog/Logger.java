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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.util.nanolog.internals.BufferedDailyLogger;
import org.util.nanolog.internals.BufferedLogger;
import org.util.nanolog.internals.ConfigLoader;
import org.util.nanolog.internals.ConsoleLogger;
import org.util.nanolog.internals.InstantDailyLogger;
import org.util.nanolog.internals.InstantLogger;

public abstract class Logger implements AutoCloseable {

	private static final StackWalker		sw			= StackWalker.getInstance(Set.of(Option.RETAIN_CLASS_REFERENCE), 2);
	private static final DateTimeFormatter	timeFormat	= DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
	private static final Writer				writer		= new FileWriter(FileDescriptor.out);
	private static final ConsoleLogger		CONSOLE		= new ConsoleLogger();

	private static final int	ERROR	= Level.ERROR.value;
	private static final int	WARN	= Level.WARN.value;
	private static final int	INFO	= Level.INFO.value;
	private static final int	DEBUG	= Level.DEBUG.value;
	private static final int	TRACE	= Level.TRACE.value;

	private final int	level	= ConfigLoader.getLevel().value;
	protected boolean	status	= true;

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
		write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}
	
	public final void error(final String t, final String v) {
		write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}
	
	public final void error(final Object o) {
		write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}
	
	public final void error(final Exception e) {
		write(ERROR, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}
	
	
	
	
	
	public final void warn(final String s) {
		write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}
	
	public final void warn(final String t, final String v) {
		write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}
	
	public final void warn(final Object o) {
		write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}
	
	public final void warn(final Exception e) {
		write(WARN, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}
	
	
	
	
	public final void info(final String s) {
		write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}
	
	public final void info(final String t, final String v) {
		write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}
	
	public final void info(final Object o) {
		write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}
	
	public final void info(final Exception e) {
		write(INFO, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}
	
	
	
	
	public final void debug(final String s) {
		write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}
	
	public final void debug(final String t, final String v) {
		write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}
	
	public final void debug(final Object o) {
		write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}
	
	public final void debug(final Exception e) {
		write(DEBUG, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}
	
	
	
	
	
	public final void trace(final String s) {
		write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}
	
	public final void trace(final String t, final String v) {
		write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}
	
	public final void trace(final Object o) {
		write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}
	
	public final void trace(final Exception e) {
		write(TRACE, this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
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

	
	public final void activate(boolean status) {
		this.status = status;
	}


	public void close() throws Exception {}

	public static final Logger getLogger(LoggerType type, LogWriter logWriter) {
		if (type == LoggerType.INSTANT && !logWriter.isIsDaily()) return new InstantLogger(logWriter);
		else if (type == LoggerType.INSTANT && logWriter.isIsDaily()) return new InstantDailyLogger(logWriter);
		else if (type == LoggerType.BUFFERED && !logWriter.isIsDaily()) return new BufferedLogger(logWriter);
		else if (type == LoggerType.BUFFERED && logWriter.isIsDaily()) return new BufferedDailyLogger(logWriter);
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
	
	public static final void scheduleDateChange(ScheduledExecutorService schedular) {
		try {
			schedular.scheduleAtFixedRate((ConsoleLogger) Logger.getConsole(), LoggingUtil.getEndOfDay(), 24 * 60 * 60, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
