LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := afadunitnative
LOCAL_SRC_FILES := ../libafadunitnative/jni/$(TARGET_ARCH_ABI)/libafadunitnative.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)

LOCAL_MODULE    := simplejniapp
LOCAL_ARM_MODE  := arm
LOCAL_CFLAGS    := -O2 -Werror -Ilibafadunitnative/headers
LOCAL_SRC_FILES := main.cpp BMPDecoder.cpp texData.cpp
LOCAL_LDLIBS    := -landroid -llog -lEGL -lGLESv1_CM
LOCAL_SHARED_LIBRARIES := afadunitnative

include $(BUILD_SHARED_LIBRARY)
