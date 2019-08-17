package org.util.nanolog.internals;

import org.util.nanolog.Logger;

public final class ConsoleLogger extends Logger implements Runnable {

	public boolean scheduled = false;
	
	@Override
	protected final void write(String s) {}

	@Override
	public final void run() {
		try {
			synchronized (DailyLogger.daily_loggers) {
				DailyLogger.daily_loggers.forEach((name, logger) -> logger.changeDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public final boolean isScheduled() {
		return scheduled;
	}

}
