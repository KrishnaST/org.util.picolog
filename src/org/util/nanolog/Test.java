package org.util.nanolog;

import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Test {

	public static void main(String[] args) {
		Logger.scheduleDateChange(new ScheduledThreadPoolExecutor(1));
		LogWriter logWriter = new LogWriter("SCB", "issuer", true);
		Logger    logger    = Logger.getLogger(LoggerType.INSTANT, logWriter);
		for (int i = 0; i < 100; i++) {
			logger.info("hi");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			logger.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
