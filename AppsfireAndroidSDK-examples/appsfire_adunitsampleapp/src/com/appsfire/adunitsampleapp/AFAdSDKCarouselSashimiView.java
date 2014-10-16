package com.appsfire.adunitsampleapp;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;

public class AFAdSDKCarouselSashimiView extends ViewPager {
	// Tag for logging
	private final static String CLASS_TAG = "AFAdSDKCarouselSashimiView";
	
	// Array of Sashimi ad views to show in the carousel
	private ArrayList<AFAdSDKSashimiView> mAdViews = null;
	
	// Activity context
	private Context mContext;
	
	// Current view that receives touches; used to correctly handle the carousel inside a scrollview for instance
	private View mTouchTarget;
	
	// Timer for refreshing the carousel when releasing a drag
	private Timer mRefreshTimer;
	
	// START OF CAROUSEL INTERFACE
	
	/**
	 * Constructor
	 * 
	 * @param context activity context
	 */
	public AFAdSDKCarouselSashimiView(Context context) {
		super(context);
		mContext = context;
	}
	
	/**
	 * Constructor
	 * 
	 * @param context activity context
	 * @param attrs attribute set
	 */
	public AFAdSDKCarouselSashimiView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	/**
	 * Set the Sashimi ad views to display
	 * 
	 * @param items array of Sashimi ad views to display in the carousel
	 */
	public void setAdViewsArray(ArrayList<AFAdSDKSashimiView> items) {
		final AFAdSDKCarouselSashimiView pager = this;
		
		mAdViews = items;
		setAdapter (new CarouselPagerAdapter ());
        if (android.os.Build.VERSION.SDK_INT >= 11) {
        	setPageTransformer (false, new ZoomOutPageTransformer ());
        }
		setOffscreenPageLimit(mAdViews.size());
		
		setOnPageChangeListener(new OnPageChangeListener() {
            private int mPreviousState = ViewPager.SCROLL_STATE_IDLE;

            @Override
            public void onPageSelected(int position) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                	@Override
                	public void run() {
		            	pager.invalidate();
		            	((ViewGroup) pager.getParent()).invalidate();
                	}
                });
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                	@Override
                	public void run() {
		            	pager.invalidate();
		            	((ViewGroup) pager.getParent()).invalidate();
                	}
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mPreviousState == ViewPager.SCROLL_STATE_IDLE) {
                    if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    	// Starting a drag gesture, stop parent from interfering with our touches, if needed
                        mTouchTarget = pager;                        
                    }
                } else {
                    if (state == ViewPager.SCROLL_STATE_IDLE || state == ViewPager.SCROLL_STATE_SETTLING) {
                    	// Finished a drag gesture, parent can capture touches again
                        mTouchTarget = null;
                    }
                    
                    if (state == ViewPager.SCROLL_STATE_SETTLING) {
                        if (mRefreshTimer != null) {
                        	// Unset previous timer if needed
                            mRefreshTimer.cancel ();
                            mRefreshTimer = null;
                        }                    	

                        // Drag released, the pager is settling; redraw ads until the scrolling has stopped
                        mRefreshTimer = new Timer ();
                        mRefreshTimer.schedule(new RedrawTask(), 10, 10);                    	
                    }
                    
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                    	// The pager scrolling has fully settled, don't keep drawing views
                        if (mRefreshTimer != null) {
                            mRefreshTimer.cancel ();
                            mRefreshTimer = null;
                        }                    	
                    }
                }

                mPreviousState = state;
                ((Activity) mContext).runOnUiThread(new Runnable() {
                	@Override
                	public void run() {
    	            	pager.invalidate();
    	            	((ViewGroup) pager.getParent()).invalidate();
                	}
                });
            }
        });		
	}
	
	/**
	 * Dispose of all Sashimi ad view contents. This method MUST be called when the carousel is removed
	 */
	
	public void releaseAdViews () {
		AFAdSDK adSdk = AFSDKFactory.getAFAdSDK();
		
		for (AFAdSDKSashimiView adView : mAdViews) {			
			adSdk.releaseSashimiView (adView);
		}
	}
	
	/**
	 * Set margin to use for showing adjacent ads. Can be called from an AdSizeProvider
	 * 
	 * @param margin new margin
	 */
	
	public void setAdMargin (int margin) {
		setPadding(margin/2, 0, 0, 0);
		setClipToPadding(false);
		setPageMargin(-margin/2);		
	}
	
	// END OF CAROUSEL INTERFACE
	
	// Arbitrate touches between this pager and a potentially scrolling parent
	
	@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mTouchTarget != null) {
            boolean wasProcessed = mTouchTarget.onTouchEvent(ev);

            if (!wasProcessed) {
                mTouchTarget = null;
            }

            return wasProcessed;
        }

        return super.dispatchTouchEvent(ev);
    }

	// To keep lint happy
	
	@Override
	public boolean performClick() {
		return super.performClick();
	}

	// Calculate height from the child ad views, to allow WRAP_CONTENT to work
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int mode = MeasureSpec.getMode(heightMeasureSpec);
	    
	    if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	        int height = 0;
	        for (int i = 0; i < getChildCount(); i++) {
	            View child = getChildAt(i);
	            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	            int h = child.getMeasuredHeight();
	            if (h > height) height = h;
	        }
	        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
	    }
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	// Redraw sashimi views while the pager is settling
	
    private class RedrawTask extends TimerTask {
        @Override
        public void run() {
            ((Activity) mContext).runOnUiThread(new Runnable() {
            	@Override
            	public void run() {
		           	invalidate();
		        	((ViewGroup) getParent()).invalidate();
            	}
            });
        }
  	}
   
    // Pager adapter implementation, feeds ad views to the pager
    
	class CarouselPagerAdapter extends PagerAdapter {
	    @Override
	    public int getCount() {
	    	// Supply number of ad views
	        return mAdViews.size();
	    }

	    public int getItemPosition (Object object) {
	    	return POSITION_UNCHANGED;
	    }
	    
	    @SuppressLint("NewApi")
		public Object instantiateItem(final ViewGroup collection, int position) {
	    	final AFAdSDKSashimiView adView = mAdViews.get(position);
	    	
	    	// Attach ad view to carousel
	    	
 	        adView.setOnAssetsReceivedListener(new AFAdSDKSashimiView.AFAdSDKSashimiAssetsReceivedListener () {
 				public void onAssetsReceived() {
 					// Sashimi assets downloaded, you can act upon this here if needed
 					Log.d (CLASS_TAG, "sashimi assets received for ad " + adView);
  				}
 	        });        
	    	
  	        ((ViewPager) collection).addView(adView, 0);
  	        
  	        if (android.os.Build.VERSION.SDK_INT >= 11) {
  	           adView.setAlpha(1);
  	           adView.setScaleX(1);
  	           adView.setScaleY(1);
  	        }
  	        
 	        return adView;	    	
	    }

	    @Override
	    public void destroyItem (ViewGroup collection, int position, Object view) {
	    	// Detach ad view from carousel
	        ((ViewPager) collection).removeView((AFAdSDKSashimiView) view);
	    }
	    
	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	        return arg0 == ((View) arg1);
	    }
	    
	    @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public void startUpdate(View arg0) {
        }

        @Override
        public void finishUpdate(View arg0) {
        }
    }
	
	// Transform used to scale views down while paging
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
		// Smallest scaling factor that views are scaled down to when moving away
	    private static final float MIN_SCALE = 0.8f;
	 
	    public void transformPage (View view, float position) {
	        int pageWidth = view.getWidth();
	        int pageHeight = view.getHeight();
	 
	        if (position < -1) {
	            // Offscreen to the left
	            view.setScaleX(MIN_SCALE);
	            view.setScaleY(MIN_SCALE);
	            view.setAlpha(1);
	        }
	        else if (position <= 1) {
	            // Scale views down as they move away from the center
	            float scaleFactor = Math.max (MIN_SCALE, 1 - Math.abs(position/2));
	            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
	            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
	            if (position < 0) {
	                view.setTranslationX (horzMargin - vertMargin / 2);
	            }
	            else {
	                view.setTranslationX (-horzMargin + vertMargin / 2);
	            }
	 
	            // Scale the page down (between MIN_SCALE and 1)
	            view.setScaleX(scaleFactor);
	            view.setScaleY(scaleFactor);
	 
	            // Fade the page relative to its size.
	            view.setAlpha(1);
	        }
	        else {
	            // Offscreen to the right
	            view.setScaleX(MIN_SCALE);
	            view.setScaleY(MIN_SCALE);
	            view.setAlpha(1);
	        }
	    }
	}
}
