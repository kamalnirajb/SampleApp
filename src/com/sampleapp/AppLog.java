package com.sampleapp;


public class AppLog {

	public static final int logType = 1; // 0 = none, 1= System.out.println, 2 = Toast
	public static final int logLevel = 2; // 0 = none, 1 = error, 2 = debug + error
	
	public static void d(String str)
	{
		if(logLevel >= 2 )
			show(str);
	}
	public static void e(String str)
	{
		if(logLevel >= 1 )
			show(str);
	}
	
	
	private static void show(String str)
	{
		switch(logType)
		{
			case 0:
			break;
			case 1:
					System.out.println(str);
			break;
			case 2:
					if(AppActivity.top_activity != null)
					{
						try{
							AppActivity.top_activity.showToast(str);
						} catch (Exception e) {}
					}
			break;
		}
	}

}
