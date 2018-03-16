
#include "keypipe.h"

const char *API_URL = "http://";

JNIEXPORT jstring

JNICALL Java_devkai_app_base_model_RestClient_getApi
        (JNIEnv * env) {
    return env->NewStringUTF(API_URL);
}