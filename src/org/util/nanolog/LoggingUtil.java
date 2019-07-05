package org.util.nanolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.util.nanolog.internals.NullWriter;

public final class LoggingUtil {

	protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	public static final String stackTraceToString(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.toString();
		} catch (Exception ex) {}
		return "";
	}

	public static final long getEndOfDay() {
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return (cal.getTime().getTime() - date.getTime()) / 1000;
	}

	public static final Writer getFileWriter(String name) {
		try {
			File file = new File(name);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			return new FileWriter(file, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NullWriter.getInstance();
	}

	public static final Writer getDatedWriter(String name) {
		if (name == null) return NullWriter.getInstance();
		return getFileWriter("logs/" + LoggingConfig.getRootPath() + LocalDate.now().format(dateFormat) + "/" + name + ".log");
	}

	public static final Writer getWriter(String name) {
		if (name == null) return NullWriter.getInstance();
		return getFileWriter("logs/" + LoggingConfig.getRootPath() + name + ".log");
	}

	public static final String getWriterName(String name) {
		return "logs/" + LoggingConfig.getRootPath() + name + ".log";
	}

	public static final String getDatedWriterName(String name) {
		return "logs/" + LoggingConfig.getRootPath() + LocalDate.now().format(dateFormat) + "/" + name + ".log";
	}
	
	public static final ScheduledExecutorService getScheduledExecutorService() {
		return new ScheduledThreadPoolExecutor(1);
	}

}
