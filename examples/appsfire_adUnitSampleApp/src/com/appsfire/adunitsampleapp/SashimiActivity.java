package com.appsfire.adunitsampleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKSashimiFormat;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSize;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView.AFAdSDKSashimiStyleMode;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SashimiActivity extends Activity {
	// Tag for logging messages
	private static final String CLASS_TAG = "MainAdUnitActivity";

	// Ad sdk instance
	private static AFAdSDK adSdk;
	private int action;
	private ListView listView;
	private SashimiListAdapter listAdapter;
	private AFAdSDKSashimiView sashimiView;
	
    // Create activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adunit_sashimiview);
        
		// Get action code sent by MainAdUnitActivity
        try {
	        Bundle b = getIntent().getExtras();
	        action = b.getInt("action");
        	Log.d (CLASS_TAG, "create with action: " + action);
        } catch (Exception e) {
        	Log.d (CLASS_TAG, "exception getting action: " + e.toString ());
        }
        
        // Get SDK
        adSdk = App.getSDK();
        adSdk.registerContext(this);  
                
        // Create listview with content
        listView = (ListView) findViewById (R.id.lvSashimi);
        listAdapter = new SashimiListAdapter (this);
        listView.setAdapter(listAdapter);
        
		if (action == 2 || action == 3 || action == 4 || action == 5) {
			listView.setDivider(new ColorDrawable(0xFFFFFFFF));
			listView.setDividerHeight(1);
		}
		
        switch (action) {
		case 1:
		case 2:
		case 3:												
			// Show Minimal Sashimi
			Log.d (CLASS_TAG, "in-stream ads available: " + adSdk.numberOfSashimiAdsAvailableForFormat(AFAdSDKSashimiFormat.AFAdSDKSashimiFormatMinimal));
			if (adSdk.numberOfSashimiAdsAvailableForFormat(AFAdSDKSashimiFormat.AFAdSDKSashimiFormatMinimal) != 0) {
				removeSashimiView ();
				
				sashimiView = adSdk.sashimiViewForFormat (AFAdSDKSashimiFormat.AFAdSDKSashimiFormatMinimal, new AFAdSDK.AFAdSizeProvider () {
					@Override
					public AFAdSize provideSize() {
						WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
						Display display = wm.getDefaultDisplay();
						DisplayMetrics dm = new DisplayMetrics ();
						AFAdSDK.AFAdSize size = new AFAdSDK.AFAdSize();
						
						display.getMetrics (dm);
						size.width = listView.getWidth();
						size.height = Math.min (listView.getWidth(), (int) (Math.max (dm.widthPixels, dm.heightPixels) * 0.6)) / 3;
						return size;
					}
				}, this);
				
				if (sashimiView != null) {
					if (action == 2) {
						// Dark style template
						sashimiView.setStyleMode(AFAdSDKSashimiStyleMode.AFAdSDKSashimiMinimalStyleModeDark);
						findViewById (R.id.sashimiView).setBackgroundColor(0xff272727);
					}
					
					if (action == 3) {
						// Custom colors
						sashimiView.setContentBackgroundColor(0xff444488);
						sashimiView.setTitleTextColor(0xffaaefff);
						sashimiView.setCategoryTextColor(0xff88cfff);
						sashimiView.setTaglineTextColor(0xffaaefff);
						sashimiView.setPriceBackgroundColor(0xff357abd);
						sashimiView.setPriceTextColor(0xffaaefff);
						sashimiView.setIconBorderColor(0xffaaeeff);
						sashimiView.setCallToActionButtonColor(false, 0xff33b3c8);
						sashimiView.setCallToActionButtonColor(true, 0xff129398);
						
						findViewById (R.id.sashimiView).setBackgroundColor(0xff242468);
					}
					
	                Toast toastInstance = Toast.makeText (this, "Loading...", Toast.LENGTH_SHORT);				                
	                toastInstance.show ();																																	
					insertSashimiView ();
				}
				else {
	                Toast toastInstance = Toast.makeText (this, "Error showing in-stream ad", Toast.LENGTH_LONG);				                
	                toastInstance.show ();																																	
				}
			}
			else {
                Toast toastInstance = Toast.makeText (this, "No in-stream ads available yet", Toast.LENGTH_LONG);				                
                toastInstance.show ();																								
			}
			break;
		
		case 4:
			// Show Sashimi With Custom Subclass
			if (adSdk.numberOfSashimiAdsAvailableForFormat(AFAdSDKSashimiFormat.AFAdSDKSashimiFormatMinimal) != 0) {
				removeSashimiView ();
				
				sashimiView = adSdk.sashimiViewForSubclass (MySashimiView.class, new AFAdSDK.AFAdSizeProvider () {
					@Override
					public AFAdSize provideSize() {
						WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
						Display display = wm.getDefaultDisplay();
						DisplayMetrics dm = new DisplayMetrics ();
						AFAdSDK.AFAdSize size = new AFAdSDK.AFAdSize();
						
						display.getMetrics (dm);
						size.width = listView.getWidth();
						size.height = Math.min (listView.getWidth(), (int) (Math.max (dm.widthPixels, dm.heightPixels) * 0.6)) / 3;
						return size;
					}
				}, this);
				
				if (sashimiView != null) {
					findViewById (R.id.sashimiView).setBackgroundColor (0xff44447f);

	                Toast toastInstance = Toast.makeText (this, "Loading...", Toast.LENGTH_SHORT);				                
	                toastInstance.show ();																																	
					insertSashimiView ();
				}
				else {
	                Toast toastInstance = Toast.makeText (this, "Error showing in-stream ad", Toast.LENGTH_LONG);				                
	                toastInstance.show ();																																	
				}
			}
			else {
                Toast toastInstance = Toast.makeText (this, "No in-stream ads available yet", Toast.LENGTH_LONG);				                
                toastInstance.show ();																								
			}
			break;
		
		case 5:
			// Show Extended Sashimi (Dark)
			if (adSdk.numberOfSashimiAdsAvailableForFormat(AFAdSDKSashimiFormat.AFAdSDKSashimiFormatExtended) != 0) {
				removeSashimiView ();
			
				findViewById (R.id.sashimiView).setBackgroundColor(0xff272727);
				
				sashimiView = adSdk.sashimiViewForFormat (AFAdSDKSashimiFormat.AFAdSDKSashimiFormatExtended, new AFAdSDK.AFAdSizeProvider () {
					@Override
					public AFAdSize provideSize() {
						WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
						Display display = wm.getDefaultDisplay();
						DisplayMetrics dm = new DisplayMetrics ();
						AFAdSDK.AFAdSize size = new AFAdSDK.AFAdSize();
						
						display.getMetrics (dm);
						size.width = listView.getWidth();
						if (dm.widthPixels > dm.heightPixels)
							size.height = (int) ((Math.max (dm.widthPixels, dm.heightPixels) * 1.0) / 1.3f);
						else
							size.height = (int) ((Math.min (dm.widthPixels, dm.heightPixels) * 1.0) / 1.3f);
						return size;
					}
				}, this);
				
				if (sashimiView != null) {
					sashimiView.setStyleMode(AFAdSDKSashimiStyleMode.AFAdSDKSashimiMinimalStyleModeDark);
	                Toast toastInstance = Toast.makeText (this, "Loading...", Toast.LENGTH_SHORT);				                
	                toastInstance.show ();																																	
					insertSashimiView ();
				}
				else {
	                Toast toastInstance = Toast.makeText (this, "Error showing in-stream ad", Toast.LENGTH_LONG);				                
	                toastInstance.show ();																																	
				}
			}
			else {
                Toast toastInstance = Toast.makeText (this, "No in-stream ads available yet", Toast.LENGTH_LONG);				                
                toastInstance.show ();																								
			}
			break;
				
		default:
			break;
        }
	}

	// Terminate activity
	
	@Override
	public void finish() {
		removeSashimiView ();
		adSdk.unRegisterContext(this);
		super.finish();
	}	
	
	// Start activity

	@Override
	protected void onStart() {
		super.onStart();
		App.getSDK().onStart (this);		
	}

	// Stop activity
	
	@Override
	protected void onStop() {
		super.onStop();
		App.getSDK().onStop ();		
	}
	
	// Pause activity
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	// Resume activity
	protected void onResume() {
		super.onResume();		
	}
		
	// Insert created sashimi ad view at the bottom of the expandable listview
	
	private void insertSashimiView() {
		// Add listener to be called when the Sashimi assets have been downloaded       
        sashimiView.setOnAssetsReceivedListener(new AFAdSDKSashimiView.AFAdSDKSashimiAssetsReceivedListener () {
			public void onAssetsReceived() {
				Log.d (CLASS_TAG, "sashimi assets received");
		                
				runOnUiThread(new Runnable() {
				    @Override
				    public void run() {
				    	// Refresh listview; the Sashimi will now appear in it
						listAdapter.notifyDataSetChanged();
						
				    	// Scroll listview to the top, in case the user moved it while waiting for the ad to load
				    	listView.setSelection(0);
				    }
				});						
			}        	
        });        
        
        sashimiView.setOnDisplayListener (new AFAdSDKSashimiView.AFAdSDKSashimiDisplayListener () {
			public void onSashimiDisplayed() {
				// Just log that the Sashimi is now visible, as a demo of setOnDisplayListener()
				Log.d (CLASS_TAG, "sashimi displayed");
			}
        });
	}
	
	// Remove sashimi ad view
	
	private void removeSashimiView() {
		if (sashimiView != null) {
			// Free resources used by Sashimi
			adSdk.releaseSashimiView (sashimiView);
			sashimiView = null;
			listAdapter.notifyDataSetChanged();
		}
	}
	
    // Listview adapter for fake content and Sashimi
	
	public class SashimiListAdapter extends BaseAdapter {
		private final Context context;
		ArrayList<RSSFeed.Item> rssItems;

		public SashimiListAdapter(Context context) {
			super();
			this.context = context;
			this.rssItems = App.getRSSFeed().getItems();
		}

		@Override
		public int getCount() {
			int nCount = rssItems.size();
			
			if (sashimiView != null)
				nCount++;
			return nCount;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = null;
			int nCount = rssItems.size();
			
			if (sashimiView != null)
				nCount++;
			
			if (position >= 0 && position < nCount) {
				if (sashimiView != null && position == 2) {
					// Show Sashimi in row #3
					rowView = sashimiView;
				}
				else {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.adunit_sashimilistitem, parent, false);
					TextView textView = (TextView) rowView.findViewById(R.id.lvItem);
					
					if (sashimiView != null && position > 2) {
						// Below Sashimi row #3, shift position to get the right fake content for this row
						position--;
					}
					
					RSSFeed.Item item = rssItems.get(position);					
					textView.setText(Html.fromHtml("<b>"+item.getTitle()+"</b><br>"+item.getDescription()));
					textView.setMaxLines(3);
					textView.setEllipsize(TruncateAt.END);
					
					switch (action) {
					case 2:
					case 5:
						// Dark style mode
						textView.setBackgroundColor (0xff474747);
						textView.setTextColor(Color.WHITE);
						break;
						
					case 3:
						// Custom colors
						textView.setBackgroundColor(0xff444488);
						textView.setTextColor(0xffaaefff);
						break;						
						
					case 4:
						// Custom Sashimi class with custom colors
						textView.setBackgroundColor (0xff54548f);
						textView.setTextColor (Color.WHITE);
						break;
						
					default:
						break;
					}
				}
			}
			
			return rowView;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	} 
}

