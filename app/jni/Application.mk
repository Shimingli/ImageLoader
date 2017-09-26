#jni打包的C语言类库默认仅支持 arm架构，需要在jni目录下创建 Android.mk 文件添加如下代码可以支持x86架构
#或者是 ：=all
APP_ABI :=armeabi armeabi-v7a x86