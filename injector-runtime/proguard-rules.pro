# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 1               #the proguar class
#-dontoptimize                      #don't optimize the input class file
-dontusemixedcaseclassnames         #don't use big and little mixuse
-dontpreverify                      #pre verify
-verbose                            #print log
-ignorewarnings                     #ignore waning

#keep line, don's show class name
-renamesourcefileattribute
-keepattributes SourceFile,LineNumberTable

#system
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
-keepclassmembers class * extends android.view.View {
    void set*(***);
    *** get*();
}
-keepclassmembers class * extends android.os.Parcelable {
    public <methods>;
}
#activity
-keep class * extends android.app.Activity {}

-keep class **.R$* {}
-keep class com.tencent.mobileqq.* {}