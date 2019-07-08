package org.util.nanolog;

import java.io.Writer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class LogWriter {

	protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public final String root;
	public final String name;
	public final boolean isDaily;
	private String writerName;
	private Writer writer;
	
	private LogWriter(String root, String name, String writerName, Writer writer, boolean isDaily) {
		this.root = getRoot(root);
		this.name = name;
		this.writerName = writerName;
		this.writer = writer;
		this.isDaily = isDaily;
	}
	
	public static final LogWriter getFileWriter(final String root, final String name) {
		String writerName = getWriterName(root, name);
		return new LogWriter(root, name, writerName, LoggingUtil.getFileWriter(writerName), false);
	}
	
	public static final LogWriter getDailyFileWriter(final String root, String name) {
		String writerName = getDatedWriterName(root, name);
		return new LogWriter(root, name, writerName, LoggingUtil.getFileWriter(writerName), true);
	}
	
	public String getName() {
		return name;
	}
	
	public final Writer getWriter() {
		if(isDaily) {
			final String writerName = getDatedWriterName(this.root, this.name);
			if(writerName.equals(this.writerName)) return this.writer;
			else {
				this.writerName = writerName;
				this.writer = LoggingUtil.getFileWriter(this.writerName);
				return this.writer;
			}
		}
		else return this.writer;
	}

	
	public boolean isIsDaily() {
		return isDaily;
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
