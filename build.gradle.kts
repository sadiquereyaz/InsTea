// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    alias(libs.plugins.googleGmsGoogleServices) apply false
    id("com.google.devtools.ksp") version "1.9.21-1.0.15" apply false    //room
}
allprojects{
    repositories{
        maven{ url = uri("https://oss.sonatype.org/content/repositories/snapshots/")}
    }
}