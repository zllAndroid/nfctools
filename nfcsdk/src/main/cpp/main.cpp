#include <jni.h>
#include <string>
#include "define.h"

extern "C"
JNIEXPORT jstring JNICALL Java_com_rfidandcard_www_nfcsdk_NFC_bytesToHex(JNIEnv *env,jclass clasz,jbyteArray bytes)
{
    char buf[1024]={0};
    int len = env->GetArrayLength(bytes);
    if(len == 0){
        return NULL;
    }
    jboolean  copy = false;
    jbyte *dptr = env->GetByteArrayElements(bytes,&copy);
    int n = 0;
    for(int i=0; i < len;i++){
        n += sprintf(&buf[n],"%02X",dptr[i]);
    }
    env->ReleaseByteArrayElements(bytes,dptr,0);
    return env->NewStringUTF(buf);
}