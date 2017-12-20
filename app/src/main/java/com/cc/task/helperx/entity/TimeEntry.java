package com.cc.task.helperx.entity;




import com.cc.task.helperx.task.TimeTask;

import java.io.Serializable;

public class TimeEntry implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2925670900274521257L;


    private TimeTask task;
    private long startTime;
    private long delayTime;

    private String taskId;

    public TimeEntry() {
        super();
    }

    public TimeEntry(TimeTask task, long startTime, long delayTime) {
        super();
        this.task = task;
        this.startTime = startTime;
        this.delayTime = delayTime;
    }

    public TimeTask getTask() {
        return task;
    }

    public void setTask(TimeTask task) {
        this.task = task;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public String toString() {
		return "TimeEntry [task=" + task + ", startTime=" + startTime + ", delayTime=" + delayTime + ", taskId="
				+ taskId + "]";
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TimeEntry timeEntry = (TimeEntry) o;

        return taskId.equals(timeEntry.taskId);

    }

    @Override
    public int hashCode() {
        return taskId.hashCode();
    }
}
