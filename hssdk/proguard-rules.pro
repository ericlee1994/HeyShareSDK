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

-keep enum com.ainemo.sdk.NemoSDKListener** {
    **[] $VALUES;
    public *;
    }
-keepclassmembers enum * { *; }
-keepnames class * implements java.io.Serializable
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
    }
-keep class com.ainemo.sdk.model.Settings{*;}
-keep class com.ainemo.sdk.NemoSDK{
    public *;
    }
-keep class com.google.gson.stream.** {*;}
-keep class com.google.gson.** {*;}
-keep class com.google.gson.Gson {*;}
-keep class com.google.gson.examples.android.model.** {*;}
-keep class io.reactivex.internal.util.**{*;}

-keep class android.utils.RestartHandler{*;}
-keep class com.ainemo.sdk.utils.SignatureHelper{*;}
-keep class com.ainemo.sdk.module.ConnectNemoCallback{*;}
-keep class com.ainemo.sdk.NemoReceivedCallListener{*;}
-keep class com.ainemo.sdk.NemoSDKListener{*;}
-keep class com.ainemo.sdk.module.data.VideoInfo{*;}
-keep class com.ainemo.sdk.module.push.**{*;}
-keep class com.ainemo.sdk.module.rest.**{*;}
-keep class com.ainemo.sdk.NemoSDKErrorCode{*;}

-keep class com.ainemo.sdk.otf.** {*;}
-keep class com.ainemo.a.**{*;}
-keep class com.ainemo.sdk.module.**{*;}
-keep class android.http.b{*;}
-keep class vulture.module.call.**{*;}
-keep class android.log.**{*;}
-keep class com.wa.util.**{*;}

-dontwarn com.ainemo.a.**
-dontwarn com.ainemo.sdk.module.**
-dontwarn android.http.b
-dontwarn com.ainemo.sdk.otf.**
-dontwarn vulture.module.call.**
-dontwarn com.serenegiant.**
-dontwarn okio.**


-keep class com.shgbit.hssdk.sdk.HeyShareSDK{*;}
-keep class com.shgbit.hssdk.bean.**{*;}
-keep class com.shgbit.hssdk.callback.**{*;}