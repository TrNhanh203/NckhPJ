// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false


//    //id("com.android.application") version "8.1.4" apply false
//    id("com.android.library") version "8.1.4" apply false
//    //id("org.jetbrains.kotlin.android") version "2.0.0" apply false
//    id("dagger.hilt.android.plugin") version "2.48" apply false

}




