package com.sampleapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.io.UnsupportedEncodingException;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.util.Base64;
//import android.util.Log;


public class AppHttpClient {

	private String url;
	private DefaultHttpClient httpClient;
	private HttpContext localContext;
    private String ret;
    private int status;

	private HttpResponse response = null;
	private HttpPost httpPost = null;
	private HttpGet httpGet = null;

    public AppHttpClient(String url){
    	
    	this.url = url;
    	
        HttpParams params = new BasicHttpParams();

        // Setting up parameters
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");
        params.setBooleanParameter("http.protocol.expect-continue", false);

        // Setting timeout
        HttpConnectionParams.setConnectionTimeout(params, 10000);
        HttpConnectionParams.setSoTimeout(params, 10000);
        
        // Registering schemes for both HTTP and HTTPS
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        final SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
        sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        registry.register(new Scheme("https", sslSocketFactory, 443));

        // Creating thread safe client connection manager
        ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(params, registry);

        
        httpClient = new DefaultHttpClient(manager, params);
        
        
        localContext = new BasicHttpContext();    
    }

    private String userName = "";
    private String password = "";
    
    public void setAuthBasic(String userName, String password)
    {
    	if (userName == null || password == null)
            throw new IllegalArgumentException("Auth Basic invalid param: userName=" + userName
                    + ", password=" + password);
    	
    	this.userName = userName;
    	this.password = password;
    }
    
    private String postData = "";
    
    public void addField(String name, String value) throws Exception
    {
    	if (name == null || value == null)
            throw new IllegalArgumentException("Field invalid: name=" + name
                    + ", value=" + value);
    	
	   	String param = null;
	   	try{
	   		param = name +"="+ URLEncoder.encode(value,"UTF-8");
	   	} catch (Exception e) {
	   		AppLog.e("Unable to encode data for send.");
			throw new Exception("Unable to encode data for send.");
	   	}
	   	
	   	String sep = postData.equals("")?"":"&";
    	postData += sep + param;
	
    }
    
    public void addFile(String name, String fileFullPath) throws Exception
    {
    	String fileName;
    	
    	try{
			fileName = fileFullPath.split("/")[fileFullPath.split("/").length-1];
		} catch(Exception e) {
			AppLog.e("Invalid file path.");
			throw new Exception("Invalid file path.");
		}
    	
    	FileInputStream fis = null;
    	
    	try{
			fis = new FileInputStream(fileFullPath);
		} catch (Exception e) {
			AppLog.e("File not found.");
			throw new Exception("File not found.");
		}
    	
    	File fl = new File(fileFullPath);
		long fileSize = fl.length();
    	
		ByteBuffer content = ByteBuffer.allocate( (int)fileSize ); // this is array based
		
    	int fileChunkSize = 1024 * 65;
        byte[] bytes = new byte[fileChunkSize];
        int size;
        while ((size = fis.read(bytes,0,fileChunkSize)) !=-1) {
        	content.put(bytes, 0, size);
        }
        fis.close();
        
        String fileContentEncoded = Base64.encodeToString(content.array(),Base64.DEFAULT);
        
        //name = "base64_file_content"+name;
    	
        addField("file_name_"+name,fileName);
        addField("file_content_base64_encoded_"+name,fileContentEncoded);

    }
    
    public Boolean sendPost() {
        return sendPost(postData, null);
    }

    
    public Boolean sendPost(String data) {
        return sendPost(data, null);
    }

    public Boolean sendJSONPost(JSONObject data) {
        return sendPost(data.toString(), "application/json");
    }

    private Boolean sendPost(String data, String contentType) {
        ret = null;

        httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109);

        httpPost = new HttpPost(url);
        response = null;

        StringEntity tmp = null;        

        AppLog.d("Setting httpPost headers");

        httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; U; Linux " + "i686; en-US; rv:1.8.1.6) Gecko/20061201 Firefox/2.0.0.6 (Ubuntu-feisty)");
        httpPost.setHeader("Accept", "text/html,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
        
        if(!userName.equals(""))
        {
        	httpPost.addHeader("Authorization", "Basic " + Base64.encodeToString((userName+":"+password).getBytes(),Base64.NO_WRAP));
        }

        if (contentType != null) {
            httpPost.setHeader("Content-Type", contentType);
        } else {
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        try {
            tmp = new StringEntity(data,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            AppLog.e("HttpUtils : UnsupportedEncodingException : "+e);
        }

        httpPost.setEntity(tmp);

        AppLog.d(url + "?" + data);

        try {
            response = httpClient.execute(httpPost,localContext);

            if (response != null) {
            	status = response.getStatusLine().getStatusCode();
                ret = EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            AppLog.e("HttpUtils: " + e);
        }

        AppLog.d("Returning value:" + ret);

        return (status == 200);
    }

    public Boolean sendGet() {
        httpGet = new HttpGet(url);
        
        if(!userName.equals(""))
        {
        	httpGet.addHeader("Authorization", "Basic " + Base64.encodeToString((userName+":"+password).getBytes(),Base64.NO_WRAP));
        }

        try {
            response = httpClient.execute(httpGet);  
        } catch (Exception e) {
            AppLog.e(e.getMessage());
        }
        // we assume that the response body contains the error message  
        try {
        	if (response != null) {
            	status = response.getStatusLine().getStatusCode();
                ret = EntityUtils.toString(response.getEntity());
            }  
        } catch (IOException e) {
            AppLog.e(e.getMessage());
        }

        return (status ==200);
    }
    
    public int getResponseCode() {
        return status;
    }

    public String getResponseBody() {
        return ret;
    }
	
}
