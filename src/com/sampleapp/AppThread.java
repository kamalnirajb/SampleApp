package com.sampleapp;


public class AppThread extends Thread {

	public final AppThread this_thread = this;
	private static int currentThreadCount =0;
	
	public AppThread(Runnable runnable, AppActivity ownerActivity)
	{
		super(runnable);
		String threadName = ownerActivity.getClass().getName() + "__" +
							String.valueOf(AppThread.currentThreadCount);
		AppThread.currentThreadCount++;
		this.setName(threadName);
	}
	public static boolean sleepWithInteruptCheck(int mSec)
	{
		try {sleep(mSec);} catch (Exception e) {/* InterruptedException */ return false; }
		return true;
	}
}
