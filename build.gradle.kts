// Top-level build file where you can add configuration options common to all sub-projects/modules.
// Project-level build.gradle.kts
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Google Services প্লাগইনটি এখানে একবার ডিফাইন করতে হবে
    id("com.google.gms.google-services") version "4.4.1" apply false
}