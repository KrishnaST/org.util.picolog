package org.util.nanolog;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.util.nanolog.internals.NullWriter;

public final class LogWriter {

	protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public final String root;
	public final String name;
	public final boolean isDaily;
	private String writerName;
	private Writer writer;
	
	private LogWriter(String root, String name, boolean isDaily) {
		this.root = getRoot(root);
		this.name = name;
		this.isDaily = isDaily;
		this.writerName = isDaily ? getDatedWriterName(root, name) : getWriterName(root, name);
		this.writer = getFileWriter(writerName);
	}
	
	public final String getName() {
		return name;
	}
	
	public final boolean isIsDaily() {
		return isDaily;
	}
	
	
	public static final LogWriter getLogWriter(final String root, final String name) {
		return new LogWriter(root, name, false);
	}
	
	public static final LogWriter getDailyLogWriter(final String root, String name) {
		return new LogWriter(root, name, true);
	}
	
	
	public final Writer getWriter() {
		if(isDaily) {
			final String writerName = getDatedWriterName(this.root, this.name);
			if(writerName.equals(this.writerName)) return this.writer;
			else {
				this.writerName = writerName;
				this.writer = getFileWriter(this.writerName);
				return this.writer;
			}
		}
		else return this.writer;
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
	
	
	private static final String getRoot(final String root) {
		if (root == null || "".equals(root)) return "";
		else if (root.endsWith("/")) {
			if(root.length() > 1) return root;
			else return "";
		}
		else return root + "/";
	}
	
	public static final String getWriterName(final String root, final String name) {
		return "logs/" + root + name + ".log";
	}

	public static final String getDatedWriterName(final String root, final String name) {
		return "logs/" + root + LocalDate.now().format(dateFormat) + "/" + name + ".log";
	}
}
