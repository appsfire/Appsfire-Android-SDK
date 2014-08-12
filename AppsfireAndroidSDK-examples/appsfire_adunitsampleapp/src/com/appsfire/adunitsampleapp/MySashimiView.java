package com.appsfire.adunitsampleapp;

import com.appsfire.adUnitJAR.adUnit.Ad.AFFlatAdType;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFAdSizeProvider;
import com.appsfire.adUnitJAR.sdk.AFAdSDKSashimiView;
import com.appsfire.adUnitJAR.utils.AFUtils;

import android.content.Context;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MySashimiView extends AFAdSDKSashimiView {
	/** Tag for logging */
	private final static String CLASS_TAG = "MySashimiView";
	private final float m_fIconHeightFraction = 0.72f;
	private final float m_fTextHeightFraction = 0.75f;

	/**
	 * Constructor 
	 *
	 * @param context application context
	 * @param adIconBitmap bitmap to use for ad icon
	 * @param adScreenshotBitmap bitmap to use for ad screenshot
	 * @param sizeProvider callback for the app to provide the size of the sashimi view
	 */
	public MySashimiView (Context context, AFAdSizeProvider sizeProvider) {
        super(context, sizeProvider);
        
        m_context = context;
        m_styleMode = AFAdSDKSashimiStyleMode.AFAdSDKSashimiMinimalStyleModeLight;
        
        // Make the view focusable so as to get key events
        setFocusable (true);
        setFocusableInTouchMode (true);
        requestFocus ();
        
        // Create resources for drawing
        
        m_backgroundPaint = new Paint ();
        m_backgroundPaint.setColor (0xff8888ff);
        
        m_appPriceBackgroundPaint = new Paint ();
        m_appPriceBackgroundPaint.setColor (0xffccccff);
        m_appPriceBackgroundPaint.setAntiAlias (true);
        
        m_actionButtonNormalPaint = new Paint ();
        m_actionButtonNormalPaint.setStyle(Style.STROKE);
        m_actionButtonNormalPaint.setColor (Color.WHITE);
        m_actionButtonNormalPaint.setAntiAlias (true);
        
        m_actionButtonPushedPaint = new Paint ();
        m_actionButtonPushedPaint.setStyle(Style.STROKE);
        m_actionButtonPushedPaint.setColor (0xff999999);
        m_actionButtonPushedPaint.setAntiAlias (true);
        
        m_appNameTextPaint = new TextPaint ();
        m_appNameTextPaint.setColor (Color.WHITE);
        m_appNameTextPaint.setAntiAlias (true);
         
        m_appCategoryTextPaint = new TextPaint ();
        m_appCategoryTextPaint.setColor (0xffccccff);
        m_appCategoryTextPaint.setAntiAlias (true);
         
        m_appDetailsTextPaint = new TextPaint ();
        m_appDetailsTextPaint.setColor (Color.WHITE);
        m_appDetailsTextPaint.setAntiAlias (true);
        
        m_appPriceTextPaint = new TextPaint ();
        m_appPriceTextPaint.setColor (Color.BLACK);
        m_appPriceTextPaint.setTextAlign (Paint.Align.CENTER);
        m_appPriceTextPaint.setAntiAlias (true);
       
        m_actionButtonTextPaint = new TextPaint ();
        m_actionButtonTextPaint.setColor (Color.WHITE);
        m_actionButtonTextPaint.setAntiAlias (true);
        
        // Load image assets
        
        m_adBadgeImage = AFUtils.loadStaticBitmap (context, "adbadgedark.png");
    	
    	m_normalTypeface = AFUtils.loadStaticTypeface (context, "affont.ttf");
    	m_boldTypeface = AFUtils.loadStaticTypeface (context, "affontbold.ttf");
    	m_blackTypeface = AFUtils.loadStaticTypeface (context, "affontblack.ttf");
    	
    	if (m_normalTypeface != null) {
    		m_appCategoryTextPaint.setTypeface(m_normalTypeface);
    		m_appDetailsTextPaint.setTypeface(m_normalTypeface);
    	}
    	
    	if (m_boldTypeface != null) {
    		m_appNameTextPaint.setTypeface(m_boldTypeface);
    		m_actionButtonTextPaint.setTypeface(m_boldTypeface);
    	}
    	
    	if (m_blackTypeface != null) {
    		m_appPriceTextPaint.setTypeface(m_blackTypeface);
    	}    	
 	}
		
	/**
	 * Provide assets to the sashimi
	 *
	 * @param adIconBitmap bitmap to use for the ad's icon
	 * @param adScreenshotBitmap bitmap to use for the ad's screenshot
	 */
	public void onAssetsReceived (Bitmap adIconBitmap, Bitmap adScreenshotBitmap) {		
    	// Apply round mask to app icon, and store the result as the icon image
    	
    	BitmapDrawable iconImage;
    	
    	if (adIconBitmap != null)
    		iconImage = new BitmapDrawable (m_context.getResources(), adIconBitmap); 
    	else
    		iconImage = AFUtils.loadStaticBitmap (m_context, "default_icon.png");    	
    	m_roundMaskImage = AFUtils.loadStaticBitmap (m_context, "round_mask.png");   	
    	m_iconImage = new BitmapDrawable (m_context.getResources(), multiplyBitmaps (iconImage.getBitmap(), m_roundMaskImage.getBitmap()));
    	
    	// Get screenshot
    	
    	if (adScreenshotBitmap != null)
	    	m_screenshotImage = new BitmapDrawable (m_context.getResources(), AFUtils.fixBitmapEdges(adScreenshotBitmap)); 
    	else
    		m_screenshotImage = AFUtils.loadStaticBitmap (m_context, "default_screenshot.jpeg");
    	m_screenshotImage.setFilterBitmap(true);
    	m_screenshotImage.setAntiAlias(true);
    	
		super.onAssetsReceived (adIconBitmap, adScreenshotBitmap);
	}
	
	/**
	 * Remove view, free resources
	 */
	public void finish() {	
		// Stop drawing
		m_bFinished = true;
		if (m_iconImage != null) {
			Log.d (CLASS_TAG, "recycle sashimi bitmaps");
			m_iconImage.getBitmap().recycle();
			m_iconImage = null;
		}
	}
	
	/**
	 * Get style mode
	 *
	 * @return style mode
	 */
	public AFAdSDKSashimiStyleMode getStyleMode() {
		return m_styleMode;
	}
	
	/**
	 * Get background color for the content
	 * 
	 * @return background color
	 */
	@Override
	public int getContentBackgroundColor () {
		return m_backgroundPaint.getColor ();
	}
	
	/**
	 * Set background color for the content (defaults to the style's background color)
	 * 
	 * @param nColor new background color, such as Color.BLUE
	 */
	@Override
	public void setContentBackgroundColor (int nColor) {
		m_backgroundPaint.setColor (nColor);
		
		// Update background gradient as well
		RadialGradient gradient = new RadialGradient(m_nTotalViewWidth/2, m_nTotalViewHeight/2, Math.max (m_nTotalViewWidth,m_nTotalViewHeight)/2, 
													 m_backgroundPaint.getColor(), (m_backgroundPaint.getColor() & 0x00ffffff) / 2 | 0xff000000,
													 android.graphics.Shader.TileMode.CLAMP);
        m_backgroundPaint.setShader (gradient);
	}
		
	/**
	 * Get the color of the application title
	 * 
	 * @return label color
	 */
	@Override
	public int getTitleTextColor () {
		return m_appNameTextPaint.getColor();
	}
	
	/**
	 * Set the color of the application title (defaults to the style's title color)
	 * 
	 * @param nColor new text color, such as Color.WHITE
	 */
	@Override
	public void setTitleTextColor (int nColor) {
		m_appNameTextPaint.setColor (nColor);
	}
	
	/**
	 * Get the color of the application category
	 * 
	 * @return label color
	 */
	@Override
	public int getCategoryTextColor () {
		return m_appCategoryTextPaint.getColor();
	}
	
	/**
	 * Set the color of the application category (defaults to the style's category color)
	 * 
	 * @param nColor new text color, such as Color.WHITE
	 */
	@Override
	public void setCategoryTextColor (int nColor) {	
		m_appCategoryTextPaint.setColor (nColor);
	}
	
	/**
	 * Get the color of the application tagline
	 * 
	 * @return label color
	 */
	@Override
	public int getTaglineTextColor () {
		return m_appDetailsTextPaint.getColor ();
	}
	
	/**
	 * Set the color of the application tagline (defaults to the style's tagline color)
	 * 
	 * @param nColor new text color, such as Color.WHITE
	 */
	@Override
	public void setTaglineTextColor (int nColor) {		
		m_appDetailsTextPaint.setColor (nColor);
	}
	
	/**
	 * Get the color of the application's price
	 * 
	 * @return label color
	 */
	@Override
	public int getPriceTextColor () {
		return m_appPriceTextPaint.getColor ();
	}
	
	/**
	 * Set the color of the application's price (defaults to the style's price color)
	 * 
	 * @param nColor new text color, such as Color.WHITE
	 */
	@Override
	public void setPriceTextColor (int nColor) {
		m_appPriceTextPaint.setColor (nColor);
	}
	
	/**
	 * Get background color for the price tag
	 * 
	 * @return background color
	 */
	@Override
	public int getPriceBackgroundColor () {
		return m_appPriceBackgroundPaint.getColor ();
	}
	
	/**
	 * Set background color for the price tag (defaults to the style's price tag color)
	 * 
	 * @param nColor new background color, such as 0xffb3c833
	 */
	@Override
	public void setPriceBackgroundColor (int nColor) {
		m_appPriceBackgroundPaint.setColor (nColor);
	}
	
	/**
	 * Get the color of the call to action button
	 * 
	 * @param bPushed true to get the color in the pushed-down state, false to get the color in the normal state
	 *
	 * @return button color
	 */
	public int getCallToActionButtonColor (boolean bPushed) {
		if (bPushed)
			return m_actionButtonPushedPaint.getColor();
		else
			return m_actionButtonNormalPaint.getColor();
	}
	
	/**
	 * Set the color of the application title (defaults to the style's title color)
	 * 
	 * @param bPushed true to set the color in the pushed-down state, false to set the color in the normal state
	 * @param nColor new button color, such as Color.WHITE
	 */
	public void setCallToActionButtonColor (boolean bPushed, int nColor) {		
		if (bPushed)
			m_actionButtonPushedPaint.setColor(nColor);
		else
			m_actionButtonNormalPaint.setColor(nColor);
	}
	
	/**
	 * React to call to action button (Download now) being clicked
	 */
	void onActionButtonClicked () {
		onAdClicked ();
	}
	
	/**
	 * Handle initial size or size change
	 *
	 * @param w new width
	 * @param h new height
	 * @param oldw previous width
	 * @param oldh old height
	 */
	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		int nImageWidth, nImageHeight, nRescaledImageWidth, nRescaledImageHeight, nTextSize;
		int nButtonWidth, nButtonHeight, nTextWidth, nTextHeight;
		int nMaxWidth = -1, nMaxHeight = -1, nDimForAdBadge = -1;
		
		// Mark size as known, to start drawing
		m_bSizeKnown = true;
		
        // Get display dimensions so as to figure out the maximum scaling for the ad
		if (m_context != null) {
	        WindowManager wm = (WindowManager) m_context.getSystemService (Context.WINDOW_SERVICE);
	        
	        if (wm != null) {
	           Display display = wm.getDefaultDisplay();
	           
	           if (display != null) {
	              DisplayMetrics dm = new DisplayMetrics ();
	              double fXDots;
	              
	              display.getMetrics (dm);
	              fXDots = dm.widthPixels * dm.densityDpi;
	              if (fXDots < 115200) fXDots = 115200;
	              if (fXDots > 192000) fXDots = 192000;
	              
	              nMaxWidth = (int) (dm.widthPixels * (0.9 - 0.3 * (fXDots - 115200.0) / (192000.0 - 115200.0)));
	              if (getFlatAdType() == AFFlatAdType.AFFlatAdTypeSashimi_ge)
		              nMaxHeight = (int) (nMaxWidth / 2.25);
	              else
		              nMaxHeight = nMaxWidth / 2;
	              nDimForAdBadge = Math.max(dm.widthPixels, dm.heightPixels);
	              if (dm.widthPixels < dm.heightPixels)
	            	  nDimForAdBadge = (int) (nDimForAdBadge * 0.8);
	           }
	        }
		}
		
		// Update background gradient
		RadialGradient gradient = new RadialGradient(w/2, h/2, Math.max (w,h)/2, 
													 m_backgroundPaint.getColor(), (m_backgroundPaint.getColor() & 0x00ffffff) / 2 | 0xff000000,
													 android.graphics.Shader.TileMode.CLAMP);
        m_backgroundPaint.setShader (gradient);
        
		// Store view bounds
		
		m_nViewX = 0;
		m_nViewY = 0;
		m_nViewWidth = w;
		m_nViewHeight = h;
		m_nTotalViewWidth = w;
		m_nTotalViewHeight = h;
		
		if (nMaxWidth > 0 && nMaxHeight > 0) {
			if (m_nViewWidth > nMaxWidth) {
				m_nViewX = (m_nViewWidth - nMaxWidth) / 2;
				m_nViewWidth = nMaxWidth;
			}
			
			if (m_nViewHeight > nMaxHeight) {
				if (getFlatAdType() == AFFlatAdType.AFFlatAdTypeSashimi_ge) {
					// Extended format, put the ad content at the bottom
					m_nViewY = (m_nViewHeight - nMaxHeight);
					m_nViewHeight = nMaxHeight;
				}
				else {
					// Minimal format, center vertically
					m_nViewY = (m_nViewHeight - nMaxHeight) / 2;
					m_nViewHeight = nMaxHeight;
				}
			}
		}
		
		Log.d (CLASS_TAG, "view changed to " + m_nViewX + "," + m_nViewY + " " + w + " x " + h);
		
		// Decide on text height depending on the view dimensions
		
		if (m_iconImage != null) {
			nImageWidth = m_iconImage.getBitmap().getWidth ();
			nImageHeight = m_iconImage.getBitmap().getHeight ();
		}
		else {
			nImageWidth = 1;
			nImageHeight = 1;
		}
		nRescaledImageHeight = (int) (m_nViewHeight * m_fTextHeightFraction);
		nRescaledImageWidth = (int) (nImageWidth * nRescaledImageHeight / nImageHeight);
		
		nTextSize = (int) (nRescaledImageHeight * 0.14 * 14 / 11);
		if (nTextSize < 8) nTextSize = 8;
		if (nTextSize > 90) nTextSize = 90;
        m_appNameTextPaint.setTextSize(nTextSize);
		
		nTextSize = (int) (nRescaledImageHeight * 0.14);
		if (nTextSize < 8) nTextSize = 8;
		if (nTextSize > 90) nTextSize = 90;
        m_appCategoryTextPaint.setTextSize(nTextSize);
        
		nTextSize = (int) (nRescaledImageHeight * 0.14 * 12 / 11);
		if (nTextSize < 8) nTextSize = 8;
		if (nTextSize > 90) nTextSize = 90;
        m_appDetailsTextPaint.setTextSize(nTextSize);
        
        if (m_normalTypeface != null)
			nTextSize = (int) (nRescaledImageHeight * 0.18);
        else
			nTextSize = (int) (nRescaledImageHeight * 0.20);
		if (nTextSize < 8) nTextSize = 8;
		if (nTextSize > 90) nTextSize = 90;
        m_appPriceTextPaint.setTextSize(nTextSize);     
        		
		// Fit the call to action text on the button
		
        if (m_iconImage != null) {
			nImageWidth = m_iconImage.getBitmap().getWidth ();
			nImageHeight = m_iconImage.getBitmap().getHeight ();
        }
        else {
        	nImageWidth = 1;
        	nImageHeight = 1;
        }
		nRescaledImageHeight = (int) (m_nViewHeight * m_fIconHeightFraction);
		nRescaledImageWidth = (int) (nImageWidth * nRescaledImageHeight / nImageHeight);

		nButtonWidth = (int) (m_nViewWidth * 0.5);
		nButtonHeight = (int) (nRescaledImageHeight * 0.40);
		if (nButtonHeight < 8) nButtonHeight = 8;
		if (nButtonHeight > 150) nButtonHeight = 150;
				
		CharSequence buttonTextString = AFUtils.getActionButtonText();
		float fButtonTextSize = (float) nButtonHeight;
		if (fButtonTextSize > (int) (nRescaledImageHeight * 0.14 * 12 / 11))
			fButtonTextSize = (int) (nRescaledImageHeight * 0.14 * 12 / 11);
		m_actionButtonTextPaint.setTextSize (nButtonHeight);
		do {
			nTextWidth = (int) m_actionButtonTextPaint.measureText (buttonTextString.toString());
			nTextHeight = AFUtils.getTextHeight (buttonTextString, m_actionButtonTextPaint, nButtonWidth);
			if (nTextWidth >= (int)(nButtonWidth * 0.8) ||
				nTextHeight >= (int)(nButtonHeight * 0.6)) {
				fButtonTextSize -= 0.5f;
				m_actionButtonTextPaint.setTextSize (fButtonTextSize);
			}
		} while (nTextWidth >= (int)(nButtonWidth * 0.8) ||
				 nTextHeight >= (int)(nButtonHeight * 0.6));
		
		if (nDimForAdBadge > 0 && m_nViewWidth > 0) {
	        m_fAdBadgeWidthFraction = (nDimForAdBadge * 0.075f) / ((float) m_nViewWidth);
		}
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {
	    if (event.getAction() == MotionEvent.ACTION_DOWN) {
	    	setActionButtonState (true);
	    }
	    
	    if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
	    	setActionButtonState (false);
	    	
	    	if (event.getAction() == MotionEvent.ACTION_UP) {
		        // Clicked the view
	    		Log.d (CLASS_TAG, "sashimi view tapped");
	    		onActionButtonClicked ();
	    	}
	    }
	    
	    return true;
	}
	
	/**
	 * Set the action button state
	 *
	 * @param bPushed true if pushed down, false if up
	 */
	private void setActionButtonState (final boolean bPushed) {
		((Activity) m_context).runOnUiThread (new Runnable() {
			public void run() {
				m_bActionButtonPushed = bPushed;
				invalidate ();
			}
		});
	}
	
	/**
	 * Draw view group (like onDraw for a single view)
	 * 
	 * @param canvas graphics canvas
	 */
	@Override
	protected void dispatchDraw (Canvas canvas) {
		if (m_bSizeKnown && !m_bFinished && m_iconImage != null && m_roundMaskImage != null && m_adBadgeImage != null &&
			m_iconImage.getBitmap().isRecycled() == false) {
			int nImageWidth, nImageHeight, nRescaledImageWidth, nRescaledImageHeight, nIconX, nIconY, nBannerWidth, nBannerHeight, nBadgeX, nBadgeY, nMargin;
			int nButtonX, nButtonY, nButtonWidth, nButtonHeight, nTextX, nAppTitleTextY, nAppCategoryTextY, nAppDescriptionTextY;
			int nRescaledBadgeImageWidth, nRescaledBadgeImageHeight;
			float cx, cy, y1, y2, gap, h1, h2, h3;
			Paint.FontMetrics fmTitle, fmPrice;
			
			// Paint ad content background
			canvas.drawRect (new RectF (0, 0, m_nTotalViewWidth, 0 + m_nTotalViewHeight), m_backgroundPaint);			
			
			// Draw app icon
			
			nImageWidth = m_iconImage.getBitmap().getWidth ();
			nImageHeight = m_iconImage.getBitmap().getHeight ();
			nRescaledImageHeight = (int) (m_nViewHeight * m_fIconHeightFraction);
			nRescaledImageWidth = (int) (nImageWidth * nRescaledImageHeight / nImageHeight);

			nIconX = (int) (m_nTotalViewWidth * 0.035);
			nIconY = m_nViewY + (int) ((m_nViewHeight * 0.5) - (nRescaledImageHeight / 2));
			nMargin = (int) (nRescaledImageWidth / 200);
			if (nMargin < 1) nMargin = 1;
			m_iconImage.setBounds ((int) nIconX, (int) nIconY, (int) (nIconX + nRescaledImageWidth), (int) (nIconY + nRescaledImageHeight));
			m_iconImage.draw (canvas);	
			
			// Draw action button
			
			nImageWidth = m_iconImage.getBitmap().getWidth ();
			nImageHeight = m_iconImage.getBitmap().getHeight ();
			nRescaledImageHeight = (int) (m_nViewHeight * m_fIconHeightFraction);
			nRescaledImageWidth = (int) (nImageWidth * nRescaledImageHeight / nImageHeight);

			nButtonWidth = (int) (m_nViewWidth * 0.5);
			nButtonHeight = (int) (nRescaledImageHeight * 0.40);
			if (nButtonHeight < 8) nButtonHeight = 8;
			if (nButtonHeight > 150) nButtonHeight = 150;
			int nTextHeight;
			nTextHeight = AFUtils.getTextHeight (AFUtils.getActionButtonText(), m_actionButtonTextPaint, nButtonWidth);
			if (nButtonHeight > (nTextHeight / 0.6))
				nButtonHeight = (int) (nTextHeight / 0.6);
			
			// Calculate location of "Ad" badge

			nImageWidth = m_adBadgeImage.getBitmap().getWidth ();
			nImageHeight = m_adBadgeImage.getBitmap().getHeight ();
			nRescaledBadgeImageWidth = (int) (m_nViewWidth * m_fAdBadgeWidthFraction);
			nRescaledBadgeImageHeight = (int) (nImageHeight * nRescaledBadgeImageWidth / nImageWidth);
			nBadgeX = m_nTotalViewWidth - (int) (m_nTotalViewWidth * 0.01) - nRescaledBadgeImageWidth;
			nBadgeY = (int) (m_nTotalViewHeight * 0.02);

			// Draw call to action button
			
			nButtonX = (int) (nIconX + nRescaledImageWidth + (m_nTotalViewWidth * 0.025));
			nButtonY = (int) (nIconY + nRescaledImageHeight - nButtonHeight + m_nViewHeight * 0);
			
			if (m_bActionButtonPushed) {
				// Pushed down state
				canvas.drawRoundRect(new RectF(nButtonX, nButtonY, nButtonX + nButtonWidth, nButtonY + nButtonHeight), (int) (nButtonWidth * 0.04), (int) (nButtonWidth * 0.04), m_actionButtonPushedPaint);
			}
			else {
				// Normal enabled state
				canvas.drawRoundRect(new RectF(nButtonX, nButtonY, nButtonX + nButtonWidth, nButtonY + nButtonHeight), (int) (nButtonWidth * 0.04), (int) (nButtonWidth * 0.04), m_actionButtonNormalPaint);
			}
			
			StaticLayout slCTA = new StaticLayout(AFUtils.getActionButtonText(), m_actionButtonTextPaint, nButtonWidth, Layout.Alignment.ALIGN_CENTER, 1, 1, false);
			canvas.save ();
			canvas.translate (nButtonX, (int) (nButtonY + ((nButtonHeight - slCTA.getHeight()) / 2)));
			slCTA.draw (canvas);
			canvas.restore ();
			
			// Draw advertised app name
			
			fmTitle = m_appNameTextPaint.getFontMetrics();
			nTextX = (int) (nIconX + nRescaledImageWidth + (m_nTotalViewWidth * 0.025));
			nAppTitleTextY = (int) (nIconY - m_nViewHeight * 0);
			canvas.drawText(getTitleText().toString(), nTextX, nAppTitleTextY + m_appNameTextPaint.getTextSize() - fmTitle.bottom, m_appNameTextPaint); 							
			
			int nDescriptionMaxWidth;
			if (canDrawPrice ())
				nDescriptionMaxWidth = (int) (nBadgeX - nTextX);
			else
				nDescriptionMaxWidth = (int) (m_nTotalViewWidth - (m_nTotalViewWidth * 0.025) - nTextX);
			
			String strDescription = getTaglineText().toString();
			StaticLayout slDescription;
			do {
				slDescription = new StaticLayout(strDescription, m_appDetailsTextPaint, nDescriptionMaxWidth, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
			
				if (slDescription.getLineCount() > 1) {		
					String workingText = strDescription.substring(0, slDescription.getLineEnd(2 - 1)).trim();
                    int lastSpace = workingText.lastIndexOf(' ');
                    if (lastSpace == -1) {
                        break;
                    }
                    strDescription = workingText.substring(0, lastSpace) + "...";
				}
			} while (slDescription.getLineCount() > 1);
		 	
			int nCategoryMaxWidth = nDescriptionMaxWidth;
			StaticLayout slCategory = new StaticLayout(getCategoryText().toString(), m_appCategoryTextPaint, nCategoryMaxWidth, Layout.Alignment.ALIGN_NORMAL, 1, 1, false);
			
			h1 = m_appNameTextPaint.getTextSize();
			h2 = slCategory.getHeight();
			h3 = slDescription.getHeight();
			y1 = nAppTitleTextY + h1;
			y2 = nButtonY;
			
			// Draw advertised app category
			
			gap = (y2 - y1) - h2 - h3;
			nAppCategoryTextY = (int) (nAppTitleTextY + h1 + gap / 3);			
			
			canvas.save ();
			canvas.translate (nTextX, nAppCategoryTextY);
			slCategory.draw (canvas);
			canvas.restore ();
			
			// Draw advertised app description
						
			nAppDescriptionTextY = (int) (nAppCategoryTextY + m_appDetailsTextPaint.getTextSize() + gap / 3);
			
			canvas.save ();
			canvas.translate (nTextX, nAppDescriptionTextY);
			slDescription.draw (canvas);
			canvas.restore ();
						
			// Draw "Ad" badge

			m_adBadgeImage.setBounds ((int) nBadgeX, (int) nBadgeY, (int) (nBadgeX + nRescaledBadgeImageWidth), (int) (nBadgeY + nRescaledBadgeImageHeight));
			m_adBadgeImage.draw (canvas);	
			
			// Draw price banner
			
			cx = (int) (m_nTotalViewWidth * 0.965);
			if (getFlatAdType() == AFFlatAdType.AFFlatAdTypeSashimi_ge)
				cy = (int) (m_nViewY + m_nViewHeight * 0.85);
			else
				cy = (int) (m_nTotalViewHeight * 0.85);
			
			fmPrice = m_appPriceTextPaint.getFontMetrics();
			float fontHeight = fmPrice.bottom - fmPrice.top;
			nBannerWidth = (int) (m_nViewWidth * 0.5);
			nBannerHeight = (int) (fontHeight + (int) (m_nViewHeight * 0.041));

			if (canDrawPrice ()) {
				canvas.save ();
				canvas.translate (cx, cy);
				canvas.rotate (-45.0f, 0, 0);
				canvas.drawRect (new RectF (-nBannerWidth / 2, -nBannerHeight, nBannerWidth / 2, 0), m_appPriceBackgroundPaint);
				
				canvas.drawText(getPriceText().toString(), 0, -(m_appPriceTextPaint.descent() + m_appPriceTextPaint.ascent()) / 2 - (nBannerHeight / 2), m_appPriceTextPaint);
				canvas.restore();			
			}
		}		
	}
	
	/** Application context */
	private Context m_context;
	
	/** Display style mode */
	private AFAdSDKSashimiStyleMode m_styleMode;
	
	/** Brush for drawing the ad background */
	private Paint m_backgroundPaint;
	
	/** Brush for drawing the price banner */
	private Paint m_appPriceBackgroundPaint;
	
	/** Brush for drawing the call to action button in normal state */
	private Paint m_actionButtonNormalPaint;
	
	/** Brush for drawing the call to action button in pushed state */
	private Paint m_actionButtonPushedPaint;
	
	/** Brush for drawing the action button's text */
	private TextPaint m_actionButtonTextPaint;
	
	/** true if ad is finished and being cleaned up */
	private boolean m_bFinished;
	
	/** Brush for drawing the app name text */
	private TextPaint m_appNameTextPaint;
	
	/** Brush for drawing the app category text */
	private TextPaint m_appCategoryTextPaint;
	
	/** Brush for drawing the app details text */
	private TextPaint m_appDetailsTextPaint;
	
	/** Brush for drawing the app price text */
	private TextPaint m_appPriceTextPaint;
	
	/** Image to use for the "Ad" badge */
	private BitmapDrawable m_adBadgeImage;
	
	/** Round icon mask (also used for drawing the border) */
	private BitmapDrawable m_roundMaskImage;
	
	/** Image to use for the app icon */
	private BitmapDrawable m_iconImage;
	
	/** Image to use for the app screenshot */
	private BitmapDrawable m_screenshotImage;
	
	/** Normal typeface */
	private Typeface m_normalTypeface;
	
	/** Bold typeface */
	private Typeface m_boldTypeface;
	
	/** Black typeface */
	private Typeface m_blackTypeface;
	
	/** true if action button is currently pushed */
	private boolean m_bActionButtonPushed;
		
	/** true if the size of the view is known yet */
	private boolean m_bSizeKnown;
	
	/** X coordinate of top, left of the view's content */
	private int m_nViewX;
	
	/** Y coordinate of top, left of the view's content */
	private int m_nViewY;
	
	/** Width of the view's content */
	private int m_nViewWidth;
	
	/** Height of the view's content */
	private int m_nViewHeight;
	
	/** Total width of the view including borders */
	private int m_nTotalViewWidth;
	
	/** Total height of the view including borders */
	private int m_nTotalViewHeight;
	
	/** Fraction of the view width to use for the badge */
	private float m_fAdBadgeWidthFraction = 0.125f;
}
