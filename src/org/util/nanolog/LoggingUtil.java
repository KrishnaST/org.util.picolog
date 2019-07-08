package org.util.nanolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Calendar;
import java.util.Date;

import org.util.nanolog.internals.NullWriter;

public final class LoggingUtil {

	

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

	

}
