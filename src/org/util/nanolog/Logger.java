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
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.util.nanolog.internals.BufferedDailyLogger;
import org.util.nanolog.internals.BufferedLogger;
import org.util.nanolog.internals.ConfigLoader;
import org.util.nanolog.internals.ConsoleLogger;
import org.util.nanolog.internals.InstantDailyLogger;
import org.util.nanolog.internals.InstantLogger;

public abstract class Logger implements AutoCloseable {

	private static final StackWalker       sw         = StackWalker.getInstance(Set.of(Option.RETAIN_CLASS_REFERENCE), 2);
	private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");
	private static final Writer            writer     = new FileWriter(FileDescriptor.out);

	private static boolean logc = ConfigLoader.logConsole();
	private static int     lvl  = ConfigLoader.getLevel().value;

	private static final int ERROR = Level.ERROR.value;
	private static final int WARN  = Level.WARN.value;
	private static final int INFO  = Level.INFO.value;
	private static final int DEBUG = Level.DEBUG.value;

	private final int level  = lvl;
	protected boolean status = true;

	public static final Logger CONSOLE = new ConsoleLogger();
	
	protected abstract void write(String s);

	protected static final void writeConsole(String s) {
		try {
			writer.write(s);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public final void log(final String s) {
		if (status) write(this, s);
	}
	
	public final void logtrace(final String s) {
		if (status && level > DEBUG) write(this, s);
	}
	
	public final void errorback(final String s) {
		write(this, sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public final void error(final Supplier<?> s) {
		write(this, sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public final void error(final String t, final Supplier<?> v) {
		write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public final void error(final String s) {
		if (status) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public final void error(final String t, final String v) {
		if (status) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public final void error(final Object o) {
		if (status) write(this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public final void error(final Exception e) {
		if (status) write(this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	public final void warnback(final String s) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public final void warn(final Supplier<?> s) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public final void warn(final String t, final Supplier<?> v) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public final void warn(final String s) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public final void warn(final String t, final String v) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public final void warn(final Object o) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public final void warn(final Exception e) {
		if (status && level > ERROR) write(this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	public final void infoback(final String s) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public final void info(final Supplier<?> s) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public final void info(final String t, final Supplier<?> v) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public final void info(final String s) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public final void info(final String t, final String v) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public final void info(final Object o) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public final void info(final Exception e) {
		if (status && level > WARN) write(this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	public final void debugback(final String s) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public final void debug(final Supplier<?> s) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public final void debug(final String t, final Supplier<?> v) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public final void debug(final String s) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public final void debug(final String t, final String v) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public final void debug(final Object o) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public final void debug(final Exception e) {
		if (status && level > INFO) write(this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	public final void traceback(final String s) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public final void trace(final Supplier<?> s) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public final void trace(final String t, final Supplier<?> v) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public final void trace(final String s) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public final void trace(final String t, final String v) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public final void trace(final Object o) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public final void trace(final Exception e) {
		if (status && level > DEBUG) write(this, sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	public static final void consoleback(final String s) {
		write(sw.walk(f -> f.skip(2).findFirst().get()), s);
	}

	public static final void console(final Supplier<?> s) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), s.get().toString());
	}

	public static final void console(final String t, final Supplier<?> v) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), t, v.get().toString());
	}

	public static final void console(final String s) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), s);
	}

	public static final void console(final String t, final String v) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), t, v);
	}

	public static final void console(final Object o) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), String.valueOf(o));
	}

	public static final void console(final Exception e) {
		write(sw.walk(f -> f.skip(1).findFirst().get()), stackTraceToString(e));
	}

	private static final void write(final StackFrame frame, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(s).append("\r\n");
			String ssb = sb.toString();
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final StackFrame frame, final String t, final String v) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(t).append(" : ").append(v).append("\r\n");
			String ssb = sb.toString();
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final Logger logger, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(s).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final void write(final Logger logger, final StackFrame frame, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(s).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final Logger logger, final StackFrame frame, final String t, final String v) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(frame.getDeclaringClass().getSimpleName()).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(t).append(" : ").append(v).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public final void setStatus(boolean status) {
		this.status = status;
	}

	public static final void setConsoleStatus(boolean status) {
		Logger.logc = status;
	}

	public static final boolean getConsoleStatus() {
		return Logger.logc;
	}

	public void close() throws Exception {}

	public static final Logger getLogger(LoggerType type, LogWriter logWriter) {
		if (type == LoggerType.INSTANT && !logWriter.isIsDaily()) return new InstantLogger(logWriter);
		else if (type == LoggerType.INSTANT && logWriter.isIsDaily()) return new InstantDailyLogger(logWriter);
		else if (type == LoggerType.BUFFERED && !logWriter.isIsDaily()) return new BufferedLogger(logWriter);
		else if (type == LoggerType.BUFFERED && logWriter.isIsDaily()) return new BufferedDailyLogger(logWriter);
		else return CONSOLE;
	}

	private static final String stackTraceToString(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter  pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception ex) {}
		return "";
	}

	public static final void scheduleDateChange(final ScheduledExecutorService schedular) {
		try {
			schedular.scheduleAtFixedRate((ConsoleLogger) Logger.CONSOLE, getEndOfDay(), 24 * 60 * 60, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final long getEndOfDay() {
		Date     date = new Date();
		Calendar cal  = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return (cal.getTime().getTime() - date.getTime()) / 1000;
	}

	public static void main(String[] args) {
		try {
			Logger.CONSOLE.info("hi");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
