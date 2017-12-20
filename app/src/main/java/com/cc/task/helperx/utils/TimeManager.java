package com.cc.task.helperx.utils;



import com.cc.task.helperx.entity.TimeEntry;
import com.cc.task.helperx.task.TimeTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TimeManager {

	private static long currentTime = 0;

	private LinkedList<TimeEntry> caches;

	private List<TimeEntry> removeList;

	private ExecutorService threadPool;

	private boolean isStop = true;

	public TimeManager() {
		threadPool = Executors.newCachedThreadPool();
		caches = new LinkedList<>();
		removeList = new ArrayList<>();
	}

	/**
	 *  时间轮循
	 * @param task Runnable Task
	 * @param delayTime 延迟执行时间
	 * @param startTime 执行间隔时间
	 */
	public void schedule(TimeTask task, long delayTime, long startTime) {
		TimeEntry entry = new TimeEntry();
		entry.setTaskId(task.getId());
		entry.setTask(task);
		entry.setDelayTime(delayTime);
		entry.setStartTime(startTime);
		if(caches.contains(entry)){
			caches.remove(entry);
			caches.add(entry);
		}else{
			caches.add(entry);
		}
		if (isStop) {
			isStop = false;
			threadPool.submit(taskExecoutor);
		}
	}

	private void firstExecute(TimeTask task, long delayTime) {
		if (delayTime == 0) {
			threadPool.submit(task);
			task.setIsFirst(true);
		} else if (currentTime % delayTime == 0) {
			threadPool.submit(task);
			task.setIsFirst(true);
		}
	}

	public void cnacelAll() {
		if (caches != null && !caches.isEmpty()) {
			caches.clear();
		}
		if (removeList != null && !removeList.isEmpty()) {
			removeList.clear();
		}
	}

	private Runnable taskExecoutor = new Runnable() {
		public void run() {
			while (!isStop) {
				sleep();
				if (caches != null && !caches.isEmpty()) {
					if (!removeList.isEmpty()) {
						caches.removeAll(removeList);
						removeList.clear();
					}
					for (TimeEntry entry : caches) {
						long delayTime = entry.getDelayTime();
						long startTime = entry.getStartTime();
						TimeTask task = entry.getTask();
						if (!task.isStop()) {
							if (!task.isFirst()) {
								firstExecute(task, delayTime);
							} else if (currentTime % startTime == 0) {
								threadPool.submit(task);
							}
						} else {
							removeList.add(entry);
						}
					}
				}
			}
		};

		private void sleep() {
			try {
				Thread.sleep(1L);
				currentTime += 1;
				if (currentTime == Long.MAX_VALUE) {
					currentTime = 0;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};
}
