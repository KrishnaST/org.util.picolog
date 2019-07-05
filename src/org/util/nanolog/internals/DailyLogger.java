package org.util.nanolog.internals;

import java.util.concurrent.ConcurrentHashMap;

public interface DailyLogger {

	static final ConcurrentHashMap<String, DailyLogger>	daily_loggers	= new ConcurrentHashMap<>();
	
	public void changeDate();
}
