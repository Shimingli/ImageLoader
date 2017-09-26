#include "com_shiming_imageloader_jnitest_JniUtils.h"
#include "ShimingImageBlur.c"
#include <android/log.h>

//用c++实现的，方法名必须为本地方法的全类名改为下划线
//第一个参数为java虚拟机的内存地址的二级指正，用于本地方法与java虚拟机在内存中的交互
//第二个参数为一个java对象，即是那个对象调用了这个c的方法 ，
//后面的参数就是我们java的方法参数
JNIEXPORT void JNICALL Java_com_shiming_imageloader_jnitest_JniUtils_blurIntArray
(JNIEnv *env, jclass obj, jintArray arrIn, jint w, jint h, jint r)
{
	jint *pix;
	pix = env->GetIntArrayElements(arrIn, 0);
	if (pix == NULL)
		return;
	//Start
	pix = StackBlur(pix, w, h, r);
	//End
	//int size = w * h;
	//jintArray result = env->NewIntArray(size);
	//env->SetIntArrayRegion(result, 0, size, pix);
	env->ReleaseIntArrayElements(arrIn, pix, 0);
	//return result;
}
