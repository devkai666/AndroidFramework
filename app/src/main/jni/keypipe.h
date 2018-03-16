
#ifndef DEVKAI_KEYPIPE_H
#define DEVKAI_KEYPIPE_H

#include "jni.h"
#include <stdio.h>
#include <string.h>

extern "C" {
JNIEXPORT jstring
JNICALL Java_devkai_app_base_model_RestClient_getApi(JNIEnv * env);
}
#endif
