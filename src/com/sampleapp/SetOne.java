package com.sampleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetOne extends Fragment implements OnGroupClickListener,
		OnChildClickListener, OnGroupCollapseListener, OnGroupExpandListener,
		OnClickListener {

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static Context _context;
	private static final String ARG_SET_NUMBER = "set_number";
	private List<String> listDataHeader;
	private HashMap<String, List<HashMap<String, String>>> listDataChild;
	private static ExpandableListAdapter listAdapter;
	private static ExpandableListView expListView;
	private static String TAG = "SetOne";
	private Button addButton;
	private EditText cityTextField;
	private View rootView;
	List<HashMap<String, String>> cityNtemprature;
	private static AppActivity this_activity;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SetOne newInstance(AppActivity activity, int sectionNumber) {
		this_activity = activity;
		_context = activity;
		SetOne fragment = new SetOne();
		Bundle args = new Bundle();
		args.putInt(ARG_SET_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_set_one, container, false);
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<HashMap<String, String>>>();
		listDataHeader.add("Cities");
		cityNtemprature = new ArrayList<HashMap<String, String>>();
		cityTextField = (EditText) rootView.findViewById(R.id.city_text);
		addButton = (Button) rootView.findViewById(R.id.add_button);
		addButton.setOnClickListener(this);
		rootView.setOnClickListener(this);
		expListView = (ExpandableListView) rootView
				.findViewById(R.id.cityListView);

		expListView.setOnGroupClickListener(this);
		expListView.setOnChildClickListener(this);
		expListView.setOnGroupCollapseListener(this);
		expListView.setOnGroupExpandListener(this);

		listAdapter = new ExpandableListAdapter(getActivity(), getActivity()
				.getBaseContext(), listDataHeader, listDataChild);
		expListView.setAdapter(listAdapter);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((HomeActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SET_NUMBER));
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		AppUtility.blur(getActivity());
		AppUtility.showToast(_context, listDataHeader.get(groupPosition)
				+ " Expanded");
	}

	@Override
	public void onGroupCollapse(int groupPosition) {
		AppUtility.blur(getActivity());
		AppUtility.showToast(_context, listDataHeader.get(groupPosition)
				+ " Collapsed");
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		AppUtility.blur(getActivity());
		AppUtility.showToast(
				_context,
				listDataHeader.get(groupPosition)
						+ " : "
						+ listDataChild.get(listDataHeader.get(groupPosition))
								.get(childPosition));
		return false;
	}

	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		AppUtility.blur(getActivity());
		AppUtility.showToast(_context,
				"Group Clicked " + listDataHeader.get(groupPosition));
		return false;
	}

	@Override
	public void onClick(View clickedView) {
		Log.i(TAG, "View Clicked :: " + clickedView.getTag());
		AppUtility.blur(getActivity());

		if (clickedView == addButton) {
			String cityName = cityTextField.getText().toString();
			if (cityNtemprature.size() >= 15) {
				AppUtility.showToast(_context,
						"Completed 15 cities limit. Can't enter more.");
			} else if (cityName == null || cityName.length() == 0) {
				AppUtility.showToast(_context, "Please enter cityname");
			} else {
				HashMap<String, String> ctntemp = new HashMap<String, String>();
				ctntemp.put(cityName, cityNtemprature.size() + "");
				cityNtemprature.add(ctntemp);
				listDataChild.put(listDataHeader.get(0), cityNtemprature);
				if (expListView.isGroupExpanded(0)) {
					expListView.collapseGroup(0);
				}
				expListView.expandGroup(0);
			}
		}
		cityTextField.setText("");
	}

//	private void getLocationData() {
//		new AppThread(new Runnable() {
//            public void run() {
//            	try{
//	            	AppHttpClient hc 
//	            	= new AppHttpClient(AppSettings.BASE_URL);
//	            	
//	            	if(hc.sendGet())
//	            	{
//	            		String responseBody = hc.getResponseBody();
//	            		AppLog.d(responseBody);
//	            		AppUtility.showToast(_context, responseBody);
//	            		
//	            		JSONObject subObj = new JSONObject(responseBody);
//	            	}
//	            	
//            	}catch(Exception e){}           	
//            }
//        },this_activity).start();
//	}
}