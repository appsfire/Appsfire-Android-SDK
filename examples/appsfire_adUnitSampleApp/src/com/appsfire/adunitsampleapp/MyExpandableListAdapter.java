package com.appsfire.adunitsampleapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appsfire.adunitsampleapp.MainAdUnitActivity.AdSDKStatus;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

/**
 * Our expandable list adapter class
 */

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	
	private static final String CLASS_TAG = "MyExpandableListAdapter";
	private Context mContext;
	private List<String> mListDataHeader;
	private HashMap<String, List<String>> mListDataChild;
	ExpandableListView expListView;
	private Map<Integer,TextView> statusTxtViewMap= new HashMap<Integer,TextView>();
	
	public MyExpandableListAdapter(Context context, List<String> listDataHeader,
			HashMap<String, List<String>> listDataChild, ExpandableListView expListView) {
		super();
		this.mContext = context;
		this.mListDataHeader = listDataHeader;
		this.mListDataChild =  listDataChild;
		this.expListView = expListView;
	}

	@Override
	public Object getGroup(int groupPosition) {
		
		return mListDataHeader.get(groupPosition);
	}
	

    /** To say to adapter that positions are the IDs */
	@Override
	public long getGroupId(int groupPosition) {
		
		return groupPosition;
	}

	@Override
	public int getGroupCount() {
		
		return mListDataHeader.size();
	}
	
	@Override
	public int getChildrenCount(int groupPosition) {
		
		return mListDataChild.get(getGroup(groupPosition)).size();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		
		return mListDataChild.get(getGroup(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	@Override
	public long getCombinedChildId (long groupId, long childId) {
		return groupId * 10 + childId;
	}
	
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {		
		String headerTitle = (String) getGroup(groupPosition);
		Log.i(CLASS_TAG, "headerTitle[" + groupPosition + "]=" + headerTitle);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.adunit_listgroupheader, null);		
		}
		TextView lvListHeader = (TextView) convertView.findViewById(R.id.lvListHeader);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lvListHeader.setText(headerTitle);
        return convertView;
	}
	
	@Override
    public View getChildView (int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { 
        final String rowText = (String) getChild(groupPosition, childPosition);
        View rowView = convertView;
        
        if (rowView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = infalInflater.inflate(R.layout.adunit_listitem, null);
        }
        
        TextView rowTxtView = (TextView) rowView.findViewById(R.id.lvItem);
        int statusItemsSize = MainAdUnitActivity.statusItems.length;
		MainAdUnitActivity activity = (MainAdUnitActivity)mContext;
        
    	if (groupPosition == 0 && childPosition < statusItemsSize) {
    		// Status item
    		
    		if (activity.getDisplayedAdSdkStatus (childPosition))
    			rowTxtView.setBackgroundColor(Color.parseColor("#007300")); // green
    		else
    			rowTxtView.setBackgroundColor(Color.parseColor("#7f0000")); // dark red
        	rowTxtView.setTextColor(Color.parseColor("#ffffff")); // white        	
    	}
    	else if (groupPosition == 1) {
    		// Action button
        	rowTxtView.setBackgroundColor(Color.parseColor("#ffffff"));
        	rowTxtView.setTextColor(Color.parseColor("#000000"));       		
    	}
        
        if (convertView == null) {
        	if (groupPosition == 0 && childPosition < statusItemsSize) {
        		// Status item
        			        	
	        	statusTxtViewMap.put(groupPosition*10 +childPosition,rowTxtView);
	        	rowTxtView.setTag(groupPosition*10 +childPosition);
	        	
	        	if (childPosition == statusItemsSize - 1) {
	        		// statusItem views are drawn
	        		activity.onListStatusViewDrawingComplete();
	        	}
        	}        	
        }
                
    	rowTxtView.setText(rowText);    	
        return rowView;
    }
	
	public TextView getCachedChildTextView(int groupPosition, int childPosition) {
		return statusTxtViewMap.get(groupPosition*10 + childPosition);
	}
	
	@Override
	public boolean hasStableIds() {	
		return true;
	}

	@Override
	public boolean isChildSelectable (int groupPosition, int childPosition) {		
		return true;
	}	
}
