package com.appsfire.simplejniapp;

import java.util.Arrays;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.appsfire.adUnitJAR.sdk.AFAdSDK;
import com.appsfire.adUnitJAR.sdk.AFAdSDK.AFSDKFeature;
import com.appsfire.adUnitJAR.sdkimpl.AFSDKFactory;

public class MyGLActivity extends Activity {
	// Create instance of the Appsfire Ad SDK
	private static AFAdSDK adSdk;
	
	// Tag for logging messages
	public static String CLASS_TAG = "AFSimpleJNIApp";
	
	static {
		// Load native libraries: first the Appsfire Ad SDK JNI lib, then your own
		System.loadLibrary("afadunitnative");
		System.loadLibrary("simplejniapp");		
	}
	
	// Create activity
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Request fullscreen display
        this.requestWindowFeature (Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		// Set application context and monetization features for the SDK, but don't prepare; this will be done by the C++ code
        adSdk = AFSDKFactory.getAFAdSDK().
        		  setAppContext(this).
				  setFeatures(Arrays.asList(AFSDKFeature.AFSDKFeatureMonetization));
        
        // Create GL view and set it as the app's content
        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);       
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
		adSdk.onStop ();		
		super.onStop();
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        simplejniapp.onPause ();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
        simplejniapp.onResume ();
    }
    
	private GLSurfaceView mGLView;
}

class MyGLSurfaceView extends GLSurfaceView {
    public MyGLSurfaceView(Context context) {
        super(context);
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);
        setZOrderOnTop(false);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public boolean onTouchEvent(final MotionEvent event) {
        queueEvent(new Runnable(){
            public void run() {
                int nAction = 0, i, nPointerCount;
          
                // Send touch event to native C++ code
                
                switch (event.getAction () & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                   nAction = 1;
                   break;
                case MotionEvent.ACTION_MOVE:
                   nAction = 2;
                   break;
                case MotionEvent.ACTION_UP:
                   nAction = 3;
                   break;
                case MotionEvent.ACTION_CANCEL:
                   nAction = 4;
                   break;
                default:
                   break;
                }
                
                nPointerCount = event.getPointerCount();
                for (i = 0; i < nPointerCount; i++) {
                	simplejniapp.onTouchEvent (event.getPointerId(i), nPointerCount, event.getX(i), event.getY(i), nAction);
                }
             }});
            return true;
        }

        MyGLRenderer mRenderer;
}

class MyGLRenderer implements GLSurfaceView.Renderer {
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    	simplejniapp.onSurfaceCreated();
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {
    	simplejniapp.onSurfaceChanged(w, h);
    }

    public void onDrawFrame(GL10 gl) {
    	simplejniapp.onDrawFrame();
    }
}

class simplejniapp {
	// C++ methods defined by this demo JNI app, see main.cpp
    public static native void onPause ();
    public static native void onResume ();
    public static native void onSurfaceCreated ();
    public static native void onSurfaceChanged (int width, int height);
    public static native void onTouchEvent (int nPointerId, int nPointerCount, float x, float y, int nAction);
    public static native void onDrawFrame ();
}
