LOCAL_PATH := $(call my-dir)

$(shell mkdir -p $(TARGET_OUT)/vendor/IndiaCommon/system/etc)
$(shell cp -r $(LOCAL_PATH)/apns-conf.xml $(TARGET_OUT)/vendor/IndiaCommon/system/etc)
