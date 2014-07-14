package com.sampleapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity _activity;
	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<HashMap<String, String>>> _listDataChild;
	private static String TAG = "ExpandableListAdapter";

	public ExpandableListAdapter(FragmentActivity activity, Context context,
			List<String> listDataHeader,
			HashMap<String, List<HashMap<String, String>>> listChildData) {
		this._activity = activity;
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {

		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		HashMap<String, String> cityObj = (HashMap<String, String>) getChild(
				groupPosition, childPosition);

		for (Entry<String, String> entry : cityObj.entrySet()) {
			final String cityName = entry.getKey();
			final String temprature = entry.getValue();
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) this._context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.list_item, null);
			}

			TextView lblCityName = (TextView) convertView
					.findViewById(R.id.lblCityName);

			lblCityName.setText(cityName);

			TextView lblTemprature = (TextView) convertView
					.findViewById(R.id.lblTemprature);

			lblTemprature.setText(Html.fromHtml("Temprature : " + temprature
					+ "<sup>o</sup>"));
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		try {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition)).size();
		} catch (Exception e) {
			Log.i(TAG, "Exception :: " + e);
			return 0;
		}

	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		try {
			return this._listDataHeader.size();
		} catch (Exception e) {
			Log.i(TAG, "Exception :: " + e);
			return 0;
		}
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}