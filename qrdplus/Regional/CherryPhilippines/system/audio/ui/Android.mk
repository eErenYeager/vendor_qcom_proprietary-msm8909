LOCAL_PATH := $(call my-dir)

$(shell mkdir -p $(TARGET_OUT)/vendor/CherryPhilippines/system/media/audio/ui)
$(shell cp -r $(LOCAL_PATH)/*.ogg $(TARGET_OUT)/vendor/CherryPhilippines/system/media/audio/ui)
