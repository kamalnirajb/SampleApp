package com.sampleapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class AppGPS {

	public static double [] getLatLon(Context serviceOrActivityContext, boolean exact) throws Exception
	{
		Context appContext = serviceOrActivityContext.getApplicationContext();
		
		final LocationManager mlocManager = 
				(LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
		
		if(Looper.myLooper()==null)
		{
			Looper.prepare();
		}
		
		final Handler handler = new Handler();
		
		
		final double [] latlon = new double[3];
		
		latlon[0]=-1000;
		latlon[1]=-1000;
		latlon[2]=-4000000;
		
		
		LocationListener mlocListener  = new LocationListener() {
			
			public void onLocationChanged(Location loc)  
		    {  
				latlon[0]=loc.getLatitude();  
				latlon[1]=loc.getLongitude();
				latlon[2]=loc.getAltitude();
		    	
				mlocManager.removeUpdates(this);
				handler.getLooper().quit();
		    }
			public void onProviderDisabled(String provider)  
		    {  
		        //print "Currently GPS is Disabled";  
				mlocManager.removeUpdates(this);
				handler.getLooper().quit();
		    }  
		    public void onProviderEnabled(String provider)  
		    {  
		        //print "GPS got Enabled";  
		    }  
		    public void onStatusChanged(String provider, int status, Bundle extras)  
		    {  
		    }
		};  
		
		if(mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener, handler.getLooper());
			
			Looper.loop();

			if(latlon[0]>-1000 && latlon[1]>-1000){}
			else
			{
				AppLog.e("Unable fetch GPS data thru GPS_PROVIDER.");
				throw new Exception("Unable fetch GPS data thru GPS_PROVIDER.");
			}
			//mlocManager.removeUpdates(mlocListener);
			//Looper.myLooper().quit();
			return latlon;
		}
		
		else if(!exact && mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			mlocManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener, handler.getLooper());
			
			Looper.loop();

			if(latlon[0]>-1000 && latlon[1]>-1000){}
			else
			{
				AppLog.e("Unable fetch GPS data thru NETWORK_PROVIDER.");
				throw new Exception("Unable fetch GPS data thru NETWORK_PROVIDER.");
			}
			
			return latlon;
		}
		else
		{
			AppLog.e("GPS channels are off.");
			throw new Exception("GPS channels are off.");
		}
	}
	
}
