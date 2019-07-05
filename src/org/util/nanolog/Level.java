package org.util.nanolog;


public enum Level {
	
	ERROR(1), WARN(2), INFO(3), DEBUG(4), TRACE(5);
	
	public final int value;
	
	private Level(int value){
		this.value = value;
	}
	
}
