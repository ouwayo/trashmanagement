#include <jni.h>
#include <string>
#include <sstream>
#include <random>
 using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_systemmonitor_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}
double userBalance = 0;
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_systemmonitor_MainActivity_addtoUserBalanceFromJNI(
        JNIEnv *env,
        jobject /* this */) {
   // const std::string& temp="";
    userBalance+= 20.0;
    ostringstream streamObj;
    streamObj<<userBalance;
   // return strobj;
    return env->NewStringUTF("45");
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_example_systemmonitor_MainActivity_BinCapacity(
        JNIEnv *env,
        jobject /* this */) {
    // const std::string& temp="";
    int capacity;
    capacity=25;
    return env->NewStringUTF("25");
}

