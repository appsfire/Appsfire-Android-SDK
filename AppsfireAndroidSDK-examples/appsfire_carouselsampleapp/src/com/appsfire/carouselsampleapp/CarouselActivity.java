package com.appsfire.carouselsampleapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKError;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFSDKFeature;
import com.appsfire.adUnitJAR.sdk.AFAdSDKEventsDelegate;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSDKSashimiFormat;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSize;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView.AFAdSDKSashimiStyleMode;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;

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
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CarouselActivity extends Activity implements AFAdSDKEventsDelegate {
	// Tag for logging messages
	private static final String CLASS_TAG = "CarouselActivity";

	// App key
	private static final String YOUR_API_KEY = "YOUR_API_KEY_HERE";
	
	// App secret
	private static final String YOUR_API_SECRET = "YOUR_API_SECRET_HERE";
	
	// true for debug mode, false for production mode (set to false when distributing your app)
	private static final Boolean IS_AFADSDK_DEBUG = true;

	// Create instance of the ad SDK
	private static AFAdSDK adSdk = AFSDKFactory.getAFAdSDK().
												setAPIKey(YOUR_API_KEY).
												setAPISecret(YOUR_API_SECRET).
												setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureEngage, AFSDKFeature.AFSDKFeatureMonetization)).
												setDebugModeEnabled(IS_AFADSDK_DEBUG);

	private ListView listView;
	private SashimiListAdapter listAdapter;
	private AFAdSDKCarouselSashimiView carouselView;

	// Class to represent one entry in the content to be displayed around the carousel
    public class Item {
        private String title;
        private String description;

        public Item(String title, String description, String link) {
        	this.title = title;
        	this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }
    }
    
	// Content around the carousel itself
    ArrayList<Item> mItems = new ArrayList<Item>();

	// Create activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adunit_sashimiview);
      	  
		// Set our events delegate, start SDK
		adSdk.setEventsDelegate(this).
		prepare(this);
        Toast toastInstance = Toast.makeText (this, "Initializing SDK...", Toast.LENGTH_SHORT);				                
        toastInstance.show ();
				  
		// Populate content
		mItems = new ArrayList<Item>();
    	mItems.add(new Item ("How do cryptocurrencies work?", "Not so long ago the thought of reaching into your pocket for a digital currency might have seemed too far-fetched.", "http://rss.cnn.com/~r/rss/edition_business/~3/f3Vny2RFogA/index.html"));
    	mItems.add(new Item ("Is your hotel chic? Check the label", "These days, no fashion house portfolio is complete without a hotel -- or at the very least, a luxuriously designed suite.", "http://rss.cnn.com/~r/rss/edition_business/~3/MqQfpA4work/index.html"));
    	mItems.add(new Item ("Can airplane seats keep the peace?", "There is no shortage of adjectives one can apply to airline seats: uncomfortable, bulky, cramped, outdated and -- from an airline's point of view -- overpriced. It's no wonder then that many carriers are looking to make a change.", "http://rss.cnn.com/~r/rss/edition_business/~3/PbW-NsM9gss/index.html"));
    	mItems.add(new Item ("The future of supersonic flight", "Concorde is a thing of the past, but a number of companies are racing to release the first supersonic business jet.", "http://rss.cnn.com/~r/rss/edition_business/~3/_GNOjRF_BzY/index.html"));
    	mItems.add(new Item ("Bobbi Brown's billion dollar idea", "When Bobbi Brown introduced her eponymous lipstick line to Bergdorf Goodman back in 1991, she never expected all 10 pinky-brown shades to fly off the shelves in just one day.", "http://rss.cnn.com/~r/rss/edition_business/~3/ADCywkcvf1E/index.html"));
    	mItems.add(new Item ("The dress that launched an empire", "When Diane von Furstenberg first unveiled her wrap dress back in 1974, she never expected the DVF brand and dress to reach such iconic status in the world of fashion.", "http://rss.cnn.com/~r/rss/edition_business/~3/hyeC-xMR3rM/index.html"));
    	mItems.add(new Item ("Name your price for Paris hotel stay", "Several hotels in the French capital are conducting a potentially risky experiment, asking guests to decide what they're willing to pay.", "http://rss.cnn.com/~r/rss/edition_business/~3/JulEx2QcEPI/index.html"));
    	mItems.add(new Item ("World's coolest bookstores ", "From Maastricht to Melbourne, these itineraries make bookish travelers look stylish.", "http://rss.cnn.com/~r/rss/edition_business/~3/d-jv_r_dVmI/index.html"));
    	mItems.add(new Item ("World-class motor museums", "With factory tours and collections of stunning vintage prototypes, southern Germany is petrolhead paradise.", "http://rss.cnn.com/~r/rss/edition_business/~3/5WCSi9K5NHw/index.html"));
    	mItems.add(new Item ("New air safety task force duties", "The director general of IATA says a new task force will help countries share air safety information.", "http://rss.cnn.com/~r/rss/edition_business/~3/w38Sd3qdLAk/qmb-iata-task-force-better-info.cnn.html"));
    	mItems.add(new Item ("Don't let the auto industry kill you", "Shanin Specter says we need to strengthen laws that punish auto companies for selling defective cars that kill people.", "http://rss.cnn.com/~r/rss/edition_business/~3/azL30Mn_ves/index.html"));
    	mItems.add(new Item ("Meet Airbus' newest jet", "Takeoff on one of Airbus' new A350WXB test planes is a strangely quiet experience.", "http://rss.cnn.com/~r/rss/edition_business/~3/cQmhHh2gXWQ/index.html"));
		
		// Create listview with content
		listView = (ListView) findViewById (R.id.lvSashimi);
		listAdapter = new SashimiListAdapter (this);
		listView.setAdapter(listAdapter);			        
	}

	// Destroy activity
	
	@Override
	public void onDestroy() {
		removeCarouselView ();
		super.onDestroy();
	}	
	
	// Start activity

	@Override
	protected void onStart() {
		super.onStart();
		adSdk.onStart (this);		
	}

	// Stop activity
	
	@Override
	protected void onStop() {
		super.onStop();
		adSdk.onStop ();		
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

	// Display carousel, once Sashimi ads are available
	
	private void displayCarousel() {
		int nAdsForCarousel = adSdk.numberOfSashimiAdsAvailableForFormat(AFAdSDKSashimiFormat.AFAdSDKSashimiFormatExtended);
		
		if (nAdsForCarousel != 0) {
			removeCarouselView ();
				
			carouselView = new AFAdSDKCarouselSashimiView (this);
			if (carouselView != null) {
				ArrayList<AFAdSDKSashimiView> items = new ArrayList<AFAdSDKSashimiView>();
					
				for (int i = 0; i < nAdsForCarousel; i++) {
					AFAdSDKSashimiView adView = adSdk.sashimiViewForFormat (AFAdSDKSashimiFormat.AFAdSDKSashimiFormatExtended, new AFAdSDK.AFAdSizeProvider () {
						@Override
						public AFAdSize provideSize() {
							WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
							Display display = wm.getDefaultDisplay();
							DisplayMetrics dm = new DisplayMetrics ();
							AFAdSDK.AFAdSize size = new AFAdSDK.AFAdSize();
							int nPageMargin;
								
							display.getMetrics (dm);
							size.width = listView.getWidth();
							if (dm.widthPixels > dm.heightPixels)
								size.height = (int) ((Math.max (dm.widthPixels, dm.heightPixels) * 1.0) / 1.3f);
							else
								size.height = (int) ((Math.min (dm.widthPixels, dm.heightPixels) * 1.0) / 1.3f);
							nPageMargin = Math.min((int) (size.width / 4), 200);
							carouselView.setAdMargin(nPageMargin);
							size.height = size.height * (size.width - nPageMargin) / size.width;
							size.width = size.width - nPageMargin;
							return size;
						}
					}, this);
						
					items.add(adView);
				}
					
				carouselView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				carouselView.setAdViewsArray(items);
					
                Toast toastInstance = Toast.makeText (this, "Showing carousel...", Toast.LENGTH_SHORT);				                
	            toastInstance.show ();																																	
				insertCarouselView ();
			}				
		}
   }

	private void insertCarouselView() {
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
		    	// Refresh listview; the carousel will now appear in it
				listAdapter.notifyDataSetChanged();
				
		    	// Scroll listview to the top, in case the user moved it while waiting for the ad to load
		    	listView.setSelection(0);
		    }
		});		
	}
	
	// Remove sashimi ad view
	
	private void removeCarouselView() {
		if (carouselView != null) {
			carouselView.releaseAdViews ();
			carouselView = null;
			listAdapter.notifyDataSetChanged();
		}
	}
	
	// Listview adapter for fake content and carousel
	
	public class SashimiListAdapter extends BaseAdapter {
		private final Context context;

		public SashimiListAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			int nCount = mItems.size();
			
			if (carouselView != null)
				nCount++;
			return nCount;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = null;
			int nCount = mItems.size();
			
			if (carouselView != null)
				nCount++;
			
			if (position >= 0 && position < nCount) {
				if (carouselView != null && position == 2) {
					// Show carousel in row #3
					rowView = carouselView;
				}
				else {
					LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					rowView = inflater.inflate(R.layout.adunit_sashimilistitem, parent, false);
					TextView textView = (TextView) rowView.findViewById(R.id.lvItem);
					
					if (carouselView != null && position > 2) {
						// Below carousel row #3, shift position to get the right fake content for this row
						position--;
					}
					
					Item item = mItems.get(position);					
					textView.setText(Html.fromHtml("<b>"+item.getTitle()+"</b><br>"+item.getDescription()));
					textView.setMaxLines(3);
					textView.setEllipsize(TruncateAt.END);					
				}
			}
			
			rowView.setWillNotDraw(false);

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

   // AFAdSDKEventsDelegate implementation

	@Override
	public void onEngageSDKInitialized() {
		// SDK initialized
		Log.i (CLASS_TAG, "onEngageSDKInitialized");
	}

	@Override
	public void onAdUnitInitialized() {
		// Ad unit initialized
		Log.i (CLASS_TAG, "onAdUnitInitialized");
	}

	
	@Override
	public void onAdsLoaded() {
		// Ads metadata downloaded
		Log.i (CLASS_TAG,"onAdsLoaded");
	}

	@Override
	public void onModalAdAvailable() {
		// A modal ad (sushi interstitial) is available
		Log.i (CLASS_TAG, "onModalAdAvailable");
	}
	
	@Override
	public void onInStreamAdAvailable() {
		// One or more in-stream (sashimi) ads are available
		Log.i (CLASS_TAG,"onInStreamAdAvailable");
      displayCarousel ();
	}
		
	@Override
	public void onModalAdPreDisplay() {
		// A modal ad is about to display
		Log.i (CLASS_TAG,"onModalAdPreDisplay");
	}

	@Override
	public void onModalAdDisplayed() {
		// A modal ad is displayed
		Log.i (CLASS_TAG,"onModalAdDisplayed");
	}

	@Override
	public void onModalAdFailedToDisplay(AFAdSDKError errCode) {
		// A modal ad failed to display
		Log.i (CLASS_TAG,"onModalAdFailedToDisplay");
	}

	@Override
	public void onModalAdPreDismiss() {
		// A modal ad is about to close
		Log.i (CLASS_TAG,"onModalAdPreDismiss");
	}

	@Override
	public void onModalAdDismissed() {
		// A modal ad has closed
		Log.i (CLASS_TAG,"onModalAdDismissed");
	}
}

