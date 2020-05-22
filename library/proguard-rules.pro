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
-keep class com.googlecode.openbeans.** { *; }
-dontwarn com.googlecode.openbeans.**
-keep class org.apache.harmony.** { *; }
-dontwarn org.apache.harmony.**
-keep class org.bouncycastle.jce.** { *; }
-dontwarn org.bouncycastle.jce.**
-keep class org.bouncycastle.x509.** { *; }
-dontwarn org.bouncycastle.x509.**
-keep class net.i2p.crypto.x509.** { *; }
-dontwarn net.i2p.crypto.**