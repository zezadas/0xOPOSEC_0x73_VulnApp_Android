#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_pt_oposec_vulnapp_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "This could be more interesting. But it's empty";
    return env->NewStringUTF(hello.c_str());
}
