package com.appsfire.adunitsampleapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKModalType;
import com.appsfire.adUnitJAR.exceptions.AFAdAlreadyDisplayedException;
import com.appsfire.adUnitJAR.exceptions.AFNoAdException;

/**
 * Demo app's activity class
 */

public class MainAdUnitActivity extends Activity {
	// Tag for logging messages
	private static final String CLASS_TAG = "MainAdUnitActivity";
	
	// Ad sdk instance
	private static AFAdSDK adSdk;
	
	private MyExpandableListAdapter expListAdapter;
	private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private HashMap<Integer,Integer> m_status = new HashMap<Integer,Integer> ();
    
    // Listview headers (ad SDK statuses and action buttons)
    private static final String statusHeaders[]= {
    	"Ad. SDK Status",
    	"Actions",
    };
    
    // Listview entries showing the ad SDK statuses
    public static final String statusItems[]= {
    	"Engage SDK Initialized",
    	"Ad Unit Initialized",
    	"Ads Loaded",
    	"Modal Ad Available",
    	"In-Stream Ad Available",
    	"Modal Ad Requested"
    };
    
    // Listview entries showing action buttons
    private static final String actionItems[]= {
    	"Request Sushi",
        "Show Minimal Sashimi (Light)",
        "Show Minimal Sashimi (Dark)",
        "Show Minimal Sashimi (Custom Colors)",
        "Show Sashimi With Custom Subclass",
        "Show Extended Sashimi (Dark)",
        "Show Sashimi Carousel"
    };
        
    // SDK statues displayed in the listview
    
    public enum AdSDKStatus {    	
		engageSDKInitialized (0),
	    adSDKInitialized (1),
	    adsLoaded (2),
	    modalAdAvailable (3),
	    inStreamAdAvailable (4),
	    requestedAnAd (5);
		
		private final int code;
		
		private AdSDKStatus(int code) {
		    this.code = code;
		}
		
		public int size() {
		     return statusItems.length;
		}

		public int getCode() {
		     return code;
		}		  
    }
    
    // Get red/green state of an sdk status. Used by MyExpandableListAdapter
    
    public boolean getDisplayedAdSdkStatus (int nStatus) {
    	synchronized (this) {
	    	Integer result = m_status.get (Integer.valueOf (nStatus));
	    	
	    	if (result == null || result == 0)
	    		return false;
	    	else
	    		return true;
    	}
    }
    
    // Set red/green state of an sdk status
    
    public void setDisplayedAdSdkStatus (final AdSDKStatus nStatus, final boolean bSuccess) {
    	synchronized (this) {
	    	m_status.put (nStatus.getCode(), bSuccess ? 1 : 0);  
    	}
    	
    	runOnUiThread (new Runnable () {
    		public void run() {
    			// Update listview entry's background color now
				expListAdapter.getCachedChildTextView(0, nStatus.getCode()).setBackgroundColor(Color.parseColor(bSuccess ? "#007300" : "#7f0000"));
    		}
    	});
    }
    
    // Create activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adunit_statuses_actions);
		
		// Always expand the listview contents
        expListView = (ExpandableListView) findViewById (R.id.lvStatusesActions);        
        expListView.setOnGroupClickListener (new ExpandableListView.OnGroupClickListener() {
            public boolean onGroupClick (ExpandableListView arg0, View itemView, int itemPosition, long itemId) {
            	expListView.expandGroup (itemPosition);
                return true;
            }
        });
                   
        // Fill in the listview contents
        
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        
        listDataHeader.addAll(Arrays.asList(statusHeaders));
 
        List<String> statuses = new ArrayList<String>(Arrays.asList(statusItems));
        List<String> actions = new ArrayList<String>(Arrays.asList(actionItems));
 
        listDataChild.put (listDataHeader.get(0), statuses); // Header, Child data
        listDataChild.put (listDataHeader.get(1), actions);
 
        // Set list adapter
        expListAdapter = new MyExpandableListAdapter(this, listDataHeader, listDataChild, expListView); 
        expListView.setAdapter(expListAdapter);
        
        // Always open the list view as expanded
        for (int i = 0; i < listDataHeader.size(); ++i)
        	expListView.expandGroup(i);  
                      
        // set up a onClickListener for listView
        expListView.setOnChildClickListener (new ExpandableListView.OnChildClickListener() {			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				// make the first category items clickable
				if (groupPosition == 0 && childPosition < statusItems.length ) {
					// do not handle clicks in these positions
					return false;
				} else {
					return handleChildClicksLogic(parent, v, groupPosition, childPosition, id);
				}
			}
		});
        
        adSdk = App.getSDK();
        adSdk.registerContext(this);  
	}

	// Terminate activity
	
	@Override
	public void finish() {
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

	// React to the listview being done drawing, after the activity startup
	
	public void onListStatusViewDrawingComplete() {
		// Initialize the Appsfire SDK now
		adSdk.prepare(this);		
	}
	
	/**
	 * @return the expListAdapter
	 */
	public MyExpandableListAdapter getExpListAdapter() {
		return expListAdapter;
	}
	
	private boolean handleChildClicksLogic(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Log.d(CLASS_TAG, "Listview[" + groupPosition + "][" + childPosition + "] clicked, id=" + id );
		switch (groupPosition) {
			case 0:
				break;
			case 1 : 
				{
					switch(childPosition) {
						case 0:
							// Request Sushi
							((MainAdUnitActivity)App.getSDK().getContext("MainAdUnitActivity")).setDisplayedAdSdkStatus(AdSDKStatus.requestedAnAd, true);
							
							// check if a modalAd of type Sushi is available and if so request it
							if (adSdk.isAModalAdOfTypeAvailable(AFAdSDKModalType.AFAdSDKModalTypeSushi)) {
								try {
					        	    adSdk.requestModalAd(AFAdSDKModalType.AFAdSDKModalTypeSushi, this);			                        
								} catch (AFNoAdException e) {									
					                Toast toastInstance = Toast.makeText (this, "No modal ad available", Toast.LENGTH_LONG);				                
					                toastInstance.show ();																								
								} catch (AFAdAlreadyDisplayedException e) {									
								}
							}
							else {
				                Toast toastInstance = Toast.makeText (this, "No modal ads available yet", Toast.LENGTH_LONG);				                
				                toastInstance.show ();																
							}
							break;
							
						case 1:
						case 2:
						case 3:			
						case 4:
						case 5:
						case 6:
							// Show Sashimi
							Log.d (CLASS_TAG, "show Sashimi activity");
							Intent intent = new Intent(MainAdUnitActivity.this, SashimiActivity.class);
							Bundle b = new Bundle();
							b.putInt("action", childPosition);
							intent.putExtras(b);
							startActivity(intent);
							break;
						
						default:
							break;
					}
				}
				break;
			case 2:
				break;
			default:
				break;
		}
		return true;
	}	
}

