# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Software\Developing\Android\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class description to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# A imageEntity is loaded with a relative path so the package of this class must be preserved.

# Add this global rule
-keepattributes *Annotation,Signature

# This rule will properly ProGuard all the model classes in
# the package com.yourcompany.models. Modify to fit the structure
# of your app.
-keepclassmembers class dev.alimansour.voiceassistant.model.** { *; }
-keepclassmembers class dev.alimansour.voiceassistant.data.remote.response.** { *; }

-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class com.android.** { *; }
-keep class com.google.** { *; }
-keep class org.jsoup.** { *; }
-keep class org.joda.** { *; }
-keep class com.github.siyamed.shapeimageview.**{ *; }
-keep class com.tibadev.ahlyringtones.model.** { *; }
# Keep the class names used to check for availablility
-keepnames class com.facebook.login.LoginManager
-keepnames class com.twitter.sdk.android.core.identity.TwitterAuthClient
# Don't note a bunch of dynamically referenced classes
-dontwarn com.android.**
-dontwarn com.google.**
-dontwarn org.jsoup.**
-dontnote com.facebook.**
-dontnote com.twitter.**
-dontnote com.squareup.okhttp.**
-dontnote okhttp3.internal.**
-dontwarn org.joda.**
-dontwarn com.github.siyamed.**

# 3P providers are optional
-dontwarn com.facebook.**
-dontwarn com.twitter.**

# Recommended flags for Firebase Auth
-keepattributes Signature
-keepattributes *Annotation*

# Picasso
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

-printmapping mapping.txt