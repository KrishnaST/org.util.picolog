package org.util.nanolog;

import java.io.FileDescriptor;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.util.nanolog.internals.BufferedDailyLogger;
import org.util.nanolog.internals.BufferedLogger;
import org.util.nanolog.internals.ConfigLoader;
import org.util.nanolog.internals.ConsoleLogger;
import org.util.nanolog.internals.EncryptionProvider;
import org.util.nanolog.internals.InstantDailyLogger;
import org.util.nanolog.internals.InstantLogger;

public abstract class Logger implements AutoCloseable {

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

	public final void encrypt(final byte[] b) {
		write(this, Thread.currentThread().getStackTrace()[2], EncryptionProvider.encrypt(b));
	}

	public final void encrypt(final String t, final byte[] b) {
		write(this, Thread.currentThread().getStackTrace()[2], t, EncryptionProvider.encrypt(b));
	}
	
	public final void errorback(final String s) {
		write(this, Thread.currentThread().getStackTrace()[3], s);
	}

	public final void error(final Supplier<?> s) {
		write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public final void error(final String t, final Supplier<?> v) {
		write(this, Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public final void error(final String s) {
		if (status) write(this, Thread.currentThread().getStackTrace()[2], s);
	}

	public final void error(final String t, final String v) {
		if (status) write(this, Thread.currentThread().getStackTrace()[2], t, v);
	}

	public final void error(final Object o) {
		if (status) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public final void error(final Exception e) {
		if (status) write(this, Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}

	public final void warn(final Supplier<?> s) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public final void warn(final String t, final Supplier<?> v) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public final void warn(final String s) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], s);
	}

	public final void warn(final String t, final String v) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], t, v);
	}

	public final void warn(final Object o) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public final void warn(final Exception e) {
		if (status && level > ERROR) write(this, Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}

	public final void info(final Supplier<?> s) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public final void info(final String t, final Supplier<?> v) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public final void info(final String s) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], s);
	}

	public final void info(final String t, final String v) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], t, v);
	}

	public final void info(final Object o) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public final void info(final Exception e) {
		if (status && level > WARN) write(this, Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}

	public final void debug(final Supplier<?> s) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public final void debug(final String t, final Supplier<?> v) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public final void debug(final String s) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], s);
	}

	public final void debug(final String t, final String v) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], t, v);
	}

	public final void debug(final Object o) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public final void debug(final Exception e) {
		if (status && level > INFO) write(this, Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}

	public final void traceback(final String s) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[3], s);
	}

	public final void trace(final Supplier<?> s) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public final void trace(final String t, final Supplier<?> v) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public final void trace(final String s) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], s);
	}

	public final void trace(final String t, final String v) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], t, v);
	}

	public final void trace(final Object o) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public final void trace(final Exception e) {
		if (status && level > DEBUG) write(this, Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}
	
	public static final void consoleback(final String s) {
		write(Thread.currentThread().getStackTrace()[3], s);
	}

	public static final void console(final Supplier<?> s) {
		write(Thread.currentThread().getStackTrace()[2], String.valueOf(s.get()));
	}

	public static final void console(final String t, final Supplier<?> v) {
		write(Thread.currentThread().getStackTrace()[2], t, String.valueOf(v.get()));
	}

	public static final void console(final String s) {
		write(Thread.currentThread().getStackTrace()[2], s);
	}

	public static final void console(final String t, final String v) {
		write(Thread.currentThread().getStackTrace()[2], t, v);
	}

	public static final void console(final Object o) {
		write(Thread.currentThread().getStackTrace()[2], String.valueOf(o));
	}

	public static final void console(final Exception e) {
		write(Thread.currentThread().getStackTrace()[2], stackTraceToString(e));
	}
	
	

	private static final void write(final StackTraceElement frame, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			String cName = frame.getClassName();
			String className = cName.substring(cName.lastIndexOf(".") + 1);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(className).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(s).append("\r\n");
			String ssb = sb.toString();
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void write(final StackTraceElement frame, final String t, final String v) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			String cName = frame.getClassName();
			String className = cName.substring(cName.lastIndexOf(".") + 1);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append(className).append(".");
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

	private static final void write(final Logger logger, final StackTraceElement frame, final String s) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			String cName = frame.getClassName();
			String className = cName.substring(cName.lastIndexOf(".") + 1);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append("[").append(Thread.currentThread().getId()).append("] ").append(className).append(".");
			sb.append(frame.getMethodName()).append("(").append(frame.getLineNumber()).append(") ").append(s).append("\r\n");
			String ssb = sb.toString();
			logger.write(ssb);
			if (logc) writeConsole(ssb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final void write(final Logger logger, final StackTraceElement frame, final String t, final String v) {
		try {
			final StringBuilder sb = new StringBuilder(50);
			String cName = frame.getClassName();
			String className = cName.substring(cName.lastIndexOf(".") + 1);
			sb.append("[").append(LocalDateTime.now().format(timeFormat)).append("] ").append("[").append(Thread.currentThread().getId()).append("] ").append(className).append(".");
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

	public static final ScheduledFuture<?> scheduleDateChange(final ScheduledExecutorService schedular) {
		try {
			final ConsoleLogger CONSOLE = (ConsoleLogger) Logger.CONSOLE;
			if (CONSOLE.scheduled) return null;
			CONSOLE.scheduled = true;
			return schedular.scheduleAtFixedRate(CONSOLE, getEndOfDay(), 24 * 60 * 60, TimeUnit.SECONDS);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static final long getEndOfDay() {
		final Date     date = new Date();
		final Calendar cal  = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return (cal.getTime().getTime() - date.getTime()) / 1000;
	}

}
