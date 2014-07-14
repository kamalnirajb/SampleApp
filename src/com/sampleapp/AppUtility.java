package com.sampleapp;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class AppUtility {
	public static void blur(Activity act) {
		InputMethodManager inputMethodManager = (InputMethodManager) act
				.getSystemService(act.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(act.getCurrentFocus()
				.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showToast(Context context, String msg) {
		Toast.makeText(context, msg + "", Toast.LENGTH_SHORT).show();
	}
}
