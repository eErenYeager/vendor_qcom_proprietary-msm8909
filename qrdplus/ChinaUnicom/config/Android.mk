LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

#################################################
PRE_LOAD_SPEC := .preloadspec
$(shell mkdir -p $(TARGET_OUT)/vendor/ChinaUnicom)
$(shell cp -r $(LOCAL_PATH)/$(PRE_LOAD_SPEC) $(TARGET_OUT)/vendor/ChinaUnicom/$(PRE_LOAD_SPEC))

#################################################
SPEC_PROP := vendor.prop
$(shell mkdir -p $(TARGET_OUT)/vendor/ChinaUnicom/system/vendor)
$(shell cp -r $(LOCAL_PATH)/$(SPEC_PROP) $(TARGET_OUT)/vendor/ChinaUnicom/system/vendor/$(SPEC_PROP))

#################################################
EXCLUDE_LIST := exclude.list
$(shell mkdir -p $(TARGET_OUT)/vendor/ChinaUnicom)
$(shell cp -r $(LOCAL_PATH)/$(EXCLUDE_LIST) $(TARGET_OUT)/vendor/ChinaUnicom/$(EXCLUDE_LIST))

