# AndroidX
-keep class androidx.** { *; }

# Kotlin
-keep class kotlin.Metadata { *; }
-keep class kotlin.** { *; }

# Retrofit
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface com.chaouni.omnimind.** { *; }

# Room Database
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.Database { *; }

# Coroutines
-keep class kotlinx.coroutines.** { *; }

# ViewModel
-keep class androidx.lifecycle.** { *; }

# Material Components
-keep class com.google.android.material.** { *; }

# Keep all Activities, Services, and BroadcastReceivers
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver

# Keep Parcelable classes
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# Keep R classes
-keep class **.R$* { *; }

# Keep data classes
-keepclassmembers class ** {
    private <fields>;
    private <methods>;
}
