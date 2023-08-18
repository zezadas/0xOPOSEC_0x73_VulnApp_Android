#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_pt_oposec_vulnapp_activities_MainActivity_stringFromJNI(JNIEnv *env, jobject thiz) {
        char* s = "cgvdL~hERAREDRShC_RhYVC^ARh[^UJ";
        char* output = static_cast<char *>(calloc(32, sizeof(char)));
        for (int i=0;i<31;i++) {
            output[i] = s[i] ^ 0x37;
        }
        return env->NewStringUTF(output);
}