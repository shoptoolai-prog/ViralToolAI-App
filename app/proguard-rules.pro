# ProGuard Rules for Production Release - ViralToolAI

# Keep data models used for Moshi / JSON serialization
-keepclassmembers class * {
    @com.squareup.moshi.Json *;
    @com.squareup.moshi.JsonClass *;
}
-keep class com.example.data.** { *; }
-keep class com.example.creator.** { *; }

# Room persistence rules
-keep class * extends androidx.room.RoomDatabase
-dontwarn androidx.room.paging.**

# Retrofit & OkHttp
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes *Annotation*
-dontwarn okhttp3.**
-dontwarn retrofit2.**

# Kotlin Coroutines
-keepclassmembers class kotlinx.coroutines.** { *; }

# Compose
-keepclassmembers class * extends androidx.compose.ui.Modifier { *; }

