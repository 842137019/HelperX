package com.cc.task.helperx.task;


import com.cc.task.helperx.utils.RandomName;

public abstract class TimeTask implements Runnable {

	private boolean isFirst;

	private boolean isStop;
	
	private String id;
	
	public TimeTask(){
		id = RandomName.getRandomName(20);
	}

	public void cancel() {
		setIsStop(true);
	}

	public void reset() {
		setIsStop(false);
		setIsFirst(false);
	}

	public void setIsFirst(boolean isTrue) {
		this.isFirst = isTrue;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setIsStop(boolean isStop) {
		this.isStop = isStop;
	}

	public boolean isStop() {
		return isStop;
	}
	
	public String getId() {
		return id;
	}

}
