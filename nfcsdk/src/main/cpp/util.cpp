#include <jni.h>
#include <string>
#include <android/log.h>
#include "util.h"

void Logger(int level,const char *fmt,...)
{
    va_list  ap;
    va_start(ap,fmt);
    if(level == LOGGER_DEBUG) {
        __android_log_vprint(ANDROID_LOG_DEBUG, "NFCSDKCPP", fmt, ap);
    }else if(level == LOGGER_ERROR){
        __android_log_vprint(ANDROID_LOG_ERROR, "NFCSDKCPP", fmt, ap);
    }else {
        __android_log_vprint(ANDROID_LOG_INFO, "NFCSDKCPP", fmt, ap);
    }
    va_end(ap);
}

extern "C"
JNIEXPORT jstring JNICALL Java_com_rfidandcard_www_nfcsdk_Util_bytesToHex(JNIEnv *env,jclass clasz,jbyteArray bytes)
{
    char buf[1024]={0};
    int len = env->GetArrayLength(bytes);
    if(len == 0){
        return nullptr;
    }
    jboolean  copy = false;
    jbyte *dptr = env->GetByteArrayElements(bytes,&copy);
    int n = 0;
    for(int i=0; i < len;i++){
        n += sprintf(&buf[n],"%02X",(unsigned char)dptr[i]);
    }
    env->ReleaseByteArrayElements(bytes,dptr,0);
    return env->NewStringUTF(buf);
}