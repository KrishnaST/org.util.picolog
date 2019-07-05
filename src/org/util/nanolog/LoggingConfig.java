package org.util.nanolog;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.util.nanolog.internals.ConfigLoader;
import org.util.nanolog.internals.ConsoleLogger;

public final class LoggingConfig {

	public static Level		level		= ConfigLoader.getLevel();
	public static boolean	logMeta		= ConfigLoader.logMetaData();
	public static boolean	logConsole	= ConfigLoader.logConsole();
	private static String	rootPath	= "";

	public static final String getRootPath() {
		return rootPath;
	}

	public static final void setRootPath(String rootPath) {
		if (rootPath == null || "".equals(rootPath)) return;
		if (rootPath.endsWith("/")) LoggingConfig.rootPath = rootPath;
		else LoggingConfig.rootPath = rootPath + "/";
	}

	public static final void setConfig(Level level, boolean logMeta, String rootPath) {
		LoggingConfig.level = level;
		LoggingConfig.logMeta = logMeta;
		if (rootPath == null || "".equals(rootPath)) return;
		if (rootPath.endsWith("/")) LoggingConfig.rootPath = rootPath;
		else LoggingConfig.rootPath = rootPath + "/";
	}

	public static final void enableConsole(boolean flag) {
		LoggingConfig.logConsole = flag;
	}

	public static final void scheduleDateChange(ScheduledExecutorService schedular) {
		try {
			schedular.scheduleAtFixedRate((ConsoleLogger) Logger.getConsole(), LoggingUtil.getEndOfDay(), 24 * 60 * 60, TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
