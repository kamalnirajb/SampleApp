package com.sampleapp;


public class AppSettings {

    public static String BASE_URL(String cityName){
    	return  "http://api.openweathermap.org/data/2.5/forecast/daily?q=" + cityName +"&cnt=14&APPID=ae17f8b4ce323deda6f0a2c1d52e4d87";
    }
}
