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
	private LocalDate writerDate;
	private Writer writer;
	
	public LogWriter(String root, String name, boolean isDaily) {
		this.root = getRoot(root);
		this.name = name;
		this.isDaily = isDaily;
	}
	
	public final String getName() {
		return name;
	}
	
	public final boolean isIsDaily() {
		return isDaily;
	}
	
	
	public final Writer getWriter() {
		if(isDaily) {
			this.writerDate = LocalDate.now();
			final String writerName = getDatedWriterName(this.root, this.name, writerDate);
			if(writerName.equals(this.writerName)) return this.writer;
			else {
				this.writerName = writerName;
				this.writer = getFileWriter(this.writerName);
				return this.writer;
			}
		}
		else {
			if(this.writer == null) {
				this.writerName = getWriterName(this.root, this.name);
				return this.writer = getFileWriter(this.writerName);
			}
			return this.writer;
		}
	}

	public final Writer changeDate() {
		Logger.console("*****************changing date.");
		if(isDaily) {
			final String writerName = getDatedWriterName(this.root, this.name, writerDate.plusDays(1));
			this.writerName = writerName;
			final Writer oldWriter = this.writer;
			this.writer = getFileWriter(this.writerName);
			try(oldWriter){} catch (Exception e) {}
			return this.writer;
			
		}
		return this.writer;
	}
	
	private static final Writer getFileWriter(String name) {
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
	
	private static final String getWriterName(final String root, final String name) {
		return "logs/" + root + name + ".log";
	}

	private static final String getDatedWriterName(final String root, final String name, final LocalDate date) {
		return "logs/" + root + date.format(dateFormat) + "/" + name + ".log";
	}
}
