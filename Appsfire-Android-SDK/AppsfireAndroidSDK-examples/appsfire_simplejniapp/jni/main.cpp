#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include <time.h>
#include <fcntl.h>
#include <wchar.h>
#include <sys/stat.h>
#include <pthread.h>
#include <jni.h>
#include <errno.h>
#include <EGL/egl.h>
#include <GLES/gl.h>
#include <android/log.h>
#include "afAdUnitJNI.h"
#include "BMPDecoder.h"

#define LOGI(...) ((void)__android_log_print(ANDROID_LOG_INFO, "simplejniapp", __VA_ARGS__))
#define LOGW(...) ((void)__android_log_print(ANDROID_LOG_WARN, "simplejniapp", __VA_ARGS__))

/* true for debug mode, false for production mode (set to false when distributing your app) */
#define IS_AFADSDK_DEBUG true

/* App key */
#define YOUR_API_KEY "YOUR_API_KEY_HERE"

/**
 * Shared state for our app.
 */
struct myappdata {
	bool sdkInitialized;
    int animating;
    int nLastClock;
    int32_t width;
    int32_t height;
    float angle;
    GLuint nTexId;
    int nBmpWidth, nBmpHeight, nTexWidth, nTexHeight;
};

static struct myappdata g_data;

extern "C" {
	// "Tap to display.." tembedded BMP file data
	extern const unsigned char g_texData[];
};

// Appsfire Ad SDK event handler

void handleAFSDKEvent (AfAdSDKEventType nSdkEvent, void *lpOpaqueData) {
	switch (nSdkEvent) {
	case AFSDKEventAdsLoaded: 				LOGI ("AF SDK event AFSDKEventAdsLoaded"); break;
	case AFSDKEventAdUnitInitialized:		LOGI ("AF SDK event AFSDKEventAdsLoaded"); break;
	case AFSDKEventEngageSDKInitialized:	LOGI ("AF SDK event AFSDKEventEngageSDKInitialized"); break;
	case AFSDKEventModalAdAvailable:		LOGI ("AF SDK event AFSDKEventModalAdAvailable"); break;
	case AFSDKEventModalAdFailedToDisplay:	LOGI ("AF SDK event AFSDKEventModalAdFailedToDisplay"); break;
	case AFSDKEventModalAdCancelled:		LOGI ("AF SDK event AFSDKEventModalAdCancelled"); break;
	case AFSDKEventModalAdUnknownType:		LOGI ("AF SDK event AFSDKEventModalAdUnknownType"); break;
	case AFSDKEventModalAdPreDisplay:		LOGI ("AF SDK event AFSDKEventModalAdPreDisplay"); break;
	case AFSDKEventModalAdDisplayed:		LOGI ("AF SDK event AFSDKEventModalAdDisplayed"); break;
	case AFSDKEventModalAdPreDismiss:		LOGI ("AF SDK event AFSDKEventModalAdPreDismiss"); break;
	case AFSDKEventModalAdDismissed:		LOGI ("AF SDK event AFSDKEventModalAdDismissed"); break;
	default:								LOGI ("AF SDK event %d", nSdkEvent); break;
	}
}

// Create "Tap to display.." texture from embedded BMP file data

static void create_texture() {
	BMPDecoder bmp;
	int i, nPower;
	const unsigned int *lpBmpPixels;
	unsigned int *lpTexPixels;

	if (g_data.nTexId) {
		// Re-creating; delete previous texture
		glDeleteTextures (1, &g_data.nTexId);
		g_data.nTexId = 0;
	}

	// Decode from BMP
	bmp.decode (g_texData, 145230);
	g_data.nBmpWidth = bmp.getImageWidth ();
	g_data.nBmpHeight = bmp.getImageHeight ();

	// Find nearest power of 2 for width and height

    for (nPower = 30; (1 << nPower) >= g_data.nBmpWidth && nPower >= 0; nPower--) ;
    nPower++;
    g_data.nTexWidth = 1 << nPower;

    for (nPower = 30; (1 << nPower) >= g_data.nBmpHeight && nPower >= 0; nPower--) ;
    nPower++;
    g_data.nTexHeight = 1 << nPower;

    // Copy pixels into texture, aligned to the pow2 size

    lpTexPixels = new unsigned int [g_data.nTexWidth * g_data.nTexHeight];
    memset (lpTexPixels, 0, g_data.nTexWidth * g_data.nTexHeight * sizeof (unsigned int));

    lpBmpPixels = bmp.getPixels();
    for (i = 0; i < g_data.nBmpHeight; i++) {
       memcpy (lpTexPixels + i * g_data.nTexWidth, lpBmpPixels + i * g_data.nBmpWidth, g_data.nBmpWidth * sizeof (unsigned int));
    }

    // Create texture

    glEnable (GL_TEXTURE_2D);
    glPushMatrix ();

    glLoadIdentity ();
    glFlush ();
    glGenTextures (1, &g_data.nTexId);
    glBindTexture (GL_TEXTURE_2D, g_data.nTexId);
	glTexParameterx (GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameterx (GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexParameterx (GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	glTexParameterx (GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexImage2D (GL_TEXTURE_2D, 0, GL_RGBA, g_data.nTexWidth, g_data.nTexHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE, lpTexPixels);

    glPopMatrix ();

    delete [] lpTexPixels;
}

// Methods exported to MyGLActivity.java by this C++ code

extern "C" {
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onPause(JNIEnv * env, jobject obj);
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onResume(JNIEnv * env, jobject obj);
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onSurfaceCreated(JNIEnv * env, jobject obj);
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onSurfaceChanged(JNIEnv * env, jobject obj, int width, int height);
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onTouchEvent(JNIEnv * env, jobject obj, int nPointerId, int nPointerCount, float x, float y, int nAction);
   JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onDrawFrame(JNIEnv * env, jobject obj);
};

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onPause(JNIEnv * env, jobject obj) {
    // Lost focus
	g_data.animating = 0;
}

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onResume(JNIEnv * env, jobject obj) {
	struct timeval tv;

	// Gained focus

	g_data.animating = 1;
    gettimeofday (&tv, NULL);
    g_data.nLastClock = tv.tv_sec * 1000 + (tv.tv_usec / 1000);

    AFAdSDK_setEventHandler (handleAFSDKEvent, NULL);
    if (!g_data.sdkInitialized) {
    	LOGI ("initialize Appsfire SDK from C++ code");
    	g_data.sdkInitialized = true;
    	AFAdSDK_setAPIKey(YOUR_API_KEY);
		AFAdSDK_setDebugModeEnabled (IS_AFADSDK_DEBUG);
		AFAdSDK_prepare ();
    }
}

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onSurfaceCreated(JNIEnv * env, jobject obj) {
	// (Re)create texture when GL surface is created
	create_texture ();
}

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onSurfaceChanged(JNIEnv * env, jobject obj, int width, int height) {
	g_data.width = width;
    g_data.height = height;

    // Initialize GL state
    glHint (GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glShadeModel (GL_SMOOTH);
    glDisable (GL_CULL_FACE);
    glDisable (GL_DEPTH_TEST);
    glDisable (GL_LIGHTING);
    glDisable (GL_TEXTURE_2D);
    glDisable (GL_BLEND);
    glViewport (0, 0, width, height);
    LOGI ("screen size is now %dx%d", width, height);
}

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onTouchEvent(JNIEnv * env, jobject obj, int nPointerId, int nPointerCount, float x, float y, int nAction) {
	// Touch input event

	LOGI ("touch action %d", nAction);
	if (nAction == 1) {
		AFAdSDKResult nResult;

		// Touch down - ask native SDK to show Sushi

		LOGI ("Sushi available: %s", AFAdSDK_isAModalAdOfTypeAvailable (AFAdSDKModalTypeSushi) ? "yes" : "no");
		LOGI ("request Sushi interstitial ad");
		nResult = AFAdSDK_requestModalAd (AFAdSDKModalTypeSushi);
		LOGI ("requestSushiModalAd result: %d", nResult);
	}
}

JNIEXPORT void JNICALL Java_com_appsfire_simplejniapp_simplejniapp_onDrawFrame(JNIEnv * env, jobject obj) {
    float fVertexArray[8], fUvArray[8];
    float dx, dy, dx2, dy2, fRescaledWidth, fRescaledHeight, fScale;
    struct timeval tv;
    int nCurClock, nTimeElapsed;

    // Figure out how much time has elapsed, for animation

    gettimeofday (&tv, NULL);
    nCurClock = (tv.tv_sec * 1000 + (tv.tv_usec / 1000));
    nTimeElapsed = nCurClock - g_data.nLastClock;
    g_data.nLastClock = nCurClock;

    if (nTimeElapsed < 0) nTimeElapsed = 0;
    if (nTimeElapsed > 100) nTimeElapsed = 100;

    g_data.angle += (float) (360.0 * nTimeElapsed / 5000.0);

    // Draw frame

    fRescaledWidth = floorf ((float) g_data.width * 0.95f);
    fRescaledHeight = floorf ((float) g_data.nBmpHeight * fRescaledWidth / (float) g_data.nBmpWidth);

    dx = floorf (((float) g_data.width - fRescaledWidth) / 2);
    dy = floorf (((float) g_data.height - fRescaledHeight) / 2);
    dx2 = dx + fRescaledWidth;
    dy2 = dy + fRescaledHeight;

    glMatrixMode (GL_PROJECTION);
    glLoadIdentity ();
    glOrthof (0, (float) g_data.width, (float) g_data.height, 0, -100, 100);

    glColorMask (1, 1, 1, 1);
    glClearColor (0.6f, 0.2f, 0.05f, 1.0f);
    glClear (GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    glColorMask (1, 1, 1, 0);

    fScale = 0.8f + (sinf (g_data.angle * 3.14159265358979323846f / 180.0f) + 1) * 0.5f * 0.2f;

    glMatrixMode (GL_MODELVIEW);
    glLoadIdentity ();
    glTranslatef ((float) (g_data.width / 2), (float) (g_data.height / 2), 0);
    glScalef (fScale, fScale, 1.0f);
    glTranslatef (-((float) (g_data.width / 2)), -((float) (g_data.height / 2)), 0);

    glEnable (GL_TEXTURE_2D);
    glBindBuffer (GL_ELEMENT_ARRAY_BUFFER, 0);
    glEnableClientState (GL_VERTEX_ARRAY);
    glEnableClientState (GL_TEXTURE_COORD_ARRAY);
    glDisableClientState (GL_COLOR_ARRAY);
    glVertexPointer (2, GL_FLOAT, 0, fVertexArray);
    glTexCoordPointer (2, GL_FLOAT, 0, fUvArray);

    glBindTexture (GL_TEXTURE_2D, g_data.nTexId);

    glEnable (GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glColor4f (1, 1, 1, 1);

    fVertexArray[0] = dx;
    fVertexArray[1] = dy;
    fVertexArray[2] = dx2;
    fVertexArray[3] = dy;
    fVertexArray[4] = dx;
    fVertexArray[5] = dy2;
    fVertexArray[6] = dx2;
    fVertexArray[7] = dy2;

    fUvArray[0] = 0;
    fUvArray[1] = 0;
    fUvArray[2] = (float) g_data.nBmpWidth / (float) g_data.nTexWidth;
    fUvArray[3] = fUvArray[1];
    fUvArray[4] = fUvArray[0];
    fUvArray[5] = (float) g_data.nBmpHeight / (float) g_data.nTexHeight;
    fUvArray[6] = fUvArray[2];
    fUvArray[7] = fUvArray[5];

    glDrawArrays (GL_TRIANGLE_STRIP, 0, 4);
}



