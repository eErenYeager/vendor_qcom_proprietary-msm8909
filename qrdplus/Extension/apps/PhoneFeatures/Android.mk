LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE_TAGS := optional eng

LOCAL_SRC_FILES := $(call all-subdir-java-files)

LOCAL_JAVA_LIBRARIES := telephony-common qcrilhook

LOCAL_PACKAGE_NAME := PhoneFeatures

LOCAL_CERTIFICATE := platform

include $(BUILD_PACKAGE)
