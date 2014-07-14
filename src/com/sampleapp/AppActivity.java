package com.sampleapp;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;






import android.app.Activity;
//import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
//import android.content.DialogInterface;
import android.content.Intent;
//import android.content.res.Resources.Theme;
//import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
//import android.util.AttributeSet;
import android.util.TypedValue;
//import android.view.DragEvent;
import android.view.Gravity;
//import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.Window;
//import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.SslErrorHandler;
import android.net.http.SslError;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class AppActivity extends ActionBarActivity {

	public static AppActivity top_activity = null;
	public static String this_package_name = "";
	public final AppActivity this_activity = this;
	public static Context this_app = null;
	////////////////////
	// In the range of 1000 - 1999 //
	protected static final int APP_ACTIVITY_INTENT_REQ_CODE_IMAGE_PICK = 1001;
	
	////////////////////
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppActivity.top_activity = this;
        AppActivity.this_app = this.getApplication();
        AppActivity.this_package_name = this.getPackageName();
    }
	
	
	@Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        AppActivity.top_activity = this;
	}
	
	@Override
	protected void onStop() {
	    super.onStop();  // Always call the superclass method first
	    interruptAllThisActivityThread();
	}
	
	public void interruptAllThread()
	{
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		for(int i=0;i<threadArray.length;i++)
		{
			threadArray[i].interrupt();
		}
	}
	
	public void interruptAllThisActivityThread()
	{
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		for(int i=0;i<threadArray.length;i++)
		{
			if(threadArray[i].getName().indexOf(this.getClass().getName())==0)
			{
				threadArray[i].interrupt();
			}
		}
	}
	
	public void showToast(final String str)
    {
		this.runOnUiThread(new Runnable() {
            public void run() {
            	Toast.makeText(this_activity, str, Toast.LENGTH_SHORT).show();
              }
        });
    	
    }
	
	// Alert box start
	protected void showAlertOKClicked(int refId)
	{
		// to be implemented by derived class
	}
	
	private Dialog appAlertDialog = null;
	public void showAlert(final String str, final int refId)
	{
		this.runOnUiThread(new Runnable() {
            public void run() {
            	
            	if(appAlertDialog==null)
        		{
            		appAlertDialog = new Dialog(this_activity);
            		appAlertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            		appAlertDialog.setCancelable(false);
        		}
            	
            	int titleBGColor = 0xFF454545;
            	int titleColor = 0xFFFFFFFF;
            	int titleSize = 16;
            	int bodyBGColor = 0xFFCCCCCC;
            	int bodyColor = 0xFF000000;
            	int bodySize = 12;
            	
            	LinearLayout main = new LinearLayout(this_activity);
            	main.setOrientation(LinearLayout.VERTICAL);
            	main.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.setBackgroundColor(bodyBGColor);

            	TextView title = new TextView(this_activity);
        		title.setText("Alert");
        		title.setTextColor(titleColor);
        		title.setBackgroundColor(titleBGColor);
        		title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
        		title.setPadding(4, 6, 4, 6);
        		title.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.addView(title);
            	
            	TextView body = new TextView(this_activity);
            	body.setText(str);
            	body.setTextColor(bodyColor);
            	body.setBackgroundColor(bodyBGColor);
            	body.setTextSize(TypedValue.COMPLEX_UNIT_DIP,bodySize);
            	body.setPadding(4, 4, 4, 8);
            	body.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.addView(body);
            	
            	Button button = new Button(this_activity);
            	button.setText("  OK  ");
            	button.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
            	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
	            		LinearLayout.LayoutParams.WRAP_CONTENT, 
                		LinearLayout.LayoutParams.WRAP_CONTENT);
            	lp.gravity = Gravity.CENTER;
            	button.setLayoutParams(lp);
            	button.setOnClickListener(new OnClickListener() {
    				//@Override
    				public void onClick(View v) {
    					appAlertDialog.cancel();
    					showAlertOKClicked(refId);
    				}
    			});
            	main.addView(button);
            	appAlertDialog.setContentView(main);
            	appAlertDialog.show();
            }
        });
	}
	// Alert Box end
	
	//Confirm Dialog Box Start
	
	protected void showConfirmOKClicked(int refId)
	{
		// to be implemented by derived class
	}
	
	protected void showConfirmCancelClicked(int refId)
	{
		// to be implemented by derived class
	}
	
	private Dialog appConfirmDialog = null;
	public void showConfirm(final String str, final int refId)
	{
		this.runOnUiThread(new Runnable() {
            public void run() {
            	
            	if(appConfirmDialog==null)
        		{
            		appConfirmDialog = new Dialog(this_activity);
            		appConfirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            		appConfirmDialog.setCancelable(false);
        		}
            	
            	int titleBGColor = 0xFF454545;
            	int titleColor = 0xFFFFFFFF;
            	int titleSize = 16;
            	int bodyBGColor = 0xFFCCCCCC;
            	int bodyColor = 0xFF000000;
            	int bodySize = 12;
            	
            	LinearLayout main = new LinearLayout(this_activity);
            	main.setOrientation(LinearLayout.VERTICAL);
            	main.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.setBackgroundColor(bodyBGColor);

            	TextView title = new TextView(this_activity);
        		title.setText("Confirm");
        		title.setTextColor(titleColor);
        		title.setBackgroundColor(titleBGColor);
        		title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
        		title.setPadding(4, 6, 4, 6);
        		title.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.addView(title);
            	
            	TextView body = new TextView(this_activity);
            	body.setText(str);
            	body.setTextColor(bodyColor);
            	body.setBackgroundColor(bodyBGColor);
            	body.setTextSize(TypedValue.COMPLEX_UNIT_DIP,bodySize);
            	body.setPadding(4, 4, 4, 8);
            	body.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	main.addView(body);
            	
            	LinearLayout buttonsLayout = new LinearLayout(this_activity);
            	buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
            	
            	buttonsLayout.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
            	buttonsLayout.setGravity(Gravity.CENTER);
            	
            	
            	
            	Button buttonOk = new Button(this_activity);
            	buttonOk.setText("  OK  ");
            	buttonOk.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
            	
            	
            	
            	Button buttonCancel = new Button(this_activity);
            	buttonCancel.setText(" Cancel ");
            	buttonCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP,titleSize);
            	
            	
            	
            	buttonsLayout.addView(buttonOk);
            	buttonsLayout.addView(buttonCancel);
            	
            	// Ok button click listener
            	buttonOk.setOnClickListener(new OnClickListener() {
    				//@Override
    				public void onClick(View v) {
    					appConfirmDialog.cancel();
    					showConfirmOKClicked(refId);
    				}
    			});
            	
            	//Cancel Button click listener
            	buttonCancel.setOnClickListener(new OnClickListener() {
    				//@Override
    				public void onClick(View v) {
    					appConfirmDialog.cancel();
    					showConfirmCancelClicked(refId);
    				}
    			});
            	main.addView(buttonsLayout);
            	
            	appConfirmDialog.setContentView(main);
            	
            	appConfirmDialog.show();
            	
              }
        });
	}
	
	//Confirm Dialog Box End
	
	private ProgressDialog progressDialog = null;
	
	public void showProgressDialog(final String str)
    {
		this.runOnUiThread(new Runnable() {
            public void run() {
            		if(progressDialog==null)
            			progressDialog = new ProgressDialog(this_activity);
            		progressDialog.setMessage(str);
            		progressDialog.setCancelable(false);
            		progressDialog.show();
              }
        });
    	
    }
	public void hideProgressDialog()
	{
		this.runOnUiThread(new Runnable() {
            public void run() {
            		if(progressDialog!=null)
            			progressDialog.dismiss();
            		progressDialog = null;
              }
        });
	}
	
	public void clickEffect(View view,int dark)
	{
		PorterDuffColorFilter filter;
		if(dark == 2)
			filter=new PorterDuffColorFilter(0x99CCCCCC, PorterDuff.Mode.LIGHTEN);
		else if(dark == 1)
			filter=new PorterDuffColorFilter(0x11FFFFFF, PorterDuff.Mode.DARKEN);
		else 
			filter=new PorterDuffColorFilter(0x11FFFFFF, PorterDuff.Mode.LIGHTEN);

		if(view.getClass().getSimpleName().equalsIgnoreCase("ImageView"))
			((ImageView)view).setColorFilter(filter);
		boolean setBgColor = false;
		if(view.getBackground()!=null)
			view.getBackground().setColorFilter(filter);
		else
		{
			setBgColor =true;
			Drawable dd = null;
			if(dark == 2)
				dd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x11CCCCCC,0x11CCCCCC});
			else if(dark == 1)
				dd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x11BBBBBB,0x11BBBBBB});
			else
				dd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x11AAAAAA,0x11AAAAAA});
				
			view.setBackgroundDrawable(dd);
			//view.setBackgroundColor(0xFFAAAAFF);
		}
		final View v = view;
		final boolean sbc = setBgColor;
		new AppThread(new Runnable() {
            public void run() {
            	AppThread.sleepWithInteruptCheck(350);
            	this_activity.runOnUiThread(new Runnable() {
                    public void run() {
                    	if(sbc)
                    		v.setBackgroundDrawable(null);
                    	else if(v.getBackground()!=null)
                    		v.getBackground().setColorFilter(null);                   	
                    	
                    	if(v.getClass().getSimpleName().equalsIgnoreCase("ImageView"))
                			((ImageView)v).setColorFilter(null);
                      }
                });
            	
            }
        },this_activity).start();
	}
	
	public void setEditText(final int id, final String str)
    {
		this.runOnUiThread(new Runnable() {
            public void run() {
            	((EditText) findViewById(id)).setText(str);
              }
        });
    	
    }
	
	public void setTextView(final int id, final String str)
    {
		this.runOnUiThread(new Runnable() {
            public void run() {
            	((TextView) findViewById(id)).setText(str);
              }
        });
    	
    }
	
	
	public void setImageView_WithPath(final int id, final String local_path)
	{
		this.runOnUiThread(new Runnable() {
            public void run() {
				File imgFile = new  File(local_path);
				if(imgFile.exists())
				{
				    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
				    ImageView myImage = (ImageView) findViewById(id);
				    myImage.setImageBitmap(myBitmap);
				}
            }
        });
	}
	public void setImageView_WithUrl(final int id, final String url)
	{
		new AppThread(new Runnable() {
            public void run() {
            	try{
	            		InputStream is = (InputStream) new URL(url).getContent();
	                    final Drawable drawable = Drawable.createFromStream(is, "some src name "+id);
	           
	                    this_activity.runOnUiThread(new Runnable() {
	                        public void run() {
	                        	ImageView imgView =(ImageView)findViewById(id);
	                        	imgView.setImageDrawable(drawable);
	                        }
	                    });
                    
            	}catch(Exception e){
            		AppLog.e(e.toString());
            	}           	
            }
        },this_activity).start();
	}
	
	/*
	public void loadWebView_WithUrl(final int id, final String url)
	{
		this_activity.runOnUiThread(new Runnable() {
            public void run() {
            	WebView webView =(WebView)findViewById(id);
            	webView.loadUrl(url);
            	webView.getSettings().setJavaScriptEnabled(true);
            }
        });
	}
	*/
	
	public void loadWebView_WithUrl(final int id, final String url)
	{
		this_activity.runOnUiThread(new Runnable() {
            public void run() {
            	WebView webView =(WebView)findViewById(id);
            	webView.setWebViewClient(new WebViewClient (){
            		public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	        view.loadUrl(url);
            	        return true;
            	    }
            		
            		@Override
            	    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
            	        handler.proceed();
            	    }
            		
            		 
            	});
            	webView.loadUrl(url);
            	webView.getSettings().setJavaScriptEnabled(true);
            }
        });
	}
	
	
	
	
	public void loadWebView_WithData(final int id, final String data)
	{
		this_activity.runOnUiThread(new Runnable() {
            public void run() {
            	try{
		            	WebView webView =(WebView)findViewById(id);
		            	String base64 = android.util.Base64.encodeToString(data.getBytes("UTF-8"), android.util.Base64.DEFAULT);
		            	webView.loadData(base64, "text/html; charset=utf-8", "base64");
		            	webView.getSettings().setJavaScriptEnabled(true);
            	}catch(Exception e){}
            }
        });
	}
	
	private Map <Integer,String[]> spinnerArrText = 
			new HashMap <Integer,String[]>();
	private Map <Integer,String[]> spinnerArrValue = 
					new HashMap <Integer,String[]>();
	private Map <Integer,Integer> spinnerLoading = 
							new HashMap <Integer,Integer>();
					
	
	public void setSpinnerData(final int id, String [] arrText, final String [] arrValue, final String selectedValue)
	{
		final Spinner spinner = (Spinner) findViewById(id);

		
		if(spinnerArrText.get(id) == null)
		{
			spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, 
			            int pos, long id2) {
			        // An item was selected. You can retrieve the selected item using
			        // parent.getItemAtPosition(pos)
					if(spinnerLoading.get(id)!=null && spinnerLoading.get(id).intValue()==1){
						spinnerLoading.put(id, 0);
					}
					else
					{
						this_activity.onSpinnerItemSelected(id, 
								spinnerArrText.get(id)[pos], 
								spinnerArrValue.get(id)[pos]);
					}
			    }
	
			    public void onNothingSelected(AdapterView<?> parent) {
			        // Another interface callback
			    }
			});
		}
		
		spinnerArrText.put(id, arrText);
		spinnerArrValue.put(id, arrValue);
				
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this_activity, android.R.layout.simple_spinner_item, arrText);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 
		this_activity.runOnUiThread(new Runnable() {
            public void run() {
				spinner.setAdapter(adapter);
				
				for(int i=0;i<arrValue.length;i++)
				{
					if(arrValue[i].equals(selectedValue))
					{
						spinnerLoading.put(id, 1);
						spinner.setSelection(i);
						
						break;
					}
				}
				
            }
        });
		
		
	}
	
	protected void onSpinnerItemSelected(int id, String text, String value)
	{
		// to be implemented by derived class
	}
	
	public String getSpinnerSelectedValue(int id)
	{
		Spinner spinner = (Spinner) findViewById(id);
		int i = spinner.getSelectedItemPosition();
		return spinnerArrValue.get(id)[i];
	}
	
	public String getSpinnerSelectedText(int id)
	{
		Spinner spinner = (Spinner) findViewById(id);
		int i = spinner.getSelectedItemPosition();
		return spinnerArrText.get(id)[i];
	}
	
	protected void openImageGalleryForSelection()
	{
		//Intent intent = new Intent(Intent.ACTION_PICK,
	    //           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");

		startActivityForResult(intent, APP_ACTIVITY_INTENT_REQ_CODE_IMAGE_PICK);
	}
	
	
	
	protected void openImageGalleryForSelectionDone(Uri uri, String filePath)
	{
		// Derived class will override
		AppLog.d("uri: "+uri.toString());
		AppLog.d("filePath: "+filePath);
	}
	
	////////////////////////////////////////
	protected void onActivityResult(int requestCode, int resultCode, Intent returnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, returnedIntent); 

	    switch(requestCode) { 
	    case APP_ACTIVITY_INTENT_REQ_CODE_IMAGE_PICK:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = returnedIntent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            openImageGalleryForSelectionDone(selectedImage, filePath);
	            
	        }
	    }
	}

	
	/////////////////////////////////////////
	
	public void populateGalleryPager(final int idGallery, final String [] imageUrls)
	{
		this_activity.runOnUiThread(new Runnable() {
            public void run() {
            	__populateGalleryPager(idGallery, imageUrls);
            }
        });
	}
	
	
	
	private void __populateGalleryPager(final int idGallery, final String [] imageUrls)
	{
		ViewPager viewPager = (ViewPager) findViewById(idGallery);
		
		/*
		viewPager.setOnDragListener(new View.OnDragListener() {
			
			@Override
			public boolean onDrag(View v, DragEvent event) {
				// TODO Auto-generated method stub
				return true;
			}
		});
		*/
		/*
		viewPager.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				return false;
			}
		});
		*/
		PagerAdapter pagerAdapter = new PagerAdapter() {
			
	        public int getCount() {
	            return imageUrls.length;
	        }
	 
	        public Object instantiateItem(View collection, int position) {
	 
	            //LayoutInflater inflater = (LayoutInflater) collection.getContext()
	            //        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
	            /*
	            int resId = 0;
	            switch (position) {
	            case 0:
	                resId = R.layout.farleft;
	                break;
	            case 1:
	                resId = R.layout.left;
	                break;
	            case 2:
	                resId = R.layout.middle;
	                break;
	            case 3:
	                resId = R.layout.right;
	                break;
	            case 4:
	                resId = R.layout.farright;
	                break;
	            }
	 
	            View view = inflater.inflate(resId, null);
	            */
	            
	            ImageView imageView = new ImageView(this_activity);
	            LinearLayout.LayoutParams vp = 
	                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 
	                		LinearLayout.LayoutParams.MATCH_PARENT);
	            imageView.setLayoutParams(vp);
	            
	         
	            
	            //Drawable dd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x11AAAAAA,0x11AAAAAA});
				//imageView.setBackgroundDrawable(dd);
				
				int id = (idGallery % 10000 ) * 1000 + position;
				imageView.setId(id);
				
				((ViewPager) collection).addView(imageView, 0);
				try{
					if(imageUrls[position].indexOf("http://")==0 || imageUrls[position].indexOf("https://")==0)
						this_activity.setImageView_WithUrl(id, imageUrls[position]);
					else if(Integer.parseInt(imageUrls[position])>0)
						imageView.setImageResource(Integer.parseInt(imageUrls[position]));
				} catch(Exception e) {}
	 
	            return imageView;
	        }
	 
	        @Override
	        public void destroyItem(View arg0, int arg1, Object arg2) {
	            ((ViewPager) arg0).removeView((View) arg2);
	 
	        }
	 
	        @Override
	        public boolean isViewFromObject(View arg0, Object arg1) {
	            return arg0 == ((View) arg1);
	 
	        }
	 
	        @Override
	        public Parcelable saveState() {
	            return null;
	        }
			
			
		};
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
	}
	
}
