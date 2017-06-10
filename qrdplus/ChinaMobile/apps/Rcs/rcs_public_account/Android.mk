LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional

LOCAL_SRC_FILES := $(call all-java-files-under, src)

LOCAL_PACKAGE_NAME := PublicAccount
LOCAL_CERTIFICATE := shared

LOCAL_MODULE_PATH := $(TARGET_OUT)/vendor/ChinaMobile/system/app

LOCAL_JAVA_LIBRARIES := rcs_service_api

LOCAL_JAVA_LIBRARIES += vcard

include $(BUILD_PACKAGE)
